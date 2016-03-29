package com.example.bcosaj.k335;

/**
 * Created by bcosaj on 29.03.2016.
 */
public class FacebookPost extends Post{

    public final String CONTENT;
    public final int IMAGE;

    public FacebookPost(String CREATOR, String DATE, String CONTENT, int IMAGE) {
        super(CREATOR, DATE);
        this.CONTENT = CONTENT;
        this.SOURCE = R.drawable.facebook_icon;
        this.IMAGE = IMAGE;
    }
}
