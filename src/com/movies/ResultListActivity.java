package com.movies;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 7/6/12
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */

//  TODO: handle empty list - get rid of list.get(0)

public class ResultListActivity extends ListActivity {
    private static final String tag = "RESULT_LIST_ACTIVITY";

    TmdbQuery searcher;

    int movieId;
    String characterQuery;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.layout_list_results);
        searcher = new TmdbQuery(getBaseContext());

        Bundle extras = getIntent().getExtras();
        final ArrayList<Listable> list = extras.getParcelableArrayList("list");
        final int listItemId = extras.getInt("listItemId");
        final Class<?> infoClass = (Class<?>) extras.get("infoClass");

        ResultListAdapter adapter = new ResultListAdapter(getBaseContext(), listItemId, list);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent infoActivity = new Intent(getBaseContext(), infoClass);
                infoActivity.putExtra("id", (Integer) view.getTag());
                startActivity(infoActivity);
            }
        });

    }

}
