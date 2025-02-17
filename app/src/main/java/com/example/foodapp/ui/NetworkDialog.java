package com.example.foodapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodapp.R;
import com.example.foodapp.utils.NetworkUtil;

public class NetworkDialog extends DialogFragment {

    private RetryCallback callback;

    public interface RetryCallback {
        void onRetry();
    }

    public NetworkDialog(RetryCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_network, container, false);

        LottieAnimationView animationView = view.findViewById(R.id.retryLottieAnimation);
        TextView textViewMessage = view.findViewById(R.id.textMessage);
        Button btnRetry = view.findViewById(R.id.btnRetry);


        btnRetry.setOnClickListener(v -> {
            if (NetworkUtil.isNetworkAvailable(getContext())) {
                callback.onRetry();
                dismiss();
            } else {
                textViewMessage.setText(R.string.still_offline_check_your_internet);
            }
        });
        setCancelable(true);
        return view;
    }
}
