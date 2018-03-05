package edu.dartmouthcs65.museumtour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

public class RoomView extends AppCompatActivity {
    int roomNum;
    StorageReference rmImgRef, rmHitRef;
    ImageView rmView, rmHitView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);

        // Load main imageview
        rmView = (ImageView) findViewById(R.id.rmImg);
        rmHitView = (ImageView) findViewById(R.id.rmHitbox);

        // Get the number of the room to be displayed
        Intent myIntent = getIntent();
        roomNum = myIntent.getIntExtra(Globals.ROOM_NUM_KEY, 0);


        if (roomNum != 0) {

            // If room number is valid, load heatmap and image
            Log.d("Room", "Loading images for room " + String.valueOf(roomNum));
            rmHitRef = MainActivity.storage.getReferenceFromUrl("gs://cs65-museumtour.appspot.com/RoomHitboxes/" + (String.valueOf(roomNum)) + ".png");
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(rmHitRef)
                    .dontAnimate()
                    .into(rmHitView);
            rmImgRef = MainActivity.storage.getReferenceFromUrl("gs://cs65-museumtour.appspot.com/RoomFloorplans/" + (String.valueOf(roomNum)) + ".png");
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(rmImgRef)
                    .dontAnimate()
                    .into(rmView);

        }
    }



}
