package edu.dartmouthcs65.museumtour;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nate on 2/28/2018.
 */

public class MuseumRoom {

//    Image roomImage;
//    Image heatMap;

    public ArrayList<WorkDisplayed> roomWorks;
    public String roomName;

    public MuseumRoom(){}

    public MuseumRoom(String name, WorkDisplayed[] works){
        roomName = name;
        roomWorks = new ArrayList<>();
        Collections.addAll(roomWorks, works);
    }
}
