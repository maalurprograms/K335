package com.example.bcosaj.k335;

import android.net.Uri;

/**
 * Created by bcosaj on 23.03.2016.
 */
public class InstagramPost extends Post {

    public final Uri CONTENT;
    public final String IMAGETITLE;

    public InstagramPost(Uri CONTENT, String IMAGETITLE, String DATE) {
        super(DATE);
        this.CONTENT = CONTENT;
        this.SOURCE = R.drawable.instagram_icon;
        this.IMAGETITLE = IMAGETITLE;
    }
}
