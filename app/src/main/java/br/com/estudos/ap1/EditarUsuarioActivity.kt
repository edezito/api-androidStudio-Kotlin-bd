package br.com.estudos.ap1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.estudos.ap1.dao.UsuariosIMCDao
import br.com.estudos.ap1.databinding.ActivityEditarUsuarioBinding
import java.math.BigDecimal

class EditarUsuarioActivity : AppCompatActivity() {

    private val dao = UsuariosIMCDao()
    private val binding by lazy {
        ActivityEditarUsuarioBinding.inflate(layoutInflater)
    }

    private var usuarioOriginalNome: String? = null

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

        val novoUsuario = br.com.estudos.ap1.model.UsuarioIMC(
            nome = nome,
            altura = altura,
            peso = peso,
            imc = imc
        )

        usuarioOriginalNome?.let { nomeOriginal ->
            val usuarioExistente = dao.buscaPorNome(nomeOriginal)
            if (usuarioExistente != null) {
                dao.remover(usuarioExistente)
            }
        }

        dao.salvar(novoUsuario)
        Toast.makeText(this, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }
}