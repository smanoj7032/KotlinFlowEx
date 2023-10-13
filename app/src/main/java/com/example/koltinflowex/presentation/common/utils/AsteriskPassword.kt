package com.example.koltinflowex.presentation.common.utils

import android.text.method.PasswordTransformationMethod
import android.view.View

class AsteriskPassword : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    inner class PasswordCharSequence(private val mSource: CharSequence) : CharSequence {
        override val length: Int
            get() = mSource.length

        override fun get(index: Int): Char {
            return '*'
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return mSource.subSequence(startIndex, endIndex)
        }
    }
}