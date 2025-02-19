package com.example.foodapp.ui.search.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
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
import com.example.foodapp.ui.dialogs.PopupSnackbar;
import com.example.foodapp.ui.search.presenter.SearchPresenter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements OnMealClickListener, SearchView {

    private EditText searchEditText;
    private ImageButton backButton;
    private RecyclerView recyclerView;
    private Chip selectedChipGroup;
    private Chip chipAll,chipCategory, chipCountry, chipIngredient;
    private Chip selectedChip;
    private ChipGroup chipGroupCategory, chipGroupCountry, chipGroupIngredient;
    private Button applyButton;
    private LinearLayout filterFrame;
    private FrameLayout lottieOverlay;
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
        chipAll = view.findViewById(R.id.chip_all);
        chipCategory = view.findViewById(R.id.chip_category);
        chipCountry = view.findViewById(R.id.chip_country);
        chipIngredient = view.findViewById(R.id.chip_ingredient);
        filterFrame = view.findViewById(R.id.filterFrame);
        applyButton = view.findViewById(R.id.apply_Button);
        chipGroupCategory = view.findViewById(R.id.chipGroupCategory);
        chipGroupCountry = view.findViewById(R.id.chipGroupCountry);
        chipGroupIngredient = view.findViewById(R.id.chipGroupIngredient);
        lottieOverlay = view.findViewById(R.id.lottieOverlay);
        loadingProgressBar = view.findViewById(R.id.lottieProgressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        searchAdapter = new SearchAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(searchAdapter);

        onBack();
        backButton.setOnClickListener(view1 -> navigateToMainFragment());

        applyButton.setOnClickListener(view1 -> {
            searchPresenter.filterBy(selectedFilterType(), selectedChip.getText().toString());
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyButton.setVisibility(View.GONE);
                filterFrame.setVisibility(View.GONE);
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

    @SuppressLint("SetTextI18n")
    private void setupFilterSelection() {
        chipAll.setChecked(true);
        chipAll.setChipBackgroundColorResource(R.color.dark_green_color);
        chipAll.setTextColor(getResources().getColor(R.color.white));

        chipAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            searchPresenter.clearAllFilters();
            filterFrame.setVisibility(View.GONE);
            applyButton.setVisibility(View.GONE);
            if (isChecked) {
                chipAll.setChipBackgroundColorResource(R.color.dark_green_color);
                chipAll.setTextColor(getResources().getColor(R.color.white));

                chipCategory.setChipBackgroundColorResource(R.color.white);
                chipCategory.setTextColor(getResources().getColor(R.color.black));

                chipCountry.setChipBackgroundColorResource(R.color.white);
                chipCountry.setTextColor(getResources().getColor(R.color.black));

                chipIngredient.setChipBackgroundColorResource(R.color.white);
                chipIngredient.setTextColor(getResources().getColor(R.color.black));

            } else {
                chipAll.setChipBackgroundColorResource(R.color.white);
                chipAll.setTextColor(getResources().getColor(R.color.black));
            }
        });

        chipCategory.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedChipGroup = chipCategory;
            if (isChecked) {
                chipCategory.setChipBackgroundColorResource(R.color.dark_green_color);
                chipCategory.setTextColor(getResources().getColor(R.color.white));

                chipAll.setChipBackgroundColorResource(R.color.white);
                chipAll.setTextColor(getResources().getColor(R.color.black));

                chipCountry.setChipBackgroundColorResource(R.color.white);
                chipCountry.setTextColor(getResources().getColor(R.color.black));

                chipIngredient.setChipBackgroundColorResource(R.color.white);
                chipIngredient.setTextColor(getResources().getColor(R.color.black));
            } else {
                chipCategory.setChipBackgroundColorResource(R.color.white);
                chipCategory.setTextColor(getResources().getColor(R.color.black));
            }
            showFilterGroup(chipGroupCategory);
        });

        chipCountry.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedChipGroup = chipCountry;
            if (isChecked) {
                chipCountry.setChipBackgroundColorResource(R.color.dark_green_color);
                chipCountry.setTextColor(getResources().getColor(R.color.white));

                chipAll.setChipBackgroundColorResource(R.color.white);
                chipAll.setTextColor(getResources().getColor(R.color.black));

                chipCategory.setChipBackgroundColorResource(R.color.white);
                chipCategory.setTextColor(getResources().getColor(R.color.black));

                chipIngredient.setChipBackgroundColorResource(R.color.white);
                chipIngredient.setTextColor(getResources().getColor(R.color.black));
            } else {
                chipCountry.setChipBackgroundColorResource(R.color.white);
                chipCountry.setTextColor(getResources().getColor(R.color.black));
            }
            showFilterGroup(chipGroupCountry);
        });

        chipIngredient.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedChipGroup = chipIngredient;
            if (isChecked) {
                chipIngredient.setChipBackgroundColorResource(R.color.dark_green_color);
                chipIngredient.setTextColor(getResources().getColor(R.color.white));

                chipAll.setChipBackgroundColorResource(R.color.white);
                chipAll.setTextColor(getResources().getColor(R.color.black));

                chipCategory.setChipBackgroundColorResource(R.color.white);
                chipCategory.setTextColor(getResources().getColor(R.color.black));

                chipCountry.setChipBackgroundColorResource(R.color.white);
                chipCountry.setTextColor(getResources().getColor(R.color.black));
            } else {
                chipIngredient.setChipBackgroundColorResource(R.color.white);
                chipIngredient.setTextColor(getResources().getColor(R.color.black));
            }
            showFilterGroup(chipGroupIngredient);
        });
    }

    private String selectedFilterType() {
        if (selectedChipGroup == chipCategory) {
            return "categories";
        } else if (selectedChipGroup == chipCountry) {
            return "countries";
        } else if (selectedChipGroup == chipIngredient) {
            return "ingredients";
        }
        return null;
    }
    private void showFilterGroup(ChipGroup selectedGroup) {
        filterFrame.removeAllViews();
        filterFrame.addView(selectedGroup);
        for (int i = 0; i < selectedGroup.getChildCount(); i++) {
            Chip chip = (Chip) selectedGroup.getChildAt(i);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    searchPresenter.clearAllFilters();
                    clearOtherSelections(selectedGroup, chip);
                    selectedChip = chip;
                    chip.setChipBackgroundColor(getResources().getColorStateList(R.color.main_color));
                } else {
                    chip.setChipBackgroundColor(getResources().getColorStateList(R.color.white2));
                }
            });
        }

        applyButton.setVisibility(View.VISIBLE);
        filterFrame.setVisibility(View.VISIBLE);
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

    private void clearOtherSelections(ChipGroup chipGroup, Chip selectedChip) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip != selectedChip) {
                chip.setChecked(false);
            }
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
        lottieOverlay.setVisibility(View.VISIBLE);
        loadingProgressBar.playAnimation();
    }

    @Override
    public void hideLoading() {
        lottieOverlay.setVisibility(View.GONE);
        loadingProgressBar.cancelAnimation();
    }

    @Override
    public void onMealClick(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_mealDetailsFragment, bundle);
    }
    private void onBack(){
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {}
        });
    }

}
