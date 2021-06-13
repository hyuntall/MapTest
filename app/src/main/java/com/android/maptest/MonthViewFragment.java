package com.android.maptest;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonthViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthViewFragment newInstance(String param1, String param2) {
        MonthViewFragment fragment = new MonthViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 뷰페이저를 선언하여 프래그먼트에 연결시킨다.
        View rootView = inflater.inflate(R.layout.fragment_month_view, container, false);
        ViewPager2 vpPager = rootView.findViewById(R.id.vpPager);
        FragmentStateAdapter adapter = new MonthCalendarAdapter(this);
        vpPager.setAdapter(adapter);
        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // 뷰 페이저의 각 페이지별로 년도, 월을 달리하여 액션바에 표시하는 코드이다.
                // 월에는 모듈러를 사용하여 12월이 지나면 1월이 되도록 설정하였고
                // 해당 월과 페이지의 position을 더한 값을 12로 나누어 년도를 표현하였다.
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH)+1;
                int ym = ((Calendar.getInstance().get(Calendar.MONTH)+position))/12;
                month = (Calendar.getInstance().get(Calendar.MONTH)+position)%12+1;
                System.out.println(year + ym);
                ActionBar ab = ((MainActivity)getActivity()).getSupportActionBar();
                ab.setTitle((year+ym)+ "년 " + month + "월");
            }
        });
        return rootView;
    }
}