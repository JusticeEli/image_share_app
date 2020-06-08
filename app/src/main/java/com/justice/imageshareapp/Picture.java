
package com.justice.imageshareapp;
public class Picture {
    private String mImageUrl;
    private int mLikes;

    public Picture(String imageUrl, int likes) {
        mImageUrl = imageUrl;
        mLikes = likes;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
    public int getLikeCount() {
        return mLikes;
    }
}
