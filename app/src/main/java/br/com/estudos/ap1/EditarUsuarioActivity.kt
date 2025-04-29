package br.com.estudos.ap1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import br.com.estudos.ap1.databinding.ActivityEditarUsuarioBinding
import br.com.estudos.ap1.model.AppDatabase
import br.com.estudos.ap1.model.UsuarioIMC
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.math.RoundingMode

class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var usuario: UsuarioIMC
    private val binding by lazy {
        ActivityEditarUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Recupera o ID do usuário da Intent
        val usuarioId = intent.getIntExtra("usuario_id", -1)
        if (usuarioId != -1) {
            // Carregar os dados do usuário do banco de dados
            carregarUsuario(usuarioId)
        }

        binding.btnSalvar.setOnClickListener {
            salvarAlteracoes()
        }

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    // Função para carregar os dados do usuário no UI
    private fun carregarUsuario(id: Int) {
        lifecycleScope.launch {
            // Aqui, usamos o operador safe-call (?.) para verificar se o retorno é null
            usuario = db.usuariosDao().buscaPorId(id) ?: run {
                // Caso o usuário não seja encontrado, podemos exibir uma mensagem ou tratá-lo de alguma maneira
                Toast.makeText(this@EditarUsuarioActivity, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                return@launch  // Sai da função caso o usuário não seja encontrado
            }

            // Atualizar os campos de edição com os dados do usuário
            binding.editTextNome.setText(usuario.nome)
            binding.editTextPeso.setText(usuario.peso.toString())
            binding.editTextAltura.setText(usuario.altura.toString())
        }
    }

    // Função para salvar as alterações feitas pelo usuário
    private fun salvarAlteracoes() {
        val nome = binding.editTextNome.text.toString()
        val peso = binding.editTextPeso.text.toString().toBigDecimalOrNull()
        val altura = binding.editTextAltura.text.toString().toBigDecimalOrNull()

        if (nome.isBlank() || peso == null || altura == null || altura <= BigDecimal.ZERO) {
            Toast.makeText(this, "Preencha os campos corretamente.", Toast.LENGTH_SHORT).show()
            return
        }

        val novoImc = peso.divide(altura.pow(2), 2, RoundingMode.HALF_UP)

        val usuarioAtualizado = usuario.copy(
            nome = nome,
            peso = peso,
            altura = altura,
            imc = novoImc
        )

        lifecycleScope.launch {
            db.usuariosDao().atualizar(usuarioAtualizado)
            Toast.makeText(this@EditarUsuarioActivity, "Usuário atualizado", Toast.LENGTH_SHORT).show()

            setResult(RESULT_OK)
            finish()
        }
    }
}
