package com.example.foodapp.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.foodapp.R;

public class PopupSnackbar extends Dialog {

    private TextView messageTextView;
    private int displayDuration = 5000;

    public PopupSnackbar(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_snackbar);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setDimAmount(0f);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER;
        lp.y = 100;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        messageTextView = findViewById(R.id.popup_snackbar_message);
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        messageTextView.setHorizontallyScrolling(false);
        messageTextView.setLines(1);
        messageTextView.setSingleLine(false);

        GradientDrawable bgDrawable = new GradientDrawable();
        bgDrawable.setColor(ContextCompat.getColor(getContext(), android.R.color.background_light));

        float cornerRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getContext().getResources().getDisplayMetrics());
        bgDrawable.setCornerRadius(cornerRadiusPx);
        findViewById(R.id.popup_snackbar_container).setBackground(bgDrawable);

        int paddingPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getContext().getResources().getDisplayMetrics());
        findViewById(R.id.popup_snackbar_container).setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
    }



    public void showMessage(String message, boolean isError) {
        new Handler(Looper.getMainLooper()).post(() -> {
            int backgroundColor = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);

            if (isError) {
                backgroundColor = ContextCompat.getColor(getContext(), R.color.red);
            } else {
                backgroundColor = ContextCompat.getColor(getContext(), R.color.dark_green_color);
            }

            GradientDrawable bgDrawable = new GradientDrawable();
            bgDrawable.setColor(backgroundColor);

            float cornerRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getContext().getResources().getDisplayMetrics());
            bgDrawable.setCornerRadius(cornerRadiusPx);

            findViewById(R.id.popup_snackbar_container).setBackground(bgDrawable);

            messageTextView.setText(message);
            show();

            new Handler(Looper.getMainLooper()).postDelayed(PopupSnackbar.this::dismiss, displayDuration);
        });
    }
}