package ca.ggolda.lendit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.ggolda.lendit.R;

/**
 * Created by gcgol on 11/23/2016.
 */

public class MainSetUsernameActivity extends AppCompatActivity {

    String UserID;
    DatabaseReference mUsersDatabaseReference;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        // Get UserID
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        UserID = user.getUid();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");


        // Confirm prcl add button
        TextView setButton = (TextView) this.findViewById(R.id.button_ok);
        setButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                setUsername();
            }
        });

    }


    private void setUsername() {

        EditText text = (EditText)findViewById(R.id.username_edittext);
        String value = text.getText().toString();
        mUsersDatabaseReference.child(UserID).child("username").setValue(value);


        Intent intent = new Intent(MainSetUsernameActivity.this, MainSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }
}
