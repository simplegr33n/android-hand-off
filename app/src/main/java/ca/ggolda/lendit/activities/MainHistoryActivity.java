package ca.ggolda.lendit.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import ca.ggolda.lendit.fragments.FragProfile;
import ca.ggolda.lendit.R;


public class MainHistoryActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        FragProfile emptyFragment = new FragProfile();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, emptyFragment);
        transaction.commit();

        // Add button
        ImageView addButton = (ImageView) this.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainHistoryActivity.this, MainAddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Profile button
        ImageView profileButton = (ImageView) this.findViewById(R.id.search_button);
        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainHistoryActivity.this, MainSearchActivity.class);
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
                Intent intent = new Intent(MainHistoryActivity.this, MainTransactionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Logout button click event
        ImageView logoutButton = (ImageView) this.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        // History recycler
        RecyclerView mRecyclerHistory = (RecyclerView) findViewById(R.id.history_recycler);
        // Stage
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
