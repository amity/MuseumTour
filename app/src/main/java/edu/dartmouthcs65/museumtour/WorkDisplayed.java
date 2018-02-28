package edu.dartmouthcs65.museumtour;


import com.google.firebase.storage.StorageReference;

/**
 * Created by Nate on 2/25/2018.
 */

public class WorkDisplayed {

    // WiP: Will probably scrap, as we've implemented Firebase database.


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
