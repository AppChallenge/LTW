
package com.autodesk.tct.widget;

import java.util.Random;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.autodesk.tct.R;

public class CustomAlertDialog extends DialogFragment {
    public final static String TAG = "CustomAlertDialog";

    public final static int POSITIVE_BUTTON = 0;
    public final static int NEGATIVE_BUTTON = 1;

    private final static int[] MASCOTS = {
            R.drawable.avatar_hat, R.drawable.avatar_smile, R.drawable.avatar_start
    };

    public interface AlertDialogListener {
        public void onDialogPositiveBtnClicked(DialogFragment dialog);

        public void onDialogNegtiveBtnClicked(DialogFragment dialog);
    }

    private static CustomAlertDialog sInstance = null;
    private int mMsgStringResId;
    private int mPositiveBtnStringResId;
    private int mNegtiveBtnStringResId;
    private boolean mHasActionView = true;
    private AlertDialogListener mDialogListener = null;

    private TextView mMsgTextView;
    private TextView mActionView;
    private TextView mCancelView;
    private ImageView mMascotView;

    public static CustomAlertDialog getInstance() {
        if (sInstance == null) {
            sInstance = new CustomAlertDialog();
        }

        return sInstance;
    }

    private CustomAlertDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        mMsgTextView = (TextView) dialogView.findViewById(R.id.dialog_message_view);
        mMsgTextView.setText(mMsgStringResId);
        mActionView = (TextView) dialogView.findViewById(R.id.dialog_action_view);
        if (mHasActionView) {
            mActionView.setText(mPositiveBtnStringResId);
            mActionView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mDialogListener != null) {
                        mDialogListener.onDialogPositiveBtnClicked(CustomAlertDialog.this);
                    }
                }

            });
        } else {
            mActionView.setVisibility(View.INVISIBLE);
        }
        mCancelView = (TextView) dialogView.findViewById(R.id.dialog_cancel_view);
        mCancelView.setText(mNegtiveBtnStringResId);
        mCancelView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDialogListener != null) {
                    mDialogListener.onDialogNegtiveBtnClicked(CustomAlertDialog.this);
                }
            }

        });
        mMascotView = (ImageView) dialogView.findViewById(R.id.dialog_mascot);
        mMascotView.setImageResource(getRandomMascotResourceId());
        builder.setView(dialogView);
        return builder.create();
    }

    public void setMessage(int resId) {
        mMsgStringResId = resId;
    }

    public void setButton(int positiveResId, int negativeResId, AlertDialogListener listener) {
        mPositiveBtnStringResId = positiveResId;
        mNegtiveBtnStringResId = negativeResId;
        mDialogListener = listener;
    }

    public void setHasActionView(boolean has) {
        mHasActionView = has;
    }

    private int getRandomMascotResourceId() {
        int randomIndex = new Random().nextInt(MASCOTS.length);
        return MASCOTS[randomIndex];
    }
}
