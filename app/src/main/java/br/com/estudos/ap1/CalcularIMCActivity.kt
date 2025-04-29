package br.com.estudos.ap1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import br.com.estudos.ap1.databinding.ActivityCalcularImcBinding
import br.com.estudos.ap1.model.AppDatabase
import br.com.estudos.ap1.model.UsuarioIMC
import coil.load
import imagemDialog
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class CalcularIMCActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCalcularImcBinding.inflate(layoutInflater)
    }
    private var url: String? = null

    // Banco de dados
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Cadastrar usuário"

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()

        configurarBotaoCalcular()

        binding.imagemForm.setOnClickListener {
            imagemDialog(this)
                .mostra(url) { imagem ->
                    url = imagem
                    binding.imagemForm.load(imagem) // Carrega a imagem
                }
        }
    }

    private fun configurarBotaoCalcular() {
        val botaoSalvar = binding.btnCalcular

        botaoSalvar.setOnClickListener {
            val usuarioNovo = criaUsuario()

            if (usuarioNovo.nome.isNotBlank()) {
                lifecycleScope.launch {
                    db.usuariosDao().salvar(usuarioNovo)
                    finish() // Fecha a tela após salvar
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun criaUsuario(): UsuarioIMC {
        val nome = binding.editarname.text.toString().trim()
        val alturaStr = binding.editAltura.text.toString().trim()
        val pesoStr = binding.editPeso.text.toString().trim()

        // Validação dos campos
        if (!validarCampos(nome, alturaStr, pesoStr)) {
            return UsuarioIMC(
                id = 0L,
                nome = "",
                altura = BigDecimal.ZERO,
                peso = BigDecimal.ZERO,
                imc = BigDecimal.ZERO,
                imagem = null
            )
        }

        val imc = calcularIMC(alturaStr, pesoStr)
        return UsuarioIMC(
            id = 0L, // o Room vai gerar o ID automaticamente se você configurar ele assim
            nome = nome,
            altura = alturaStr.toBigDecimal(),
            peso = pesoStr.toBigDecimal(),
            imc = imc,
            imagem = url
        )
    }

    private fun validarCampos(nome: String, altura: String, peso: String): Boolean {
        return nome.isNotBlank() && altura.isNotBlank() && peso.isNotBlank()
    }

    private fun calcularIMC(alturaStr: String, pesoStr: String): BigDecimal {
        val altura = alturaStr.toBigDecimalOrNull()
        val peso = pesoStr.toBigDecimalOrNull()

        if (altura == null || peso == null || altura <= BigDecimal.ZERO) {
            throw NumberFormatException("Valores inválidos.")
        }

        return peso.divide(altura.pow(2), 2, RoundingMode.HALF_UP)
    }
}