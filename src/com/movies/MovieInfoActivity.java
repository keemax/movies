package com.movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.*;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 7/10/12
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class MovieInfoActivity extends Activity {
    private static final String tag = "MOVIE_INFO_ACTIVITY";

    ImageView poster;
    TextView title;
    TextView mpaaRating;
    TextView releaseDate;
    TextView runtime;
    TextView rating;
    TextView tomatometer;
    TextView tagline;
    TextView homepage;
    TextView imdbLink;
    TextView moneyInfo;
    TextView overview;
    LinearLayout directorView;
    LinearLayout writerView;
    TextView fullCast;
    TextView fullCrew;

    TmdbQuery searcher;
    ResultListAdapter adapter;

    int movieId;
    Movie movie;

    ArrayList<CrewMember> crew;
    ArrayList<CrewMember> directors;
    ArrayList<CrewMember> writers;
    ArrayList<CastMember> cast;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.layout_movie_info);
        searcher = new TmdbQuery(getBaseContext());

        poster = (ImageView) findViewById(R.id.image_movie_poster);
        title = (TextView) findViewById(R.id.text_movie_title);
        mpaaRating = (TextView) findViewById(R.id.text_movie_mpaa_rating);
        releaseDate = (TextView) findViewById(R.id.text_movie_release_date);
        runtime = (TextView) findViewById(R.id.text_movie_runtime);
        rating = (TextView) findViewById(R.id.text_movie_rating);
        tomatometer = (TextView) findViewById(R.id.text_movie_tomatometer);
        tagline = (TextView) findViewById(R.id.text_movie_tagline);
        homepage = (TextView) findViewById(R.id.text_movie_homepage);
        imdbLink = (TextView) findViewById(R.id.text_movie_imdb_link);
        moneyInfo = (TextView) findViewById(R.id.text_movie_money_info);
        overview = (TextView) findViewById(R.id.text_movie_overview);
        directorView = (LinearLayout) findViewById(R.id.linearlayout_movie_directors);
        writerView = (LinearLayout) findViewById(R.id.linearlayout_movie_writers);
        fullCast = (TextView) findViewById(R.id.clickable_movie_cast);
        fullCrew = (TextView) findViewById(R.id.clickable_movie_crew);

        Bundle extras = getIntent().getExtras();
        movieId = extras.getInt("id");
        movie = searcher.getMovieById(movieId);
        String mpaaRatingVal = searcher.getMpaaRatingByMovieId(movieId);
        int tomatoScore = searcher.getTomatometerByMovie(movie.getTitle());
        ArrayList<CrewMember> directors = new ArrayList<CrewMember>();
        ArrayList<CrewMember> writers = new ArrayList<CrewMember>();
        poster.setTag(movie.getIdTag());
        ImageGetter imgGetter = new ImageGetter(getBaseContext());
        try {
            String url = getString(R.string.baseurl_images) + movie.getMediumImage(getResources())
                    + movie.getImagePath();
            imgGetter.DisplayImage(url, poster, 1.2f);
        } catch(RejectedExecutionException e) {
            Log.d(tag, "async task queue full at the moment, imgGet rejected");
        }
        title.setText(Html.fromHtml("<b>" + movie.getTitle() + "</b>"));
        mpaaRating.setText("mpaa rating: " + mpaaRatingVal);
        releaseDate.setText("released: " + movie.getReleaseDate());
        runtime.setText("runtime: " + movie.getRuntime() + " minutes");
        rating.setText("TMDb rating: " + movie.getVoteAverage() + " after " + movie.getVoteCount() + " votes");
        if (tomatoScore == -1) {
            ((ViewManager)tomatometer.getParent()).removeView(tomatometer);
        }
        else {
            tomatometer.setText("tomatometer: " + tomatoScore + "%");
        }
        tagline.setText(movie.getTagline());
        if (!movie.getHomepage().equals("")) {
            SpannableString homepageString = new SpannableString("homepage");
            homepageString.setSpan(new StyleSpan(Typeface.ITALIC), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            homepageString.setSpan(new URLSpan(movie.getHomepage()), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            homepage.setText(homepageString);
            homepage.setMovementMethod(LinkMovementMethod.getInstance());
        }
        else
            ((ViewManager)homepage.getParent()).removeView(homepage);
        SpannableString imdbString = new SpannableString("imdb");
        imdbString.setSpan(new StyleSpan(Typeface.ITALIC), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        imdbString.setSpan(new URLSpan(movie.getImdbLink()), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        imdbLink.setText(imdbString);
        imdbLink.setMovementMethod(LinkMovementMethod.getInstance());
        moneyInfo.setText("budget: $" + (movie.getBudget() <= 100 ? "unavailable" : movie.getBudget()) +
                          "\nrevenue: $" + (movie.getRevenue() <= 100 ? "unavailable" : movie.getRevenue())+
                          "\nprofit ~= $" + ((movie.getRevenue() - movie.getBudget()) <= 100 ? "unavailable" :
                          (movie.getRevenue() - movie.getBudget())));
        overview.setText(movie.getOverview());

        crew = (ArrayList<CrewMember>) searcher.getCrewByMovieId(movieId);
        for (CrewMember member : crew) {
            if (member.getDepartment().equals("Directing"))
                directors.add(member);
            else if (member.getDepartment().equals("Writing"))
                writers.add(member);
        }

        //fill directors section
        adapter = new ResultListAdapter(getBaseContext(), R.layout.list_item_cast_member, directors);
        for (int i = 0; i < directors.size(); i++) {
            View directorItem = adapter.getView(i, null, directorView);
            directorItem.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent personInfo = new Intent(getBaseContext(), PersonInfoActivity.class);
                    personInfo.putExtra("id", (Integer) view.getTag());
                    startActivity(personInfo);
                }
            });
            directorView.addView(directorItem);
        }
        if (directors.size() == 0) {
            directorView.addView(adapter.getNoResultsView());
        }
        adapter = new ResultListAdapter(getBaseContext(), R.layout.list_item_cast_member, writers);
        for (int i = 0; i < writers.size(); i++) {
            View writerItem = adapter.getView(i, null, writerView);
            writerItem.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent personInfo = new Intent(getBaseContext(), PersonInfoActivity.class);
                    personInfo.putExtra("id", (Integer) view.getTag());
                    startActivity(personInfo);
                }
            });
            writerView.addView(writerItem);
        }
        if (writers.size() == 0) {
            writerView.addView(adapter.getNoResultsView());
        }
        cast = (ArrayList<CastMember>) searcher.getCastByMovieId(movieId);

        fullCast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent castResults = new Intent(getBaseContext(), ResultListActivity.class);
                castResults.putParcelableArrayListExtra("list", cast);
                castResults.putExtra("listItemId", R.layout.list_item_cast_member);
                castResults.putExtra("infoClass", PersonInfoActivity.class);
                startActivity(castResults);
            }
        });
        fullCrew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent crewResults = new Intent(getBaseContext(), ResultListActivity.class);
                crewResults.putParcelableArrayListExtra("list", crew);
                crewResults.putExtra("listItemId", R.layout.list_item_cast_member);
                crewResults.putExtra("infoClass", PersonInfoActivity.class);
                startActivity(crewResults);
            }
        });

    }
}
