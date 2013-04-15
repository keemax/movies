package com.movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 6/20/12
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchCharacter extends Activity {
    static final String tag = "SEARCH";
    EditText searchMovieInput;
    EditText searchCharacterInput;
    Button findMoviesButton;
    Button searchButton;
    RadioGroup searchResults;

    TmdbQuery searcher;


    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.layout_search_character);
        searcher = new TmdbQuery(getBaseContext());
        searchResults = (RadioGroup) findViewById(R.id.radiogroup_movies);
        findMoviesButton = (Button) findViewById(R.id.button_find_movie);
        findMoviesButton.setOnClickListener(findListener);
        searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(searchListener);
        searchMovieInput = (EditText) findViewById(R.id.edittext_search_movie);
        searchCharacterInput = (EditText) findViewById(R.id.edittext_search_character);

    }
    public View.OnClickListener findListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String movieQuery = "";
            try{
                movieQuery = URLEncoder.encode(searchMovieInput.getText().toString().trim(), "UTF-8");
            } catch(Exception e) {
                Log.d(tag, "failed to encode search query");
            }
            searchResults.removeAllViews();
            List<Movie> results = searcher.searchMovie(movieQuery);
            for (Movie result : results) {
                RadioButton radio = new RadioButton(getBaseContext());
                radio.setText(result.toString());
                radio.setTag(result.getId());
                searchResults.addView(radio);
            }
        }
    };

    public View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int movieId = (Integer) findViewById(searchResults.getCheckedRadioButtonId()).getTag();
            String characterQuery = "";
            try{
                characterQuery = URLEncoder.encode(searchCharacterInput.getText().toString().trim(), "UTF-8");
            } catch(Exception e) {
                Log.d(tag, "failed to encode character query");
            }
            Intent searchIntent = new Intent(SearchCharacter.this, ResultListActivity.class);
//            searchIntent.putExtra("id", movieId);
//            searchIntent.putExtra("character_query", characterQuery);
            ArrayList<CastMember> cast = (ArrayList<CastMember>) searcher.getCastByMovieId(movieId);
            searchIntent.putParcelableArrayListExtra("list", cast);
            searchIntent.putExtra("infoClass", PersonInfoActivity.class);
            startActivity(searchIntent);
        }
    };


}
