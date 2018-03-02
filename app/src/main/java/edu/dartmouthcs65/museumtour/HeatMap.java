package edu.dartmouthcs65.museumtour;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by laub on 3/2/18.
 */

public class HeatMap {


    // A function that helps determine what a user has tapped.
    // myME: a mouse event corresponding to a user touch
    // hitboxBM: the bitmap of the hitbox overlayed the image the user sees and taps
    // colorArray: an array of ints. Each int must correspond to exactly one color on the hitmap
    // to function as intended. The index of each color should correspond to the index of the label
    // associated with that color (e.g., if the color of the natural history room is indexed at
    // colorArray[2], the label "natural history room" should be indexed at some labelArray[2]

    // returns colorIndex: the index corresponding to the color tapped. IMPORTANT; defaults to zero,
    // which MUST be associated with a null space (e.g. the hallway in mainMap, or a random part of
    // the floor in museumRoom)
    public static int FindItemClicked(MotionEvent myME, Bitmap hitboxBM, int[] colorArray) {

        int roomIndex = 0; // Index of matching color in colorArray (default 0)
        if (hitboxBM == null || myME == null || colorArray == null)
            return 0;

        // On up touch, test color of hit location. If color matches hitbox of a room, select that
        // room.

        // Get x and y click location
        final int xCoord = (int) myME.getX();
        final int yCoord = (int) myME.getY();

        // Get the hitbox color at these coordinates
        Integer colorTouched = null; // Color of the pixel at the pressed location

        colorTouched = hitboxBM.getPixel(xCoord, yCoord);

        // If color was selected, see if it matches any of the

        if (colorTouched != null) {
            for (int i = 0; i < colorArray.length; i++) {
                if (colorsMatch(colorArray[i], colorTouched)) {
                    roomIndex = i;
                    break;
                }
            }
        }

        return roomIndex;

    }

    // Compares two colors, c1 and c2. If their RGB componants are all the same (within a certain
    // tolerance defined in the globals file. Returns true if the colors match, false otherwise.
    private static boolean colorsMatch(int c1, int c2) {
        if (Math.abs(Color.red(c1) - Color.red(c2)) < Globals.COLOR_TOLERANCE
                & Math.abs(Color.green(c1) - Color.green(c2)) < Globals.COLOR_TOLERANCE
                & Math.abs(Color.blue(c1) - Color.blue(c2)) < Globals.COLOR_TOLERANCE ) {
            return true;
        } else return false;
    }


}
