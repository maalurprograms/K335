package com.example.bcosaj.k335;

import android.app.DownloadManager;
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

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        ArrayList<Post> posts = new ArrayList<Post>();
        posts.add(new TwitterFacebookPost("Jonas Cosandey", "Hello World this should be a long text but i dont know what to write so i just type some things so i can test.", R.drawable.twitter_icon, "12.2.1234"));
        posts.add(new TwitterFacebookPost("Jonas Cosandey", "Facebook Test", R.drawable.facebook_icon, "2.2.1234"));
        posts.add(new YouTubePost("Jonas Cosandey", R.drawable.youtube_icon, Uri.parse("https://www.youtube.com/watch?v=Ld4H349oyTA&ebc=ANyPxKp2b6BBFnv5GIvRdg0nvC6OJ1yCQnAhh-aZG7vl3wPjG3xDiS9edL4rpjpkCeqzSL1MgpzxIApyCZ9kRHXK2IHZthnhpg"), "A Video Example", R.drawable.youtube_icon, "12.3.123"));
        posts.add(new InstagramPost("Jonas Cosandey", R.drawable.instagram_icon, R.drawable.instagram_icon, "21.3.43"));

        ListView news = (ListView)findViewById(R.id.main_news_list);
        PostAdapter newsAdapter = new PostAdapter(this, R.layout.item_list_layout, posts);
        news.setAdapter(newsAdapter);

//        AdapterView.OnItemClickListener mListClickedHandler = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getApplicationContext(), BadiDetailsActivity.class);
//                String selected = parent.getItemAtPosition(position).toString();
////                Toast toast = Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT);
////                toast.show();
//                switch (selected){
//                    case AARBERG:
//                        intent.putExtra("badi", "71");
//                        break;
//                    case ADELBODEN:
//                        intent.putExtra("badi", "27");
//                        break;
//                    case BERN:
//                        intent.putExtra("badi", "6");
//                        break;
//                    default:
//                        intent.putExtra("badi", "55");
//                        break;
//                }
//                intent.putExtra("name", selected);
//                startActivity(intent);
//            }
//        };
//        badis.setOnItemClickListener(mListClickedHandler);
//
//        // Set the adapter for the list view
//        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, new String[]));
//        // Set the list's click listener
//        listView.setOnItemClickListener(new DrawerItemClickListener());

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
                holder.content_decription = (TextView) convertView.findViewById(R.id.yt_content_description);


                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.srclogo.setImageResource(post.SOURCE);
            holder.creator.setText(post.CREATOR);

            if (post instanceof TwitterFacebookPost){
                TwitterFacebookPost convertedPost = (TwitterFacebookPost)post;
                holder.contentT_FB.setText(convertedPost.CONTENT);

                holder.contentT_FB.setVisibility(View.VISIBLE);
                holder.contentYT_I.setVisibility(View.GONE);
                holder.content_decription.setVisibility(View.VISIBLE);

            } else if (post instanceof YouTubePost) {
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

    // Website for Token: apps.twitter.com

    public void getTwitterPosts(String username){

    }

//    private void getBadiTemp(String url){
//        new AsyncTask<String, String, String>(){
//            @Override
//            protected String doInBackground(String[] badi) {
//                String msg = "";
//                try {
//                    URL url = new URL(badi[0]);
//                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//                    int code = conn.getResponseCode();
//                    mDialog.dismiss();
//                    Log.i(TAG, Integer.toString(code));
//                    msg = IOUtils.toString(conn.getInputStream());
//                } catch (Exception e){
//                    Log.v(TAG, e.toString());
//                }
//                return msg;
//            }
//
//            public void onPostExecute(String result){
//                parseJson(result);
//            }
//        }.execute(url);
//    }
//
//    public  void parseJson(String jsonstring){
//        ArrayAdapter temps = new ArrayAdapter<String >(this, android.R.layout.simple_list_item_1);
//        try{
//            JSONObject jsonObject = new JSONObject(jsonstring);
//            JSONObject becken = jsonObject.getJSONObject("becken");
//            Iterator keys = becken.keys();
//            while (keys.hasNext()){
//                String key = (String)keys.next();
//                JSONObject subObj = becken.getJSONObject(key);
//                String name = subObj.getString("beckenname");
//                String temp = subObj.getString("temp");
//                temps.add(name+": "+temp+" C");
//            }
//            ListView badidetails = (ListView)findViewById(R.id.badidetails);
//            badidetails.setAdapter(temps);
//        }catch (JSONException e){
//            Log.v(TAG, e.toString());
//        }
//    }
}
