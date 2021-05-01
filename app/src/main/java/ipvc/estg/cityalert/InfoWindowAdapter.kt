package ipvc.estg.cityalert


import android.app.Activity
import android.content.Context
import android.graphics.ColorSpace.get
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso


class InfoWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter {

    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.info_window, null)

    private fun getInfo(marker: Marker, view: View){

        val title = view.findViewById<TextView>(R.id.title)
        val snippet = view.findViewById<TextView>(R.id.snippet)
        val image = view.findViewById<ImageView>(R.id.imagem)


        //Divide o snippet para obter a imagem
        val info = marker.snippet.split("\n").toTypedArray()

        var descricaoIr = info[0] + "\n" + info[1] +  "\n" + info[2]


        val imageUrl = "https://cityalertcm.000webhostapp.com/myslim/api/"+info[3]



        title.text = marker.title
        snippet.text = descricaoIr

        //Utiliza a libraria Picasso para obter a imagem da irregularidade
        Picasso.get().load(imageUrl).resize(800,600).into(image)


    }

    override fun getInfoContents(m: Marker): View {
        getInfo(m, mWindow)
        return mWindow
    }

    override fun getInfoWindow(m: Marker): View {
        getInfo(m, mWindow)
        return mWindow
    }
}