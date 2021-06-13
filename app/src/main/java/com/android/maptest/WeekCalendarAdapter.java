package com.android.maptest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Calendar;

public class WeekCalendarAdapter extends FragmentStateAdapter {
    private static int NUM_ITEMS=100;
    public WeekCalendarAdapter(@NonNull WeekViewFragment fragmentActivity) {
        super(fragmentActivity);
    }

    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH)+1;
    int day;
    int day2;
    Calendar mCal;
    int dm;

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        mCal = Calendar.getInstance();
        // year 변수에 저장된 값을 Year에, month변수에 저장된 값을 Month에, 1을 Day에 넣는다.
        mCal.set(Integer.parseInt(String.valueOf(year)), Integer.parseInt(String.valueOf(month+dm)) - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        int dayMax = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        dm = (position*7)/42;
        //System.out.println((position*7+1-dayNum) + "position" + dm + "dm" + dayNum + "dayNum" + dayMax + "dayMAx");
        day = position;
        return WeekCalendarFragment.newInstance(year, month+dm, day, day2);
    }

    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }

}
