package br.com.estudos.ap1.dao

import br.com.estudos.ap1.model.UsuarioIMC

class UsuariosIMCDao {

    private val usuarioIMCS = mutableListOf<UsuarioIMC>()

    fun buscaTodos(): List<UsuarioIMC> {
        return usuarioIMCS.toList()
    }

    fun salvar(usuarioIMC: UsuarioIMC) {
        usuarioIMCS.add(usuarioIMC)
    }

    fun atualizar(usuarioIMC: UsuarioIMC): Boolean {
        val index = usuarioIMCS.indexOfFirst { it.nome == usuarioIMC.nome }
        return if (index != -1) {
            usuarioIMCS[index] = usuarioIMC
            true
        } else {
            false
        }
    }

    fun remover(usuarioIMC: UsuarioIMC): Boolean {
        return usuarioIMCS.remove(usuarioIMC)
    }

    fun buscaPorNome(nome: String): UsuarioIMC? {
        return usuarioIMCS.find { it.nome == nome }
    }

}