package br.com.thomas.weyandmarvel.extension

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.switchVisibility(isVisible: Boolean) {
    if(isVisible)
        show()
    else
        hide()
}