package br.com.estudos.ap1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.estudos.ap1.databinding.UsuarioImcBinding
import br.com.estudos.ap1.model.UsuarioIMC
import coil.load

class ListaUsuariosAdapter(
    private val context: Context,
    imc: List<UsuarioIMC>,
    var quandoClicaNoItem: (usuario: UsuarioIMC) -> Unit = {}
) : RecyclerView.Adapter<ListaUsuariosAdapter.ViewHolder>() {

    private val imc = imc.toMutableList()

    inner class ViewHolder(private val binding: UsuarioImcBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var usuarioIMC: UsuarioIMC

        init {
            itemView.setOnClickListener {
                if (::usuarioIMC.isInitialized) {
                    quandoClicaNoItem(usuarioIMC)
                }
            }
        }

        fun vincula(usuarioIMC: UsuarioIMC) {
            this.usuarioIMC = usuarioIMC
            binding.usuarioName.text = usuarioIMC.nome
            binding.usuarioAltura.text = usuarioIMC.altura.toString()
            binding.usuarioPeso.text = usuarioIMC.peso.toString()

            // Verificar a visibilidade da imagem
            val visibilidade = if (!usuarioIMC.imagem.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.imageView.visibility = visibilidade

            // Carregar a imagem com Coil
            usuarioIMC.imagem?.let { imagemUrl ->
                binding.imageView.load(imagemUrl) {
                    crossfade(true)
                    placeholder(R.drawable.imagem_padrao) // Coloca uma imagem padrão enquanto carrega
                    error(R.drawable.erro) // Caso a imagem não seja carregada
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = UsuarioImcBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imc = imc[position]
        holder.vincula(imc)
    }

    override fun getItemCount(): Int = imc.size

    fun atualiza(imc: List<UsuarioIMC>) {
        this.imc.clear()
        this.imc.addAll(imc)
        notifyDataSetChanged()

    }
}