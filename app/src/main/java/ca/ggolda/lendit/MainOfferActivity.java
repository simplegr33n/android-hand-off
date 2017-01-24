package ca.ggolda.lendit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainOfferActivity extends AppCompatActivity {

    // Offer 1 - Accept 2 - Available 3 - Collected 4 - Return 5 - Close 6
    String mContract_state = "1" ;
    String mItem_id;
    String mDay_cost;
    String mBorrower_id;
    String mBorrowerUsername;
    String mBorrowerImage;
    String mLender_id;
    String mLenderUsername;
    String mLenderImage;
    String mItemName;
    String mItemImage;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mContractsDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        // Collect the extra currentItem from Detail
        Intent i = getIntent();
        InstanceItem detailItem = i.getParcelableExtra("offer_contract");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mContractsDatabaseReference = mFirebaseDatabase.getReference().child("contracts");

        mItemName = detailItem.getItemName();
        mDay_cost = detailItem.getItemPrice();
        mLender_id = detailItem.getUserId();
        mItem_id = detailItem.getItemId();
        mItemImage = detailItem.getItemImageUrl();
        mLenderUsername = detailItem.getUsername();
        mLenderImage = detailItem.getUserImage();

        // Get Borrower Id
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        mBorrower_id = user.getUid();

        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");
        // For Borrower Username
        mUsersDatabaseReference.child(mBorrower_id).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBorrowerUsername = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // For Borrower Image
        mUsersDatabaseReference.child(mBorrower_id).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBorrowerImage = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



        // Setview for Contract
        ImageView itemImage = (ImageView) findViewById(R.id.item_image);
        Picasso.with(this).load(mItemImage).into(itemImage);



        //HEADER
        // Back button
        ImageView addButton = (ImageView) this.findViewById(R.id.back_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Transactions button
        ImageView transactionsButton = (ImageView) this.findViewById(R.id.transactions_button);
        transactionsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainOfferActivity.this, MainTransactionsActivity.class);
                startActivity(intent);
            }
        });

        // Profile button
        ImageView profileButton = (ImageView) this.findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainOfferActivity.this, MainProfileActivity.class);
                startActivity(intent);
            }
        });


        // Search button
        ImageView searchButton = (ImageView) this.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainOfferActivity.this, MainSearchActivity.class);
                startActivity(intent);
            }
        });



        ImageView actionButton = (ImageView) findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // TODO: create entry in DB with relevant fields
                //Toast.makeText(MainOfferActivity.this, "NOTHING TO OFFER YET", Toast.LENGTH_SHORT).show();
                makeOffer();


            }

        });


    }

    private void makeOffer(){

        String eventId = mContractsDatabaseReference.push().getKey();
        InstanceContract newContract = new InstanceContract(eventId, mContract_state,
                mLender_id, mLenderUsername, mLenderImage,
                mBorrower_id, mBorrowerUsername, mBorrowerImage,
                mItem_id, mItemName, mItemImage);

        mContractsDatabaseReference.child(eventId).setValue(newContract);

        mUsersDatabaseReference.child(mBorrower_id).child("offers").child(eventId).setValue("1");
        mUsersDatabaseReference.child(mLender_id).child("offers").child(eventId).setValue("1");



        Toast.makeText(this, "Successful Offer!" + eventId , Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainOfferActivity.this, MainTransactionsActivity.class);
        startActivity(intent);

    }






    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}