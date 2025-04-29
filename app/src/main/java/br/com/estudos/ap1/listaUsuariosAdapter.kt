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
    private var usuarioIMCS: MutableList<UsuarioIMC>,  // A lista agora é mutável
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

            // Carrega a imagem, com fallback e erro definidos
            binding.imageView.load(usuarioIMC.img) {
                fallback(R.drawable.erro)
                error(R.drawable.erro)
            }

            // Configura os botões de ação
            binding.buttonEditar.setOnClickListener {
                onEditarClick(usuarioIMC)  // Chama a função de editar
            }

            binding.buttonDeletar.setOnClickListener {
                onDeletarClick(usuarioIMC)  // Chama a função de deletar
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

    // Cria o ViewHolder e infla o layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val binding = UsuarioImcBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return UsuarioViewHolder(binding)
    }

    // Vincula os dados do usuário ao item da lista
    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        holder.vincula(usuarioIMCS[position])
    }

    // Retorna o número total de itens na lista
    override fun getItemCount() = usuarioIMCS.size

    // Função para atualizar a lista de usuários no adapter
    fun atualizarUsuario(novaLista: List<UsuarioIMC>) {
        usuarioIMCS.clear()  // Limpa a lista antiga
        usuarioIMCS.addAll(novaLista)  // Adiciona todos os novos itens
        notifyDataSetChanged()  // Notifica o RecyclerView de que os dados mudaram
    }
}