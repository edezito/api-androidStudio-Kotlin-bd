package br.com.estudos.ap1.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.estudos.ap1.dao.UsuariosDao

@Database(
    entities = [UsuarioIMC::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuariosDao(): UsuariosDao
}