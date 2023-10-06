package com.crystal.listeningcat.util

import android.content.Context

class PrefUtil {
    companion object {
        private const val PREF_NAME = "listenCatPref"
        private const val TEXT_SIZE = "text_size_seek"

        fun setTextSizePref(context: Context, size: Float) {
            val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
            with(pref.edit()) {
                putFloat(TEXT_SIZE, size)
                commit()
            }
        }

        fun getTextSizePref(context: Context): Float {
            val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return pref.getFloat(TEXT_SIZE, 20f)
        }
    }
}