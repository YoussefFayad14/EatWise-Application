package com.example.foodapp.ui.profile.view;

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
import com.example.foodapp.R;
import com.example.foodapp.ui.profile.presenter.ProfilePresenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ProfileFragment extends Fragment implements ProfileView {

    private Button btnLogout;
    private EditText etUsername, etEmail;
    private ProfilePresenter presenter;
    private GoogleSignInClient mGoogleSignInClient;

    public ProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        presenter = new ProfilePresenter(requireContext(), mGoogleSignInClient, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnLogout = view.findViewById(R.id.btn_logout);
        etUsername = view.findViewById(R.id.et_username);
        etEmail = view.findViewById(R.id.et_email);

        presenter.loadUserData();

        btnLogout.setOnClickListener(v -> presenter.logOut());

        return view;
    }

    @Override
    public void displayUserData(String username, String email) {
        if (isAdded()) {
            etUsername.setText(username);
            etEmail.setText(email);
        }
    }

    @Override
    public void navigateToLogin() {
        if (getView() != null && isAdded()) {
            Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_loginFragment);
        }
    }
}
