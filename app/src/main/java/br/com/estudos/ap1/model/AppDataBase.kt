package br.com.estudos.ap1.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.estudos.ap1.model.UsuarioIMC
import br.com.estudos.ap1.database.dao.UsuariosDao

@Database(entities = [UsuarioIMC::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuariosDao(): UsuariosDao
}