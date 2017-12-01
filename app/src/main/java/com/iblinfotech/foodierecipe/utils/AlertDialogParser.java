package com.iblinfotech.foodierecipe.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.R;

public class AlertDialogParser {
    public static void showMessageDialog(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_msg_dialog);
        // set the custom dialog components - text and button
        TextView text = (TextView) dialog.findViewById(R.id.txtmsg);
        text.setText(message);
        Button dialogButton = (Button) dialog.findViewById(R.id.btncancel);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
