package com.android.ichooseyou.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class Alert(context: Context?, alertType: AlertType?) {
    enum class AlertType {
        INFORMATION,
        WARNING,
        ERROR,
        CONFIRMATION
    }

    private val builder: AlertDialog.Builder

    init {
        builder = AlertDialog.Builder(context!!)
        when (alertType) {
            AlertType.INFORMATION -> builder.setTitle("Information")
            AlertType.WARNING -> builder.setTitle("Warning")
            AlertType.ERROR -> builder.setTitle("Error")
            AlertType.CONFIRMATION -> builder.setTitle("Confirmation")
            null -> TODO()
        }
    }

    fun setContent(message: String?): Alert {
        builder.setMessage(message)
        return this
    }

    fun setPositiveButton(text: String?, listener: DialogInterface.OnClickListener?): Alert {
        builder.setPositiveButton(text, listener)
        return this
    }

    fun setNegativeButton(text: String?, listener: DialogInterface.OnClickListener?): Alert {
        builder.setNegativeButton(text, listener)
        return this
    }

    fun setNeutralButton(text: String?, listener: DialogInterface.OnClickListener?): Alert {
        builder.setNeutralButton(text, listener)
        return this
    }

    fun show() {
        builder.create().show()
    }
}
