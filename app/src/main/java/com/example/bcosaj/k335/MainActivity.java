package com.example.bcosaj.k335;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "TEST";

    private final static String getTokenURL = "https://api.twitter.com/oauth2/token";
    private static String bearerToken;

    ArrayList<Post> posts = new ArrayList<Post>();
    PostAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ListView listView = (ListView) findViewById(R.id.nav_bar_list);

        posts = new ArrayList<Post>();
        posts.add(new TwitterPost("Jonas Cosandey", "Hello World this should be a long text but i dont know what to write so i just type some things so i can test.", "12.2.1234"));
        posts.add(new FacebookPost("Jonas Cosandey", "Facebook Test", "Test", 0));
        posts.add(new YouTubePost("Jonas Cosandey", R.drawable.youtube_icon, Uri.parse("https://www.youtube.com/watch?v=Ld4H349oyTA&ebc=ANyPxKp2b6BBFnv5GIvRdg0nvC6OJ1yCQnAhh-aZG7vl3wPjG3xDiS9edL4rpjpkCeqzSL1MgpzxIApyCZ9kRHXK2IHZthnhpg"), "A Video Example", "12.3.123"));
        posts.add(new InstagramPost("Jonas Cosandey", R.drawable.instagram_icon, "21.3.43"));

        ListView news = (ListView)findViewById(R.id.main_news_list);
        newsAdapter = new PostAdapter(this, R.layout.item_list_layout, posts);
        news.setAdapter(newsAdapter);

        YoutubeFetch("https://www.googleapis.com/youtube/v3/channels?part=contentDetails&forUsername=OneDirectionVEVO&key=AIzaSyDSkGmwHSqOMxvfF0XtlqbjTIUqkDwTEyU");
        new TwitterTask().execute();
    }

    public void addPost(Post post){
        posts.add(post);
        newsAdapter.postList = posts;
        newsAdapter.notifyDataSetChanged();
    }

    private void YoutubeFetch(String url)
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
                String playlistId = parsePlaylistId(result);
                Log.i(TAG, "PLAYLIST ID: " + playlistId);
                //String videoId = parseVideoId(result);
                jsonFetchVideo("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=UUbW18JZRgko_mOGm5er8Yzg&key=AIzaSyDSkGmwHSqOMxvfF0XtlqbjTIUqkDwTEyU");
            }
        }.execute(url);
    }

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
                String videoId = parseVideoId(result);
                Log.i(TAG, "VIDEO ID: " + videoId);
                addPost(new YouTubePost("John Mahama", R.drawable.youtube_icon, Uri.parse("https://www.youtube.com/watch?v="+videoId), "1Direction ist schrott", "10.2.1023"));

            }
        }.execute(url);
    }


    private String parseVideoId(String jsonstring) {
        Log.v(TAG, "Starting parse....");
        ArrayAdapter temps = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        try {
            JSONObject jsonObj = new JSONObject(jsonstring);
            JSONArray items = jsonObj.getJSONArray("items");
            JSONObject o = items.getJSONObject(0);
            JSONObject snippet = o.getJSONObject("snippet");
            JSONObject resource = snippet.getJSONObject("resourceId");
            String videoid = resource.getString("videoId");
            Log.v(TAG, videoid); // GOT ID!!
            return videoid;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String parsePlaylistId(String jsonstring) {
        Log.v(TAG, "Starting parse....");
        ArrayAdapter temps = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        try {
            JSONObject jsonObj = new JSONObject(jsonstring);
            JSONArray items = jsonObj.getJSONArray("items");
            JSONObject o = items.getJSONObject(0);
            String videoid = o.getString("id");
            Log.v(TAG, videoid); // GOT ID!!
            return videoid;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
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
            holder.creator.setText(post.CREATOR);
            holder.date.setText(post.DATE);

            if (post instanceof TwitterPost){
                TwitterPost convertedPost = (TwitterPost)post;
                holder.contentT_FB.setText(convertedPost.CONTENT);

                holder.contentT_FB.setVisibility(View.VISIBLE);
                holder.contentYT_I.setVisibility(View.GONE);
                holder.content_decription.setVisibility(View.GONE);

            } else if(post instanceof FacebookPost){
                FacebookPost convertedPost = (FacebookPost)post;
                holder.contentT_FB.setText(convertedPost.CONTENT);

                holder.contentT_FB.setVisibility(View.VISIBLE);
                holder.contentYT_I.setVisibility(View.GONE);
                holder.content_decription.setVisibility(View.GONE);
            }else if (post instanceof YouTubePost) {
                final YouTubePost convertedPost = (YouTubePost) post;
                holder.contentYT_I.setImageResource(convertedPost.CONTENT);
                holder.content_decription.setText(convertedPost.DESCRIPTION);

                holder.contentT_FB.setVisibility(View.GONE);
                holder.contentYT_I.setVisibility(View.VISIBLE);
                holder.content_decription.setVisibility(View.VISIBLE);

                holder.contentYT_I.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, convertedPost.LINK));
                    }
                });
            } else{
                InstagramPost convertedPost = (InstagramPost) post;
                holder.contentYT_I.setImageResource(convertedPost.CONTENT);

                holder.contentT_FB.setVisibility(View.GONE);
                holder.contentYT_I.setVisibility(View.VISIBLE);
                holder.content_decription.setVisibility(View.GONE);
            }

            return convertView;

        }

    }

    private class TwitterTask extends AsyncTask<String, Integer, List<Status>> {

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
                String user;
                user = "digitec_de";
                statuses = twitter.getUserTimeline(user);
                Log.i("Status Count", statuses.size() + " Feeds");
                return statuses;
            } catch (TwitterException te) {
                te.printStackTrace();
            }
            return statuses;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            if(statuses == null) {
                Log.v("twitter", "No statuses to show");
            }
            for (twitter4j.Status status:statuses) {
                addPost(new TwitterPost(status.getUser().getName(), status.getText(), status.getCreatedAt().toString()));
            }
        }

    }
}
