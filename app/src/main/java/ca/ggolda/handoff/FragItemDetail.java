package ca.ggolda.handoff;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class FragItemDetail extends Fragment {

    String mItemName;
    String mItemDescription;
    String UserId;
    FirebaseAuth mFirebaseAuth;

    InstanceItem detailItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_item, container, false);

        // Get UserID
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        UserId = user.getUid();

        // Get ContractInstance bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            detailItem = bundle.getParcelable("current_item");

            TextView itemName = (TextView) v.findViewById(R.id.item_name);
            itemName.setText(detailItem.getItemName());

            TextView itemDescription = (TextView) v.findViewById(R.id.item_description);
            itemDescription.setText(detailItem.getItemDescription());

            TextView userName = (TextView) v.findViewById(R.id.username);
            userName.setText(detailItem.getUsername());

            LinearLayout borrowView = (LinearLayout) v.findViewById(R.id.borrow_view);
            LinearLayout editView = (LinearLayout) v.findViewById(R.id.edit_view);
            LinearLayout disableView = (LinearLayout) v.findViewById(R.id.disable_view);
            LinearLayout detailsView = (LinearLayout) v.findViewById(R.id.details_view);


            ImageView userImage = (ImageView) v.findViewById(R.id.user_image);
            ImageView userView = (ImageView) v.findViewById(R.id.background_user);


            TextView itemPrice = (TextView) v.findViewById(R.id.item_price);
            itemPrice.setText(detailItem.getItemPrice());


            ImageView itemImage = (ImageView) v.findViewById(R.id.item_image);
            Picasso.with(getActivity()).load(detailItem.getItemImageUrl()).into(itemImage);

            if (detailItem.getUserId().equals(UserId)) {
                detailsView.setVisibility(View.GONE);
                borrowView.setVisibility(View.GONE);

                userView.setVisibility(View.GONE);
                userImage.setVisibility(View.GONE);
                userName.setVisibility(View.GONE);

                editView.setVisibility(View.VISIBLE);
                disableView.setVisibility(View.VISIBLE);


            } else {
                editView.setVisibility(View.GONE);
                disableView.setVisibility(View.GONE);

                userImage.setVisibility(View.VISIBLE);
                userName.setVisibility(View.VISIBLE);

                Picasso.with(getActivity()).load(detailItem.getUserImage()).transform(new CircleTransform()).into(userImage);

            }



            // Borrow button
            ImageView borrowButton = (ImageView) v.findViewById(R.id.borrow_button);
            borrowButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MainOfferActivity.class);
                    intent.putExtra("offer_contract", detailItem);
                    startActivity(intent);
                }
            });


        }


            // User Profile button
            ImageView userProfileImage = (ImageView) v.findViewById(R.id.user_image);
            userProfileImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    FragProfile profileFrag = new FragProfile();

                    Bundle bundle = new Bundle();

                    bundle.putString("profile_user", detailItem.getUserId()); // use as per your need
                    profileFrag.setArguments(bundle);

                    // Insert the fragment by replacing any existing fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, profileFrag)
                            .commit();
                }
            });


        return v;
    }
}