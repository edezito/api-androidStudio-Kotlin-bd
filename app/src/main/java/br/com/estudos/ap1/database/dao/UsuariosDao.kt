package br.com.estudos.ap1.dao

import androidx.room.*
import br.com.estudos.ap1.model.UsuarioIMC

@Dao
interface UsuariosDao {

    // Buscar todos os usuários
    @Query("SELECT * FROM usuario_imc")
    suspend fun buscaTodos(): List<UsuarioIMC>

    // Inserir um novo usuário
    @Insert
    suspend fun adiciona(usuarioIMC: UsuarioIMC)

    // Salvar ou atualizar
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(usuarioIMC: UsuarioIMC)

    // Atualizar informações
    @Update
    suspend fun atualizar(usuarioIMC: UsuarioIMC)

    // Remover usuário
    @Delete
    suspend fun remover(usuarioIMC: UsuarioIMC)

    // Buscar usuário pelo nome
    @Query("SELECT * FROM usuario_imc WHERE nome = :nome LIMIT 1")
    suspend fun buscaPorNome(nome: String): UsuarioIMC?
}