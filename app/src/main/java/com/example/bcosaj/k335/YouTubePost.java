package com.example.bcosaj.k335;

import android.widget.ImageView;
import android.widget.VideoView;

import android.net.Uri;

/**
 * Created by bcosaj on 23.03.2016.
 */
public class YouTubePost extends Post {

    public final int CONTENT;
    public final Uri LINK;
    public final String DESCRIPTION;

    public YouTubePost(String CREATOR, int CONTENT, Uri LINK, String DESCRIPTION, String DATE) {
        super(CREATOR, DATE);
        this.CONTENT = CONTENT;
        this.LINK = LINK;
        this.DESCRIPTION = DESCRIPTION;
        this.SOURCE = R.drawable.youtube_icon;
    }
}
