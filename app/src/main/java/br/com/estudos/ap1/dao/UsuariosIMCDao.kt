package br.com.estudos.ap1.database.dao

import androidx.room.*
import br.com.estudos.ap1.model.UsuarioIMC

@Dao
interface UsuariosDao {

    @Insert
    fun salvar(usuario: UsuarioIMC)

    @Query("SELECT * FROM usuarios")
    fun buscarTodos(): List<UsuarioIMC>

    @Delete
    fun deletar(usuario: UsuarioIMC)

    @Update
    fun atualizar(usuario: UsuarioIMC)
}