package com.example.bcosaj.k335;

import android.net.Uri;

/**
 * Created by bcosaj on 29.03.2016.
 */
public class FacebookPost extends Post{

    public final Uri LINK;
    public final Uri IMAGE;
    public final String CONTENT;

    public FacebookPost(String CREATOR, String DATE, String CONTENT, Uri IMAGE, Uri LINK) {
        super(DATE);
        this.CONTENT = CONTENT;
        this.SOURCE = R.drawable.facebook_icon;
        this.LINK = LINK;
        this.IMAGE = IMAGE;
    }
}
