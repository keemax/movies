package com.movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 6/20/12
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class HomeActivity extends Activity {
    private final static String tag = "HOME_ACTIVITY";
    TmdbQuery searcher;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.layout_home);

        searcher = new TmdbQuery(getBaseContext());

        Button searchCharacterButton = (Button) findViewById(R.id.button_find_character);
        searchCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search = new Intent(getApplicationContext(), SearchCharacter.class);
                startActivity(search);
            }
        });

        Button searchMovieButton = (Button) findViewById(R.id.button_find_movie);
        searchMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movieQuery = "";
                EditText searchMovieInput = (EditText) findViewById(R.id.edittext_find_movie);
                try{
                    movieQuery = URLEncoder.encode(searchMovieInput.getText().toString().trim(), "UTF-8");
                } catch(Exception e) {
                    Log.d(tag, "failed to encode search query");
                }
                ArrayList<Movie> results = (ArrayList<Movie>) searcher.searchMovie(movieQuery);
                Intent movieResults = new Intent(getBaseContext(), ResultListActivity.class);
                movieResults.putParcelableArrayListExtra("list", results);
                movieResults.putExtra("listItemId", R.layout.list_item_movie_with_picture);
                movieResults.putExtra("infoClass", MovieInfoActivity.class);
                startActivity(movieResults);
            }
        });

        Button searchPersonButton = (Button) findViewById(R.id.button_find_person);
        searchPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personQuery = "";
                EditText searchPersonInput = (EditText) findViewById(R.id.edittext_find_person);
                try{
                    personQuery = URLEncoder.encode(searchPersonInput.getText().toString().trim(), "UTF-8");
                } catch(Exception e) {
                    Log.d(tag, "failed to encode search query");
                }
                ArrayList<Person> results = (ArrayList<Person>) searcher.searchPerson(personQuery);
                Intent personResults = new Intent(getBaseContext(), ResultListActivity.class);
                personResults.putParcelableArrayListExtra("list", results);
                personResults.putExtra("listItemId", R.layout.list_item_cast_member);
                personResults.putExtra("infoClass", PersonInfoActivity.class);
                startActivity(personResults);
            }
        });

        Button getNowPlayingButton = (Button) findViewById(R.id.button_now_playing);
        getNowPlayingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Movie> results = (ArrayList<Movie>) searcher.getNowPlaying();
                Intent nowPlayingResults = new Intent(getBaseContext(), ResultListActivity.class);
                nowPlayingResults.putParcelableArrayListExtra("list", results);
                nowPlayingResults.putExtra("listItemId", R.layout.list_item_movie_with_picture);
                nowPlayingResults.putExtra("infoClass", MovieInfoActivity.class);
                startActivity(nowPlayingResults);
            }
        });

        Button getPopularButton = (Button) findViewById(R.id.button_popular);
        getPopularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Movie> results = (ArrayList<Movie>) searcher.getPopular();
                Intent popularResults = new Intent(getBaseContext(), ResultListActivity.class);
                popularResults.putParcelableArrayListExtra("list", results);
                popularResults.putExtra("listItemId", R.layout.list_item_movie_with_picture);
                popularResults.putExtra("infoClass", MovieInfoActivity.class);
                startActivity(popularResults);
            }
        });

        Button getTopRatedButton = (Button) findViewById(R.id.button_top_rated);
        getTopRatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Movie> results = (ArrayList<Movie>) searcher.getTopRated();
                Intent topRatedResults = new Intent(getBaseContext(), ResultListActivity.class);
                topRatedResults.putParcelableArrayListExtra("list", results);
                topRatedResults.putExtra("listItemId", R.layout.list_item_movie_with_picture);
                topRatedResults.putExtra("infoClass", MovieInfoActivity.class);
                startActivity(topRatedResults);
            }
        });
    }

}
