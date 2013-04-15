package com.movies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 7/9/12
 * Time: 12:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class CastMember implements Listable {
    final static String tag = "CAST_MEMBER";
    ImageGetter imgGetter;

    private String character;
    private int id;
    private String name;
    private Person person;
    private int order;
    private String profilePath;

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(character);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(order);
        dest.writeString(profilePath);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public CastMember(Parcel in) {
        character = in.readString();
        id = in.readInt();
        name = in.readString();
        order = in.readInt();
        profilePath = in.readString();
    }

    public static final Parcelable.Creator<CastMember> CREATOR = new Parcelable.Creator<CastMember>() {
        public CastMember createFromParcel(Parcel in) {
            return new CastMember(in);
        }
        public CastMember[] newArray(int size) {
            return new CastMember[size];
        }
    };

    public CastMember(JSONObject info) {

        try {
            if (info.has("character"))
                character = info.getString("character");
            if (info.has("id")) {
                id = info.getInt("id");
            }
            if (info.has("name"))
                name = info.getString("name");
            if (info.has("order"))
                order = info.getInt("order");
            if (info.has("profile_path"))
                profilePath = info.getString("profile_path");
        } catch(JSONException e) {
            Log.d(tag, "failed to create cast member");
            e.printStackTrace();
        }

    }
    @Override
    public String toString() {
        return name + " as " + character;
    }

    public String getCharacter() {
        return character;
    }
    public int getId() {
        return id;
    }
    public Person getPerson() {
        return person;
    }
    public String getName() {
        return name;
    }
    public int getOrder() {
        return order;
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
        return Html.fromHtml(name + "<br><i>" + character + "</i>" );
    }

}
