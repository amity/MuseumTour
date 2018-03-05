package edu.dartmouthcs65.museumtour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailView extends AppCompatActivity {

    public String workIdKey = "workId";
    // IN PROGRESS: Nate

    //TODO: set up different view for nat. hist. (artist-classification)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        WorkDisplayed work = (WorkDisplayed) getIntent().
                getSerializableExtra(MainActivity.WORK_KEY);
        boolean isArt = getIntent().getBooleanExtra(MainActivity.IS_ART_KEY, true);

        TextView header = findViewById(R.id.detailHeader);
        if (isArt){
            header.setText(work.name
                    + "\nArtist: " + work.artist
                    + "\nYear: " + work.year);
        } else{
            header.setText(work.name
                    + "\nScientific Name: " + work.artist
                    + "\nTime Period: " + work.year);
        }

        TextView description = findViewById(R.id.detailDescription);
        description.setText(work.description);

        ImageView imageView = findViewById(R.id.detailImage);
        imageView.setImageResource(R.drawable.art);

    }
}
