package br.com.estudos.ap1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.estudos.ap1.database.AppDatabase
import br.com.estudos.ap1.databinding.ActivityListaUsuarioBinding
import br.com.estudos.ap1.model.UsuarioIMC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var adapter: UsuarioAdapter
    private val binding by lazy {
        ActivityListaUsuarioBinding.inflate(layoutInflater)
    }
    private val usuarioDao by lazy {
        AppDatabase.instancia(this).usuarioIMCDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configuraRecyclerView()

        // Recupera dados enviados pela Intent
        val nome = intent.getStringExtra("nome")
        val altura = intent.getStringExtra("altura")?.toBigDecimalOrNull()
        val peso = intent.getStringExtra("peso")?.toBigDecimalOrNull()
        val imc = intent.getStringExtra("imc")?.toBigDecimalOrNull()

        if (nome != null && altura != null && peso != null && imc != null) {
            val usuarioIMC = UsuarioIMC(nome, altura, peso, imc)
            salvarUsuario(usuarioIMC)
        }

        binding.fabAdicionarUsuario.setOnClickListener {
            val intent = Intent(this, CalcularIMCActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        atualizaLista()
    }

    private fun configuraRecyclerView() {
        adapter = UsuarioAdapter(
            context = this,
            usuarioIMCS = mutableListOf(),
            onEditarClick = { usuario ->
                val intent = Intent(this, EditarUsuarioActivity::class.java).apply {
                    putExtra("nome", usuario.nome)
                    putExtra("altura", usuario.altura.toString())
                    putExtra("peso", usuario.peso.toString())
                    putExtra("imc", usuario.imc.toString())
                }
                startActivity(intent)
            },
            onDeletarClick = { usuario ->
                deletarUsuario(usuario)
            }
        )

        binding.recyclerViewUsuarios.apply {
            adapter = this@ListaUsuariosActivity.adapter
            layoutManager = LinearLayoutManager(this@ListaUsuariosActivity)
        }
    }

    private fun atualizaLista() {
        lifecycleScope.launch {
            val usuarios = withContext(Dispatchers.IO) {
                usuarioDao.buscaTodos()
            }
            adapter.atualizarUsuario(usuarios.toMutableList())
        }
    }

    private fun salvarUsuario(usuario: UsuarioIMC) {
        lifecycleScope.launch(Dispatchers.IO) {
            usuarioDao.salvar(usuario)
        }
    }

    private fun deletarUsuario(usuario: UsuarioIMC) {
        lifecycleScope.launch(Dispatchers.IO) {
            usuarioDao.remover(usuario)
            // Atualiza lista depois da exclusão
            val usuariosAtualizados = usuarioDao.buscaTodos()
            withContext(Dispatchers.Main) {
                adapter.atualizarUsuario(usuariosAtualizados.toMutableList())
            }
        }
    }
}