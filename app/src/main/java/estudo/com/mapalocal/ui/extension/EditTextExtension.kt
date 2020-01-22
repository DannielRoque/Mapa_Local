package estudo.com.mapalocal.ui.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class EditTextExtension {
    fun EditText.onChangeText(delegate: (text: String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(
                textType: CharSequence?,start: Int,before: Int,count: Int) {
                delegate(textType.toString())
            }
        })
    }
}