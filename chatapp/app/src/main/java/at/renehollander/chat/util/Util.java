package at.renehollander.chat.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

public class Util {

    public static void messageDialog(Context context, String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setTitle(title);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public static void messageDialogRunLater(Activity activity, String title, String message) {
        activity.runOnUiThread(() -> messageDialog(activity, title, message));
    }

}
