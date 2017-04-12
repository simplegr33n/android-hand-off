package ca.ggolda.lendit.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import ca.ggolda.lendit.objects.InstanceItem;
import ca.ggolda.lendit.objects.ItemAdd;
import ca.ggolda.lendit.R;


public class MainAddConfirmActivity extends AppCompatActivity {

    private static final String TAG = "MainAddConfirmActivity";

    String mUsername;
    String mUserImage;
    String mItemName = "";
    String mItemPrice = "";
    String mItemDescription = "";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemsDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private StorageReference mItemPhotosStorageReference;
    private FirebaseStorage mFirebaseStorage;

    private ChildEventListener mUsersChildEventListenter;

    private Bitmap mBitmap;

    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_confirm);

        // Get UserID
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        UserID = user.getUid();

        // Collect the extra currentItem from ItemAdapter
        Intent i = getIntent();
        ItemAdd detailItem = i.getParcelableExtra("new_item_add");

        // Set info to be upped to database to info brought from intent
        mItemName = detailItem.getItemName();
        mItemPrice = String.valueOf(detailItem.getItemPrice());
        mItemDescription = detailItem.getItemDescription();

        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemsDatabaseReference = mFirebaseDatabase.getReference().child("items");
        mItemPhotosStorageReference = mFirebaseStorage.getReference().child("item_photos");


        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");
        // For username grab
        mUsersDatabaseReference.child(UserID).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsername = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // For userimage grab
        mUsersDatabaseReference.child(UserID).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserImage = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        // Create bitmap for display that provided by AddActivity
        File imgFile = new File(detailItem.getImagePath());
        if (!imgFile.equals(null)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            mBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
        } else {
            return;
        }

        ImageView itemImage = (ImageView) findViewById(R.id.detail_image);
        itemImage.setImageBitmap(mBitmap);

        TextView itemName = (TextView) findViewById(R.id.item_name);
        itemName.setText(detailItem.getItemName());

        TextView itemDesc = (TextView) findViewById(R.id.item_description);
        itemDesc.setText(detailItem.getItemDescription());

        TextView itemPrice = (TextView) findViewById(R.id.price_detail_view);
        itemPrice.setText("$" + detailItem.getItemPrice());


        // Confirm prcl add button
        TextView addButton = (TextView) this.findViewById(R.id.confirm_button);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        // Edit (back) button
        ImageView editButton = (ImageView) this.findViewById(R.id.cancel_button);
        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Search button
        ImageView searchButton = (ImageView) this.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAddConfirmActivity.this, MainSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Transactions button
        ImageView transactionsButton = (ImageView) this.findViewById(R.id.transactions_button);
        transactionsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAddConfirmActivity.this, MainTransactionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


        // Profile button
        ImageView profileButton = (ImageView) this.findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAddConfirmActivity.this, MainProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    protected void addItem() {
        Uri selectedImageUri = getImageUri(MainAddConfirmActivity.this, mBitmap);
        // Get a reference to store file at chat_photos/<FILENAME>
        StorageReference photoRef = mItemPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

        // Upload file to Firebase Storage
        photoRef.putFile(selectedImageUri)
                .addOnSuccessListener(MainAddConfirmActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // When the image has successfully uploaded, we get its download URL
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();


                        String eventId = mItemsDatabaseReference.push().getKey();
                        InstanceItem newItem = new InstanceItem(eventId, UserID, mUsername, mUserImage,
                                mItemName, mItemPrice, downloadUrl.toString(), mItemDescription);
                        // Update Items
                        mItemsDatabaseReference.child(eventId).setValue(newItem);
                        // Update User
                        mUsersDatabaseReference.child(UserID).child("items").child(eventId).setValue(true);


                        Toast.makeText(MainAddConfirmActivity.this, "Submission Success!", Toast.LENGTH_SHORT).show();

                        //Return to Profile after successful addition
                        Intent intent = new Intent(MainAddConfirmActivity.this, MainProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });
    }


    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        if (mBitmap != null) {
            // TODO: create a release() method
            //tempUri.release();
            mBitmap = null;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}