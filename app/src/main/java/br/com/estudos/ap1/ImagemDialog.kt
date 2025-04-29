import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.com.estudos.ap1.R
import br.com.estudos.ap1.databinding.ImagemBinding
import coil.load
import com.google.android.material.textfield.TextInputEditText

class imagemDialog(private val context: Context) {

    fun mostra(
        urlPadrao: String? = null,
        quandoImagemCarregada: (imagem: String) -> Unit
    ) {
        val binding = ImagemBinding.inflate(LayoutInflater.from(context))

        // Se houver uma URL padrão, carregar a imagem
        urlPadrao?.let {
            binding.imagemForm.load(it) {
                placeholder(R.drawable.imagem_padrao) // Imagem padrão enquanto carrega
                error(R.drawable.erro) // Imagem de erro se não conseguir carregar
            }
            binding.imagemFormOutput.setText(it)
        }

        // Ação de carregar nova imagem
        binding.btnImagem.setOnClickListener {
            val url = binding.imagemFormOutput.text.toString()
            // Carregar a imagem com a URL informada
            binding.imagemForm.load(url) {
                placeholder(R.drawable.imagem_padrao) // Imagem padrão enquanto carrega
            }
        }

        // Mostrar o AlertDialog com os controles
        AlertDialog.Builder(context)
            .setView(binding.root)
            .setPositiveButton("Confirmar") { _, _ ->
                val url = binding.imagemFormOutput.text.toString()
                quandoImagemCarregada(url) // Retorna a URL da imagem
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
}