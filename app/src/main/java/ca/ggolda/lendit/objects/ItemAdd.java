package ca.ggolda.lendit.objects;


import android.os.Parcel;
import android.os.Parcelable;


public class ItemAdd implements Parcelable {
    private String mItemName;
    private double mItemPrice;
    private int mItemImage;
    private String mItemDescription;
    private String mImagePath;



    //Constructor for Items with name, image resource, and description
    public ItemAdd(String itemName, double itemPrice, int itemImage, String itemDescription){
        mItemName = itemName;
        mItemPrice = itemPrice;
        mItemImage = itemImage;
        mItemDescription = itemDescription;
    }

    //Constructor for Items with name, image resource, and description
    public ItemAdd(String itemName, double itemPrice, String imagePath, String itemDescription){
        mItemName = itemName;
        mItemPrice = itemPrice;
        mImagePath = imagePath;
        mItemDescription = itemDescription;
    }

    // Constructor using parcel
    protected ItemAdd(Parcel in) {
        mItemName = in.readString();
        mItemPrice = in.readDouble();
        mItemImage = in.readInt();
        mItemDescription = in.readString();
        mImagePath = in.readString();
    }




    public String getItemName() {
        return mItemName;
    }

    public double getItemPrice() {
        return mItemPrice;
    }

    public int getItemImage() {
        return mItemImage;
    }

    public String getItemDescription() {
        return mItemDescription;
    }

    public String getImagePath() {
        return mImagePath;
    }


    // Everything from here down included as part of Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mItemName);
        dest.writeDouble(mItemPrice);
        dest.writeInt(mItemImage);
        dest.writeString(mItemDescription);
        dest.writeString(mImagePath);
    }

    public static final Creator<ItemAdd> CREATOR = new Creator<ItemAdd>() {
        @Override
        public ItemAdd createFromParcel(Parcel in) {
            return new ItemAdd(in);
        }

        @Override
        public ItemAdd[] newArray(int size) {
            return new ItemAdd[size];
        }
    };
}