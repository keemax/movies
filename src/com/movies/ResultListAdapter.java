package com.movies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.movies.Listable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 7/10/12
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResultListAdapter extends ArrayAdapter {
    private final static String tag = "RESULT_LIST_ADAPTER";
    private ArrayList<Listable> list;
    private Context ctx;
//    private ArrayList<ImageView> imageViews;
    private LayoutInflater inflater;
    ImageGetter imgGetter;
    public ResultListAdapter(Context inCtx, int textViewResourceId, List<? extends Listable> items) {
        super(inCtx, textViewResourceId, items);
        list = (ArrayList<Listable>) items;
        ctx = inCtx;
//        imageViews = new ArrayList<ImageView>();
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imgGetter = new ImageGetter(ctx);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Listable current = list.get(position);
        if (v == null) {
            v = inflater.inflate(list.get(0).getListItemId(), null);

        }
        v.setTag(current.getIdTag());

        ImageView listablePicture = (ImageView) v.findViewById(current.getImageViewId());
        listablePicture.setTag(current.getIdTag());
        try {
            String url = ctx.getResources().getString(R.string.baseurl_images) +
                         current.getMediumImage(ctx.getResources()) + current.getImagePath();
            imgGetter.DisplayImage(url, listablePicture, 1);
        } catch(RejectedExecutionException e) {
            Log.d(tag, "async task queue full at the moment, imgGet rejected");
        }

        TextView listableLabel = (TextView) v.findViewById(current.getTextViewId());
        listableLabel.setText(current.getHtmlDisplay());


//        v.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent infoActivity = new Intent(ctx, list.get(0).getInfoActivityClass());
//                infoActivity.putExtra("id", (Integer) view.getTag());
//                ctx.startActivity(infoActivity);
//            }
//        });
        return v;
    }
    public View getNoResultsView() {
        LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return vi.inflate(R.layout.empty_list, null);
    }
}
