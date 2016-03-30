package com.example.bcosaj.k335;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by bcosaj on 30.03.2016.
 */
abstract class TwitterTask extends AsyncTask<String, Integer, List<Status>> {

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
            user = "LinusTech";
            statuses = twitter.getUserTimeline(user);
            Log.i("Status Count", statuses.size() + " Feeds");
            return statuses;
        } catch (TwitterException te) {
            te.printStackTrace();
        }
        return statuses;
    }

    @Override
    abstract public void onPostExecute(List<twitter4j.Status> statuses);
}