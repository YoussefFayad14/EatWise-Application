package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
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


public class LoginFragment extends Fragment {
    private TextView tvCreateAccount, tvErrorMessage, tvForgetPassword;
    private EditText etEmail, etPassword;
    private Button btnLoginAsGuest,btnLoginAsUser;
    private ImageButton btnGoogleLogin, btnFacebookLogin, btnTwitterLogin;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_Log_IN = 200;



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
        btnFacebookLogin = view.findViewById(R.id.btn_facebook_login);
        btnTwitterLogin = view.findViewById(R.id.btn_twitter_login);

        tvCreateAccount.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
        });

        btnLoginAsGuest.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainFragment);
        });
        btnLoginAsUser.setOnClickListener(view1 -> loginAsUser());
        btnGoogleLogin.setOnClickListener(view1 -> loginWithGoogle());

    }

    private void loginWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        startActivityForResult(googleSignInClient.getSignInIntent(), RC_Log_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_Log_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                auth.signInWithCredential(credential).addOnCompleteListener(authTask -> {
                    if (authTask.isSuccessful()) {
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_mainFragment);
                    } else {
                        tvErrorMessage.setText("Google login failed");
                    }
                });
            }
        }
    }

    private void loginAsUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            tvErrorMessage.setText("Enter email and password");

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvErrorMessage.setText("Invalid email format");

        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult ->
                            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_mainFragment)
                    )
                    .addOnFailureListener(e -> {
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            tvErrorMessage.setText("No account found with this email");
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            tvErrorMessage.setText("Incorrect password");
                        } else if (e instanceof FirebaseNetworkException) {
                            tvErrorMessage.setText("Network error. Check your connection");
                        } else {
                            tvErrorMessage.setText(e.getMessage());
                        }
                    });
        }
    }


}