package ipvc.estg.cityalert

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_new_note.*

class AdicionaNota : AppCompatActivity() {

    private lateinit var  addTitleView: EditText
    private lateinit var  addDesView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_note)

        addTitleView = findViewById(R.id.addTitle)
        addDesView = findViewById(R.id.addDescription)


        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            val replyIntent = Intent()
            if (TextUtils.isEmpty(addTitleView.text) || TextUtils.isEmpty(addDesView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)

                if (TextUtils.isEmpty(addTitleView.text) && TextUtils.isEmpty(addDesView.text)) {
                    addTitleView.error = resources.getString(R.string.empty);
                    addDesView.error = resources.getString(R.string.empty);

                }

                else if (TextUtils.isEmpty(addTitleView.text)){
                    addTitleView.error = resources.getString(R.string.empty);
                }

                else if (TextUtils.isEmpty(addDesView.text)){
                    addDesView.error = resources.getString(R.string.empty);
                }

            }

            else {

                replyIntent.putExtra(EXTRA_REPLY_TITULO, addTitleView.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DESCRICAO, addDesView.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }


        }
    }

    companion object {

        const val EXTRA_REPLY_TITULO = "ipvc.estg.cityalert.titulo"
        const val EXTRA_REPLY_DESCRICAO = "ipvc.estg.cityalert.description"
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}