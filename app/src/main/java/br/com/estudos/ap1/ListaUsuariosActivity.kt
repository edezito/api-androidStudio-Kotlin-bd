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

        val nome = intent.getStringExtra("nome")
        val altura = intent.getStringExtra("altura")?.toBigDecimalOrNull()
        val peso = intent.getStringExtra("peso")?.toBigDecimalOrNull()
        val imc = intent.getStringExtra("imc")?.toBigDecimalOrNull()

        if (nome != null && altura != null && peso != null && imc != null) {
            val usuarioIMC = UsuarioIMC(nome, altura, peso, imc)
            dao.salvar(usuarioIMC)  // Salva no DAO
        }

        adapter = UsuarioAdapter(context = this, usuarioIMCS = dao.buscaTodos())
        configuraRecyclerView()

        binding.fabAdicionarUsuario.setOnClickListener {
            val intent = Intent(this, CalcularIMCActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.atualizarUsuario(dao.buscaTodos())
    }

    private fun configuraRecyclerView() {
        binding.recyclerViewUsuarios.apply {
            adapter = this@ListaUsuariosActivity.adapter
            layoutManager = LinearLayoutManager(this@ListaUsuariosActivity)
        }
    }

}