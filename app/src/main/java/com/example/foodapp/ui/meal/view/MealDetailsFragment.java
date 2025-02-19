package com.example.foodapp.ui.meal.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.data.local.AppDatabase;
import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.data.remote.model.Ingredient;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.ui.meal.MealDetailsContract;
import com.example.foodapp.ui.meal.presenter.MealDetailsPresenter;
import com.example.foodapp.ui.dialogs.PopupSnackbar;
import com.example.foodapp.utils.NetworkUtil;

import java.util.List;

public class MealDetailsFragment extends Fragment implements MealDetailsContract.View {
    private TextView tvMealName, tvCategory, tvCountry, tvInstructions, tvMeasures;
    private GridLayout gridIngredients;
    private WebView webView;
    private ImageView offlineImage, imageMeal;
    private ImageButton backButton,btnAddFavourite;
    private PopupSnackbar popupSnackbar;
    private MealDetailsPresenter presenter;
    private Meal meal;

    public MealDetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            meal = getArguments().getParcelable("meal");
            presenter = new MealDetailsPresenter(this, meal, new FavoriteMealRepository(AppDatabase.getInstance(getContext()).favoriteMealDao()));
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
        imageMeal = view.findViewById(R.id.imageMeal);
        offlineImage = view.findViewById(R.id.img_offline);
        webView = view.findViewById(R.id.video);
        btnAddFavourite = view.findViewById(R.id.addToFavourite);
        backButton = view.findViewById(R.id.back_Button2);
        popupSnackbar = new PopupSnackbar(requireContext());

        presenter.loadMealDetails();

        backButton.setOnClickListener(v -> presenter.onBackPressed());

        btnAddFavourite.setOnClickListener(v -> {
            presenter.addToFavorites(new FavoriteMeal(
                    meal.getIdMeal(),
                    meal.getMealName(),
                    meal.getCategory(),
                    meal.getArea(),
                    meal.getMealThumb(),
                    meal.getIngredients(),
                    meal.getMeasures(),
                    meal.getInstructions(),
                    meal.getYoutubeLink()
            ));
        });

        return view;
    }

    private void loadMealImage(String mealThumb) {
        if (mealThumb != null && !mealThumb.isEmpty()) {
            Glide.with(requireContext())
                    .load(mealThumb)
                    .into(imageMeal);
        }
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
        loadMealImage(mealThumb);
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
        if(NetworkUtil.isNetworkAvailable(requireContext())){
            offlineImage.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            if (videoUrl != null && !videoUrl.isEmpty()) {
                setupWebView(videoUrl);
            }
        }else{
            offlineImage.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
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
    public void showMessage(String message, boolean isError) {
        popupSnackbar.showMessage(message, isError);
    }

    @Override
    public void handleBackNavigation() {
        Navigation.findNavController(getView()).navigate(R.id.action_mealDetailsFragment_to_mainFragment);
    }
}
