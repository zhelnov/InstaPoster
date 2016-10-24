package com.ololosha.instaposter.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Pojo for upcoming tweets array
 * <p>
 * Created by alexs on 10/24/16.
 */

public class UpcomingTweet {

    @SerializedName("_id")
    private String id;

    @SerializedName("favorite_count")
    private long favoriteCount;

    @SerializedName("retweet_count")
    private long retweetCount;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("captions")
    private List<String> captions = new ArrayList<>();

    @SerializedName("photo")
    private List<String> photos = new ArrayList<>();

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The _id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The favoriteCount
     */
    public long getFavoriteCount() {
        return favoriteCount;
    }

    /**
     * @param favoriteCount The favorite_count
     */
    public void setFavoriteCount(long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    /**
     * @return The retweetCount
     */
    public long getRetweetCount() {
        return retweetCount;
    }

    /**
     * @param retweetCount The retweet_count
     */
    public void setRetweetCount(long retweetCount) {
        this.retweetCount = retweetCount;
    }

    /**
     * @return The createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The captions
     */
    public List<String> getCaptions() {
        return captions;
    }

    /**
     * @param captions The captions
     */
    public void setCaptions(List<String> captions) {
        this.captions = captions;
    }

    /**
     * @return The photo
     */
    public List<String> getPhotos() {
        return photos;
    }

    /**
     * @param photos The photo
     */
    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "UpcomingTweet{" +
                "id='" + id + '\'' +
                ", favoriteCount=" + favoriteCount +
                ", retweetCount=" + retweetCount +
                ", createdAt='" + createdAt + '\'' +
                ", captions=" + captions +
                ", photo=" + photos +
                '}';
    }
}