package br.com.estudos.ap1.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
@Entity(tableName = "usuario_imc")
data class UsuarioIMC(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val altura: BigDecimal,
    val peso: BigDecimal,
    val imc: BigDecimal,
    val imagem: String? = null
) : Parcelable