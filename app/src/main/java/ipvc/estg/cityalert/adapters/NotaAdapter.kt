package ipvc.estg.cityalert.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import ipvc.estg.cityalert.AdicionaNota
import ipvc.estg.cityalert.ConsultaNota
import ipvc.estg.cityalert.MainActivity
import ipvc.estg.cityalert.R
import ipvc.estg.cityalert.entities.Nota
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.add_new_note.view.*
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class NotaAdapter (context: Context) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = mutableListOf<Nota>() //Cached copy of notes



    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notaItemView: TextView = itemView.findViewById(R.id.titulo)
        val desItemView: TextView = itemView.findViewById(R.id.des)



        //
        init {
            notaItemView.setOnClickListener {
                /*val position : Int = adapterPosition
                Toast.makeText(notaItemView.context,"You clicked on number ${position}", Toast.LENGTH_LONG).show()*/

                val titulo = notaItemView.titulo.text
                val descricao = desItemView.des.text //Erro //?

                val intent = Intent(itemView.context, ConsultaNota::class.java)
                intent.putExtra("NoteTitle", titulo)
                intent.putExtra("NoteDes", descricao)
                itemView.context.startActivity(intent)


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.notaItemView.text = current.titulo
        holder.desItemView.text = current.descricao


    }



    internal fun setNotes(notas: MutableList<Nota> ){
        this.notas = notas

        notifyDataSetChanged()
    }

    internal fun getNotas(): List<Nota> {
        return this.notas
    }

    override fun getItemCount() = notas.size
}

