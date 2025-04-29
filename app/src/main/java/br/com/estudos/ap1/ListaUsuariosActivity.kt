package br.com.estudos.ap1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.estudos.ap1.dao.UsuariosIMCDao
import br.com.estudos.ap1.databinding.ActivityListaUsuarioBinding
import br.com.estudos.ap1.model.UsuarioIMC

class ListaUsuariosActivity : AppCompatActivity() {

    private val dao = UsuariosIMCDao()
    private lateinit var adapter: UsuarioAdapter
    private val binding by lazy {
        ActivityListaUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Recuperando dados enviados pela Intent
        val nome = intent.getStringExtra("nome")
        val altura = intent.getStringExtra("altura")?.toBigDecimalOrNull()
        val peso = intent.getStringExtra("peso")?.toBigDecimalOrNull()
        val imc = intent.getStringExtra("imc")?.toBigDecimalOrNull()

        // Caso os dados estejam presentes, salva o novo usuário
        if (nome != null && altura != null && peso != null && imc != null) {
            val usuarioIMC = UsuarioIMC(nome, altura, peso, imc)
            dao.salvar(usuarioIMC)
        }

        // Configura o RecyclerView
        configuraRecyclerView()

        // Ao clicar no FAB, abre a tela de calcular IMC
        binding.fabAdicionarUsuario.setOnClickListener {
            val intent = Intent(this, CalcularIMCActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Atualiza a lista de usuários ao retornar para a tela
        adapter.atualizarUsuario(dao.buscaTodos().toMutableList())  // Garante que seja uma lista mutável
    }

    private fun configuraRecyclerView() {
        // Inicializa o adapter e passa os dados necessários
        adapter = UsuarioAdapter(
            context = this,
            usuarioIMCS = dao.buscaTodos().toMutableList(),  // Converte a lista para MutableList
            onEditarClick = { usuario ->
                // Ao clicar para editar, envia os dados para a tela de edição
                val intent = Intent(this, EditarUsuarioActivity::class.java).apply {
                    putExtra("nome", usuario.nome)
                    putExtra("altura", usuario.altura.toString())
                    putExtra("peso", usuario.peso.toString())
                    putExtra("imc", usuario.imc.toString())
                }
                startActivity(intent)
            },
            onDeletarClick = { usuario ->
                // Ao clicar para deletar, remove o usuário e atualiza a lista
                dao.remover(usuario)
                adapter.atualizarUsuario(dao.buscaTodos().toMutableList())  // Atualiza a lista de usuários
            }
        )

        // Configura o RecyclerView
        binding.recyclerViewUsuarios.apply {
            adapter = this@ListaUsuariosActivity.adapter
            layoutManager = LinearLayoutManager(this@ListaUsuariosActivity)
        }
    }
}