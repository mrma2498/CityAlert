package ipvc.estg.cityalert

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_edita_nota.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs
import kotlin.system.measureNanoTime

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var noteViewModel: NotaViewModel


    private val newNoteActivityRequestCode = 1

    private val editNoteActivityRequestCode = 2


    private lateinit var sensorManager: SensorManager
    var sensor: Sensor? = null


    var currentX: Float = 0.0F
    var currentY: Float = 0.0F
    var currentZ: Float = 0.0F

    var lastX: Float = 0.0F
    var lastY: Float = 0.0F
    var lastZ: Float = 0.0F

    var itIsNotFirstTime: Boolean = false

    var xDif: Float = 0.0F
    var yDif: Float = 0.0F
    var zDif: Float = 0.0F

    var shakeThreshold: Float = 5f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)




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




        /*Eliminar uma nota atrav??s de um swipe right*/
        val itemTouchHelperCallback =
                object :
                        ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.RIGHT) {
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




        //Elimina todas as notas, mostrando o dialog para a confirma????o ou n??o da a????o
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



        adapter.setOnButtonClick(object : NotaAdapter.OnButtonClick {

            override fun Edit(position: Int, title: String, desc: String){

                val intent = Intent(this@MainActivity, EditaNota::class.java)
                intent.putExtra("id",position)
                intent.putExtra("titulo", title)
                intent.putExtra("desc",desc)
                startActivityForResult(intent,editNoteActivityRequestCode)
            }

        })


    }


    /*Insere uma nova atividade na lista das atividades*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {

            val pTitulo = data?.getStringExtra(AdicionaNota.EXTRA_REPLY_TITULO)
            val pDesc = data?.getStringExtra(AdicionaNota.EXTRA_REPLY_DESCRICAO)


            if (pTitulo!= null && pDesc != null) {
                val nota = Nota(titulo = pTitulo, descricao = pDesc)
                noteViewModel.insert(nota)

                //Adiciona snackbar a dizer que a nota foi adicionada com sucesso
                val snackbarSucess = findViewById<View>(R.id.mainactivity)

                Snackbar.make(snackbarSucess, R.string.note_sucess, Snackbar.LENGTH_SHORT)
                    .show()
            }

        }

        else if (requestCode == editNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {

            val titulo = data?.getStringExtra(EditaNota.EXTRA_REPLY_TITULO)
            val desc = data?.getStringExtra(EditaNota.EXTRA_REPLY_DESCRICAO)
            val pos = data?.getIntExtra(EditaNota.EXTRA_REPLY_POSICAO, 0)




            if (titulo!= null && desc != null && pos != null) {
                val nota = Nota(id = pos, titulo = titulo, descricao = desc)
                noteViewModel.update(nota)

                //Adiciona snackbar a dizer que a nota foi adicionada com sucesso
                val snackbarSucess = findViewById<View>(R.id.mainactivity)

                Snackbar.make(snackbarSucess, R.string.editsucess, Snackbar.LENGTH_SHORT)
                        .show()
            }

        }


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //
    }


    override fun onSensorChanged(event: SensorEvent) {
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.


        currentX = event.values[0]
        currentY = event.values[1]
        currentZ = event.values[2]


        if (itIsNotFirstTime){
            xDif = abs(lastX - currentX)
            yDif = abs(lastY - currentY)
            zDif = abs(lastZ - currentZ)

            if ((xDif > shakeThreshold && yDif > shakeThreshold) || (xDif > shakeThreshold && zDif > shakeThreshold)
                    || (yDif > shakeThreshold && zDif > shakeThreshold)){

                Log.d("MARIA","Shake Phone")
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
        lastX = currentX
        lastY = currentX
        lastZ = currentZ
        itIsNotFirstTime = true
    }

    override fun onResume() {
        super.onResume()
        sensor?.also { move ->
            sensorManager.registerListener(this, move, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}

