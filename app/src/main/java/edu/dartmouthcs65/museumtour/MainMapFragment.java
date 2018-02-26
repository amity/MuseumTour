package edu.dartmouthcs65.museumtour;

import android.content.Context;
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


public class MainMapFragment extends Fragment implements View.OnTouchListener{
    // Index of the room pressed on the DOWN touch. Will be compared to room index
    // pressed on the UP touch. If the two are not equal, do not consider a valid click.
    private Integer lastRoomIndex;

    // Room image Views
    View rm0View, rm1View, rm2View, rm3View;

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
        rm0View = mainMapView.findViewById(R.id.rm_0_img);
        rm1View = mainMapView.findViewById(R.id.rm_1_img);
        rm2View = mainMapView.findViewById(R.id.rm_2_img);
        rm3View = mainMapView.findViewById(R.id.rm_3_img);

        // Set transparancy of all rooms to 0, except null room
        rm0View.setAlpha(1);
        rm1View.setAlpha(0);
        rm2View.setAlpha(0);
        rm3View.setAlpha(0);



        return mainMapView;
    }

    public boolean onTouch(View myView, MotionEvent myME) {
        Log.d("Touch", "Touch registered");

        // On down touch, ensure hitbox image is brought to front.
        // On up touch, test color of hit location. If color matches hitbox of a room, select that
        // room.

        // Get x and y click location
        final int xCoord = (int) myME.getX();
        final int yCoord = (int) myME.getY();

        // Get the hitbox color at these coordinates
        Integer colorTouched = null; // Color of the pixel at the pressed location
        ImageView hitboxImg = (ImageView) getActivity().findViewById(R.id.hitboxes);

        if (hitboxImg != null) {
            // Convert image to bitmap
            hitboxImg.setDrawingCacheEnabled(true);
            Bitmap hitboxBM = Bitmap.createBitmap(hitboxImg.getDrawingCache());
            // Get color of pixel at x, y
            if (hitboxBM != null) {
                colorTouched = hitboxBM.getPixel(xCoord, yCoord);
            } else return false;
        }

        // If color was selected, see if it matches any of the

        Integer roomIndex = 0;  // Index of clicked room. 0 represents null room
        if (colorTouched != null) {
            for (int i = 0; i < Globals.ROOM_COLOR.length; i++) {
                if (colorsMatch(Globals.ROOM_COLOR[i], colorTouched)) {
                    roomIndex = i;
                    break;
                }
            }
        }

        // get the motion event action, and test if up or down press
        final int meAction = myME.getAction();
        if (meAction == MotionEvent.ACTION_DOWN){
            // Down press, remember room index
            lastRoomIndex = roomIndex;
        } else if (meAction == MotionEvent.ACTION_UP) {
            if (roomIndex != null & lastRoomIndex != null) {
                if (lastRoomIndex == roomIndex) {
                    Toast.makeText(getActivity().getApplicationContext(), "Room pressed: " + Globals.ROOM_NAMES[roomIndex], Toast.LENGTH_SHORT).show();

                    // Set all transparancy to zero. Then, switch transparency of selected room
                    // to 100
                    rm0View.setAlpha(0);
                    rm1View.setAlpha(0);
                    rm2View.setAlpha(0);
                    rm3View.setAlpha(0);

                    // TODO: Make this switch statement launch the room activity (or fragment,
                    // TODO: depending on impplementation) before changing map UI
                    switch (lastRoomIndex) {
                        case 0:
                            rm0View.setAlpha(1);
                            break;
                        case 1:
                            rm1View.setAlpha(1);
                            break;
                        case 2:
                            rm2View.setAlpha(1);
                            break;
                        case 3:
                            rm3View.setAlpha(1);
                            break;
                    }
                }
            }
        }


        return true;
    }

    // Compares two colors, c1 and c2. If their RGB componants are all the same (within a certain
    // tolerance defined in the globals file. Returns true if the colors match, false otherwise.
    private boolean colorsMatch(int c1, int c2) {
        if (Math.abs(Color.red(c1) - Color.red(c2)) < Globals.COLOR_TOLERANCE
                & Math.abs(Color.green(c1) - Color.green(c2)) < Globals.COLOR_TOLERANCE
                & Math.abs(Color.blue(c1) - Color.blue(c2)) < Globals.COLOR_TOLERANCE ) {
            return true;
        } else return false;
    }

}
