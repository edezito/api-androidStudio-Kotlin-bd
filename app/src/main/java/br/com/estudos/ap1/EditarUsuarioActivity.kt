package br.com.estudos.ap1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.estudos.ap1.database.AppDatabase
import br.com.estudos.ap1.databinding.ActivityEditarUsuarioBinding
import br.com.estudos.ap1.model.UsuarioIMC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class EditarUsuarioActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEditarUsuarioBinding.inflate(layoutInflater)
    }

    private var usuarioOriginalNome: String? = null
    private val usuarioDao by lazy {
        AppDatabase.instancia(this).usuarioIMCDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configuraCampos()

        binding.botaoSalvarAlteracoes.setOnClickListener {
            salvarAlteracoes()
        }
    }

    private fun configuraCampos() {
        usuarioOriginalNome = intent.getStringExtra("nome")

        binding.editNome.setText(usuarioOriginalNome)
        binding.editAltura.setText(intent.getStringExtra("altura"))
        binding.editPeso.setText(intent.getStringExtra("peso"))
        binding.editIMC.setText(intent.getStringExtra("imc"))
    }

    private fun salvarAlteracoes() {
        val nome = binding.editNome.text.toString()
        val altura = binding.editAltura.text.toString().toBigDecimalOrNull()
        val peso = binding.editPeso.text.toString().toBigDecimalOrNull()
        val imc = binding.editIMC.text.toString().toBigDecimalOrNull()

        if (nome.isBlank() || altura == null || peso == null || imc == null) {
            Toast.makeText(this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show()
            return
        }

        val novoUsuario = UsuarioIMC(
            nome = nome,
            altura = altura,
            peso = peso,
            imc = imc
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                usuarioOriginalNome?.let { nomeOriginal ->
                    val usuarioExistente = usuarioDao.buscaPorNome(nomeOriginal)
                    if (usuarioExistente != null) {
                        usuarioDao.remover(usuarioExistente)
                    }
                }
                usuarioDao.salvar(novoUsuario)
            }

            Toast.makeText(this@EditarUsuarioActivity, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}