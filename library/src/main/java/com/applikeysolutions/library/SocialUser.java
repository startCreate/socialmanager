package com.applikeysolutions.library;

import android.os.Parcel;
import android.os.Parcelable;

public class SocialUser implements Parcelable {

  private  String userId;
  private  String accessToken;
  private  String profilePictureUrl;
  private  String username;
  private  String fullName;
  private  String email;
  private  String pageLink;

  public String getUserId() {
    return userId;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getProfilePictureUrl() {
    return profilePictureUrl;
  }

  public String getUsername() {
    return username;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }

  public String getPageLink() {
    return pageLink;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public void setProfilePictureUrl(String profilePictureUrl) {
    this.profilePictureUrl = profilePictureUrl;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPageLink(String pageLink) {
    this.pageLink = pageLink;
  }
/*  public SocialUser() {
  }*/

/*  public SocialUser(SocialUser other) {
    this.userId = other.userId;
    this.accessToken = other.accessToken;
    this.profilePictureUrl = other.profilePictureUrl;
    this.username = other.username;
    this.fullName = other.fullName;
    this.email = other.email;
    this.pageLink = other.pageLink;
  }*/

  private SocialUser(Builder builder) {
    userId = builder.userId;
    accessToken = builder.accessToken;
    profilePictureUrl = builder.profilePictureUrl;
    username = builder.username;
    fullName = builder.fullName;
    email = builder.email;
    pageLink = builder.pageLink;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SocialUser that = (SocialUser) o;

    return userId != null ? userId.equals(that.userId) : that.userId == null;
  }

  @Override
  public int hashCode() {
    return userId != null ? userId.hashCode() : 0;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SocialUser {").append("\n\n");
    sb.append("userId=").append(userId).append("\n\n");
    sb.append("username=").append(username).append("\n\n");
    sb.append("fullName=").append(fullName).append("\n\n");
    sb.append("email=").append(email).append("\n\n");
    sb.append("profilePictureUrl=").append(profilePictureUrl).append("\n\n");
    sb.append("pageLink=").append(pageLink).append("\n\n");
    sb.append("accessToken=").append(accessToken).append("\n\n");
    sb.append('}');
    return sb.toString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.userId);
    dest.writeString(this.accessToken);
    dest.writeString(this.profilePictureUrl);
    dest.writeString(this.username);
    dest.writeString(this.fullName);
    dest.writeString(this.email);
    dest.writeString(this.pageLink);
  }

  protected SocialUser(Parcel in) {
    this.userId = in.readString();
    this.accessToken = in.readString();
    this.profilePictureUrl = in.readString();
    this.username = in.readString();
    this.fullName = in.readString();
    this.email = in.readString();
    this.pageLink = in.readString();
  }

  public static final Creator<SocialUser> CREATOR = new Creator<SocialUser>() {
    @Override
    public SocialUser createFromParcel(Parcel source) {
      return new SocialUser(source);
    }

    @Override
    public SocialUser[] newArray(int size) {
      return new SocialUser[size];
    }
  };

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

    public SocialUser build() {
      return new SocialUser(this);
    }
  }


}
