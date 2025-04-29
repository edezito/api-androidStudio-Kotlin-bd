package br.com.estudos.ap1

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.estudos.ap1.databinding.UsuarioImcBinding
import br.com.estudos.ap1.model.UsuarioIMC
import coil.load
import java.math.BigDecimal

class UsuarioAdapter(
    private val context: Context,
    private var usuarioIMCS: MutableList<UsuarioIMC>,
    private val onEditarClick: (UsuarioIMC) -> Unit,
    private val onDeletarClick: (UsuarioIMC) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(private val binding: UsuarioImcBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun vincula(usuarioIMC: UsuarioIMC) {
            binding.usuarioName.text = usuarioIMC.nome
            binding.usuarioAltura.text = "Altura: ${usuarioIMC.altura} m"
            binding.usuarioPeso.text = "Peso: ${usuarioIMC.peso} kg"
            binding.usuarioIMC.text = "IMC: ${usuarioIMC.imc.setScale(2)}"
            binding.usuarioClassificacao.text = "Classificação: ${classificarIMC(usuarioIMC.imc)}"

            // Carrega a imagem com Coil
            binding.imageView.load(usuarioIMC.img) {
                fallback(R.drawable.erro)
                error(R.drawable.erro)
            }

            // Configura ações de editar e deletar
            binding.buttonEditar.setOnClickListener {
                onEditarClick(usuarioIMC)
            }
            binding.buttonDeletar.setOnClickListener {
                onDeletarClick(usuarioIMC)
            }
        }

        private fun classificarIMC(imc: BigDecimal): String {
            return when {
                imc < BigDecimal("18.5") -> "Abaixo do peso"
                imc < BigDecimal("25") -> "Peso normal"
                imc < BigDecimal("30") -> "Sobrepeso"
                imc < BigDecimal("35") -> "Obesidade Grau I"
                imc < BigDecimal("40") -> "Obesidade Grau II"
                else -> "Obesidade Grau III"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val binding = UsuarioImcBinding.inflate(LayoutInflater.from(context), parent, false)
        return UsuarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarioIMCS[position]
        holder.vincula(usuario)
    }

    override fun getItemCount(): Int = usuarioIMCS.size

    // Atualiza os dados de forma mais eficiente
    fun atualizarUsuario(novaLista: List<UsuarioIMC>) {
        usuarioIMCS = novaLista.toMutableList()
        notifyDataSetChanged()
    }
}