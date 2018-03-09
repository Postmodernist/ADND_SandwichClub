package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

  public static Sandwich parseSandwichJson(String json) throws JSONException {
    if (json == null || json.isEmpty())
      return null;

    // Json keys
    final String NAME = "name";
    final String NAME_MAIN = "mainName";
    final String NAME_AKA = "alsoKnownAs";
    final String ORIGIN = "placeOfOrigin";
    final String DESCRIPTION = "description";
    final String IMAGE_URL = "image";
    final String INGREDIENTS = "ingredients";

    String mainName;
    List<String> alsoKnownAs = new ArrayList<>();
    String placeOfOrigin;
    String description;
    String image;
    List<String> ingredients = new ArrayList<>();

    // Parse JSON
    JSONObject jRoot = new JSONObject(json);
    JSONObject jName = jRoot.getJSONObject(NAME);
    mainName = jName.getString(NAME_MAIN);
    JSONArray jAka = jName.getJSONArray(NAME_AKA);
    for (int i = 0; i < jAka.length(); i++) {
      alsoKnownAs.add(jAka.getString(i));
    }
    placeOfOrigin = jRoot.getString(ORIGIN);
    description = jRoot.getString(DESCRIPTION);
    image = jRoot.getString(IMAGE_URL);
    JSONArray jIngredients = jRoot.getJSONArray(INGREDIENTS);
    for (int i = 0; i < jIngredients.length(); i++) {
      ingredients.add(jIngredients.getString(i));
    }

    return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
  }
}
