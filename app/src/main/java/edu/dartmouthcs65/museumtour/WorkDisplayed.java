package edu.dartmouthcs65.museumtour;


import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

/**
 * Created by Nate on 2/25/2018.
 */

public class WorkDisplayed implements Serializable {

    public String name;

    public String artist;

    public String year;

    public String description;

    public StorageReference photoRef;


    public WorkDisplayed(){}

    // Initializes work

    public WorkDisplayed(String workName,
                         String workArtist, String workYear, String workDescrip, String photoURL){
        name = workName;
        artist = workArtist;
        year = workYear;
        description = workDescrip;

        if (photoURL != null){
            photoRef = MainActivity.storage.getReferenceFromUrl(photoURL);
        }
    }
}
