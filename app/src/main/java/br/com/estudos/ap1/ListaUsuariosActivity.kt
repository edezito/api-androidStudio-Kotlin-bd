package br.com.estudos.ap1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import br.com.estudos.ap1.databinding.ActivityListaUsuarioBinding
import br.com.estudos.ap1.model.AppDatabase
import br.com.estudos.ap1.model.UsuarioIMC
import kotlinx.coroutines.launch

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val adapter by lazy {
        ListaUsuariosAdapter(
            context = this,
            imc = emptyList(),
            quandoClicaNoItem = { usuario -> editarUsuario(usuario) },
            onSalvarUsuario = { usuario -> editarUsuario(usuario) },
            onExcluirUsuario = { usuario -> excluirUsuario(usuario) }
        )
    }
    private val binding by lazy {
        ActivityListaUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configuraRecyclerView()
        configuraFab()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    override fun onResume() {
        super.onResume()
        atualizaLista()
    }

    private fun atualizaLista() {
        lifecycleScope.launch {
            val usuarios = db.usuariosDao().buscaTodos() // Busca todos os usuários
            adapter.atualiza(usuarios) // Atualiza o adapter com a nova lista
        }
    }

    private fun configuraFab() {
        val fab = binding.fabAdicionarUsuario
        fab.setOnClickListener {
            formIMC()
        }
    }

    private fun formIMC() {
        val intent = Intent(this, CalcularIMCActivity::class.java)
        startActivity(intent)
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.recyclerViewUsuarios
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun editarUsuario(usuario: UsuarioIMC) {
        val intent = Intent(this, EditarUsuarioActivity::class.java)
        intent.putExtra("usuario_id", usuario.id) // Passa o ID para a Activity de edição
        startActivity(intent)
    }

    private fun excluirUsuario(usuario: UsuarioIMC) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Excluir Usuário")
        builder.setMessage("Tem certeza que deseja excluir este usuário?")

        builder.setPositiveButton("Sim") { dialog, which ->
            lifecycleScope.launch {
                db.usuariosDao().remover(usuario)  // Exclui o usuário do banco de dados
                atualizaLista()  // Atualiza a lista após a exclusão
                Toast.makeText(this@ListaUsuariosActivity, "Usuário excluído", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss() // Fecha o diálogo sem realizar a ação
        }

        builder.show() // Exibe o diálogo de confirmação
    }
}