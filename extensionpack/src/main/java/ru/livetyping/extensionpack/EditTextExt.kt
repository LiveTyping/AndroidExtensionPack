package ru.livetyping.extensionpack

import android.content.Context
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import ru.livetyping.extensionpack.view.FAST_CLICK_DELAY
import ru.livetyping.extensionpack.view.hideKeyboard

fun EditText.addTextChangedListener(action: (text: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            action.invoke(s.toString())
        }

    })
}

fun EditText.addDoneListener(callback: (String) -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if ((actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) && text.isNotBlank()) {
            hideKeyboard()
            callback.invoke(text.toString())
            return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
    }
}

fun EditText.setFocusListener(focusedCallback: () -> Unit) {
    var lastTime = 0L
    this.setOnFocusChangeListener { view, isHasFocus ->
        if (SystemClock.elapsedRealtime() - lastTime > FAST_CLICK_DELAY) {
            if (isHasFocus) {
                lastTime = SystemClock.elapsedRealtime()
                focusedCallback.invoke()
                view.clearFocus()
                view.hideKeyboard()
            }
        }
    }
}

fun EditText.setFocusListener(focusedCallback: () -> Unit, unFocusedCallback: () -> Unit) {
    this.setOnFocusChangeListener { _, isHasFocus ->
        if (isHasFocus) focusedCallback.invoke() else unFocusedCallback.invoke()
    }
}

fun EditText.setFocusListener(isFocused: (Boolean) -> Unit) {
    this.setOnFocusChangeListener { _, isHasFocus ->
        isFocused.invoke(isHasFocus)
    }
}

fun EditText.focusWithKeyboard() {
    this.post {
        this.isFocusableInTouchMode = true
        this.requestFocus()
        val lManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        lManager.showSoftInput(this, 0)
    }
}