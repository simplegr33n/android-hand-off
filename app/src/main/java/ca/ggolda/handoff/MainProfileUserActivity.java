package ca.ggolda.handoff;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class MainProfileUserActivity extends FragmentActivity {


    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FragProfile profileUserFrag = new FragProfile();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, profileUserFrag);
        transaction.commit();


        Bundle extras = getIntent().getExtras();
        userID= extras.getString("profile_user");



        TextView userInventory = (TextView) this.findViewById(R.id.user_inventory);
        userInventory.setText("THEIR ITEMS");

        TextView userRatings = (TextView) this.findViewById(R.id.user_reviews);
        userRatings.setText("THEIR REVIEWS");



        // Add button
        ImageView addButton = (ImageView) this.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainProfileUserActivity.this, MainAddActivity.class);
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
                Intent intent = new Intent(MainProfileUserActivity.this, MainSearchActivity.class);
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
                Intent intent = new Intent(MainProfileUserActivity.this, MainTransactionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Logout button click event
        ImageView profileButton = (ImageView) this.findViewById(R.id.logout_button);
        profileButton.setImageResource(R.drawable.header_profile);
        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainProfileUserActivity.this, MainProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });





        // Lend and Borrow recyclers TODO: change to listviews
      //  RecyclerView mRecyclerLend = (RecyclerView) findViewById(R.id.borrowing_recycler);
       // RecyclerView mRecyclerInventory = (RecyclerView) findViewById(R.id.inventory_recycler);
        FrameLayout mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);



    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        //finish();
        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
