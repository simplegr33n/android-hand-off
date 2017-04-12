package ca.ggolda.lendit.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.ggolda.lendit.fragments.FragLogo;
import ca.ggolda.lendit.objects.InstanceContract;
import ca.ggolda.lendit.R;
import ca.ggolda.lendit.adapters.AdapterActive;
import ca.ggolda.lendit.adapters.AdapterOffers;


public class MainTransactionsActivity extends FragmentActivity {

    private DatabaseReference mContractsDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mOffersDatabaseReference;
    private DatabaseReference mActiveDatabaseReference;

    private ChildEventListener mActiveEventListener;
    private ChildEventListener mOffersEventListener;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;

    private String mUserID;

    private ListView mListViewOffers;
    private AdapterOffers mAdapterOffers;
    private ListView mListViewActive;
    private AdapterActive mAdapterActive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        // Get mUserID
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        mUserID = user.getUid();

        FragLogo emptyFragment = new FragLogo();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, emptyFragment);
        transaction.commit();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mContractsDatabaseReference = mFirebaseDatabase.getReference().child("contracts");
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");


        ArrayList<InstanceContract> contracts = new ArrayList<>();
        ArrayList<InstanceContract> offers = new ArrayList<>();

        mAdapterActive = new AdapterActive(MainTransactionsActivity.this, R.layout.card_inventory, contracts);
        mListViewActive = (ListView) findViewById(R.id.active_listview);
        mListViewActive.setAdapter(mAdapterActive);


        mAdapterOffers = new AdapterOffers(MainTransactionsActivity.this, R.layout.card_inventory, offers);
        mListViewOffers = (ListView) findViewById(R.id.offers_listview);
        mListViewOffers.setAdapter(mAdapterOffers);


        setUpAdapterOffers();
        setUpAdapterActive();


        //HEADER
        // Add button
        ImageView addButton = (ImageView) this.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTransactionsActivity.this, MainAddActivity.class);
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
                Intent intent = new Intent(MainTransactionsActivity.this, MainProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


        // Search button
        ImageView searchButton = (ImageView) this.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTransactionsActivity.this, MainSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

    }


    private void setUpAdapterOffers() {
        mOffersDatabaseReference = mUsersDatabaseReference
                .child(mUserID).child("offers");

        mOffersDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Log.e("CHILDEVENTLISTEN", "addedoffer: " + dataSnapshot.getKey());

                mContractsDatabaseReference.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        //offers.add(dataSnapshot.getValue(InstanceContract.class));

                        mAdapterOffers.add(dataSnapshot.getValue(InstanceContract.class));

                        // mAdapterOffers = new AdapterOffers(MainTransactionsActivity.this, R.layout.card_inventory, offers);
                        //mListViewOffers.setAdapter(mAdapterOffers);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {

                Intent intent = new Intent(MainTransactionsActivity.this, MainTransactionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Intent intent = new Intent(MainTransactionsActivity.this, MainTransactionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void setUpAdapterActive() {
        mActiveDatabaseReference = mUsersDatabaseReference
                .child(mUserID).child("active");

        // mActiveDatabaseReference.addChildEventListener(new ChildEventListener()
        mActiveEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Log.e("CHILDEVENTLISTEN", "added: " + dataSnapshot.getKey());

                mContractsDatabaseReference.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //contracts.add(dataSnapshot.getValue(InstanceContract.class));
                        //Log.e("CHILDEVENTLISTEN", "onchildaddedIS: " + contracts.size());

                        mAdapterActive.add(dataSnapshot.getValue(InstanceContract.class));

                        //mAdapterActive = new AdapterActive(MainTransactionsActivity.this, R.layout.card_inventory, contracts);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {

                Log.e("CHILDEVENTLISTENA", "Changed: " + dataSnapshot.getKey());
                //mAdapterActive.clear();
                //setUpFirebaseAdapterActive();


                // TODO: this fully refreshes activity, really i just want to refresh the particular listview! should be easy?
                Intent intent = new Intent(MainTransactionsActivity.this, MainTransactionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


                mAdapterActive.clear();
                setUpAdapterActive();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mActiveDatabaseReference.addChildEventListener(mActiveEventListener);
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
