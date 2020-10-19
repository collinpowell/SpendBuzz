package com.collinpowell.spendbuzz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.collinpowell.spendbuzz.Models.ScreenItem;
import com.collinpowell.spendbuzz.R;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {
    Context mContext;
    List<ScreenItem> ScreenList;

    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> screenList) {
        this.mContext = mContext;
        this.ScreenList = screenList;
    }

    @Override
    public int getCount() {
        return ScreenList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Screen = inflater.inflate(R.layout.intro_screen_viewpager,null);
        TextView title = Screen.findViewById(R.id.Intro_title);
        TextView description = Screen.findViewById(R.id.Intro_desc);

        title.setText(ScreenList.get(position).getTitle());
        description.setText(ScreenList.get(position).getDescription());
        container.addView(Screen);

        return Screen;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
