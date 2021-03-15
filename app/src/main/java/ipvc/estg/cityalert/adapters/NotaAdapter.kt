package ipvc.estg.cityalert.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cityalert.R
import ipvc.estg.cityalert.entities.Nota
import kotlinx.android.synthetic.main.activity_main.view.*

class NotaAdapter internal constructor(context: Context) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>() //Cached copy of notes

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notaItemView: TextView = itemView.findViewById(R.id.titulo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.notaItemView.text = current.titulo
    }

    internal fun setNotes(notas: List<Nota> ){
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size
}