package com.example.bcosaj.k335;

import android.net.Uri;

/**
 * Created by bcosaj on 23.03.2016.
 */
public class InstagramPost extends Post {

    public final int CONTENT;

    public InstagramPost(String CREATOR, int CONTENT, String DATE) {
        super(CREATOR, DATE);
        this.CONTENT = CONTENT;
        this.SOURCE = R.drawable.instagram_icon;
    }
}
