package ca.ggolda.lendit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterOffers extends ArrayAdapter<InstanceContract> {

    String mUserID;
    FirebaseAuth mFirebaseAuth;

    public AdapterOffers(Context context, int resource, List<InstanceContract> objects) {
        super(context, resource, objects);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_inventory, parent, false);
        }

        // Get UserID
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        mUserID = user.getUid();

        ImageView item_image = (ImageView) convertView.findViewById(R.id.item_image);
        ImageView user_image = (ImageView) convertView.findViewById(R.id.user_image);
        TextView item_name = (TextView) convertView.findViewById(R.id.item_name);
        LinearLayout card_tint = (LinearLayout) convertView.findViewById(R.id.card_tint);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        TextView above_icon = (TextView) convertView.findViewById(R.id.item_name);
        TextView below_icon = (TextView) convertView.findViewById(R.id.username);

        final InstanceContract current = getItem(position);


        item_name.setText(current.getItemName());
        Picasso.with(getContext()).load(current.getItemImage()).into(item_image);


        if (mUserID.equals(current.getLenderId())) {
            username.setText(current.getBorrowerUsername());
            above_icon.setVisibility(View.GONE);
            below_icon.setText("Awaiting Response!");
            Picasso.with(getContext()).load(current.getBorrowerImage()).transform(new CircleTransform()).into(user_image);
            card_tint.setBackgroundColor(Color.parseColor("#8500F024"));
        } else {
            username.setText(current.getLenderUsername());
            above_icon.setText("Awaiting");
            below_icon.setText("Response");
            Picasso.with(getContext()).load(current.getLenderImage()).transform(new CircleTransform()).into(user_image);
            card_tint.setBackgroundColor(Color.parseColor("#85F09000"));

        }


        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Get parent activity, set container view
                MainTransactionsActivity TActivity = (MainTransactionsActivity) getContext();

                FragContract contractFragment = new FragContract();
                Bundle bundle = new Bundle();
                bundle.putParcelable("current_transaction", current);
                contractFragment.setArguments(bundle);

                TActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, contractFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        return convertView;




    }


}
