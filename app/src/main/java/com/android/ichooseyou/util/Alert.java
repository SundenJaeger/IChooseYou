package com.android.ichooseyou.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class Alert {
    public enum AlertType {
        INFORMATION, WARNING, ERROR, CONFIRMATION;
    }

    private final AlertDialog.Builder builder;

    public Alert(Context context, Alert.AlertType alertType) {
        builder = new AlertDialog.Builder(context);

        switch (alertType) {
            case INFORMATION:
                builder.setTitle("Information");
                break;

            case WARNING:
                builder.setTitle("Warning");
                break;

            case ERROR:
                builder.setTitle("Error");
                break;

            case CONFIRMATION:
                builder.setTitle("Confirmation");
                break;
        }
    }

    public Alert setContent(String message) {
        builder.setMessage(message);
        return this;
    }

    public Alert setPositiveButton(String text, DialogInterface.OnClickListener listener) {
        builder.setPositiveButton(text, listener);
        return this;
    }

    public Alert setNegativeButton(String text, DialogInterface.OnClickListener listener) {
        builder.setNegativeButton(text, listener);
        return this;
    }

    public Alert setNeutralButton(String text, DialogInterface.OnClickListener listener) {
        builder.setNeutralButton(text, listener);
        return this;
    }

    public void show() {
        builder.create().show();
    }
}
