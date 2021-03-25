package ipvc.estg.cityalert.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ipvc.estg.cityalert.db.NotaRepository
import ipvc.estg.cityalert.db.NotaDB

import ipvc.estg.cityalert.entities.Nota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotaRepository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allNotes: LiveData<List<Nota>>

    init {
        val notesDao = NotaDB.getDatabase(application, viewModelScope).notaDao()
        repository = NotaRepository(notesDao)
        allNotes = repository.allNotes
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(nota)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }



    // delete by note
    /*fun deleteNote(nota: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(nota)
    }*/

    fun deleteNote(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(nota)
    }

    fun update(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(nota)
    }




}

