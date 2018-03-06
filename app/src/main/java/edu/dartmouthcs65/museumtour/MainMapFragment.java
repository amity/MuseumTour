package edu.dartmouthcs65.museumtour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;


public class MainMapFragment extends Fragment implements View.OnTouchListener {
    // Index of the room pressed on the DOWN touch. Will be compared to room index
    // pressed on the UP touch. If the two are not equal, do not consider a valid click.
    private int lastRoomIndex;

    // Room image Views
    ImageView rm0View, rm1View, rm2View, rm3View, rmHitView;
    StorageReference rm0Ref, rm1Ref, rm2Ref, rm3Ref, rmHitRef;

    public MainMapFragment() {
        // Empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainMapView = inflater.inflate(R.layout.fragment_main_map, container, false);
        mainMapView.setOnTouchListener(this);

        // Get room image views
        rm0View = (ImageView) mainMapView.findViewById(R.id.rm_0_img);
        rm1View = (ImageView) mainMapView.findViewById(R.id.rm_1_img);
        rm2View = (ImageView) mainMapView.findViewById(R.id.rm_2_img);
        rm3View = (ImageView) mainMapView.findViewById(R.id.rm_3_img);
        rmHitView = (ImageView) mainMapView.findViewById(R.id.hitboxes);

        // Set transparancy of all rooms to 0, except null room
        rm0View.setImageAlpha(255);
        rm1View.setImageAlpha(0);
        rm2View.setImageAlpha(0);
        rm3View.setImageAlpha(0);


        // Set floorplan and hitbox from firebase images
        rm0Ref = MainActivity.storage.getReferenceFromUrl("gs://cs65-museumtour.appspot.com/MainMapImgs/0.png");
        rm1Ref = MainActivity.storage.getReferenceFromUrl("gs://cs65-museumtour.appspot.com/MainMapImgs/1.png");
        rm2Ref = MainActivity.storage.getReferenceFromUrl("gs://cs65-museumtour.appspot.com/MainMapImgs/2.png");
        rm3Ref = MainActivity.storage.getReferenceFromUrl("gs://cs65-museumtour.appspot.com/MainMapImgs/3.png");
        rmHitRef = MainActivity.storage.getReferenceFromUrl("gs://cs65-museumtour.appspot.com/MainMapImgs/hitbox.png");



        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(rm0Ref)
                .dontAnimate()
                .into(rm0View);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(rm1Ref)
                .dontAnimate()
                .into(rm1View);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(rm2Ref)
                .dontAnimate()
                .into(rm2View);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(rm3Ref)
                .dontAnimate()
                .into(rm3View);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(rmHitRef)
                .dontAnimate()
                .into(rmHitView);




        return mainMapView;
    }

    public boolean onTouch(View myView, MotionEvent myME) {
        Log.d("Touch", "Touch registered");

        // get mainMap hitbox img
        ImageView hitboxImg = (ImageView) getActivity().findViewById(R.id.hitboxes);


        int roomIndex = 0;  //initialize roomIndex to default(0, hallway)
        if (hitboxImg != null) {
            // Convert image to bitmap
            hitboxImg.setDrawingCacheEnabled(true);
            Bitmap hitboxBM = Bitmap.createBitmap(hitboxImg.getDrawingCache());
            if (hitboxBM != null) {
                // find index of room touched
                roomIndex = Hitbox.FindItemClicked(myME, hitboxBM, Globals.ROOM_COLOR);
            } else return false;
        }



        // get the motion event action, and test if up or down press
        final int meAction = myME.getAction();
        if (meAction == MotionEvent.ACTION_DOWN){
            // Down press, remember room index
            lastRoomIndex = roomIndex;
        } else if (meAction == MotionEvent.ACTION_UP) {
            // Up press, if lastRoomIndex matches current roomIdex, register valid roompress
            if (lastRoomIndex == roomIndex) {
                Toast.makeText(getActivity().getApplicationContext(), "Room pressed: " + Globals.ROOM_NAMES[roomIndex], Toast.LENGTH_SHORT).show();

                // Set all transparancy to zero. Then, switch transparency of selected room
                // to 100
                rm0View.setImageAlpha(0);
                rm1View.setImageAlpha(0);
                rm2View.setImageAlpha(0);
                rm3View.setImageAlpha(0);

                // TODO: Make this switch statement launch the room activity (or fragment,
                // TODO: depending on impplementation) before changing map UI
                switch (lastRoomIndex) {
                    case 0:
                        rm0View.setImageAlpha(255);
                        break;
                    case 1:
                        Intent rm1Intent = new Intent(getActivity(), RoomView.class);
                        rm1Intent.putExtra(Globals.ROOM_NUM_KEY, 1);
                        startActivity(rm1Intent);
                        rm1View.setImageAlpha(255);
                        break;
                    case 2:
                        Intent rm2Intent = new Intent(getActivity(), RoomView.class);
                        rm2Intent.putExtra(Globals.ROOM_NUM_KEY, 2);
                        startActivity(rm2Intent);
                        rm2View.setImageAlpha(255);
                        break;
                    case 3:
                        Intent rm3Intent = new Intent(getActivity(), RoomView.class);
                        rm3Intent.putExtra(Globals.ROOM_NUM_KEY, 3);
                        startActivity(rm3Intent);
                        rm3View.setImageAlpha(255);
                        break;
                }
            }
        }


        return true;
    }

    public void onClassify(Integer roomIndex, String room) {
        rm0View.setImageAlpha(0);
        rm1View.setImageAlpha(0);
        rm2View.setImageAlpha(0);
        rm3View.setImageAlpha(0);

        switch (roomIndex) {
            case 0:
                rm0View.setImageAlpha(255);
                break;
            case 1:
                Intent rm1Intent = new Intent(getActivity(), RoomView.class);
                rm1Intent.putExtra(Globals.ROOM_NUM_KEY, 1);
                rm1View.setImageAlpha(255);
                break;
            case 2:
                Intent rm2Intent = new Intent(getActivity(), RoomView.class);
                rm2Intent.putExtra(Globals.ROOM_NUM_KEY, 2);
                rm2View.setImageAlpha(255);
                break;
            case 3:
                Intent rm3Intent = new Intent(getActivity(), RoomView.class);
                rm3Intent.putExtra(Globals.ROOM_NUM_KEY, 3);
                rm3View.setImageAlpha(255);
                break;
        }
    }
}
