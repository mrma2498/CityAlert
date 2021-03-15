package ipvc.estg.cityalert.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.cityalert.dao.NotaDao
import ipvc.estg.cityalert.entities.Nota
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Nota::class), version = 5, exportSchema = false)

public abstract class NotaDB : RoomDatabase() {

        abstract fun notaDao(): NotaDao

    private class NotasDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var notaDao = database.notaDao()

                    // Delete all content here.
                    notaDao.deleteAll()

                    // Add sample words.
                    var nota = Nota(1,"Obras Rua 32","Buraco na rua 33 pode provocar quedas")
                    notaDao.insert(nota)
                    nota = Nota(2,"Acidente na estrada", "Acidente entre duas viaturas na rua 25")
                    notaDao.insert(nota)

                }
            }
        }
    }

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
                    )
                    //Estratégias de destruição
                        //.fallbackToDestructiveMigration()
                        .addCallback(NotasDatabaseCallback(scope))
                        .build()

                    INSTANCE = instance
                    // return instance
                    instance
                }
            }
        }
    }
