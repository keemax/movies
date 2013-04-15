package com.movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 7/9/12
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */

//  TODO: crew credits

public class PersonInfoActivity extends Activity {
    private static final String tag = "PERSON_INFO_ACTIVITY";

    private static final String APPEARS_TAB = "appearsIn";
    private static final String DIRECTED_TAB = "directed";
    private static final String WROTE_TAB = "wrote";
    private static final String PRODUCED_TAB = "produced";

    ImageView picture;
    TextView name;
    TextView birth;
    TextView death;
    TextView bio;
    ListView appearsIn;

    TmdbQuery searcher;

    int personId;
    Person person;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.layout_person_info);
        searcher = new TmdbQuery(getBaseContext());

        picture = (ImageView) findViewById(R.id.image_person_info);
        name = (TextView) findViewById(R.id.text_person_name);
        birth = (TextView) findViewById(R.id.text_person_birth);
        death = (TextView) findViewById(R.id.text_person_death);
        bio = (TextView) findViewById(R.id.text_person_bio);
        appearsIn = (ListView) findViewById(R.id.listview_credits);

        Bundle extras = getIntent().getExtras();
        personId = extras.getInt("id");
        person = searcher.getPersonById(personId);
        picture.setTag(person.getIdTag());
        ImageGetter imgGetter = new ImageGetter(getBaseContext());
        try {
            String url = getString(R.string.baseurl_images) + person.getMediumImage(getResources())
                         + person.getImagePath();
            imgGetter.DisplayImage(url, picture, 1.2f);
        } catch(RejectedExecutionException e) {
            Log.d(tag, "async task queue full at the moment, imgGet rejected");
        }

        name.setText(person.getName());
        birth.setText(Html.fromHtml("born: " + person.getBirthday() + "<br><i>in</i>: " + person.getBirthPlace()));
        String deathString = person.getDeathday();
        if (deathString.equals(""))
            ((ViewManager)death.getParent()).removeView(death);
        else {
            death.setText("died: " + deathString);
        }
        bio.setText(person.getBiography());





        TabHost creditTabHost = (TabHost) findViewById(R.id.tabhost_credits);
        creditTabHost.setup();

        TabHost.TabSpec appearsSpec = creditTabHost.newTabSpec(APPEARS_TAB);
        appearsSpec.setContent(appearsIn.getId());
        appearsSpec.setIndicator("appears in");

        TabHost.TabSpec directedSpec = creditTabHost.newTabSpec(DIRECTED_TAB);
        directedSpec.setContent(appearsIn.getId());
        directedSpec.setIndicator("directed");

        TabHost.TabSpec wroteSpec = creditTabHost.newTabSpec(WROTE_TAB);
        wroteSpec.setContent(appearsIn.getId());
        wroteSpec.setIndicator("wrote");

        TabHost.TabSpec producedSpec = creditTabHost.newTabSpec(PRODUCED_TAB);
        producedSpec.setContent(appearsIn.getId());
        producedSpec.setIndicator("produced");

        List<CastCredit> castCredits = searcher.getCastCreditsByPersonId(personId);
        appearsIn.setAdapter(new ResultListAdapter(getBaseContext(), R.layout.list_item_movie_with_picture, castCredits));

        creditTabHost.addTab(appearsSpec);
        creditTabHost.addTab(directedSpec);
        creditTabHost.addTab(wroteSpec);
        creditTabHost.addTab(producedSpec);


//        List<CrewCredit> crewCredits = searcher.getCrewCreditsByPersonId(personId);
//
//        List<CrewCredit> directed = new ArrayList<CrewCredit>();
//        List<CrewCredit> wrote = new ArrayList<CrewCredit>();
//        List<CrewCredit> produced = new ArrayList<CrewCredit>();
//        for (CrewCredit credit : crewCredits) {
//            if (credit.getDepartment().equals("Directing"))
//                directed.add(credit);
//            else if (credit.getDepartment().equals("Writing"))
//                wrote.add(credit);
//            else if (credit.getDepartment().equals("Production"))
//                produced.add(credit);
//        }
        appearsIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent infoActivity = new Intent(getBaseContext(), MovieInfoActivity.class);
                infoActivity.putExtra("id", (Integer) view.getTag());
                infoActivity.putExtra("listItemId", R.layout.list_item_movie_with_picture);
                infoActivity.putExtra("infoClass", MovieInfoActivity.class);
                startActivity(infoActivity);
            }
        });
        creditTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if (s.equals(APPEARS_TAB)){
                    List<CastCredit> castCredits = searcher.getCastCreditsByPersonId(personId);
                    appearsIn.setAdapter(new ResultListAdapter(getBaseContext(), R.layout.list_item_movie_with_picture, castCredits));
                }
                else if (s.equals(DIRECTED_TAB)) {
                    List<CrewCredit> crewCredits = searcher.getCrewCreditsByPersonId(personId);

                    List<CrewCredit> directed = new ArrayList<CrewCredit>();
                    for (CrewCredit credit : crewCredits) {
                        if (credit.getDepartment().equals("Directing"))
                            directed.add(credit);
                    }
                    appearsIn.setAdapter(new ResultListAdapter(getBaseContext(), R.layout.list_item_movie_with_picture, directed));
                }
                else if (s.equals(WROTE_TAB)) {
                    List<CrewCredit> crewCredits = searcher.getCrewCreditsByPersonId(personId);

                    List<CrewCredit> wrote = new ArrayList<CrewCredit>();
                    for (CrewCredit credit : crewCredits) {
                        if (credit.getDepartment().equals("Writing"))
                            wrote.add(credit);
                    }
                    appearsIn.setAdapter(new ResultListAdapter(getBaseContext(), R.layout.list_item_movie_with_picture, wrote));
                }
                else if (s.equals(PRODUCED_TAB)) {
                    List<CrewCredit> crewCredits = searcher.getCrewCreditsByPersonId(personId);

                    List<CrewCredit> produced = new ArrayList<CrewCredit>();
                    for (CrewCredit credit : crewCredits) {
                        if (credit.getDepartment().equals("Production"))
                            produced.add(credit);
                    }
                    appearsIn.setAdapter(new ResultListAdapter(getBaseContext(), R.layout.list_item_movie_with_picture, produced));
                }
            }
        });
        creditTabHost.setCurrentTab(0);


    }
}
