package com.example.foodapp.ui.login.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.foodapp.data.repository.MealPlanRepository;
import com.example.foodapp.ui.login.LoginContract;
import com.example.foodapp.ui.login.presenter.LoginPresenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment implements LoginContract.View {
    private static final int RC_LOG_IN = 200;

    private TextView tvCreateAccount, tvErrorMessage, tvForgetPassword;
    private EditText etEmail, etPassword;
    private Button btnLoginAsGuest, btnLoginAsUser;
    private ImageButton btnGoogleLogin;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private LoginPresenter presenter;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenter(
                this,
                requireContext(),
                new FavoriteMealRepository(AppDatabase.getInstance(getContext()).favoriteMealDao()),
                new MealPlanRepository(AppDatabase.getInstance(getContext()).mealPlanDao())
        );
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
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

        tvCreateAccount.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment));
        tvForgetPassword.setOnClickListener(v -> presenter.resetPassword(etEmail.getText().toString().trim()));

        btnLoginAsGuest.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_mainFragment));
        btnLoginAsUser.setOnClickListener(v -> presenter.loginWithEmail(etEmail.getText().toString().trim(), etPassword.getText().toString().trim()));

        btnGoogleLogin.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_LOG_IN);
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOG_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    presenter.loginWithGoogle(account);
                }
            } catch (ApiException e) {
                Log.e("GoogleLogin", "Google Sign-In failed: " + e.getStatusCode());
                tvErrorMessage.setText("Google Sign-In failed. Try again.");
            }
        }
    }

    @Override
    public void showErrorMessage(String message) {
        tvErrorMessage.setText(message);
    }

    @Override
    public void navigateToMain() {
            if (!isAdded()) return;
            View view = getView();
            if (view != null) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainFragment);
            }
    }

    @Override
    public void clearFields() {
        etEmail.setText("");
        etPassword.setText("");
    }
}
