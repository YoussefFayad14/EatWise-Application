package com.example.foodapp.ui.meal.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.data.model.Ingredient;
import com.example.foodapp.data.model.Meal;
import com.example.foodapp.ui.meal.MealDetailsContract;
import com.example.foodapp.ui.meal.presenter.MealDetailsPresenter;

import java.util.List;

public class MealDetailsFragment extends Fragment implements MealDetailsContract.View {
    private TextView tvMealName, tvCategory, tvCountry, tvInstructions, tvIngredient, tvMeasures;
    private GridLayout gridIngredients;
    private WebView webView;
    private ImageButton backButton;
    private Button btnAddFavourite;
    private MealDetailsPresenter presenter;

    public MealDetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Meal meal = getArguments().getParcelable("meal");
            presenter = new MealDetailsPresenter(this, meal);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);

        tvMealName = view.findViewById(R.id.tv_mealName);
        tvCategory = view.findViewById(R.id.tv_category);
        tvCountry = view.findViewById(R.id.tv_country);
        tvInstructions = view.findViewById(R.id.tv_instructions);
        gridIngredients = view.findViewById(R.id.grid_ingredients);
        tvMeasures = view.findViewById(R.id.tv_measures);
        webView = view.findViewById(R.id.video);
        backButton = view.findViewById(R.id.back_Button2);
        btnAddFavourite = view.findViewById(R.id.btn_add_meal_favourite);

        backButton.setOnClickListener(v -> presenter.onBackPressed());

        presenter.loadMealDetails();

        return view;
    }



    private void loadIngredientsImage(List<String> ingredients) {
        gridIngredients.removeAllViews();
        for (String ingredient : ingredients) {
            if (ingredient != null && !ingredient.isEmpty()) {
                View ingredientView = LayoutInflater.from(requireContext()).inflate(R.layout.item_card_ingredient, null);
                TextView ingredientTextView = ingredientView.findViewById(R.id.itemTitle);
                ImageView ingredientImageView = ingredientView.findViewById(R.id.itemImage);
                ingredientTextView.setText(ingredient);
                Glide.with(requireContext()).
                        load(Ingredient.getImageUrlByName(ingredient)).
                        into(ingredientImageView);
                gridIngredients.addView(ingredientView);
            }
        }

    }

    @Override
    public void showMealDetails(String name, String category, String country, String instructions, List<String> ingredients, String measures, String youtubeLink, String mealThumb) {
        tvMealName.setText(name);
        tvCategory.setText(category);
        tvCountry.setText(country);
        tvInstructions.setText(instructions);
        loadIngredientsImage(ingredients);
        tvMeasures.setText(measures.trim());
        playMealVideo(youtubeLink);
    }

    @Override
    public void playMealVideo(String videoUrl) {
        if (videoUrl != null && !videoUrl.isEmpty()) {
            setupWebView(videoUrl);
        }
    }

    private void setupWebView(String youtubeUrl) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String videoId = extractVideoId(youtubeUrl);
        String embedHtml = "<html><body><iframe width='100%' height='100%' src='https://www.youtube.com/embed/" + videoId + "' frameborder='0' allowfullscreen></iframe></body></html>";

        webView.loadData(embedHtml, "text/html", "utf-8");
    }

    private String extractVideoId(String url) {
        return url.split("v=")[1];
    }

    @Override
    public void showError(String message) {
        tvMealName.setText(message);
    }

    @Override
    public void handleBackNavigation() {
        Navigation.findNavController(getView()).navigate(R.id.action_mealDetailsFragment_to_mainFragment);
    }
}
