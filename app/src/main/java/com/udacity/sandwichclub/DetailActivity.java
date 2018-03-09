package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

  public static final String EXTRA_POSITION = "extra_position";
  private static final int DEFAULT_POSITION = -1;

  @BindView(R.id.tv_aka)
  TextView akaTextView;
  @BindView(R.id.tv_ingredients)
  TextView ingredientsTextView;
  @BindView(R.id.tv_origin)
  TextView originTextView;
  @BindView(R.id.tv_description)
  TextView descriptionTextView;

  @BindColor(R.color.colorTextPrimary)
  int textPrimaryColor;
  @BindColor(R.color.colorTextSecondary)
  int textSecondaryColor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    ButterKnife.bind(this);
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
    fillTextViewFromList(akaTextView, sandwich.getAlsoKnownAs());
    fillTextViewFromList(ingredientsTextView, sandwich.getIngredients());
    fillTextViewFromString(originTextView, sandwich.getPlaceOfOrigin());
    fillTextViewFromString(descriptionTextView, sandwich.getDescription());
  }

  /**
   * Fill textView with items from list and show/hide both labelTextView and textView
   */
  private void fillTextViewFromList(TextView textView, List<String> list) {
    if (list.isEmpty()) {
      textView.setTextColor(textSecondaryColor);
      textView.setText(R.string.detail_empty);
      return;
    }
    textView.setTextColor(textPrimaryColor);
    textView.setText("");
    for (int i = 0; i < list.size(); i++) {
      textView.append(list.get(i));
      if (i != list.size() - 1) {
        textView.append(", ");
      }
    }
  }

  /**
   * Fill textView with string and show/hide both labelTextView and textView
   */
  private void fillTextViewFromString(TextView textView, String string) {
    if (string.isEmpty()) {
      textView.setTextColor(textSecondaryColor);
      textView.setText(R.string.detail_empty);
      return;
    }
    textView.setTextColor(textPrimaryColor);
    textView.setText(string);
  }
}
