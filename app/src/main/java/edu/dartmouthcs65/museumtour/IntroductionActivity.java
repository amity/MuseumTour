package edu.dartmouthcs65.museumtour;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import me.relex.circleindicator.CircleIndicator;


public class IntroductionActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(new IntroductionPagerAdapter(this));
        indicator.setViewPager(viewPager);

        LinearLayout button = (LinearLayout) findViewById(R.id.getStartedButton);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        getPreferences(0).edit().putBoolean("intro_completed", true).apply();
        finish();
    }
}
