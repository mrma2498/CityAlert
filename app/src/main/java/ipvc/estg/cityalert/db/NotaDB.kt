package ipvc.estg.cityalert.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ipvc.estg.cityalert.dao.NotaDao
import ipvc.estg.cityalert.entities.Nota
import kotlinx.coroutines.CoroutineScope


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Nota::class), version = 8, exportSchema = false)

public abstract class NotaDB : RoomDatabase() {

        abstract fun notaDao(): NotaDao

        companion object {
            // Singleton prevents multiple instances of database opening at the
            // same time.
            @Volatile
            private var INSTANCE: NotaDB? = null

            fun getDatabase(context: Context, scope: CoroutineScope): NotaDB {
                // if the INSTANCE is not null, then return it,
                // if it is, then create the database
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotaDB::class.java,
                        "nota_database"
                    ).build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
            }
        }
    }
