package com.example.bcosaj.k335;

import android.net.Uri;

/**
 * Created by bcosaj on 23.03.2016.
 */
public class InstagramPost extends Post {

    public final Uri CONTENT;

    public InstagramPost(String CREATOR, Uri CONTENT, String DATE) {
        super(CREATOR, DATE);
        this.CONTENT = CONTENT;
        this.SOURCE = R.drawable.instagram_icon;
    }
}
