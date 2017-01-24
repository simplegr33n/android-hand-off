package ca.ggolda.lendit;

/**
 * Created by gcgol on 11/04/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FragProfile extends Fragment {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mUsersDatabaseReference;
    ChildEventListener mChildEventListener;
    FirebaseAuth mFirebaseAuth;
    String mUserID;
    String profile_userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get UserID
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        mUserID = user.getUid();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");

        // Edit button
        final ImageView editButton = (ImageView) v.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                FragProfileEdit editProfile = new FragProfileEdit();

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, editProfile)
                        .commit();


            }
        });


        // Add button
        final Button historyButton = (Button) v.findViewById(R.id.history_button);
        historyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainHistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


        final Button profileUser = (Button) v.findViewById(R.id.full_button);
        profileUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MainProfileUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);

                intent.putExtra("profile_user", profile_userid);

                startActivity(intent);

            }
        });

        // Chat button
        final ImageView chatButton = (ImageView) v.findViewById(R.id.chat_button);
        chatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragChat chatFrag = new FragChat();

                Bundle bundle = new Bundle();

                bundle.putString("profile_user", profile_userid); // use as per your need
                chatFrag.setArguments(bundle);

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, chatFrag)
                        .commit();

            }
        });


        final ImageView chatBackground = (ImageView) v.findViewById(R.id.background_chat);
        final ImageView editBackground = (ImageView) v.findViewById(R.id.background_edit);
        final ImageView user_image = (ImageView) v.findViewById(R.id.user_image);
        final TextView user_name = (TextView) v.findViewById(R.id.username);
        final TextView created_date = (TextView) v.findViewById(R.id.created_date);
        final TextView location_name = (TextView) v.findViewById(R.id.user_location);
        final TextView bio = (TextView) v.findViewById(R.id.user_biography);
        final TextView name = (TextView) v.findViewById(R.id.user_name);
        final TextView points = (TextView) v.findViewById(R.id.user_points);



        Bundle mBundle = new Bundle();
        mBundle = getArguments();
        if (mBundle != null) {
            profile_userid = mBundle.getString("profile_user");

            mUsersDatabaseReference.child(profile_userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    editButton.setVisibility(View.GONE);
                    editBackground.setVisibility(View.GONE);
                    historyButton.setVisibility(View.GONE);

                    user_name.setText(dataSnapshot.child("username").getValue(String.class));
                    Picasso.with(getContext()).load(dataSnapshot.child("imageUrl").
                            getValue(String.class)).into(user_image);
                    created_date.setText(dataSnapshot.child("account_created").getValue(String.class));
                    location_name.setText(dataSnapshot.child("location_name").getValue(String.class));
                    bio.setText(dataSnapshot.child("bio").getValue(String.class));
                    name.setText(dataSnapshot.child("name").getValue(String.class));
                    points.setText(String.valueOf(dataSnapshot.child("points").getValue(long.class)));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } else {

            mUsersDatabaseReference.child(mUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    chatButton.setVisibility(View.GONE);
                    chatBackground.setVisibility(View.GONE);
                    profileUser.setVisibility(View.GONE);
                    user_name.setText(dataSnapshot.child("username").getValue(String.class));
                    Picasso.with(getContext()).load(dataSnapshot.child("imageUrl").
                            getValue(String.class)).into(user_image);
                    created_date.setText(String.valueOf(dataSnapshot.child("createdAt").getValue(long.class)));
                    location_name.setText(dataSnapshot.child("location_name").getValue(String.class));
                    bio.setText(dataSnapshot.child("bio").getValue(String.class));
                    name.setText(dataSnapshot.child("name").getValue(String.class));
                    points.setText(String.valueOf(dataSnapshot.child("points").getValue(long.class)));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }


        return v;
    }
}