package br.com.estudos.ap1.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
@Entity(tableName = "usuario_imc")
data class UsuarioIMC(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    var nome: String = "",
    var altura: BigDecimal = BigDecimal.ZERO,
    var peso: BigDecimal = BigDecimal.ZERO,
    var imc: BigDecimal = BigDecimal.ZERO,
    var imagem: String? = null
) : Parcelable