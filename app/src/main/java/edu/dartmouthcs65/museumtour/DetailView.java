package edu.dartmouthcs65.museumtour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

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

        TextView title = findViewById(R.id.detailTitle);
        TextView header = findViewById(R.id.detailHeader);

        title.setText(work.name);

        if (isArt){
            header.setText("\n Artist: " + work.artist
                    + "\n \n Year: " + work.year);
        } else{
            header.setText("\nScientific Name: " + work.artist
                    + "\n \n Time Period: " + work.year);
        }

        TextView description = findViewById(R.id.detailDescription);
        description.setText(work.description);

        ImageView imageView = findViewById(R.id.detailImage);
        StorageReference imageRef = MainActivity.storage.getReferenceFromUrl(work.URL);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .dontAnimate()
                .into(imageView);
    }
}
