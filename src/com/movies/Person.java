package com.movies;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 7/6/12
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class Person implements Listable {
    final static String tag = "PERSON";
    ImageGetter imgGetter;

    private boolean adult;
    private String biography;
    private String birthday;
    private String deathday;
    private String homepage;
    private int id;
    private String name;
    private String birthPlace;
    private String profilePath;

    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(biography);
        dest.writeString(birthday);
        dest.writeString(deathday);
        dest.writeString(homepage);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(birthPlace);
        dest.writeString(profilePath);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public Person(Parcel in) {
//        imgGetter = new ImageGetter();

        biography = in.readString();
        birthday = in.readString();
        deathday = in.readString();
        homepage = in.readString();
        id = in.readInt();
        name = in.readString();
        birthPlace = in.readString();
        profilePath = in.readString();
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
    public Person(JSONObject info) {
//        imgGetter = new ImageGetter();
        adult = false;
        biography = "";
        birthday = "";
        deathday = "";
        homepage = "";
//        id = 0;
        name = "";
        birthPlace = "";
        profilePath = "";
        try{
            if (info.has("adult"))
                adult = info.getBoolean("adult");
            if (info.has("biography"))
                biography = info.getString("biography");
            if (info.has("birthday"))
                birthday = info.getString("birthday");
            if (info.has("deathday"))
                deathday = info.getString("deathday");
            if (info.has("homepage"))
                homepage = info.getString("homepage");
            if (info.has("id"))
                id = info.getInt("id");
            if (info.has("name"))
                name = info.getString("name");
            if (info.has("place_of_birth"))
                birthPlace = info.getString("place_of_birth");
            if (info.has("profile_path"))
                profilePath = info.getString("profile_path");
        } catch(JSONException e) {
            Log.d(tag, "unable to create person from json");
            e.printStackTrace();
        }
    }

    public boolean isAdult() {
        return adult;
    }
    public String getBiography() {
        return biography;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getDeathday() {
        return deathday;
    }
    public String getHomepage() {
        return homepage;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getBirthPlace() {
        return birthPlace;
    }
    public String getProfilePath() {
        return profilePath;
    }

    //listable interface
    public String getSmallImage(Resources res) {
//        return imgGetter.getImage(res.getString(R.string.profile_small), profilePath);
        return res.getString(R.string.profile_small);
    }
    public String getMediumImage(Resources res) {
//        return imgGetter.getImage(res.getString(R.string.profile_medium), profilePath);
        return res.getString(R.string.profile_medium);
    }
    public String getLargeImage(Resources res) {
//        return imgGetter.getImage(res.getString(R.string.profile_large), profilePath);
        return res.getString(R.string.profile_large);
    }
    public String getImagePath() {
        return profilePath;
    }
    public int getListItemId() {
        return R.layout.list_item_cast_member;
    }
    public int getImageViewId() {
        return R.id.cast_member_img;
    }
    public int getTextViewId() {
        return R.id.cast_member_label;
    }
    public java.lang.Class<?> getInfoActivityClass() {
        return PersonInfoActivity.class;
    }
    public int getIdTag() {
        return id;
    }
    public Spanned getHtmlDisplay() {
        return Html.fromHtml(name);
    }


}
