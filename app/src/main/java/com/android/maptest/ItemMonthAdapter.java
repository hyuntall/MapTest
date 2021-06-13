package com.android.maptest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemMonthAdapter extends BaseAdapter {
    private Context mContext;
    private int mResource;
    private ArrayList<ItemMonth> mItems = new ArrayList<ItemMonth>();

    private DBHelper mDbHelper;
    public ItemMonthAdapter(Context context, int resource, ArrayList<ItemMonth> items) {
        mContext = context;
        mItems = items;
        mResource = resource;
    }

    // MyAdapter 클래스가 관리하는 항목의 총 개수를 반환
    @Override
    public int getCount() {
        return mItems.size();
    }

    // MyAdapter 클래스가 관리하는 항목의 중에서 position 위치의 항목을 반환
    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }
    public Object getText1(int position) {
        return mItems.get(position).nText1;
    }
    public Object getText2(int position) {
        return mItems.get(position).nText2;
    }
    public Object getYear(int position) {
        return mItems.get(position).nYear;
    }
    public Object getMonth(int position) {
        return mItems.get(position).nMonth;
    }
    public Object getDayNum(int position) {
        return mItems.get(position).nDayNum;
    }

    // 항목 id를 항목의 위치로 간주함
    @Override
    public long getItemId(int position) {
        return position;
    }

    // position 위치의 항목에 해당되는 항목뷰를 반환하는 것이 목적임
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mDbHelper = new DBHelper(mContext);
        if (convertView == null) { // 해당 항목 뷰가 이전에 생성된 적이 없는 경우
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 항목 뷰를 정의한 xml 리소스(여기서는 mResource 값)으로부터 항목 뷰 객체를 메모리로 로드
            convertView = inflater.inflate(mResource, parent,false);
        }
        // convertView 변수로 참조되는 항목 뷰 객체내에 포함된 텍스트뷰 객체를 id를 통해 얻어옴
        TextView name = (TextView) convertView.findViewById(R.id.daynum);
        // 어댑터가 관리하는 항목 데이터 중에서 position 위치의 항목의 문자열을 설정 텍스트뷰 객체에 설
        name.setText(mItems.get(position).nDayNum);
        TextView text1 = (TextView) convertView.findViewById(R.id.stext1);
        // 어댑터가 관리하는 항목 데이터 중에서 position 위치의 항목의 문자열을 설정 텍스트뷰 객체에 설정
        text1.setText(mItems.get(position).nText1);
        if(mItems.get(position).nText1 != null)
            text1.setBackgroundColor(Color.GREEN); // 월별 달력에서 표시되는 첫번째 스케줄칸의 배경을 초록색으로 설정
        TextView text2 = (TextView) convertView.findViewById(R.id.stext2);
        // 어댑터가 관리하는 항목 데이터 중에서 position 위치의 항목의 문자열을 설정 텍스트뷰 객체에 설정
        text2.setText(mItems.get(position).nText2);
        if(mItems.get(position).nText2 != null) //
            text2.setBackgroundColor(Color.CYAN); // 월별 달력에서 표시되는 두번째 스케줄칸의 배경을 시안색으로 설정

        View view = (View) convertView.findViewById(R.id.month_item);



        return convertView;
    }

}