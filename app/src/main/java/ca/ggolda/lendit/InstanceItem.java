package ca.ggolda.lendit;


import android.os.Parcel;
import android.os.Parcelable;

public class InstanceItem implements Parcelable {
    private String mItemId;
    private String mUserId;
    private String mUsername;
    private String mUserImage;
    private String mItemName;
    private String mItemPrice;
    private String mItemImageUrl;
    private String mItemDescription;


    public InstanceItem() {
    }

    //Constructor for Items with name, image resource, and description
    public InstanceItem(String itemId, String userId, String username, String userImage,
                        String itemName, String itemPrice, String itemImageUrl, String itemDescription) {
        mItemId = itemId;
        mUserId = userId;
        mUsername = username;
        mUserImage = userImage;
        mItemName = itemName;
        mItemPrice = itemPrice;
        mItemImageUrl = itemImageUrl;
        mItemDescription = itemDescription;
    }

    // Constructor using parcel
    protected InstanceItem(Parcel in) {
        mItemId = in.readString();
        mUserId = in.readString();
        mUsername = in.readString();
        mUserImage = in.readString();
        mItemName = in.readString();
        mItemPrice = in.readString();
        mItemImageUrl = in.readString();
        mItemDescription = in.readString();
    }



    public String getItemId() {
        return mItemId;
    }
    public void setItemId(String mItemId) {
        this.mItemId = mItemId;
    }

    public String getUserId() {
        return mUserId;
    }
    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getUsername() {
        return mUsername;
    }
    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getUserImage() {
        return mUserImage;
    }
    public void setUserImage(String mUserImage) {
        this.mUserImage = mUserImage;
    }

    public String getItemName() {
        return mItemName;
    }
    public void setItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public String getItemPrice() {
        return mItemPrice;
    }
    public void setItemPrice(String mItemPrice) {
        this.mItemPrice = mItemPrice;
    }

    public String getItemImageUrl() {
        return mItemImageUrl;
    }
    public void setItemImageUrl(String mItemImageUrl) {
        this.mItemImageUrl = mItemImageUrl;
    }

    public String getItemDescription() {
        return mItemDescription;
    }
    public void setItemDescription(String mItemDescription) {
        this.mItemDescription = mItemDescription;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mItemId);
        dest.writeString(mUserId);
        dest.writeString(mUsername);
        dest.writeString(mUserImage);
        dest.writeString(mItemName);
        dest.writeString(mItemPrice);
        dest.writeString(mItemImageUrl);
        dest.writeString(mItemDescription);
    }

    public static final Creator<InstanceItem> CREATOR = new Creator<InstanceItem>() {
        @Override
        public InstanceItem createFromParcel(Parcel in) {
            return new InstanceItem(in);
        }

        @Override
        public InstanceItem[] newArray(int size) {
            return new InstanceItem[size];
        }
    };

}