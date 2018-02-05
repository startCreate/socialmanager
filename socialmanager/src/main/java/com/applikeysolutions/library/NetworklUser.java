package com.applikeysolutions.library;

import android.os.Parcel;
import android.os.Parcelable;

public class NetworklUser implements Parcelable {

    public static final Creator<NetworklUser> CREATOR = new Creator<NetworklUser>() {
        @Override public NetworklUser createFromParcel(Parcel source) {
            return new NetworklUser(source);
        }

        @Override public NetworklUser[] newArray(int size) {
            return new NetworklUser[size];
        }
    };
    private String userId;
    private String accessToken;
    private String profilePictureUrl;
    private String userName;
    private String fullName;
    private String email;
    private String pageLink;

    private NetworklUser(Builder builder) {
        userId = builder.userId;
        accessToken = builder.accessToken;
        profilePictureUrl = builder.profilePictureUrl;
        userName = builder.username;
        fullName = builder.fullName;
        email = builder.email;
        pageLink = builder.pageLink;
    }

    protected NetworklUser(Parcel in) {
        this.userId = in.readString();
        this.accessToken = in.readString();
        this.profilePictureUrl = in.readString();
        this.userName = in.readString();
        this.fullName = in.readString();
        this.email = in.readString();
        this.pageLink = in.readString();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworklUser that = (NetworklUser) o;

        return userId != null ? userId.equals(that.userId) : that.userId == null;
    }

    @Override public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("NetworklUser").append("\n\n");
        sb.append("userId=").append(userId).append("\n\n");
        sb.append("userName=").append(userName).append("\n\n");
        sb.append("fullName=").append(fullName).append("\n\n");
        sb.append("email=").append(email).append("\n\n");
        sb.append("profilePictureUrl=").append(profilePictureUrl).append("\n\n");
        sb.append("pageLink=").append(pageLink).append("\n\n");
        sb.append("accessToken=").append(accessToken).append("\n\n");
        return sb.toString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.accessToken);
        dest.writeString(this.profilePictureUrl);
        dest.writeString(this.userName);
        dest.writeString(this.fullName);
        dest.writeString(this.email);
        dest.writeString(this.pageLink);
    }

    public static final class Builder {
        private String userId;
        private String accessToken;
        private String profilePictureUrl;
        private String username;
        private String fullName;
        private String email;
        private String pageLink;

        private Builder() {
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder profilePictureUrl(String profilePictureUrl) {
            this.profilePictureUrl = profilePictureUrl;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder pageLink(String pageLink) {
            this.pageLink = pageLink;
            return this;
        }

        public NetworklUser build() {
            return new NetworklUser(this);
        }
    }
}
