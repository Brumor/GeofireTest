package com.example.brumor.geofiretest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button sendButton;

    EditText messageEditText;

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.send_message);

        messageEditText = findViewById(R.id.message_text);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("test/geomessage/geofire");
        final GeoFire geoFire = new GeoFire(databaseReference);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message = messageEditText.getText().toString();

                GeoMessage currentGeomessage = new GeoMessage(message);

                databaseReference.push().setValue(currentGeomessage);
                geoFire.setLocation("test", new GeoLocation(50, 50), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {

                        Toast.makeText(getApplicationContext(), "Send.", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });

    }

    private class GeoMessage {

        String content;

        public GeoMessage(String content) {

            this.content = content;

        }
    }

}
