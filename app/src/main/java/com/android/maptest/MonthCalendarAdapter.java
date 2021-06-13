package com.android.maptest;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Calendar;

public class MonthCalendarAdapter extends FragmentStateAdapter {
    private static int NUM_ITEMS=100;
    public MonthCalendarAdapter(@NonNull MonthViewFragment fragmentActivity) {
        super(fragmentActivity);
    }

    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH)+1;
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 모듈러를 사용하여 페이지의 position에 따라 월과 년도를 달리하여 리턴한다.
        if (month == 12)
            year++;
        month = (Calendar.getInstance().get(Calendar.MONTH)+position)%12+1;
        return MonthCalendarFragment.newInstance(year, month);
    }

    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }
}
