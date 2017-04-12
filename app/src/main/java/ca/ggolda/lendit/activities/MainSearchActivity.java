package ca.ggolda.lendit.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import ca.ggolda.lendit.views.FirebaseSearchViewHolder;
import ca.ggolda.lendit.fragments.FragSearchBar;
import ca.ggolda.lendit.fragments.FragWrap;
import ca.ggolda.lendit.objects.InstanceItem;
import ca.ggolda.lendit.R;

public class MainSearchActivity extends AppCompatActivity {
    private DatabaseReference mItemsDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mFirebaseAuth;

    public static final int RC_SIGN_IN = 1;

    private StaggeredGridLayoutManager mGridManager;

    RecyclerView mRecyclerView;

    String UserID = null;
    String mRealName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemsDatabaseReference = mFirebaseDatabase.getReference().child("items");
        setUpFirebaseAdapter();

        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");

        // Get UserID
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();



        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    UserID = user.getUid();
                    //user signed in
                    OnSignedInInitialise(user.getDisplayName());
                    // Check if user has createdAt set, if not, set
                    mUsersDatabaseReference.child(UserID).child("createdAt").
                            addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null) {
                                        mUsersDatabaseReference.child(UserID).child("createdAt").setValue(ServerValue.TIMESTAMP);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });


                    // Check if user has createdAt set, if not, set
                    mUsersDatabaseReference.child(UserID).child("name").
                            addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null) {
                                        mUsersDatabaseReference.child(UserID).child("name").setValue(mRealName);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });


                    // Check if user has username
                    // if not, send to create username page
                    mUsersDatabaseReference.child(UserID).child("username").
                            addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null) {
                                        Intent intent = new Intent(MainSearchActivity.this, MainSetUsernameActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                } else {
                    //user signed out
                    OnSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        if (user != null) {
            UserID = user.getUid();
            mRealName = user.getDisplayName();

            Log.e("REALNAME", "Real" + user.getDisplayName());


            // Check if user has createdAt set, if not, set
            mUsersDatabaseReference.child(UserID).child("createdAt").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                mUsersDatabaseReference.child(UserID).child("createdAt").setValue(ServerValue.TIMESTAMP);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });


            // Check if user has createdAt set, if not, set
            mUsersDatabaseReference.child(UserID).child("name").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                mUsersDatabaseReference.child(UserID).child("name").setValue(mRealName);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });


            // Check if user has username
            // if not, send to create username page
            mUsersDatabaseReference.child(UserID).child("username").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                Intent intent = new Intent(MainSearchActivity.this, MainSetUsernameActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }


        //Refreshes user active timestamp every time search activity created
        // TODO: maybe do this refresh elsewhere?
        if (UserID != null) {
            mUsersDatabaseReference.child(UserID).child("lastActive").setValue(ServerValue.TIMESTAMP);

        }


        // Add button
        ImageView addButton = (ImageView) this.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener()

                                     {

                                         @Override
                                         public void onClick(View v) {
                                             Intent intent = new Intent(MainSearchActivity.this, MainAddActivity.class);
                                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                                     | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                             startActivity(intent);
                                         }
                                     }
        );

        // Search button
        ImageView searchButton = (ImageView) this.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener()

                                        {

                                            @Override
                                            public void onClick(View v) {

                                                FragSearchBar searchFrag = new FragSearchBar();
                                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                                transaction.add(R.id.fragment_search, searchFrag);
                                                transaction.commit();


                                            }
                                        }
        );


        // Transactions button
        ImageView transactionsButton = (ImageView) this.findViewById(R.id.transactions_button);
        transactionsButton.setOnClickListener(new View.OnClickListener()

                                              {

                                                  @Override
                                                  public void onClick(View v) {
                                                      Intent intent = new Intent(MainSearchActivity.this, MainTransactionsActivity.class);
                                                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                                              | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                      startActivity(intent);
                                                  }
                                              }
        );

        // Profile button
        ImageView profileButton = (ImageView) this.findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener()

                                         {

                                             @Override
                                             public void onClick(View v) {
                                                 Intent intent = new Intent(MainSearchActivity.this, MainProfileActivity.class);
                                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                                         | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                 startActivity(intent);
                                             }
                                         }
        );


    }

    private void OnSignedInInitialise(String username) {
    }

    private void OnSignedOutCleanUp() {
    }

    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<InstanceItem, FirebaseSearchViewHolder>
                (InstanceItem.class, R.layout.card_grid, FirebaseSearchViewHolder.class,
                        mItemsDatabaseReference) {

            @Override
            protected void populateViewHolder(FirebaseSearchViewHolder viewHolder,
                                              InstanceItem model, int position) {
                viewHolder.bindItem(model);
            }
        };
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mGridManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridManager);
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();


        // Clear fragment of previous detail item
        FragWrap fragWrap = new FragWrap();
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragWrap)
                .commit();

        if (mAuthStateListener != null) {
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }
    }
}