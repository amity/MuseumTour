package edu.dartmouthcs65.museumtour;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.sql.Time;

import me.relex.circleindicator.CircleIndicator;


public class IntroductionActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(new IntroductionPagerAdapter(this, this));
        indicator.setViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onClick(View view) {
        getSharedPreferences(Globals.SHARED_PREF, 0).edit()
                .putBoolean(Globals.INTRO_COMPLETED_KEY, true).apply();
        finish();
    }
}
