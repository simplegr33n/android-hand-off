package ca.ggolda.handoff;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gcgol on 11/01/2016.
 */
public class InstanceContract implements Parcelable {


    private String mContractId;
    private String mContractState;
    private String mLenderId;
    private String mLenderUsername;
    private String mLenderImage;
    private String mBorrowerId;
    private String mBorrowerUsername;
    private String mBorrowerImage;
    private String mItemId;
    private String mItemName;
    private String mItemImage;



    public InstanceContract() {
    }

    // Constructor for mContract with all attributes
    public InstanceContract(String contract_id, String contract_state, String lender_id, String lender_username,
                            String lender_image, String borrower_id, String borrower_username, String borrower_image,
                            String item_id, String item_name, String item_image) {

        mContractId = contract_id;
        mContractState = contract_state;
        mLenderId = lender_id;
        mLenderUsername = lender_username;
        mLenderImage = lender_image;
        mBorrowerId = borrower_id;
        mBorrowerUsername = borrower_username;
        mBorrowerImage = borrower_image;
        mItemId = item_id;
        mItemName = item_name;
        mItemImage = item_image;
    }


    // Constructor using parcel
    protected InstanceContract(Parcel in) {

        mContractId =in.readString();
        mContractState =in.readString();
        mLenderId = in.readString();
        mLenderUsername = in.readString();
        mLenderImage = in.readString();
        mBorrowerId = in.readString();
        mBorrowerUsername = in.readString();
        mBorrowerImage = in.readString();
        mItemId =in.readString();
        mItemName =in.readString();
        mItemImage =in.readString();
    }


    public String getContractId() {
        return mContractId;
    }
    public void setContractId(String mContractId) {
        this.mContractId = mContractId;
    }

    public String getContractState() {
        return mContractState;
    }
    public void setContractState(String mContractState) {
        this.mContractState = mContractState;
    }

    public String getLenderId() {
        return mLenderId;
    }
    public void setLenderId(String mLenderId) {
        this.mLenderId = mLenderId;
    }

    public String getLenderUsername() {
        return mLenderUsername;
    }
    public void setLenderUsername(String mLenderUsername) {
        this.mLenderUsername = mLenderUsername;
    }

    public String getLenderImage() {
        return mLenderImage;
    }
    public void setLenderImage(String mLenderImage) {
        this.mLenderImage = mLenderImage;
    }

    public String getBorrowerId() {
        return mBorrowerId;
    }
    public void setBorrowerId(String mBorrowerId) {
        this.mBorrowerId = mBorrowerId;
    }

    public String getBorrowerUsername() {
        return mBorrowerUsername;
    }
    public void setBorrowerUsername(String mBorrowerUsername) {
        this.mBorrowerUsername = mBorrowerUsername;
    }

    public String getBorrowerImage() {
        return mBorrowerImage;
    }
    public void setBorrowerImage(String mBorrowerImage) {
        this.mBorrowerImage = mBorrowerImage;
    }

    public String getItemId() {
        return mItemId;
    }
    public void setItemId(String mItemId) {
        this.mItemId = mItemId;
    }

    public String getItemName() {
        return mItemName;
    }
    public void setItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public String getItemImage() {
        return mItemImage;
    }
    public void setItemImage(String mItemImage) {
        this.mItemImage = mItemImage;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mContractId);
        dest.writeString(mContractState);
        dest.writeString(mLenderId);
        dest.writeString(mLenderUsername);
        dest.writeString(mLenderImage);
        dest.writeString(mBorrowerId);
        dest.writeString(mBorrowerUsername);
        dest.writeString(mBorrowerImage);
        dest.writeString(mItemId);
        dest.writeString(mItemName);
        dest.writeString(mItemImage);

    }

    public static final Creator<InstanceContract> CREATOR = new Creator<InstanceContract>() {
        @Override
        public InstanceContract createFromParcel(Parcel in) {
            return new InstanceContract(in);
        }

        @Override
        public InstanceContract[] newArray(int size) {
            return new InstanceContract[size];
        }
    };
}