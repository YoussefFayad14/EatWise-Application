package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.text.TextUtils;
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
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterFragment extends Fragment {
    private EditText etUsername, etEmail, etPassword;
    private TextView tv_BackToLogin, tvErrorMessage;
    private Button btnRegister;
    private ImageButton btnGoogleRegister;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_UP = 200;

    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

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
        btnRegister.setOnClickListener(view1 -> registerUser());
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
        if (requestCode == RC_SIGN_UP && data != null) {
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                tvErrorMessage.setText("Google sign-in failed: " + e.getStatusCode());
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        btnRegister.setEnabled(false);
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    btnRegister.setEnabled(true);
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_mainFragment);
                    }
                })
                .addOnFailureListener(e -> {
                    btnRegister.setEnabled(true);
                    tvErrorMessage.setText("Authentication failed: " + e.getMessage());
                });
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            tvErrorMessage.setText("Email and Password cannot be empty");
            return;
        }
        if (password.length() < 6) {
            tvErrorMessage.setText("Password must be at least 6 characters");
            return;
        }

        btnRegister.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    btnRegister.setEnabled(true);
                    Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_mainFragment);
                })
                .addOnFailureListener(e -> {
                    btnRegister.setEnabled(true);
                    if (e instanceof FirebaseAuthInvalidCredentialsException)
                        tvErrorMessage.setText("Invalid email format");
                    else if (e instanceof FirebaseAuthUserCollisionException) {
                        tvErrorMessage.setText("Email already in use");
                    } else if (e instanceof FirebaseNetworkException) {
                        tvErrorMessage.setText("Network error. Check your connection");
                    } else {
                        tvErrorMessage.setText(e.getMessage());
                    }
                });
    }
}
