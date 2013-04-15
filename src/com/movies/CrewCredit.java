package com.movies;

import android.content.res.Resources;
import android.os.Parcel;
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
public class CrewCredit implements Listable{
    final static String tag = "CREDIT";

    private int movieId;
    private String movieOriginalTitle;
    private String posterPath;
    private String releaseDate;
    private String movieTitle;
    private int year;
    private String department;
    private String job;



    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeInt(movieId);
        dest.writeString(movieOriginalTitle);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(movieTitle);
        dest.writeInt(year);
        dest.writeString(department);
        dest.writeString(job);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public CrewCredit(Parcel in) {
        movieId = in.readInt();
        movieOriginalTitle = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        movieTitle = in.readString();
        year = in.readInt();
        department = in.readString();
        job = in.readString();
    }
    public static final Creator<CrewCredit> CREATOR = new Creator<CrewCredit>() {
        public CrewCredit createFromParcel(Parcel in) {
            return new CrewCredit(in);
        }
        public CrewCredit[] newArray(int size) {
            return new CrewCredit[size];
        }
    };
    public CrewCredit(JSONObject info) {
        year = 0;
        try{
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
            if (info.has("department"))
                department = info.getString("department");
            if (info.has("job"))
                job = info.getString("job");
        } catch(Exception e) {
            Log.d(tag, "failed to create credit");
            e.printStackTrace();
        }

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
    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
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
    public Class<?> getInfoActivityClass() {
        return MovieInfoActivity.class;
    }
    public int getIdTag() {
        return movieId;
    }
    public Spanned getHtmlDisplay() {
        return Html.fromHtml(movieTitle + "<br><i>" + job + "</i>");
    }
}
