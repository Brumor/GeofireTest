package com.example.brumor.geofiretest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "Main Activity";

    public final int[] id = {0};

    private DatabaseReference databaseReference;

    Button sendButton;

    EditText messageEditText;

    String message;

    /*
    * Note Pour plus tard : Il y a des bugs du a l'emulateur !
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        id[0] = 0;

        sendButton = findViewById(R.id.send_message);

        messageEditText = findViewById(R.id.message_text);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message = messageEditText.getText().toString();

                String dbKey =databaseReference.push().getKey();

                Log.e(TAG, "Test 1");
                GeoMessage currentGeomessage = new GeoMessage(id[0], message);

                Log.e(TAG, "Test 2");

                databaseReference.child("test").child("geomessage")
                        .child("children").child(dbKey).setValue(currentGeomessage)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "Success !");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "FAIL");
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e(TAG, "Complete");
                    }
                });

                Log.e(TAG, "Test 3");
            }
        });

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected) {
                    Log.e(TAG, "Connected");
                } else {
                    Log.e(TAG, "Not connected");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "Listener was cancelled");

            }
        });


        databaseReference.child("test").child("geomessage").child("children").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("101", "Child Added !");
                id[0] = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("101", "Child CHanged !");
                id[0] = (int) dataSnapshot.getChildrenCount();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private static class GeoMessage {

        int id;
        String content;

        public GeoMessage() {};

        public GeoMessage(int id, String content) {

            this.id = id;
            this.content = content;

        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
