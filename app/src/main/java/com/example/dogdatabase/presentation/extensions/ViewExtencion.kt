package com.example.dogdatabase.presentation.extensions

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.getTextOrSetError(): String? {
    return editText?.text?.toString()
        ?.ifBlank {
            error = "ERROR"
            null
        }
}