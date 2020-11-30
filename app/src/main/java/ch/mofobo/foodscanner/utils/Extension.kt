package ch.mofobo.foodscanner.utils

import android.widget.ImageView

fun ImageView.loadUrl(url: String) {
    if (url.isNotBlank()) {
        Dali.get().load(url).into(this)
    }
}