package com.example.foodapp.ui.register.ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.foodapp.R;
import com.example.foodapp.data.local.AppDatabase;
import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.data.repository.WeekPlanRepository;
import com.example.foodapp.ui.register.RegisterContract;
import com.example.foodapp.ui.register.presenter.RegisterPresenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class RegisterFragment extends Fragment implements RegisterContract.View {
    private EditText etUsername, etEmail, etPassword;
    private TextView tv_BackToLogin, tvErrorMessage;
    private Button btnRegister;
    private ImageButton btnGoogleRegister;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_UP = 200;
    private RegisterPresenter presenter;

    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RegisterPresenter(
                this,
                requireContext(),
                new FavoriteMealRepository(AppDatabase.getInstance(getContext()).favoriteMealDao()),
                new WeekPlanRepository(AppDatabase.getInstance(getContext()).mealPlanDao())
                );

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etUsername = view.findViewById(R.id.et_username);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        tv_BackToLogin = view.findViewById(R.id.tv_BacktoLogin);
        tvErrorMessage = view.findViewById(R.id.tv_error_message);
        btnRegister = view.findViewById(R.id.btn_register);
        btnGoogleRegister = view.findViewById(R.id.btn_google_register);

        etPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEnd = 2;
                if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });

        tv_BackToLogin.setOnClickListener(view1 ->
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        );

        btnRegister.setOnClickListener(view1 ->
                presenter.registerUser(
                        etUsername.getText().toString().trim(),
                        etEmail.getText().toString().trim(),
                        etPassword.getText().toString().trim())
        );
        btnGoogleRegister.setOnClickListener(view1 -> signInWithGoogle());
    }

    private void togglePasswordVisibility() {
        if (etPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);
        }
        etPassword.setSelection(etPassword.getText().length());
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_UP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_UP) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                presenter.signInWithGoogle(account);
            } else {
                showErrorMessage("Google Sign-In failed. Try again.");
            }
        } catch (ApiException e) {
            showErrorMessage("Google Sign-In error: " + e.getStatusCode());
        }
    }

    @Override
    public void showErrorMessage(String message) {
        tvErrorMessage.setText(message);
    }

    @Override
    public void navigateToMain() {
        Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_mainFragment);
    }

    @Override
    public void setRegisterButtonEnabled(boolean enabled) {
        btnRegister.setEnabled(enabled);
    }
}
