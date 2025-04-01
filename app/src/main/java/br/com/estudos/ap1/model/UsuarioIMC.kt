package br.com.estudos.ap1.model

import java.math.BigDecimal

data class UsuarioIMC(
    val nome: String,
    val altura: BigDecimal,
    val peso: BigDecimal,
    val imc: BigDecimal,
    val img: String? = null
)