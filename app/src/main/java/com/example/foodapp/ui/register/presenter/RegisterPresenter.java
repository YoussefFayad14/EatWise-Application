package com.example.foodapp.ui.register.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.foodapp.R;
import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.data.repository.MealPlanRepository;
import com.example.foodapp.ui.register.RegisterContract;
import com.example.foodapp.utils.DataSyncUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterPresenter implements RegisterContract.Presenter {
    private RegisterContract.View view;
    private FirebaseAuth mAuth;
    private Context context;
    private DataSyncUtil dataSyncUtil;

    public RegisterPresenter(RegisterContract.View view, Context context, FavoriteMealRepository favoriteMealRepository, MealPlanRepository mealPlanRepository) {
        this.view = view;
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.dataSyncUtil = new DataSyncUtil(favoriteMealRepository, mealPlanRepository);
    }

    @Override
    public void registerUser(String username,String email, String password) {
        if (TextUtils.isEmpty(username)) {
            view.showErrorMessage("Username cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            view.showErrorMessage("Email and Password cannot be empty");
            return;
        }
        if (password.length() < 6) {
            view.showErrorMessage("Password must be at least 6 characters");
            return;
        }

        view.setRegisterButtonEnabled(false);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    view.setRegisterButtonEnabled(true);
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        saveUserLoginState(username, user.getEmail());
                        dataSyncUtil.syncUserData();
                        view.navigateToMain();
                    }
                })
                .addOnFailureListener(e -> {
                    view.setRegisterButtonEnabled(true);
                    if (e instanceof FirebaseAuthInvalidCredentialsException)
                        view.showErrorMessage("Invalid email format");
                    else if (e instanceof FirebaseAuthUserCollisionException)
                        view.showErrorMessage("Email already in use");
                    else if (e instanceof FirebaseNetworkException)
                        view.showErrorMessage("Network error. Check your connection");
                    else
                        view.showErrorMessage(e.getMessage());
                });
    }

    @Override
    public void signInWithGoogle(GoogleSignInAccount account) {
        if (account == null || account.getIdToken() == null) {
            view.showErrorMessage("Google Sign-In failed. No account selected.");
            return;
        }

        view.setRegisterButtonEnabled(false);
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    view.setRegisterButtonEnabled(true);
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        saveUserLoginState(user.getDisplayName(), user.getEmail());
                        dataSyncUtil.syncUserData();
                        view.navigateToMain();
                    } else {
                        view.showErrorMessage("Google Sign-In failed. No user found.");
                    }
                })
                .addOnFailureListener(e -> {
                    view.setRegisterButtonEnabled(true);
                    if (e instanceof FirebaseAuthUserCollisionException) {
                        view.showErrorMessage("Google account already linked. Try logging in.");
                    } else if (e instanceof FirebaseNetworkException) {
                        view.showErrorMessage("Network error. Check your connection.");
                    } else {
                        view.showErrorMessage("Authentication failed: " + e.getMessage());
                    }
                });
    }


    private void saveUserLoginState(String username, String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IS_LOGGED_IN", true);
        editor.putString("USERNAME", username);
        editor.putString("EMAIL", email);
        editor.apply();
    }
}
