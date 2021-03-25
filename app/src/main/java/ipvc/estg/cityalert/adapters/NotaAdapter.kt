package ipvc.estg.cityalert.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cityalert.*
import ipvc.estg.cityalert.entities.Nota
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class NotaAdapter (context: Context) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = mutableListOf<Nota>() //Cached copy of notes


    private lateinit var onButtonClickListener : OnButtonClick

    fun setOnButtonClick(newOnButtonClickListener: OnButtonClick){
        onButtonClickListener= newOnButtonClickListener
    }
    

    interface OnButtonClick{
        fun Edit (position: Int, title: String, desc: String )

    }




    class NotaViewHolder(itemView: View, onButtonClickListener: OnButtonClick) : RecyclerView.ViewHolder(itemView) {
        val notaItemView: TextView = itemView.findViewById(R.id.titulo)
        val desItemView: TextView = itemView.findViewById(R.id.des)

        val button: ImageView = itemView.findViewById(R.id.editbutton)


        init {
            notaItemView.setOnClickListener {
                //val position: Int = adapterPosition
                //Toast.makeText(notaItemView.context,"You clicked on number ${position}", Toast.LENGTH_LONG).show()

                val titulo = notaItemView.titulo.text
                val descricao = desItemView.des.text

                val intent = Intent(itemView.context, ConsultaNota::class.java)
                intent.putExtra("NoteTitle", titulo)
                intent.putExtra("NoteDes", descricao)

                itemView.context.startActivity(intent)
            }


            button.setOnClickListener {

                val position: Int = adapterPosition + 1
                val titulo = notaItemView.titulo.text
                val descricao = desItemView.des.text

               onButtonClickListener.Edit(position, titulo.toString(), descricao.toString())

            }
        }

        companion object {
            fun create(parent: ViewGroup, onItemClickListener: OnButtonClick): NotaViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recyclerview_item, parent, false)
                return NotaViewHolder(view,onItemClickListener)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotaViewHolder(itemView, onButtonClickListener)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        //holder.notaItemView.text = current.id
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

