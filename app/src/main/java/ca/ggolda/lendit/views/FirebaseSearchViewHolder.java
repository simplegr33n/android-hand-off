package ca.ggolda.lendit.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.ggolda.lendit.utils.CircleTransform;
import ca.ggolda.lendit.R;
import ca.ggolda.lendit.activities.MainSearchActivity;
import ca.ggolda.lendit.fragments.FragItemDetail;
import ca.ggolda.lendit.objects.InstanceItem;

public class FirebaseSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    List<InstanceItem> data = Collections.emptyList();

    View mView;
    Context mContext;

    InstanceItem current;

    public FirebaseSearchViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindItem(InstanceItem item) {

        current = item;

        TextView nameTextView = (TextView) mView.findViewById(R.id.item_name);
        nameTextView.setText(item.getItemName());

        TextView price = (TextView) mView.findViewById(R.id.item_price);
        price.setText(item.getItemPrice());

        TextView description = (TextView) mView.findViewById(R.id.item_description);
        description.setText(item.getItemDescription());

        ImageView item_image = (ImageView) mView.findViewById(R.id.item_image);
        Picasso.with(mContext).load(item.getItemImageUrl()).into(item_image);

        ImageView userImage = (ImageView) mView.findViewById(R.id.user_image);
        Picasso.with(mContext).load(item.getUserImage()).transform(new CircleTransform()).into(userImage);

        TextView username = (TextView) mView.findViewById(R.id.username);
        username.setText(item.getUsername());


    }

    @Override
    public void onClick(final View view) {
        final ArrayList<InstanceItem> items = new ArrayList<>();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("items");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    items.add(snapshot.getValue(InstanceItem.class));
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int position = getLayoutPosition();

                        // Get parent activity, set container view
                        MainSearchActivity PActivity = (MainSearchActivity)mContext;
                   //     InstanceItem item = data.get(position);

                        FragItemDetail itemDetailFragment = new FragItemDetail();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("current_item", current);
                        itemDetailFragment.setArguments(bundle);


                        PActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, itemDetailFragment)
                                .addToBackStack(null)
                                .commit();

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}