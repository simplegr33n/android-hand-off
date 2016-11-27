package ca.ggolda.handoff;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gcgol on 11/01/2016.
 */
public class InstanceProfile implements Parcelable {
    private String mUniqueId;
    private String mName;
    private String mUserName;
    private String mLocation;
    private String mBiograpy;
    private String mImage;
    private String mPoints;
    private String mCreateDate;



    // Constructor for mContract with all attributes
    public InstanceProfile(String uniqueId, String name, String username, String location, String biography,
                           String image, String points, String createDate) {

        mUniqueId = uniqueId;
        mName = name;
        mUserName = username;
        mLocation = location;
        mBiograpy = biography;
        mImage = image;
        mPoints = points;
        mCreateDate = createDate;

    }


    // Constructor using parcel
    protected InstanceProfile(Parcel in) {
        mUniqueId = in.readString();
        mName = in.readString();
        mUserName = in.readString();
        mLocation = in.readString();
        mBiograpy = in.readString();
        mImage = in.readString();
        mPoints = in.readString();
        mCreateDate = in.readString();

    }


    public String getUserName() {
        return mUserName;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUniqueId);
        dest.writeString(mName);
        dest.writeString(mUserName);
        dest.writeString(mLocation);
        dest.writeString(mBiograpy);
        dest.writeString(mImage);
        dest.writeString(mPoints);
        dest.writeString(mCreateDate);


    }

    public static final Creator<InstanceProfile> CREATOR = new Creator<InstanceProfile>() {
        @Override
        public InstanceProfile createFromParcel(Parcel in) {
            return new InstanceProfile(in);
        }

        @Override
        public InstanceProfile[] newArray(int size) {
            return new InstanceProfile[size];
        }
    };
}