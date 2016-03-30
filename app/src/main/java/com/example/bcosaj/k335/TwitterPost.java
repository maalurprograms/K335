package com.example.bcosaj.k335;

/**
 * Created by bcosaj on 23.03.2016.
 */
public class TwitterPost extends Post {

    public final String CONTENT;

    public TwitterPost(String CONTENT, String DATE) {
        super(DATE);
        this.CONTENT = CONTENT;
        this.SOURCE = R.drawable.twitter_icon;
    }
}
