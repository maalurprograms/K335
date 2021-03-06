package com.example.bcosaj.k335;

// Imports
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "TEST";

    // Initialize post list
    ArrayList<Post> posts = new ArrayList<>();
    PostAdapter newsAdapter;

    private SharedPreferences PREFS;
    private String PERSON;
    private String YOUTUBE_ACCOUNT;
    private String TWITTER_ACCOUNT;
    private String FACEBOOK_ACCOUNT;
    private String INSTAGRAM_ACCOUNT;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Define account usernames
        PREFS = PreferenceManager.getDefaultSharedPreferences(this);
        setUserData();

        ListView news = (ListView)findViewById(R.id.main_news_list);
        newsAdapter = new PostAdapter(this, R.layout.item_list_layout, posts);
        news.setAdapter(newsAdapter);

        // Fetch content from accounts
        YoutubeFetch(YOUTUBE_ACCOUNT);
        jsonFetchInstagram(INSTAGRAM_ACCOUNT);
        parseTwitter(TWITTER_ACCOUNT);
        jsonFetchFacebook(FACEBOOK_ACCOUNT);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts = new ArrayList<Post>();
                setUserData();
                YoutubeFetch(YOUTUBE_ACCOUNT);
                jsonFetchInstagram(INSTAGRAM_ACCOUNT);
                parseTwitter(TWITTER_ACCOUNT);
                jsonFetchFacebook(FACEBOOK_ACCOUNT);
            }
        });
    }

    private void setUserData(){
        PERSON = PREFS.getString("PERSON", "");
        if (PERSON.equals("")){
            PERSON = "Linus Tech Tips";
            YOUTUBE_ACCOUNT = "LinusTechTips";
            TWITTER_ACCOUNT = "LinusTech";
            FACEBOOK_ACCOUNT = "LinusTech";
            INSTAGRAM_ACCOUNT = "linustech";
            PREFS.edit().putString("PERSON", PERSON).commit();
            PREFS.edit().putString(PERSON, YOUTUBE_ACCOUNT+":"+TWITTER_ACCOUNT+":"+FACEBOOK_ACCOUNT+":"+INSTAGRAM_ACCOUNT).commit();
        }else {
            String[] data = PREFS.getString(PERSON, "").split(":");
            YOUTUBE_ACCOUNT = data[0];
            TWITTER_ACCOUNT = data[1];
            FACEBOOK_ACCOUNT = data[2];
            INSTAGRAM_ACCOUNT = data[3];
        }
    }

    /**
     * Add a post to the post array
     * @param post - New post content
     */
    public void addPost(Post post){
        posts.add(post);
        newsAdapter.postList = posts;
        newsAdapter.notifyDataSetChanged();
    }

    /**
     * Fetch youtube playlist id and execute function to retrieve videos
     * @param username - username of youtube channel
     */
    private void YoutubeFetch(final String username)
    {
        new AsyncTask<String, String, String>()
        {
            @Override
            protected String doInBackground(String[] badi) {
                String msg = "";
                try
                {
                    URL url = new URL("https://www.googleapis.com/youtube/v3/channels?part=contentDetails&forUsername=" + username + "&key=AIzaSyDSkGmwHSqOMxvfF0XtlqbjTIUqkDwTEyU");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    msg = IOUtils.toString(conn.getInputStream());
                    Log.v(TAG, "Loaded data successfully");
                }
                catch(Exception e)
                {
                    Log.v(TAG, "Exception while loading data");
                    e.printStackTrace();
                }
                return msg;
            }

            public void onPostExecute(String result)
            {
                String playlistId = parsePlaylistId(result);
                Log.i(TAG, "PLAYLIST ID: " + playlistId);
                String queryString = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+playlistId.toString()+"&key=AIzaSyDSkGmwHSqOMxvfF0XtlqbjTIUqkDwTEyU";
                Log.i(TAG, "QUERYSTRING: " + queryString);
                jsonFetchVideo(queryString);
            }
        }.execute();
    }

    /**
     * Fetch youtube video from a specified channel
     * @param url - url of video query string
     */
    private void jsonFetchVideo(String url)
    {
        new AsyncTask<String, String, String>()
        {
            @Override
            protected String doInBackground(String[] badi) {
                String msg = "";
                try
                {
                    URL url = new URL(badi[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    msg = IOUtils.toString(conn.getInputStream());
                    Log.v(TAG, "Loaded data successfully");
                }
                catch(Exception e)
                {
                    Log.v(TAG, "Exception while loading data");
                    e.printStackTrace();
                }
                return msg;
            }

            public void onPostExecute(String result)
            {
                List videoDataList = parseVideoId(result);
                Log.i(TAG, "VIDEO Data: " + videoDataList);
                for (int i = 0; i < videoDataList.size(); i++) {
                    List videoData = (ArrayList)videoDataList.get(i);
                    addPost(new YouTubePost(Uri.parse(videoData.get(1).toString()), Uri.parse("https://www.youtube.com/watch?v=" + videoData.get(2).toString()), videoData.get(3).toString(), videoData.get(0).toString()));
                }

            }
        }.execute(url);
    }

    /**
     * Parse JSON and retrieve youtube video data
     * @param jsonstring - The jsonstring which we parse
     */
    private List parseVideoId(String jsonstring) {
        Log.v(TAG, "Starting parse....");
        ArrayAdapter temps = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        try {
            List videoDataList = new ArrayList();
            JSONObject jsonObj = new JSONObject(jsonstring);
            Log.v(TAG, jsonObj.toString());
            JSONArray items = jsonObj.getJSONArray("items");
            for (int i = 0; i<3; i++){
                JSONObject o = items.getJSONObject(i);
                JSONObject snippet = o.getJSONObject("snippet");
                JSONObject resource = snippet.getJSONObject("resourceId");
                JSONObject thumbnail = snippet.getJSONObject("thumbnails");
                JSONObject thumbnail_s = thumbnail.getJSONObject("standard");
                String videoDate = snippet.getString("publishedAt");
                String videoTitle = snippet.getString("title");
                String videoThumb = thumbnail_s.getString("url");
                String videoid = resource.getString("videoId");

                List videoData = new ArrayList();
                videoData.add(videoDate);
                videoData.add(videoThumb);
                videoData.add(videoid);
                videoData.add(videoTitle);
                videoDataList.add(videoData);
            }


            return videoDataList;
        } catch (JSONException e) {
            e.printStackTrace();
            List error = new ArrayList();
            return error;
        }
    }
    /**
     * Parse JSON and retrieve youtube playlist id of whose videos we want to eventually retrieve
     * @param jsonstring - The jsonstring which we parse
     */
    private String parsePlaylistId(String jsonstring) {
        Log.v(TAG, "Starting parse....");
        ArrayAdapter temps = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        try {
            JSONObject jsonObj = new JSONObject(jsonstring);
            JSONArray items = jsonObj.getJSONArray("items");
            JSONObject o = items.getJSONObject(0);
            JSONObject details = o.getJSONObject("contentDetails");
            JSONObject playlists = details.getJSONObject("relatedPlaylists");
            String uploads = playlists.getString("uploads");
            Log.v(TAG, uploads); // GOT ID!!
            return uploads;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Fetch Instagram posts by username
     * @param username - The username of whose posts we want to fetch
     */
    private void jsonFetchInstagram(final String username)
    {
        new AsyncTask<String, String, String>()
        {
            @Override
            protected String doInBackground(String[] badi) {
                String msg = "";
                try
                {
                    URL url = new URL("https://www.instagram.com/" + username + "/media/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    msg = IOUtils.toString(conn.getInputStream());
                    Log.v(TAG, "Loaded data successfully");
                }
                catch(Exception e)
                {
                    Log.v(TAG, "Exception while loading data");
                    e.printStackTrace();
                }
                return msg;
            }

            public void onPostExecute(String result)
            {

                List InstaDataList = parseInsta(result);
                Log.i(TAG, "Instagram Data: " + InstaDataList);
                for (int i = 0; i < InstaDataList.size(); i++) {
                    List InstaData = (ArrayList)InstaDataList.get(i);
                    addPost(new InstagramPost(Uri.parse(InstaData.get(0).toString()), InstaData.get(1).toString(), InstaData.get(3).toString()));
                }

            }
        }.execute();
    }

    /**
     * Parse JSON and retrieve Instagram images and general data
     * @param jsonstring - The jsonstring which we parse
     */
    private List parseInsta(String jsonstring) {
        Log.v(TAG, "Starting parse....");
        try {
            List InstaDataList = new ArrayList();
            JSONObject jsonObj = new JSONObject(jsonstring);
            Log.v(TAG, jsonObj.toString());
            JSONArray items = jsonObj.getJSONArray("items");
            for (int i = 0; i<3; i++) {
                JSONObject o = items.getJSONObject(i);
                JSONObject images = o.getJSONObject("images");
                JSONObject caption = o.getJSONObject("caption");
                JSONObject from = caption.getJSONObject("from");
                JSONObject standard_resolution = images.getJSONObject("standard_resolution");

                String text = caption.getString("text");
                String name = from.getString("full_name");
                String url = standard_resolution.getString("url");
                String time = caption.getString("created_time");

                long created_time = Long.parseLong((String) time);
                Date date = new Date(created_time * 1000);


                Log.v(TAG, "URL: " + url.toString());
                Log.v(TAG, "TEXT: " + text.toString());
                Log.v(TAG, "NAME: " + name.toString());

                List InstaData = new ArrayList();
                InstaData.add(url);
                InstaData.add(text);
                InstaData.add(name);
                InstaData.add(date);
                InstaDataList.add(InstaData);
            }

            return InstaDataList;
        } catch (JSONException e) {
            e.printStackTrace();
            List error = new ArrayList();
            return error;
        }
    }

    /**
     * Fetch Facebook posts
     * @param username- Username of the facebook user of whose posts we want to retrieve
     */
    private void jsonFetchFacebook(final String username)
    {
        new AsyncTask<String, String, String>()
        {
            @Override
            protected String doInBackground(String[] badi) {
                String msg = "";
                try
                {
                    URL url = new URL("https://graph.facebook.com/v2.5/"+username+"/posts?fields=message,picture,link,created_time,full_picture,from&access_token="+AccessTokens.FACEBOOK_TOKEN);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    msg = IOUtils.toString(conn.getInputStream());
                    Log.v(TAG, "Loaded data successfully");
                }
                catch(Exception e)
                {
                    Log.v(TAG, "Exception while loading data");
                    e.printStackTrace();
                }
                return msg;
            }

            public void onPostExecute(String result)
            {

                List FcbkDataList = parseFcbk(result);
                Log.i(TAG, "Facebook Data: " + FcbkDataList);
                for (int i = 0; i < FcbkDataList.size(); i++) {
                    List FcbkData = (ArrayList)FcbkDataList.get(i);
                    addPost(new FacebookPost(FcbkData.get(4).toString(),FcbkData.get(2).toString(), FcbkData.get(0).toString(), Uri.parse(FcbkData.get(3).toString()), Uri.parse(FcbkData.get(1).toString())));
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }.execute();
    }

    /**
     * Parse JSON and retrieve facebook post data
     * @param jsonstring - The jsonstring which we parse
     */
    private List parseFcbk(String jsonstring) {
        Log.v(TAG, "Starting parse....");
        try {
            List FcbkDataList = new ArrayList();
            JSONObject jsonObj = new JSONObject(jsonstring);
            Log.v(TAG, jsonObj.toString());
            JSONArray data = jsonObj.getJSONArray("data");
            for (int i = 0; i<4; i++) {
                JSONObject o = data.getJSONObject(i);
                JSONObject from = o.getJSONObject("from");
                Log.v(TAG, o.toString());
                String text = " ";
                String link = " ";
                String image = " ";
                try {
                    text = o.getString("message");
                }
                catch (JSONException e) {
                    text = " ";
                }
                try {
                    link = o.getString("link");
                }
                catch (JSONException e) {
                    link = " ";
                }
                try {
                    image = o.getString("full_picture");
                }
                catch (JSONException e) {
                    image = " ";
                }
                String time = o.getString("created_time");
                String name = from.getString("name");

                Log.v(TAG, "FACEBOOOK URL: " + link.toString());
                Log.v(TAG, "FACEBOOOK TEXT: " + text.toString());
                Log.v(TAG, "FACEBOOOK NAME: " + name.toString());
                Log.v(TAG, "FACEBOOOK TIME: " + time.toString());
                Log.v(TAG, "FACEBOOOK IMAGE: " + image.toString());

                List FcbkData = new ArrayList();
                FcbkData.add(text);
                FcbkData.add(link);
                FcbkData.add(time);
                FcbkData.add(image);
                FcbkData.add(name);
                FcbkDataList.add(FcbkData);
            }
            Log.v(TAG, FcbkDataList.toString());
            return FcbkDataList;
        } catch (JSONException e) {
            e.printStackTrace();
            List error = new ArrayList();
            return error;
        }
    }




    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Log.i("INFO", "Pressed");
        Intent intent = new Intent(this, UserSettings.class);
        intent.putExtra("PERSON", PERSON);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class PostAdapter extends ArrayAdapter<Post> {

        private ArrayList<Post> postList;

        public PostAdapter(Context context, int textViewResourceId, ArrayList<Post> postList) {
            super(context, textViewResourceId, postList);
            this.postList = new ArrayList<Post>();
            this.postList.addAll(postList);
        }

        private class ViewHolder {
            ImageView srclogo;
            TextView creator;
            ImageView contentYT_I;
            TextView contentT_FB;
            TextView content_decription;
            TextView date;
        }

        /**
         * Define page view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Post post = postList.get(position);

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = vi.inflate(R.layout.item_list_layout, null);

                holder = new ViewHolder();
                holder.srclogo = (ImageView) convertView.findViewById(R.id.src_icon);
                holder.creator = (TextView) convertView.findViewById(R.id.post_creator);
                holder.contentYT_I = (ImageView) convertView.findViewById(R.id.in_yt_post_content);
                holder.contentT_FB = (TextView) convertView.findViewById(R.id.tw_fb_post_content);
                holder.content_decription = (TextView) convertView.findViewById(R.id.yt_video_title);
                holder.date = (TextView) convertView.findViewById(R.id.date);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.srclogo.setImageResource(post.SOURCE);
            holder.creator.setText(PERSON);
            holder.date.setText(post.DATE);
            holder.contentYT_I.setOnClickListener(null);

            if (post instanceof TwitterPost){
                TwitterPost convertedPost = (TwitterPost)post;
                holder.contentT_FB.setText(convertedPost.CONTENT);

                holder.contentT_FB.setVisibility(View.VISIBLE);
                holder.contentYT_I.setVisibility(View.GONE);
                holder.content_decription.setVisibility(View.GONE);

            } else if(post instanceof FacebookPost){
                final FacebookPost convertedPost = (FacebookPost) post;
                holder.content_decription.setText(convertedPost.CONTENT);
                Log.v(TAG, "AHDFKHSKFJD: " + convertedPost.IMAGE.toString());
                if (convertedPost.toString().length() < 10 ){
                    holder.contentYT_I.setVisibility(View.GONE);
                }
                else
                {
                    new DownloadImageTask(convertedPost.IMAGE ,holder.contentYT_I).execute();
                    holder.contentYT_I.setVisibility(View.VISIBLE);
                }
                holder.contentT_FB.setVisibility(View.GONE);
                holder.content_decription.setVisibility(View.VISIBLE);

                holder.contentYT_I.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, convertedPost.LINK));
                    }
                });

            }else if (post instanceof YouTubePost) {
                final YouTubePost convertedPost = (YouTubePost) post;

                new DownloadImageTask(convertedPost.CONTENT ,holder.contentYT_I).execute();
                holder.contentYT_I.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, convertedPost.LINK));
                    }
                });

                holder.content_decription.setText(convertedPost.VIDEOTITLE);
                holder.contentT_FB.setVisibility(View.GONE);
                holder.contentYT_I.setVisibility(View.VISIBLE);
                holder.content_decription.setVisibility(View.VISIBLE);

            } else{
                InstagramPost convertedPost = (InstagramPost) post;
                new DownloadImageTask(convertedPost.CONTENT ,holder.contentYT_I).execute();
                holder.content_decription.setText(convertedPost.IMAGETITLE);

                holder.contentT_FB.setVisibility(View.GONE);
                holder.contentYT_I.setVisibility(View.VISIBLE);
                holder.content_decription.setVisibility(View.VISIBLE);
            }

            return convertView;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView yt_thumbnail_view;
        Uri url;

        public DownloadImageTask(Uri url, ImageView yt_thumbnail_view) {
            this.url = url;
            this.yt_thumbnail_view = yt_thumbnail_view;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(url.toString()).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            yt_thumbnail_view.setImageBitmap(result);
        }
    }

    private void parseTwitter(final String username){
        new AsyncTask<String, Integer, List<Status>>(){
            @Override
            protected List<twitter4j.Status> doInBackground(String... urls) {
                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey(AccessTokens.TWITTER_CONSUMER_KEY)
                        .setOAuthConsumerSecret(
                                AccessTokens.TWITTER_CONSUMER_SECRET)
                        .setOAuthAccessToken(
                                AccessTokens.TWITTER_ACCESS_TOKEN)
                        .setOAuthAccessTokenSecret(
                                AccessTokens.TWITTER_ACCESS_TOKEN_SECRET);
                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter twitter = tf.getInstance();
                List<twitter4j.Status> statuses = null;
                try {
                    statuses = twitter.getUserTimeline(username);
                    Log.i("Status Count", statuses.size() + " Feeds");
                    return statuses;
                } catch (TwitterException te) {
                    te.printStackTrace();
                }
                return statuses;
            }

            @Override
            public void onPostExecute(List<twitter4j.Status> statuses) {
                if(statuses == null) {
                    Log.v("twitter", "No statuses to show");
                }
                int count = 1;
                for (twitter4j.Status status:statuses) {
                    if (count == 5){
                        break;
                    }
                    addPost(new TwitterPost(status.getText(), status.getCreatedAt().toString()));
                    count++;
                }
            }
        }.execute();
    }
}
