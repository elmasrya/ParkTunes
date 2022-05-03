package io.parkmobile.parktunes.global.utils

import android.content.Context
import android.widget.Toast

class ParkTunesToast(private val context: Context, private val resID: Int) {
    fun showShort() {
        Toast.makeText(context, context.getString(resID), Toast.LENGTH_SHORT).show();
    }

    fun showLong() {
        Toast.makeText(context, context.getString(resID), Toast.LENGTH_LONG).show();
    }
}

