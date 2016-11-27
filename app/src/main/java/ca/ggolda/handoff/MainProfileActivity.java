package ca.ggolda.handoff;


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


public class MainProfileActivity extends FragmentActivity {


    private DatabaseReference mItemsDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mContractsDatabaseReference;

    private DatabaseReference mInventoryDatabaseReference;
    private DatabaseReference mBorrowingDatabaseReference;

    private AdapterInventory mAdapterInventory;
    private ListView mListViewInventory;
    private AdapterBorrowing mAdapterBorrowing;
    private ListView mListViewBorrowing;

    private ChildEventListener mInventoryEventListener;
    private ChildEventListener mBorrowingEventListener;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private String UserID;

    ArrayList<InstanceContract> borrowing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FragProfile emptyFragment = new FragProfile();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, emptyFragment);
        transaction.commit();

        // Get UserID
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        UserID = user.getUid();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemsDatabaseReference = mFirebaseDatabase.getReference().child("items");
        mContractsDatabaseReference = mFirebaseDatabase.getReference().child("contracts");
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");

        ArrayList<InstanceItem> inventory = new ArrayList<>();

        borrowing = new ArrayList<>();




        mAdapterInventory = new AdapterInventory(MainProfileActivity.this, R.layout.card_inventory, inventory);
        mListViewInventory = (ListView) findViewById(R.id.inventory_listview);
        mListViewInventory.setAdapter(mAdapterInventory);




        setUpAdapterInventory();
        setUpAdapterBorrowing();


        // HEADER
        // Add button
        ImageView addButton = (ImageView) this.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainProfileActivity.this, MainAddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Search button
        ImageView profileButton = (ImageView) this.findViewById(R.id.search_button);
        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainProfileActivity.this, MainSearchActivity.class);
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
                Intent intent = new Intent(MainProfileActivity.this, MainTransactionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Logout button click event
        ImageView logoutButton = (ImageView) this.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // TODO: remove this temp measure shortcut to Chat and install real logout TEMP
                FragChat chatFrag = new FragChat();
                // Insert the fragment by replacing any existing fragment
                MainProfileActivity.this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, chatFrag)
                        .commit();

            }
        });
    }


    private void setUpAdapterInventory() {
        mInventoryDatabaseReference = mUsersDatabaseReference
                .child(UserID).child("items");

        mInventoryDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {


                mItemsDatabaseReference.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        mAdapterInventory.add(dataSnapshot.getValue(InstanceItem.class));


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void setUpAdapterBorrowing() {
        mBorrowingDatabaseReference = mUsersDatabaseReference
                .child(UserID).child("borrowing");

        mBorrowingDatabaseReference.addChildEventListener
                (new ChildEventListener() {
                     @Override
                     public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                         mContractsDatabaseReference.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {

                                 Log.e("BORROWING", "key" + dataSnapshot.getKey());
                                 Log.e("BORROWING", "value" + dataSnapshot.getValue());
                                 Log.e("BORROWING", "children" + dataSnapshot.getChildren());

                                 //mAdapterBorrowing.add(dataSnapshot.getValue(InstanceContract.class));

                                 borrowing.add(dataSnapshot.getValue(InstanceContract.class));

                                 mAdapterBorrowing = new AdapterBorrowing(MainProfileActivity.this, R.layout.card_inventory, borrowing);
                                 mListViewBorrowing = (ListView) findViewById(R.id.borrowing_listview);
                                 mListViewBorrowing.setAdapter(mAdapterBorrowing);



                             }

                             @Override
                             public void onCancelled(DatabaseError databaseError) {
                             }
                         });

                     }

                     @Override
                     public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {

                     }

                     @Override
                     public void onChildRemoved(DataSnapshot dataSnapshot) {

                     }

                     @Override
                     public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                     }

                 }

                );
    }


    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        //finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void signOut() {
        Intent signOutIntent = new Intent(this, MainProfileActivity.class);
        startActivity(signOutIntent);
        finish();
    }
}
