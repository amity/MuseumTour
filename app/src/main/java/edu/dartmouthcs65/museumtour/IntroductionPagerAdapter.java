package edu.dartmouthcs65.museumtour;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by john on 2/25/18.
 */

public class IntroductionPagerAdapter extends PagerAdapter {

    private Context mContext;
    private View.OnClickListener onClickListener;

    public IntroductionPagerAdapter(Context context, View.OnClickListener onClickListener) {
        mContext = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int id = R.layout.introduction_1;
        switch (position) {
            case 0:
                id = R.layout.introduction_1;
                break;
            case 1:
                id = R.layout.introduction_2;
                break;
            case 2:
                id = R.layout.introduction_3;
                break;
        }

        ViewGroup layout = (ViewGroup) inflater.inflate(id, container, false);
        container.addView(layout);
        if (position == 2) {
            LinearLayout button = (LinearLayout) layout.findViewById(R.id.getStartedButton);
            button.setOnClickListener(this.onClickListener);
        }
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Introduction";

            case 1:
                return "Room Detection";

            case 2:
                return "Get Started";

            default:
                return "";
        }
    }
}
