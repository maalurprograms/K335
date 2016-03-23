package com.example.bcosaj.k335;

import android.content.Context;
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
import java.util.Iterator;
import android.util.Log;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "TEST";
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
        posts.add(new Post("Jonas Cosandey The Third of Maalurion and King of the Maluurioses.", "Hello World this should be a long text but i dont know what to write so i just type some things so i can test.", R.drawable.twitter_icon, "12.2.1234"));



        ListView news = (ListView)findViewById(R.id.main_news_list);
        PostAdapter newsAdapter = new PostAdapter(this, R.layout.twitter_layout, posts);
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

        //jsonFetch("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=UUbW18JZRgko_mOGm5er8Yzg&key=AIzaSyDSkGmwHSqOMxvfF0XtlqbjTIUqkDwTEyU");
        jsonFetch("https://www.googleapis.com/youtube/v3/channels?part=contentDetails&forUsername=OneDirectionVEVO&key=AIzaSyDSkGmwHSqOMxvfF0XtlqbjTIUqkDwTEyU");

    }

    private void jsonFetch(String url)
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
                Log.i(TAG, playlistId);
                //String videoId = parseVideoId(result);
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
            TextView content;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.twitter_layout, null);

                holder = new ViewHolder();
                holder.srclogo = (ImageView) convertView.findViewById(R.id.src_icon);
                holder.creator = (TextView) convertView.findViewById(R.id.post_creator);
                holder.content = (TextView) convertView.findViewById(R.id.post_content);
                convertView.setTag(holder);

                holder.creator.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.i("INFO", "Clicked on Account");
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Post post = postList.get(position);
            holder.srclogo.setImageResource(post.SOURCE);
            holder.creator.setText(post.CREATOR);
            holder.content.setText(post.CONTENT);

            return convertView;

        }

    }
}
