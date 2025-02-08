package com.example.foodapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    private Button btnLogout;
    private EditText etUsername, etEmail;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences sharedPreferences;

    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnLogout = view.findViewById(R.id.btn_logout);
        etUsername = view.findViewById(R.id.et_username);
        etEmail = view.findViewById(R.id.et_email);

        String username = sharedPreferences.getString("USERNAME", "");
        String email = sharedPreferences.getString("EMAIL", "");

        etUsername.setText(username);
        etEmail.setText(email);
        btnLogout.setOnClickListener(v -> logOut());

        return view;
    }

    private void logOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        FirebaseAuth.getInstance().signOut();

        mGoogleSignInClient.revokeAccess().addOnCompleteListener(task ->
                Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_loginFragment)
        );
    }
}
