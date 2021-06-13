package com.android.maptest;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.w3c.dom.Text;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.WINDOW_SERVICE;

public class WeekCalendarFragment extends Fragment {
    Calendar mCal;
    int cellnum;// 캘린더 선언
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    //int year = Calendar.getInstance().get(Calendar.YEAR);
    //int month = Calendar.getInstance().get(Calendar.MONTH)+1;
    //int day;
    private DBHelper mDbHelper;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;
    private int mParam3;
    private int mParam4;
    boolean go1 = false;
    boolean go2 = false;
    int dayNumber;
    int hour;
    public WeekCalendarFragment() {
        // Required empty public constructor
    }

    public static WeekCalendarFragment newInstance(int year, int month, int day, int day2) {
        WeekCalendarFragment fragment = new WeekCalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, year);
        args.putInt(ARG_PARAM2, month);
        args.putInt(ARG_PARAM3, day);
        args.putInt(ARG_PARAM4, day2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            mParam3 = getArguments().getInt(ARG_PARAM3);
            mParam4 = getArguments().getInt(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDbHelper = new DBHelper(this.getContext());
        int year = mParam1;
        int month = mParam2;
        View rootView = inflater.inflate(R.layout.fragment_week_calendar, container, false);
        ArrayList<String> dayList = new ArrayList<String>();
        ArrayList<String> voidcell = new ArrayList<String>();
        mCal = Calendar.getInstance();
        mCal.set(Integer.parseInt(String.valueOf(year)), Integer.parseInt(String.valueOf(month)) - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        int dayMax = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dm = (mParam3*7+1-dayNum)/dayMax;
        int day = mParam3*7%42;
        System.out.println(day);
        if(day<7)
            day=0;
        int day2 = mParam4;
        int count=0;

        if(day<dayNum) {
            for (int i = 1; i < dayNum; i++) {
                dayList.add("");
                count++;
            }
            for (int i = 1; i < 8 - count; i++){
                dayList.add(String.valueOf(i));
                System.out.println("month"+month+"dayNum:"+dayNum+"day:"+(i) +"dayMAx:"+dayMax);
            }
        }
        else{
            for (int i = dayNum; i > dayNum-7; i--) { // 최대 일 수만큼 dayList에 요소를 추가한다.
                if((day-i+2)>dayMax)
                    dayList.add("");
                else
                    dayList.add(String.valueOf(day-i+2));
                System.out.println("month"+month+"dayNum:"+dayNum+"day:"+(day-i+2) +"dayMAx:"+dayMax);

            }
        }

        //해당 달의 최대 일 수를 구하기 위해 .getActualMaximum(Calendar.DAY_OF_MONTH) 함수를 사용한다.

        ArrayAdapter<String> adapt
                = new ArrayAdapter<String>(
                getActivity(),
                R.layout.item_week,R.id.item_gridview2,
                dayList); // 기존에 simple_list_item_1 리소스를 사용하였으나 텍스트 정렬을 위해
        // item_month 레이아웃을 만들어 그 내부에 만든 item_gridview를 사용하였다.

        // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        GridView gridview = rootView.findViewById(R.id.gridview2);
        // 어댑터를 GridView 객체에 연결
        gridview.setAdapter(adapt);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                cellnum = position+1;
                if(dayList.get(position) != "") {
                    dayNumber = Integer.parseInt(dayList.get(position));
                    go1 = true;
                }
            }
        });
        // 데이터 원본 준비
        String[] items = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

        //어댑터 준비 (배열 객체 이용, simple_list_item_1 리소스 사용
        ArrayAdapter<String> adapt2
                = new ArrayAdapter<String>(
                getActivity(),
                R.layout.item_datetime,R.id.datetime,
                items);

        //어댑터 연결
        ListView list = rootView.findViewById(R.id.listView);
        list.setAdapter(adapt2);
        for(int i=0; i<24; i++){
            for(int j=0; j<7; j++) {
                Cursor cursor = mDbHelper.getHourUsersBySQL(String.valueOf(year), String.valueOf(month), String.valueOf(dayList.get(j)), String.valueOf(i));
                if (cursor.moveToNext()){
                    voidcell.add(cursor.getString(cursor.getColumnIndex(UserContract.Users.SCHEDULE_TITLE)));
                }
                else
                    voidcell.add("");
            }
        }
        ArrayAdapter<String> adapt3
                = new ArrayAdapter<String>(
                getActivity(),
                R.layout.item_hour, R.id.item_hour,
                voidcell){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView tv_cell = (TextView) super.getView(position,convertView,parent);
                tv_cell.setBackgroundColor(Color.WHITE);
                return tv_cell;
            }
        }; // 기존에 simple_list_item_1 리소스를 사용하였으나 텍스트 정렬을 위해
        // item_month 레이아웃을 만들어 그 내부에 만든 item_gridview를 사용하였다.

        // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        GridView gridview2 = rootView.findViewById(R.id.gridview3);
        // 어댑터를 GridView 객체에 연결
        gridview2.setAdapter(adapt3);
        gridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(cellnum > 0)
                    Toast.makeText(getActivity(),"position="+position,Toast.LENGTH_SHORT).show();
                hour = position/7;
                System.out.println(hour);
                go2 = true;
                if(adapt3.getItem(position) != "") {
                    Cursor cursor = mDbHelper.getHourUsersBySQL(String.valueOf(year), String.valueOf(month), dayList.get(position%7), String.valueOf(hour));
                    cursor.moveToNext();
                    Intent intent = new Intent(getActivity(), schedule.class);
                    intent.putExtra("year", year);
                    intent.putExtra("month", month);
                    intent.putExtra("day", Integer.parseInt(dayList.get(position%7)));
                    intent.putExtra("hour", hour);
                    startActivity(intent);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(go1 && go2) {
                    Intent intent = new Intent(getActivity(), schedule.class);
                    intent.putExtra("year", mParam1);
                    intent.putExtra("month", mParam2);
                    intent.putExtra("day", dayNumber);
                    intent.putExtra("hour",hour);
                    startActivity(intent);

                }
            }
        });
        return rootView;
    }
}