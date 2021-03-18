package ipvc.estg.cityalert

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AdicionaNota : AppCompatActivity() {

    private lateinit var  editNotaView: EditText
    private lateinit var  editNotaView2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_note)

        editNotaView = findViewById(R.id.edit_note)
        editNotaView2 = findViewById(R.id.edit_note2)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            val replyIntent = Intent()
            if (TextUtils.isEmpty(editNotaView.text) || TextUtils.isEmpty(editNotaView2.text)){
                setResult(Activity.RESULT_CANCELED,replyIntent)
            } else {
                //val nota = editNotaView.text.toString()
                //replyIntent.putExtra(EXTRA_REPLY, nota)
                replyIntent.putExtra(EXTRA_REPLY_TITULO, editNotaView.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DESCRICAO, editNotaView2.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()

        }
    }

    companion object {

        const val EXTRA_REPLY_TITULO = "ipvc.estg.cityalert.description"
        const val EXTRA_REPLY_DESCRICAO = "ipvc.estg.cityalert.description"
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}