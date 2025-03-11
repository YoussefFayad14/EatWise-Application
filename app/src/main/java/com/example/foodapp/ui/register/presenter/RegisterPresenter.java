package com.example.foodapp.ui.register.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.example.foodapp.data.repository.FavoriteMealsRepository;
import com.example.foodapp.data.repository.WeekPlanMealsRepository;
import com.example.foodapp.ui.register.ui.RegisterView;
import com.example.foodapp.utils.DataSyncUtil;
import com.example.foodapp.utils.UserPreferences;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterPresenter {
    private RegisterView view;
    private FirebaseAuth mAuth;
    private UserPreferences userPreferences;
    private Context context;
    private DataSyncUtil dataSyncUtil;


    public RegisterPresenter(RegisterView view, Context context, FavoriteMealsRepository favoriteMealsRepository, WeekPlanMealsRepository weekPlanMealsRepository) {
        this.view = view;
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.dataSyncUtil = new DataSyncUtil(context, favoriteMealsRepository, weekPlanMealsRepository);
        this.userPreferences = new UserPreferences(context);
    }

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
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();
                        user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                userPreferences.saveUserLogin(username, user.getEmail());
                                dataSyncUtil.syncUserData();
                                view.navigateToMain();
                            }
                        });
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
                        userPreferences.saveUserLogin(user.getDisplayName(), user.getEmail());
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
}
