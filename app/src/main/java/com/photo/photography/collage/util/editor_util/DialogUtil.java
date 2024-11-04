package com.photo.photography.collage.util.editor_util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.photo.photography.R;

/**
 * Show custom dialog. Custom dialog with it own style!
 */
@SuppressLint("InflateParams")
public class DialogUtil {

    public static Dialog showCoolConfirmDialog(Context context, int titleResId, int messageResId,
                                               final ConfirmDialogOnClickListener listener) {
        String title = context.getResources().getString(titleResId);
        String message = context.getResources().getString(messageResId);
        if (Build.VERSION.SDK_INT > 20) {
            return showConfirmDialog(context, title, message, listener);
        } else {
            Dialog dialog = createCustomConfirmDialog(context, title, message, listener);
            dialog.show();
            return dialog;
        }
    }

    public static Dialog createCustomConfirmDialog(final Context context, final String title, final String content, final ConfirmDialogOnClickListener listener) {
        return createCustomConfirmDialog(context, title, content, context.getString(R.string.photo_editor_ok), context.getString(R.string.photo_editor_cancel), listener);
    }

    public static Dialog createCustomConfirmDialog(final Context context, final String title, final String content,
                                                   String confirmText, String cancelText, final ConfirmDialogOnClickListener listener) {
        final Dialog myDialog = new Dialog(context);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        final View rootView = inflater.inflate(R.layout.photo_edit_dialog_confirm, null);
        final TextView titleView = (TextView) rootView.findViewById(R.id.titleView);
        titleView.setText(title);
        final TextView contentView = (TextView) rootView.findViewById(R.id.contentView);
        contentView.setText(content);
        final TextView cancelButton = (TextView) rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                if (listener != null) {
                    listener.onCancelButtonOnClick();
                }
            }
        });

        final TextView okButton = (TextView) rootView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                if (listener != null) {
                    listener.onOKButtonOnClick();
                }
            }
        });

        cancelButton.setText(cancelText);
        okButton.setText(confirmText);
        myDialog.setTitle(title);
        myDialog.setContentView(rootView);
        myDialog.setCancelable(true);
        return myDialog;
    }

    /**
     * Show confirm dialog (Yes/No dialog)
     *
     * @param context  context that dialog will be shown
     * @param title    title of dialog
     * @param message  message of dialog
     * @param listener handle event when click button Yes/No
     * @return
     */
    public static Dialog showConfirmDialog(Context context, String title, String message,
                                           final ConfirmDialogOnClickListener listener) {
        // check context. If not check here, sometimes it can be crashed
        if (context == null)
            return null;
        Activity activity = (Activity) context;
        if (activity.isFinishing())
            return null;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message).setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null)
                            listener.onOKButtonOnClick();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (listener != null)
                    listener.onCancelButtonOnClick();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

        return alert;
    }

    /**
     * using in confirm dialog
     */
    public interface ConfirmDialogOnClickListener {
        void onOKButtonOnClick();

        void onCancelButtonOnClick();
    }

    /**
     * using in normal dialog
     */
    public interface DialogOnClickListener {
        void onOKButtonOnClick();
    }
}
