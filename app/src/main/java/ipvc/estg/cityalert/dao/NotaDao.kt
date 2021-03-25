package ipvc.estg.cityalert.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.cityalert.entities.Nota

@Dao
interface NotaDao {

    @Query("SELECT * FROM nota_table")
    fun getAllNotes(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Query("DELETE FROM nota_table")
    suspend fun deleteAll()

    @Delete
    fun deleteNote(nota: Nota)


    @Update
    fun update(nota: Nota)






}