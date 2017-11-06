package com.example.brumor.geofiretest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "Main Activity";

    private DatabaseReference databaseReference;

    Button sendButton;

    EditText messageEditText;

    String message;

    GeoFire geoFire;

    ArrayList<GeoMessage> geoMessagesArrayList;

    ListView geoMessagesListView;

    GeomessagesListAdapter geoMessagesListAdapter;

    /*
    * Note Pour plus tard : Il y a des bugs du a l'emulateur !
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.send_message);

        messageEditText = findViewById(R.id.message_text);

        geoMessagesListView = findViewById(R.id.geomessage_listview);
        geoMessagesArrayList = new ArrayList<>();
        geoMessagesListAdapter = new GeomessagesListAdapter(this, 3, geoMessagesArrayList);
        geoMessagesListView.setAdapter(geoMessagesListAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        geoFire = new GeoFire(databaseReference.child("test").child("geomessage").child("geofire"));



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message = messageEditText.getText().toString();

                String dbKey = databaseReference.push().getKey();

                GeoMessage currentGeomessage = new GeoMessage(message);

                databaseReference.child("test").child("geomessage")
                        .child("children").child(dbKey).setValue(currentGeomessage);

                //register the location in a separate branch of the database called geofire
                geoFire.setLocation(dbKey, new GeoLocation(1, 5));

            }
        });


        //Verify if the Firebase database is connected
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected) {Log.e(TAG, "Connected");}
                else {Log.e(TAG, "Not connected");}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {Log.e(TAG, "Listener was cancelled");}
        });


        geoFire.queryAtLocation(new GeoLocation(0.4, 0.5), 500).addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                Log.e(TAG, "Key entered :" + key);


                databaseReference.child("test").child("geomessage")
                        .child("children").child(key)
                        .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        geoMessagesListAdapter.add(dataSnapshot.getValue(GeoMessage.class));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}

                });

            }

            @Override
            public void onKeyExited(String key) {}

            @Override
            public void onKeyMoved(String key, GeoLocation location) {}

            @Override
            public void onGeoQueryReady() {}

            @Override
            public void onGeoQueryError(DatabaseError error) {}

        });

    }

}
