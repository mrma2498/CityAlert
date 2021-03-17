package ipvc.estg.cityalert

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.adapters.NotaAdapter
import ipvc.estg.cityalert.entities.Nota
import ipvc.estg.cityalert.viewModel.NotaViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.measureNanoTime

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
            notes?.let { adapter.setNotes(it as MutableList<Nota>) }

        })


            //Abre uma nova atividade para adicionar uma nova nota
        val fab = findViewById<FloatingActionButton>(R.id.fab)
            fab.setOnClickListener {
                val intent = Intent(this@MainActivity, AdicionaNota::class.java)
                startActivityForResult(intent, newNoteActivityRequestCode)

            }

        /*Eliminar uma nota através de um swipe left*/
        val itemTouchHelperCallback =
                object :
                        ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT) {
                    override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                    ): Boolean {

                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        val position = viewHolder.adapterPosition
                        val notas: List<Nota> = adapter.getNotas()
                        noteViewModel.deleteNote(notas[position])

                        adapter.notifyItemRemoved(position)

                        //Adiciona snackbar a dizer que a nota foi eliminada com sucesso
                        val snackbarSucess = findViewById<View>(R.id.mainactivity)

                        Snackbar.make(snackbarSucess, R.string.note_delete, Snackbar.LENGTH_SHORT)
                                .show()

                    }

                }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerview)




        //Elimina todas as notas, mostrando o dialog para a confirmação ou não da ação
        val deleteNotes = findViewById<FloatingActionButton>(R.id.deleteNotes)
        deleteNotes.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.dialogtitle)
            builder.setMessage(R.string.dialogconfirm)


                builder.setPositiveButton(R.string.conf) { dialog, which ->
                    noteViewModel.deleteAll()
                }
                builder.setNegativeButton(R.string.cancel) { dialog, which ->
                }
                builder.show()
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



            }
        } else {
            Toast.makeText(
                applicationContext,R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

}

