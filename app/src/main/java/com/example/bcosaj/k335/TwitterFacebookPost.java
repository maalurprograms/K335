package com.example.bcosaj.k335;

/**
 * Created by bcosaj on 23.03.2016.
 */
public class TwitterFacebookPost extends Post {

    public final String CONTENT;

    public TwitterFacebookPost(String CREATOR, String CONTENT, int SOURCE, String DATE) {
        super(CREATOR, SOURCE, DATE);
        this.CONTENT = CONTENT;
    }
}
