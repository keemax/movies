package com.movies;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 7/9/12
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class CastCredit implements Listable{
    final static String tag = "CREDIT";

    private String character;
    private int movieId;
    private String movieOriginalTitle;
    private String posterPath;
    private String releaseDate;
    private String movieTitle;
    private int year;



    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(character);
        dest.writeInt(movieId);
        dest.writeString(movieOriginalTitle);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(movieTitle);
        dest.writeInt(year);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public CastCredit(Parcel in) {
        character = in.readString();
        movieId = in.readInt();
        movieOriginalTitle = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        movieTitle = in.readString();
        year = in.readInt();
    }
    public static final Parcelable.Creator<CastCredit> CREATOR = new Parcelable.Creator<CastCredit>() {
        public CastCredit createFromParcel(Parcel in) {
            return new CastCredit(in);
        }
        public CastCredit[] newArray(int size) {
            return new CastCredit[size];
        }
    };
    public CastCredit(JSONObject info) {
        year = 0;
        try{
            if (info.has("character"))
                character = info.getString("character");
            if (info.has("id"))
                movieId = info.getInt("id");
            if (info.has("original_title"))
                movieOriginalTitle = info.getString("original_title");
            if (info.has("poster_path"))
                posterPath = info.getString("poster_path");
            if (info.has("release_date")) {
                releaseDate = info.getString("release_date");
                if (!releaseDate.isEmpty() && !releaseDate.equals("null")) {
                    year = Integer.parseInt(releaseDate.substring(0,4));
                }
            }
            if (info.has("title"))
                movieTitle = info.getString("title");
        } catch(Exception e) {
            Log.d(tag, "failed to create credit");
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return movieTitle + " as " + character;
    }


    public String getCharacter() {
        return character;
    }
    public int getMovieId() {
        return movieId;
    }
    public String getMovieOriginalTitle() {
        return movieOriginalTitle;
    }
    public String getPosterPath() {
        return posterPath;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public String getMovieTitle() {
        return movieTitle;
    }
    public int getYear() {
        return year;
    }


    //listable interface
    public String getSmallImage(Resources res) {
//        return imgGetter.getImage(res.getString(R.string.profile_small), profilePath);
        return res.getString(R.string.poster_small);
    }
    public String getMediumImage(Resources res) {
//        return imgGetter.getImage(res.getString(R.string.profile_medium), profilePath);
        return res.getString(R.string.poster_medium);
    }
    public String getLargeImage(Resources res) {
//        return imgGetter.getImage(res.getString(R.string.profile_large), profilePath);
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
        return movieId;
    }
    public Spanned getHtmlDisplay() {
        return Html.fromHtml(movieTitle + "<br><i>" + character + "</i>");
    }
}
