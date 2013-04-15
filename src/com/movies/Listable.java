package com.movies;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.text.Spanned;
import android.view.View;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 7/10/12
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Listable extends Parcelable {
    public int getListItemId();
    public int getImageViewId();
    public int getTextViewId();
    public String getSmallImage(Resources res);
    public String getMediumImage(Resources res);
    public String getLargeImage(Resources res);
    public String getImagePath();
    public java.lang.Class<?> getInfoActivityClass();
    public int getIdTag();
    public Spanned getHtmlDisplay();
}
