package com.movies;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 7/11/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrewMember implements Listable {
    private final static String tag = "CREW_MEMBER";

    private String department;
    private int personId;
    private String job;
    private String name;
    private String profilePath;

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(department);
        dest.writeInt(personId);
        dest.writeString(job);
        dest.writeString(name);
        dest.writeString(profilePath);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public CrewMember(Parcel in) {
        department = in.readString();
        personId = in.readInt();
        job = in.readString();
        name = in.readString();
        profilePath = in.readString();
    }
    public static final Parcelable.Creator<CrewMember> CREATOR = new Parcelable.Creator<CrewMember>() {
        public CrewMember createFromParcel(Parcel in) {
            return new CrewMember(in);
        }
        public CrewMember[] newArray(int size) {
            return new CrewMember[size];
        }
    };

    public CrewMember(JSONObject info) {
        try {
            if (info.has("department"))
                department = info.getString("department");
            if (info.has("id"))
                personId = info.getInt("id");
            if (info.has("job"))
                job = info.getString("job");
            if (info.has("name"))
                name = info.getString("name");
            if (info.has("profile_path"))
                profilePath = info.getString("profile_path");
        } catch(Exception e) {
            Log.d(tag, "failed to create CrewMember");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return name + "\n" + job;
    }
    public String getDepartment() {
        return department;
    }
    public int getId() {
        return personId;
    }
    public String getJob() {
        return job;
    }
    public String getName() {
        return name;
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
        return personId;
    }
    public Spanned getHtmlDisplay() {
        return Html.fromHtml(name + "<br><i>" + job + "</i>");
    }

}
