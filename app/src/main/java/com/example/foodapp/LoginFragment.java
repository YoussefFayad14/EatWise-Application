package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;

public class LoginFragment extends Fragment {
    private TextView tvCreateAccount, tvErrorMessage, tvForgetPassword;
    private EditText etEmail, etPassword;
    private Button btnLoginAsGuest, btnLoginAsUser;
    private ImageButton btnGoogleLogin;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_LOG_IN = 200;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCreateAccount = view.findViewById(R.id.tv_CreateAccount);
        tvErrorMessage = view.findViewById(R.id.tv_error_message);
        tvForgetPassword = view.findViewById(R.id.tv_forgetPassword);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLoginAsGuest = view.findViewById(R.id.btn_loginAsGuest);
        btnLoginAsUser = view.findViewById(R.id.btn_loginAsUser);
        btnGoogleLogin = view.findViewById(R.id.btn_google_login);

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

        tvCreateAccount.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment));

        tvForgetPassword.setOnClickListener(v -> resetPassword());


        btnLoginAsGuest.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_mainFragment));

        btnLoginAsUser.setOnClickListener(v -> loginAsUser());
        btnGoogleLogin.setOnClickListener(v -> loginWithGoogle());
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
    private void resetPassword() {
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            tvErrorMessage.setText("Enter your email to reset password");
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> {
                    tvErrorMessage.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
                    tvErrorMessage.setText("Reset link sent to your email");
                    etEmail.setText("");
                    etPassword.setText("");
                    tvErrorMessage.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
                })
                .addOnFailureListener(e ->
                        tvErrorMessage.setText("Failed to send reset email: " + e.getMessage()));
    }

    private void loginWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        startActivityForResult(googleSignInClient.getSignInIntent(), RC_LOG_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOG_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult();
                if (account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    auth.signInWithCredential(credential).addOnCompleteListener(authTask -> {
                        if (authTask.isSuccessful()) {
                            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_mainFragment);
                        } else {
                            tvErrorMessage.setText("Google login failed: " + authTask.getException().getMessage());
                        }
                    });
                }
            } else {
                tvErrorMessage.setText("Google Sign-In failed.");
            }
        }
    }

    private void loginAsUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            tvErrorMessage.setText("Enter email and password.");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvErrorMessage.setText("Invalid email format.");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null && user.isEmailVerified()) {
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_mainFragment);
                    } else {
                        tvErrorMessage.setText("Email not verified. Please check your email.");
                    }
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        tvErrorMessage.setText("No account found with this email.");
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        tvErrorMessage.setText("Incorrect password.");
                    } else if (e instanceof FirebaseNetworkException) {
                        tvErrorMessage.setText("Network error. Check your connection.");
                    } else {
                        tvErrorMessage.setText("Login failed: " + e.getMessage());
                    }
                });
    }
}
