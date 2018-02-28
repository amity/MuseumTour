package edu.dartmouthcs65.museumtour;

import android.util.Log;

import java.util.ArrayList;
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

    public MuseumRoom(String name, ArrayList<WorkDisplayed> works){
        roomName = name;
        roomWorks = new ArrayList<>();
        roomWorks.addAll(works);
        for (WorkDisplayed work: roomWorks) {
            Log.d(roomName, work.name);
        }
    }
}
