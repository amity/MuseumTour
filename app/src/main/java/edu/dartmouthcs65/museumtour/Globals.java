package edu.dartmouthcs65.museumtour;

/**
 * Created by laub on 2/25/18.
 */

public abstract class Globals {

    // Data about rooms is stored in indexes; e.g. the color corresponding to the fossil room, index
    // 0, can be found at index zero of ROOM_COLOR

    // Labels/names of the rooms
    public static final String[] ROOM_NAMES = {"Null Room", "Van Gogh Room", "Civil War Room", "Modern Art Room"};

    // Color associated with the room on the heatmap. Int converted from figma hex value
    public static final int[] ROOM_COLOR = {3815331, 2201171, 10179040, 15423319};

    // Tolerance for color matching
    public static final int COLOR_TOLERANCE = 20;



}
