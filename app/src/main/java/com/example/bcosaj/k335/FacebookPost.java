package com.example.bcosaj.k335;

/**
 * Created by bcosaj on 29.03.2016.
 */
public class FacebookPost extends Post{

    public final String CONTENT;

    public FacebookPost(String CREATOR, String DATE, String CONTENT) {
        super(CREATOR, DATE);
        this.CONTENT = CONTENT;
        this.SOURCE = R.drawable.facebook_icon;
    }
}
