package com.example.foodapp.ui.splash.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodapp.R;
import com.example.foodapp.ui.splash.SplashContract;
import com.example.foodapp.ui.splash.presenter.SplashPresenter;

public class SplashFragment extends Fragment implements SplashContract.View {
    private SplashPresenter presenter;

    public SplashFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        requireActivity().getWindow().setNavigationBarColor(Color.TRANSPARENT);

        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        );
        presenter = new SplashPresenter(this, requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.checkUserLogin();
    }

    @Override
    public void navigateToMain() {
        Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_mainFragment);
    }

    @Override
    public void navigateToLogin() {
        new Handler().postDelayed(() ->
                        Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_loginFragment),
                3000
        );
    }
}
