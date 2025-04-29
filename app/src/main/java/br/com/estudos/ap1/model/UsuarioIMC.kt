package br.com.estudos.ap1.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "usuarios")
data class UsuarioIMC(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val altura: BigDecimal,
    val peso: BigDecimal,
    val imc: BigDecimal,
    val img: String? = null
)