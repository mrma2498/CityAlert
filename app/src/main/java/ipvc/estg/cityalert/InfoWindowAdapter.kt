package ipvc.estg.cityalert

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class InfoWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter {


    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.info_window, null)

    private fun getInfo(marker: Marker, view: View){

        val title = view.findViewById<TextView>(R.id.title)
        val snippet = view.findViewById<TextView>(R.id.snippet)

        title.text = marker.title
        snippet.text = marker.snippet

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