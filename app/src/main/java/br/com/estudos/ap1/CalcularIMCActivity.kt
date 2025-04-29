package br.com.estudos.ap1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import br.com.estudos.ap1.database.AppDatabase
import br.com.estudos.ap1.databinding.ActivityCalcularImcBinding
import br.com.estudos.ap1.databinding.ImagemBinding
import br.com.estudos.ap1.model.UsuarioIMC
import coil.load
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.concurrent.thread

class CalcularIMCActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalcularImcBinding
    private lateinit var database: AppDatabase
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalcularImcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Calcular IMC"

        // Inicializa o banco
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "usuarios.db"
        ).allowMainThreadQueries() // permite rodar na main thread (não recomendado para produção)
            .build()

        configurarBotaoCalcular()

        binding.imagemForm.setOnClickListener {
            val bindingImagem = ImagemBinding.inflate(layoutInflater)
            bindingImagem.btnImagem.setOnClickListener {
                val url = bindingImagem.imagemFormOutput.text.toString()
                bindingImagem.imagemForm.load(url)
            }
            AlertDialog.Builder(this)
                .setView(bindingImagem.root)
                .setPositiveButton("Confirmar") { _, _ ->
                    url = bindingImagem.imagemFormOutput.text.toString()
                    binding.imagemForm.load(url)
                }
                .setNegativeButton("Cancelar") { _, _ -> }
                .show()
        }
    }

    private fun configurarBotaoCalcular() {
        binding.btnCalcular.setOnClickListener {
            calcularIMC()
        }
    }

    private fun calcularIMC() {
        val nome = binding.editarname.text.toString()
        val alturaStr = binding.editAltura.text.toString()
        val pesoStr = binding.editPeso.text.toString()

        if (!validarCampos(nome, alturaStr, pesoStr)) {
            Toast.makeText(this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val imc = calcularIMC(alturaStr, pesoStr)
            val usuarioIMC = UsuarioIMC(
                nome = nome,
                altura = alturaStr.toBigDecimal(),
                peso = pesoStr.toBigDecimal(),
                imc = imc,
                img = url // agora passando a URL da imagem
            )

            // Salvar no banco de dados
            thread {
                database.usuariosDao().salvar(usuarioIMC)

                runOnUiThread {
                    Toast.makeText(this, "Usuário salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    vaiParaListaUsuarios(usuarioIMC)
                }
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Valores inválidos! Insira números corretos.", Toast.LENGTH_SHORT).show()
        }
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

    private fun vaiParaListaUsuarios(usuarioIMC: UsuarioIMC) {
        val intent = Intent(this, ListaUsuariosActivity::class.java).apply {
            putExtra("nome", usuarioIMC.nome)
            putExtra("altura", usuarioIMC.altura.toString())
            putExtra("peso", usuarioIMC.peso.toString())
            putExtra("imc", usuarioIMC.imc.toString())
            putExtra("imagem", usuarioIMC.img)
        }
        startActivity(intent)
    }
}