package com.example.bcosaj.k335;

import android.net.Uri;

/**
 * Created by bcosaj on 23.03.2016.
 */
public class YouTubePost extends Post {

    public final Uri CONTENT;
    public final Uri LINK;
    public final String VIDEOTITLE;

    public YouTubePost(Uri CONTENT, Uri LINK, String VIDEOTITLE, String DATE) {
        super(DATE);
        this.CONTENT = CONTENT;
        this.LINK = LINK;
        this.VIDEOTITLE = VIDEOTITLE;
        this.SOURCE = R.drawable.youtube_icon;
    }
}
