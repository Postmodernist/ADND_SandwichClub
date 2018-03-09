package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

  public static final String EXTRA_POSITION = "extra_position";
  private static final int DEFAULT_POSITION = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    ImageView ingredientsIv = findViewById(R.id.iv_image);

    Intent intent = getIntent();
    int position = DEFAULT_POSITION;
    if (intent != null) {
      position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
    } else {
      closeOnError();
    }
    if (position == DEFAULT_POSITION) {
      // EXTRA_POSITION not found in intent
      closeOnError();
      return;
    }

    String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
    String json = sandwiches[position];
    Sandwich sandwich = null;
    try {
      sandwich = JsonUtils.parseSandwichJson(json);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    if (sandwich == null) {
      // Sandwich data unavailable
      closeOnError();
      return;
    }

    populateUI(sandwich);
    Picasso.with(this)
        .load(sandwich.getImage())
        .into(ingredientsIv);

    setTitle(sandwich.getMainName());
  }

  private void closeOnError() {
    finish();
    Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
  }

  private void populateUI(Sandwich sandwich) {
    // Get views
    TextView akaLabelTextView = findViewById(R.id.tv_aka_label);
    TextView akaTextView = findViewById(R.id.tv_aka);
    TextView ingredientsLabelTextView = findViewById(R.id.tv_ingredients_label);
    TextView ingredientsTextView = findViewById(R.id.tv_ingredients);
    TextView originLabelTextView = findViewById(R.id.tv_origin_label);
    TextView originTextView = findViewById(R.id.tv_origin);
    TextView descriptionLabelTextView = findViewById(R.id.tv_description_label);
    TextView descriptionTextView = findViewById(R.id.tv_description);

    fillTextViewFromList(akaLabelTextView, akaTextView, sandwich.getAlsoKnownAs());
    fillTextViewFromList(ingredientsLabelTextView, ingredientsTextView, sandwich.getIngredients());
    fillTextViewFromString(originLabelTextView, originTextView, sandwich.getPlaceOfOrigin());
    fillTextViewFromString(descriptionLabelTextView, descriptionTextView, sandwich.getDescription());
  }

  /**
   * Fill textView with items from list and show/hide both labelTextView and textView
   */
  private void fillTextViewFromList(TextView labelTextView, TextView textView, List<String> list) {
    if (list.isEmpty()) {
      labelTextView.setVisibility(View.GONE);
      textView.setVisibility(View.GONE);
    } else {
      textView.setText("");
      for (int i = 0; i < list.size(); i++) {
        textView.append(list.get(i));
        if (i != list.size() - 1) {
          textView.append(", ");
        }
      }
      labelTextView.setVisibility(View.VISIBLE);
      textView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Fill textView with string and show/hide both labelTextView and textView
   */
  private void fillTextViewFromString(TextView labelTextView, TextView textView, String string) {
    if (string.isEmpty()) {
      labelTextView.setVisibility(View.GONE);
      textView.setVisibility(View.GONE);
    } else {
      textView.setText(string);
      labelTextView.setVisibility(View.VISIBLE);
      textView.setVisibility(View.VISIBLE);
    }
  }
}
