package edu.dartmouthcs65.museumtour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailView extends AppCompatActivity {

    public String workIdKey = "workId";
    // IN PROGRESS: Nate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        int workId = getIntent().getIntExtra(workIdKey, 0);

//        // TODO: Setup directory + getting work from directory.
//        WorkDisplayed workDisplayed = null;
////        WorkDisplayed workDisplayed = getWork(workId);
//
//        // Update: will be using Firebase. Irrelevant for now.
//
//        ImageView detailImage = findViewById(R.id.detailImage);
//        detailImage.setImageResource(workDisplayed.photoId);a

        TextView headerText = findViewById(R.id.detailHeader);


    }
}
