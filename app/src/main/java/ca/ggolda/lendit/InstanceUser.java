package ca.ggolda.lendit;


import android.os.Parcel;
import android.os.Parcelable;

public class InstanceUser implements Parcelable {
    private String mUserId;
    private String mLastActive;
    private String mUsername;
    private String mImageUrl;



    public InstanceUser() {
    }

    //Constructor for Items with name, image resource, and description
    public InstanceUser(String userId, String lastActive, String username, String imageUrl) {

        mUserId = userId;
        mLastActive = lastActive;
        mUsername = username;
        mImageUrl = imageUrl;

    }

    // Constructor using parcel
    protected InstanceUser(Parcel in) {
        mUserId = in.readString();
        mLastActive = in.readString();
        mUsername = in.readString();
        mImageUrl = in.readString();
    }



    public String getUserId() {
        return mUserId;
    }
    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getLastActive() {
        return mLastActive;
    }
    public void setLastActive(String mLastActive) {
        this.mLastActive = mLastActive;
    }

    public String getUsername() {
        return mUsername;
    }
    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserId);
        dest.writeString(mLastActive);
        dest.writeString(mUsername);
        dest.writeString(mImageUrl);

    }

    public static final Creator<InstanceUser> CREATOR = new Creator<InstanceUser>() {
        @Override
        public InstanceUser createFromParcel(Parcel in) {
            return new InstanceUser(in);
        }

        @Override
        public InstanceUser[] newArray(int size) {
            return new InstanceUser[size];
        }
    };

}