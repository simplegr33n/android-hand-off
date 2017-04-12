package ca.ggolda.lendit.fragments;

/**
 * Created by gcgol on 11/04/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ca.ggolda.lendit.R;
import ca.ggolda.lendit.activities.MainTransactionsActivity;
import ca.ggolda.lendit.objects.InstanceContract;

public class FragContract extends Fragment {


    // Offer 1 - Accept 2 - Available 3 - Collected 4 - Return 5 - Close 6

    InstanceContract mContract;
    DatabaseReference mContractsDatabaseReference;
    DatabaseReference mUsersDatabaseReference;
    FirebaseDatabase mFirebaseDatabase;

    String UserId;


    private FirebaseAuth mFirebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contract, container, false);

        TextView actionText = (TextView) v.findViewById(R.id.contract_action);
        ImageView actionButton = (ImageView) v.findViewById(R.id.action_button);

        // Get UserID
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        UserId = user.getUid();


        // Get ContractInstance bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mContract = bundle.getParcelable("current_transaction");

            mFirebaseDatabase = FirebaseDatabase.getInstance();

            TextView nameTextView = (TextView) v.findViewById(R.id.item_name);
            nameTextView.setText(mContract.getItemName());

            ImageView itemImage = (ImageView) v.findViewById(R.id.item_image);
            Picasso.with(getContext()).load(mContract.getItemImage()).into(itemImage);

            TextView username = (TextView) v.findViewById(R.id.username);
            TextView contractAction = (TextView) v.findViewById(R.id.contract_action);
            LinearLayout actionView = (LinearLayout) v.findViewById(R.id.action_view);
            LinearLayout cancelView = (LinearLayout) v.findViewById(R.id.cancel_view);

            Log.e("GETCON", mContract.getContractId());

            mContractsDatabaseReference = mFirebaseDatabase.getReference().child("contracts")
                    .child(mContract.getContractId()).child("contractState");

            mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");


            if (mContract.getContractState().equals("1")) {

                if (UserId.equals(mContract.getLenderId())) {
                    username.setText(mContract.getBorrowerUsername());
                    contractAction.setText("Accept Offer?");
                    actionButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mContractsDatabaseReference.setValue("2");

                            mUsersDatabaseReference.child(mContract.getBorrowerId()).child("offers").
                                    child(mContract.getContractId()).removeValue();
                            mUsersDatabaseReference.child(mContract.getLenderId()).child("offers").
                                    child(mContract.getContractId()).removeValue();

                            mUsersDatabaseReference.child(mContract.getBorrowerId()).child("active").
                                    child(mContract.getContractId()).setValue("2");
                            mUsersDatabaseReference.child(mContract.getLenderId()).child("active").
                                    child(mContract.getContractId()).setValue("2");

                            mUsersDatabaseReference.child(mContract.getBorrowerId()).child("borrowing").
                                    child(mContract.getContractId()).setValue(true);


                            Intent intent = new Intent(getContext(), MainTransactionsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);

                        }
                    });

                } else {
                    username.setText(mContract.getLenderUsername());
                    contractAction.setText("Cancel Offer?");
                    actionView.setVisibility(View.GONE);
                }
            } else if (mContract.getContractState().equals("2")) {

                if (UserId.equals(mContract.getLenderId())) {
                    username.setText(mContract.getBorrowerUsername());
                    contractAction.setText("Make Available?");
                    actionButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mContractsDatabaseReference.setValue("3");

                            mUsersDatabaseReference.child(mContract.getBorrowerId()).child("active").
                                    child(mContract.getContractId()).setValue("3");
                            mUsersDatabaseReference.child(mContract.getLenderId()).child("active").
                                    child(mContract.getContractId()).setValue("3");

                            FragSuccess successFrag = new FragSuccess();
                            // Insert the fragment by replacing any existing fragment
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, successFrag)
                                    .commit();
                        }
                    });


                } else {
                    username.setText(mContract.getLenderUsername());
                    contractAction.setText("Awaiting Availability...");
                    actionView.setVisibility(View.GONE);
                    cancelView.setVisibility(View.GONE);
                }
            } else if (mContract.getContractState().equals("3")) {

                if (UserId.equals(mContract.getLenderId())) {
                    username.setText(mContract.getBorrowerUsername());
                    contractAction.setText("Waiting For Collect...");
                    actionView.setVisibility(View.GONE);
                    cancelView.setVisibility(View.GONE);

                } else {
                    username.setText(mContract.getLenderUsername());
                    contractAction.setText("Confirm Collect...");
                    actionButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mContractsDatabaseReference.setValue("4");

                            mUsersDatabaseReference.child(mContract.getBorrowerId()).child("active").
                                    child(mContract.getContractId()).setValue("4");
                            mUsersDatabaseReference.child(mContract.getLenderId()).child("active").
                                    child(mContract.getContractId()).setValue("4");

                            FragSuccess successFrag = new FragSuccess();
                            // Insert the fragment by replacing any existing fragment
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, successFrag)
                                    .commit();
                        }
                    });

                    cancelView.setVisibility(View.GONE);
                }
            } else if (mContract.getContractState().equals("4")) {

                if (UserId.equals(mContract.getLenderId())) {
                    username.setText(mContract.getBorrowerUsername());
                    contractAction.setText("Waiting For Return...");
                    actionView.setVisibility(View.GONE);
                    cancelView.setVisibility(View.GONE);

                } else {
                    username.setText(mContract.getLenderUsername());
                    contractAction.setText("Return Item");
                    actionButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mContractsDatabaseReference.setValue("5");

                            mUsersDatabaseReference.child(mContract.getBorrowerId()).child("active").
                                    child(mContract.getContractId()).setValue("5");
                            mUsersDatabaseReference.child(mContract.getLenderId()).child("active").
                                    child(mContract.getContractId()).setValue("5");

                            FragSuccess successFrag = new FragSuccess();
                            // Insert the fragment by replacing any existing fragment
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, successFrag)
                                    .commit();
                        }
                    });
                    cancelView.setVisibility(View.GONE);
                }
            } else {

                if (UserId.equals(mContract.getLenderId())) {
                    username.setText(mContract.getBorrowerUsername());
                    contractAction.setText("Close Contract?");
                    actionButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mContractsDatabaseReference.setValue("6");

                            mUsersDatabaseReference.child(mContract.getBorrowerId()).child("active").
                                    child(mContract.getContractId()).removeValue();
                            mUsersDatabaseReference.child(mContract.getLenderId()).child("active").
                                    child(mContract.getContractId()).removeValue();

                            mUsersDatabaseReference.child(mContract.getBorrowerId()).child("history").
                                    child(mContract.getContractId()).setValue(true);
                            mUsersDatabaseReference.child(mContract.getLenderId()).child("history").
                                    child(mContract.getContractId()).setValue(true);

                            mUsersDatabaseReference.child(mContract.getBorrowerId()).child("borrowing").
                                    child(mContract.getContractId()).removeValue();

                            FragSuccess successFrag = new FragSuccess();
                            // Insert the fragment by replacing any existing fragment
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, successFrag)
                                    .commit();
                        }
                    });

                    cancelView.setVisibility(View.GONE);

                } else {
                    username.setText(mContract.getLenderUsername());
                    contractAction.setText("Awaiting Contract Close");
                    cancelView.setVisibility(View.GONE);
                    actionView.setVisibility(View.GONE);
                }
            }
        }

        // Chat button
        ImageView chat_button = (ImageView) v.findViewById(R.id.chat_button);
        chat_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragChat chatFrag = new FragChat();
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, chatFrag)
                        .commit();
            }
        });


        return v;

    }


}