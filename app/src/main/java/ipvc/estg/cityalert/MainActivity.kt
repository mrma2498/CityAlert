package ipvc.estg.cityalert

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.adapters.NotaAdapter
import ipvc.estg.cityalert.entities.Nota
import ipvc.estg.cityalert.viewModel.NotaViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NotaViewModel
    private val newNoteActivityRequestCode = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotaAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        //view model
        noteViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer{ notes ->
            notes?.let { adapter.setNotes(it) }

        })


            //Fab
            val fab = findViewById<FloatingActionButton>(R.id.fab)
            fab.setOnClickListener {
                val intent = Intent(this@MainActivity, AdicionaNota::class.java)
                startActivityForResult(intent, newNoteActivityRequestCode)
            }

        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(AdicionaNota.EXTRA_REPLY)?.let {
                val nota = Nota(titulo = it, descricao = "Teste")
                noteViewModel.insert(nota)

                //Adiciona snackbar a dizer que a nota foi adicionada com sucesso
                val snackbarSucess = findViewById<View>(R.id.mainactivity)

                Snackbar.make(snackbarSucess, R.string.note_sucess, Snackbar.LENGTH_SHORT)
                        .show()

                //outras cenas

            }
        } else {
            Toast.makeText(
                applicationContext,R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

    //R.string.empty_not_saved
}