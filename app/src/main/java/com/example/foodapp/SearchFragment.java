package com.example.foodapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private ImageButton backButton;

    public SearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.ed_search_toolbar);
        backButton =view.findViewById(R.id.back_Button);

        backButton.setOnClickListener(view1 -> {
            navigateToMainFragment();

        });

        setupSearchEditText();

        return view;
    }

    private void navigateToMainFragment() {
        Navigation.findNavController(getView()).navigate(R.id.action_searchFragment_to_mainFragment);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupSearchEditText() {
        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_clear_24, 0);
            } else {
                searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_search, 0);
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_clear_24, 0);
                } else {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_search, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        searchEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEndX = searchEditText.getRight() - searchEditText.getPaddingRight();
                if (event.getRawX() >= drawableEndX - searchEditText.getCompoundDrawables()[2].getBounds().width()) {
                    searchEditText.setText("");
                    return true;
                }
            }
            return false;
        });
    }
}
