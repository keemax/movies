package com.movies;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 7/9/12
 * Time: 9:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class TmdbQuery {
    static final String tag = "TMBD_QUERY";

    Context ctx;
    Resources res;
    HttpClient client = new DefaultHttpClient();
    HttpGet tmdb = new HttpGet();

    TmdbQuery(Context inCtx) {
        ctx = inCtx;
        res = ctx.getResources();

        tmdb.addHeader("Accept", "application/json");
    }


    //returns JSONObject of form:
    // {"page" : int (current page if results > 20) add "page=" param to request uri to get other pages,
    //  "results : JSONArray [{"id" : int,
    //                         "title" : string,
    //                         "vote_average" : double,
    //                         "backdrop_path" : string,
    //                         "release_date" : string(YYYY-MM-DD),
    //                         "original_title" : string,
    //                         "vote_count" : int,
    //                         "adult" : boolean,
    //                         "poster_path" : string,
    //                         "popularity" : double }]
    //  "total_pages" : int
    //  "total_results" : int}
    public List<Movie> searchMovie(String query) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        params.put("query", query);
        List<Movie> movies = new ArrayList<Movie>();
        try {
            JSONArray results = get(res.getString(R.string.baseurl_search_movie), params).getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                movies.add(new Movie(results.getJSONObject(i)));
            }
        } catch(JSONException e) {
            Log.d(tag, "failed to obtain results");
            e.printStackTrace();
        }
        return movies;
    }
    public List<Person> searchPerson(String query) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        params.put("query", query);
        List<Person> people = new ArrayList<Person>();
        try {
            JSONArray results = get(res.getString(R.string.baseurl_search_person), params).getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                people.add(new Person(results.getJSONObject(i)));
            }
        } catch(JSONException e) {
            Log.d(tag, "failed to obtain results");
            e.printStackTrace();
        }
        return people;
    }

    public List<CastMember> getCastByMovieId(int id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        List<CastMember> cast = new ArrayList<CastMember>();
        try {
            JSONArray results = get(getBaseCastUrl(id), params).getJSONArray("cast");
            for (int i = 0; i < results.length(); i++) {
                cast.add(new CastMember(results.getJSONObject(i)));
            }
        } catch(JSONException e) {
            Log.d(tag, "failed to get cast");
            e.printStackTrace();
        }
        return cast;
    }
    public List<CrewMember> getCrewByMovieId(int id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        List<CrewMember> crew = new ArrayList<CrewMember>();
        try {
            JSONArray results = get(getBaseCastUrl(id), params).getJSONArray("crew");
            for (int i = 0; i < results.length(); i++) {
                crew.add(new CrewMember(results.getJSONObject(i)));
            }
        } catch(JSONException e) {
            Log.d(tag, "failed to get cast");
            e.printStackTrace();
        }
        return crew;
    }

    public List<CastCredit> getCastCreditsByPersonId(int id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        List<CastCredit> castCredits = new ArrayList<CastCredit>();
        try {
            JSONArray results = get(getBaseCreditsUrl(id), params).getJSONArray("cast");
            for (int i = 0; i < results.length(); i++) {
                castCredits.add(new CastCredit(results.getJSONObject(i)));
            }
        } catch(JSONException e) {
            Log.d(tag, "failed to get castCredits");
            e.printStackTrace();
        }
        return castCredits;

    }
    public List<CrewCredit> getCrewCreditsByPersonId(int id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        List<CrewCredit> crewCredits = new ArrayList<CrewCredit>();
        try {
            JSONArray results = get(getBaseCreditsUrl(id), params).getJSONArray("crew");
            for (int i = 0; i < results.length(); i++) {
                crewCredits.add(new CrewCredit(results.getJSONObject(i)));
            }
        } catch(JSONException e) {
            Log.d(tag, "failed to get castCredits");
            e.printStackTrace();
        }
        return crewCredits;

    }

    public List<Movie> getNowPlaying() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        List<Movie> nowPlaying = new ArrayList<Movie>();
        try {
            JSONArray results = get(res.getString(R.string.baseurl_movie_now_playing), params).getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                nowPlaying.add(new Movie(results.getJSONObject(i)));
            }
        } catch(JSONException e) {
            Log.d(tag, "failed to obtain results");
            e.printStackTrace();
        }
        return nowPlaying;
    }
    public List<Movie> getPopular() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        List<Movie> popular = new ArrayList<Movie>();
        try {
            JSONArray results = get(res.getString(R.string.baseurl_movie_popular), params).getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                popular.add(new Movie(results.getJSONObject(i)));
            }
        } catch(JSONException e) {
            Log.d(tag, "failed to obtain results");
            e.printStackTrace();
        }
        return popular;
    }
    public List<Movie> getTopRated() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        List<Movie> topRated = new ArrayList<Movie>();
        try {
            JSONArray results = get(res.getString(R.string.baseurl_movie_top_rated), params).getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                topRated.add(new Movie(results.getJSONObject(i)));
            }
        } catch(JSONException e) {
            Log.d(tag, "failed to obtain results");
            e.printStackTrace();
        }
        return topRated;
    }

    public Person getPersonById(int id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        JSONObject personJson = get(res.getString(R.string.baseurl_person) + id, params);
        return new Person(personJson);
    }

    public Movie getMovieById(int id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        JSONObject movieJson = get(res.getString(R.string.baseurl_movie) + id, params);
        return new Movie(movieJson);
    }

    public int getTomatometerByMovie(String movieTitle) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", res.getString(R.string.rt_api_key));
        String movieQuery = "";
        try{
            movieQuery = URLEncoder.encode(movieTitle.trim(), "UTF-8");
        } catch(Exception e) {
            Log.d(tag, "failed to encode search query");
        }
        params.put("q", movieQuery);
        params.put("page_limit", "10");     //limit number of movies returned from search query
        JSONObject results = get(res.getString(R.string.baseurl_search_movie_rt), params);
        try {
            int numResults = results.getInt("total");
            JSONArray movieResults = results.getJSONArray("movies");
            for (int i = 0; i < numResults; i++) {
                JSONObject movieResult = movieResults.getJSONObject(i);
                if (movieResult.getString("title").equalsIgnoreCase(movieTitle)) {
                    JSONObject ratings = movieResult.getJSONObject("ratings");
                    return ratings.getInt("critics_score");
                }
            }
        } catch (JSONException e) {
            Log.d(tag, "unable to obtain tomatometer rating for " + movieTitle);

        }
        return -1;

    }

    //mpaa rating is not in the movie info json object, separate request needed
    public String getMpaaRatingByMovieId(int movieId) {
        String baseurl = res.getString(R.string.baseurl_movie) + "/" + movieId + "/releases";
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", res.getString(R.string.tmdb_api_key));
        try {
            JSONArray releaseJson = get(baseurl, params).getJSONArray("countries");
            int i = 0;
            while (true) {
               JSONObject country = releaseJson.getJSONObject(i++);
               if (country.getString("iso_3166_1").equals("US")) {
                   return country.getString("certification");
               }
            }
        } catch (Exception e) {
            Log.d(tag, "failed to find mpaa rating for movie: " + movieId);
        }
        return "";
    }

    public JSONObject get(String baseUri, Map<String, String> params) {
        JSONObject results = new JSONObject();
        String uri = baseUri + "?";
        Set<String> keys = params.keySet();
        for (String key : keys) {
            uri += key + "=" + params.get(key) + "&";
        }
        Log.d(tag, "get " + uri);
        try {
            tmdb.setURI(new URI(uri));
            HttpResponse resp = client.execute(tmdb);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            resp.getEntity().writeTo(out);
            Log.d(tag, "jsonString: " + out.toString());
            results = new JSONObject(out.toString());
            out.close();
        } catch(Exception e) {
            Log.d(tag, "get failed");
            e.printStackTrace();
        }
        return results;
    }
//    public String[] getProfilePictureSizes() {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("api_key", res.getString(R.string.api_key));
//        String [] profileSizes = {""};
//        try {
//            JSONObject imageInfo = get(res.getString(R.string.baseurl_configuration), params).getJSONObject("images");
//            profileSizes = (String[]) imageInfo.get("profile_sizes");
//
//        } catch(JSONException e) {
//            Log.d(tag, "failed to get image configuration info");
//            e.printStackTrace();
//        }
//        return profileSizes;
//    }
//    public String getBaseImgUrl() {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("api_key", res.getString(R.string.api_key));
//        String baseUrl = "";
//        try {
//            JSONObject imageInfo = get(res.getString(R.string.baseurl_configuration), params).getJSONObject("images");
//            baseUrl = imageInfo.getString("base_url");
//
//        } catch(JSONException e) {
//            Log.d(tag, "failed to get image configuration info");
//            e.printStackTrace();
//        }
//        return baseUrl;
//    }
    private String getBaseCastUrl(int movieId) {
        return (res.getString(R.string.baseurl_movie) + movieId + "/casts");
    }
    private String getBaseCreditsUrl(int personId) {
        return (res.getString(R.string.baseurl_person) + personId + "/credits");
    }
}
