package com.example.foodapp.ui.search.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodapp.R;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.HomeRepository;
import com.example.foodapp.ui.PopupSnackbar;
import com.example.foodapp.ui.search.presenter.SearchPresenter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements OnMealClickListener, SearchView {

    private EditText searchEditText;
    private ImageButton backButton;
    private RecyclerView recyclerView;
    private Chip chipCategory, chipCountry, chipIngredient;
    private ChipGroup selectedGroupChip;

    private ChipGroup chipGroupCategory, chipGroupCountry, chipGroupIngredient;
    private Button applyButton;
    private LinearLayout filterFrame;
    private LottieAnimationView loadingProgressBar;
    private SearchAdapter searchAdapter;
    private SearchPresenter searchPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchPresenter = new SearchPresenter(this, new HomeRepository());

        searchEditText = view.findViewById(R.id.ed_search_toolbar);
        backButton = view.findViewById(R.id.back_Button);
        recyclerView = view.findViewById(R.id.recyclerView);
        chipCategory = view.findViewById(R.id.chip_category);
        chipCountry = view.findViewById(R.id.chip_country);
        chipIngredient = view.findViewById(R.id.chip_ingredient);
        filterFrame = view.findViewById(R.id.filterFrame);
        applyButton = view.findViewById(R.id.apply_Button);
        chipGroupCategory = view.findViewById(R.id.chipGroupCategory);
        chipGroupCountry = view.findViewById(R.id.chipGroupCountry);
        chipGroupIngredient = view.findViewById(R.id.chipGroupIngredient);
        loadingProgressBar = view.findViewById(R.id.lottieProgressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        searchAdapter = new SearchAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(searchAdapter);

        backButton.setOnClickListener(view1 -> navigateToMainFragment());

        applyButton.setOnClickListener(view1 -> {
            selectedGroupChip.setBackground(getResources().getDrawable(R.color.dark_green_color));
            applyButton.setVisibility(View.GONE);
            filterFrame.setVisibility(View.GONE);
        });



        searchPresenter.loadFilterOptions();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSearchEditText();
        setupFilterSelection();

        if (getArguments() != null) {
            String filterType = getArguments().getString("filterType");
            String value = getArguments().getString("value");

            if (filterType != null && value != null) {
                searchPresenter.filterBy(filterType, value);
            }else {
                searchPresenter.loadMeals();
            }
        }
    }

    private void navigateToMainFragment() {
        Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_mainFragment);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupSearchEditText() {
        searchEditText.setOnFocusChangeListener((v, hasFocus) -> updateSearchIcon(searchEditText.getText().toString()));

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                filterMeals();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSearchIcon(s.toString());
                searchPresenter.searchQueryChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        searchEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEndX = searchEditText.getRight() - searchEditText.getPaddingRight();
                if (searchEditText.getCompoundDrawablesRelative()[2] != null &&
                        event.getRawX() >= drawableEndX - searchEditText.getCompoundDrawablesRelative()[2].getBounds().width()) {
                    searchEditText.setText("");
                    return true;
                }
            }
            return false;
        });
    }

    private void setupFilterSelection() {
        chipCategory.setOnClickListener(v -> {
            showFilterGroup(chipGroupCategory);
            selectedGroupChip = chipGroupCategory;
        });
        chipCountry.setOnClickListener(v -> {
            showFilterGroup(chipGroupCountry);
            selectedGroupChip = chipGroupCountry;
        });
        chipIngredient.setOnClickListener(v -> {
            showFilterGroup(chipGroupIngredient);
            selectedGroupChip = chipGroupIngredient;
        });
    }

    private void showFilterGroup(ChipGroup selectedGroup) {
        filterFrame.removeAllViews();
        filterFrame.addView(selectedGroup);
        for (int i = 0; i < selectedGroup.getChildCount(); i++) {
            Chip chip = (Chip) selectedGroup.getChildAt(i);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    chip.setChipBackgroundColor(getResources().getColorStateList(R.color.main_color));
                } else {
                    chip.setChipBackgroundColor(getResources().getColorStateList(R.color.white2));
                }
            });
        }
        applyButton.setVisibility(View.VISIBLE);
        filterFrame.setVisibility(View.VISIBLE);
    }

    private void filterMeals() {
        List<String> categoryFilter = getSelectedFilters(chipGroupCategory);
        List<String> countryFilter = getSelectedFilters(chipGroupCountry);
        List<String> ingredientFilter = getSelectedFilters(chipGroupIngredient);
        searchPresenter.setFilters(categoryFilter, countryFilter, ingredientFilter);
    }

    private List<String> getSelectedFilters(ChipGroup chipGroup) {
        List<String> filters = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                filters.add(chip.getText().toString());
            }
        }
        return filters;
    }



    private void updateSearchIcon(String s) {
        int icon = s.isEmpty() ? R.drawable.baseline_search : R.drawable.baseline_clear_24;
        searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0);
    }

    @Override
    public void updateSearchResults(List<Meal> results) {
        searchAdapter.updateData(results);
    }

    @Override
    public void updateFilterOptions(String filterType, List<String> options) {
        ChipGroup chipGroup;

        switch (filterType) {
            case "categories":
                chipGroup = chipGroupCategory;
                break;
            case "countries":
                chipGroup = chipGroupCountry;
                break;
            case "ingredients":
                chipGroup = chipGroupIngredient;
                break;
            default:
                return;
        }
        chipGroup.removeAllViews();
        for (String option : options) {
            Chip chip = new Chip(getContext());
            chip.setText(option);
            chip.setCheckable(true);
            chip.setChipBackgroundColor(getResources().getColorStateList(R.color.white2));
            chipGroup.addView(chip);
        }
    }

    @Override
    public void clearSearchResults() {
        searchAdapter.updateData(new ArrayList<>());
    }

    @Override
    public void showAlert(String message, boolean flag) {
        new PopupSnackbar(getContext()).showMessage(message, flag);
    }

    @Override
    public void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        loadingProgressBar.playAnimation();
    }

    @Override
    public void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
        loadingProgressBar.cancelAnimation();
    }

    @Override
    public void onMealClick(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_mealDetailsFragment, bundle);
    }

}
