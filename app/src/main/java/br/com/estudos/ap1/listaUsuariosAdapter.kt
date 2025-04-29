package br.com.estudos.ap1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.estudos.ap1.databinding.UsuarioImcBinding
import br.com.estudos.ap1.model.UsuarioIMC
import coil.load
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import br.com.estudos.ap1.model.AppDatabase
import kotlinx.coroutines.launch

class ListaUsuariosAdapter(
    private val context: Context,
    private var imc: List<UsuarioIMC>,
    var quandoClicaNoItem: (usuario: UsuarioIMC) -> Unit = {},
    var onSalvarUsuario: (usuario: UsuarioIMC) -> Unit = {},
    var onExcluirUsuario: (usuario: UsuarioIMC) -> Unit = {}
) : RecyclerView.Adapter<ListaUsuariosAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: UsuarioImcBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var usuarioIMC: UsuarioIMC

        init {
            itemView.setOnClickListener {
                if (::usuarioIMC.isInitialized) {
                    Log.d("ListaUsuariosAdapter", "Item Clicado: ${usuarioIMC.nome}")
                    quandoClicaNoItem(usuarioIMC)
                }
            }

            // Configura o clique para o botão de salvar
            binding.editarbtn.setOnClickListener {
                if (::usuarioIMC.isInitialized) {
                    Log.d("ListaUsuariosAdapter", "Botão Editar Clicado: ${usuarioIMC.nome}")
                    onSalvarUsuario(usuarioIMC) // Passa para a Activity/Fragment
                }
            }

            // Configura o clique para o botão de excluir
            binding.excluirbtn.setOnClickListener {
                if (::usuarioIMC.isInitialized) {
                    Log.d("ListaUsuariosAdapter", "Botão Excluir Clicado: ${usuarioIMC.nome}")
                    onExcluirUsuario(usuarioIMC) // Passa para a Activity/Fragment
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
        val usuarioIMC = imc[position]
        holder.vincula(usuarioIMC)
    }

    override fun getItemCount(): Int = imc.size

    fun atualiza(imc: List<UsuarioIMC>) {
        this.imc = imc
        notifyDataSetChanged()
    }
}