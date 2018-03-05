package edu.dartmouthcs65.museumtour;

/**
 * Created by laub on 2/25/18.
 */

public abstract class Globals {

    // Data about rooms is stored in indexes; e.g. the color corresponding to the fossil room, index
    // 0, can be found at index zero of ROOM_COLOR

    // Labels/names of the rooms
    public static final String[] ROOM_NAMES = {"No Room Selected", "Vincent Van Gogh Exhibit", "Natural History Exhibit", "Modern Art Exhibit"};

    // Color associated with the room on the heatmap. Int converted from figma hex value
    public static final int[] ROOM_COLOR = {3815331, 2201171, 10179040, 15423319};

    // Tolerance for color matching
    public static final int COLOR_TOLERANCE = 20;

    // Intro completed key
    public static final String INTRO_COMPLETED_KEY = "intro_completed";
    public static final String SHARED_PREF = "museum_tour_shared_preferences";

    // Key for room number
    public static final String ROOM_NUM_KEY = "Room number key";

}
