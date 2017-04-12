package ca.ggolda.lendit.adapters;

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

import ca.ggolda.lendit.fragments.FragItemDetail;
import ca.ggolda.lendit.objects.InstanceItem;
import ca.ggolda.lendit.activities.MainProfileActivity;
import ca.ggolda.lendit.R;

public class AdapterInventory extends ArrayAdapter<InstanceItem> {

    String mUserID;
    FirebaseAuth mFirebaseAuth;

    public AdapterInventory(Context context, int resource, List<InstanceItem> objects) {
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

        final InstanceItem current = getItem(position);


        item_name.setText(current.getItemName());
        Picasso.with(getContext()).load(current.getItemImageUrl()).into(item_image);
        card_tint.setBackgroundColor(Color.parseColor("#8500F024"));



        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Get parent activity, set container view
                MainProfileActivity TActivity = (MainProfileActivity) getContext();

                FragItemDetail contractFragment = new FragItemDetail();
                Bundle bundle = new Bundle();
                bundle.putParcelable("current_item", current);
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
