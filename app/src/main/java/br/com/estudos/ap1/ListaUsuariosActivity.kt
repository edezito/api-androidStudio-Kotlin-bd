package br.com.estudos.ap1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import br.com.estudos.ap1.databinding.ActivityListaUsuarioBinding
import br.com.estudos.ap1.model.AppDatabase
import kotlinx.coroutines.launch

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val adapter by lazy {
        ListaUsuariosAdapter(context = this, imc = emptyList())
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
            val usuarios = db.usuariosDao().buscaTodos()
            adapter.atualiza(usuarios)
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
}