package com.movies;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 7/6/12
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class Movie implements Listable {
    private final static String tag = "MOVIE";
    ImageGetter imgGetter;

    private boolean adult;
    private String backdropPath;
    //    private MovieCollection belongsTo;
    private int budget;
    //    private List<Genre> genres;
    private String homepage;
    private int id;
    private String imdbId;
    private String mpaaRating;
    private String originalTitle;
    private String overview;
    private double popularity;
    private String posterPath;
//    private List<ProductionCompany> productionCompanies;
//    private List<Country> productionCountries;
//    private Date releaseDate;
    private String releaseDate;
    private int revenue;
    private int runtime;
    //    private List<Language> languages;
    private String tagline;
    private String title;
    private double voteAverage;
    private int voteCount;
    private int year;

//    private List<Title> alternativeTitles;
//    private List<CastMember> cast;
//    private List<Keyword> keywords;

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(backdropPath);
        dest.writeInt(budget);
        dest.writeString(homepage);
        dest.writeInt(id);
        dest.writeString(imdbId);
        dest.writeString(mpaaRating);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeDouble(popularity);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeInt(revenue);
        dest.writeInt(runtime);
        dest.writeString(tagline);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
        dest.writeInt(voteCount);
        dest.writeInt(year);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public Movie(Parcel in) {
        backdropPath = in.readString();
        budget = in.readInt();
        homepage = in.readString();
        id = in.readInt();
        imdbId = in.readString();
        mpaaRating = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        popularity = in.readDouble();
        posterPath = in.readString();
        releaseDate = in.readString();
        revenue = in.readInt();
        runtime = in.readInt();
        tagline = in.readString();
        title = in.readString();
        voteAverage = in.readDouble();
        voteCount = in.readInt();
        year = in.readInt();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(JSONObject info) {
        try {
            year = 0;
            mpaaRating = "";
            if (info.has("adult"))
                adult = info.getBoolean("adult");
            if (info.has("backdrop_path"))
                backdropPath = info.getString("backdrop_path");
            if (info.has("budget"))
                budget = info.getInt("budget");
            if (info.has("homepage"))
                homepage = info.getString("homepage");
            if (info.has("id"))
                id = info.getInt("id");
            if (info.has("imdb_id"))
                imdbId = info.getString("imdb_id");
            if (info.has("original_title"))
                originalTitle = info.getString("original_title");
            if (info.has("overview"))
                overview = info.getString("overview");
            if (info.has("popularity"))
                popularity = info.getDouble("popularity");
            if (info.has("poster_path"))
                posterPath = info.getString("poster_path");
            if (info.has("release_date")) {
                releaseDate = info.getString("release_date");
                if (!releaseDate.isEmpty() && !releaseDate.equals("null")) {
                    year = Integer.parseInt(releaseDate.substring(0,4));
                }
            }
            if (info.has("revenue"))
                revenue = info.getInt("revenue");
            if (info.has("runtime"))
                runtime = info.getInt("runtime");
            if (info.has("tagline"))
                tagline = info.getString("tagline");
            if (info.has("title"))
                title = info.getString("title");
            if (info.has("vote_average"))
                voteAverage = info.getDouble("vote_average");
            if (info.has("vote_count"))
                voteCount = info.getInt("vote_count");
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.d(tag, "creation from json failed");
        }
    }

    @Override
    public String toString() {
        String ret = title;
        if (year != 0) {
            ret += " (" + year + ")";
        }
        return ret;
    }

    public boolean isAdult() {
        return adult;
    }
    public String getBackdropPath() {
        return backdropPath;
    }
    public int getBudget() {
        return budget;
    }
    public String getHomepage() {
        return homepage;
    }
    public int getId() {
        return id;
    }
    public String getImdbLink() {
        return "http://www.imdb.com/title/" + imdbId;
    }
    public String getOriginalTitle() {
        return originalTitle;
    }
    public String getOverview() {
        return overview;
    }
    public double getPopularity() {
        return popularity;
    }
    public String getPosterPath() {
        return posterPath;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public int getRevenue() {
        return revenue;
    }
    public int getRuntime() {
        return runtime;
    }
    public String getTagline() {
        return tagline;
    }
    public String getTitle() {
        return title;
    }
    public double getVoteAverage() {
        return voteAverage;
    }
    public int getVoteCount() {
        return voteCount;
    }
    public int getYear() {
        return year;
    }

    //listable interface
    public String getSmallImage(Resources res) {
        return res.getString(R.string.poster_small);
    }
    public String getMediumImage(Resources res) {
        return res.getString(R.string.poster_medium);
    }
    public String getLargeImage(Resources res) {
        return res.getString(R.string.poster_large);
    }
    public String getImagePath() {
        return posterPath;
    }
    public int getListItemId() {
        return R.layout.list_item_movie_with_picture;
    }
    public int getImageViewId() {
        return R.id.image_poster_thumbnail;
    }
    public int getTextViewId() {
        return R.id.text_credit_info;
    }
    public java.lang.Class<?> getInfoActivityClass() {
        return MovieInfoActivity.class;
    }
    public int getIdTag() {
        return id;
    }
    public Spanned getHtmlDisplay() {
        String ret = title;
        if (year != 0) {
            ret += " (" + year + ")";
        }
        return Html.fromHtml("<i><b>" + ret + "</b></i>");
    }
}
