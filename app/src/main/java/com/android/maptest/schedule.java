package com.android.maptest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class schedule extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mGoogleMap = null;
    private DBHelper mDbHelper;
    String scheduleDate;
    LatLng location;
    int year;
    int month;
    int day;
    int hour;
    double lat;
    double lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map); // 구글맵 프래그먼트 생성
        mapFragment.getMapAsync(schedule.this);
        Intent intent = getIntent(); // 이전 프래그먼트로부터 데이터를 받아온다
        EditText title = findViewById(R.id.title);
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        hour = intent.getIntExtra("hour", 0);
        System.out.println(year);
        System.out.println(month);
        System.out.println(day);
        scheduleDate = String.valueOf(year)+String.valueOf(month)+String.valueOf(day);
        title.setText(year+"년 "+month+"월 "+day+"일 " +hour+"시");
        mDbHelper = new DBHelper(this); //데이터베이스 생성
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setting();
        }
        Button getAddressButton = (Button) findViewById(R.id.find);
        getAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();
            }
        }); //검색 버튼 클릭 시 주소를 찾아 지도에 표시하는 getAddress()함수 호출
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertRecord();
            }
        });//저장 버튼 클릭 시 현재 액티비티에 적힌 데이터를 SQL에 저장한다.
        Button remove = (Button)findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
            }
        });//삭제 버튼 클릭 시 현재 SQL에 저장된 데이터를 삭제한다.
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });// 취소 버튼 클릭 시 액티비티를 종료한다.
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { //이전 프래그먼트에서 받아온 데이터에 해당하는 데이터베이스에 저장된 주소로 구글맵을 이동한다.
        Cursor cursor  =mDbHelper.getDayUsersBySQL(String.valueOf(year), String.valueOf(month), String.valueOf(day));
        mGoogleMap = googleMap;
        location = new LatLng(37.5817891, 127.008175); // 데이터가 없을 시 기본 주소는 한성대학교로 표시한다.
        while(cursor.moveToNext()){
            lat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(UserContract.Users.LAT)));
            lng = Double.parseDouble(cursor.getString(cursor.getColumnIndex(UserContract.Users.LNG)));
            location = new LatLng(lat, lng);
        }
        googleMap.addMarker(new MarkerOptions().position(location));
        // move the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    private void getAddress() { // 주소를 검색하는 함수
        EditText editText = (EditText)findViewById(R.id.input);
        String address = editText.getText().toString();

        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(address,1);
            if (addresses.size() >0) {
                Address bestResult = (Address) addresses.get(0);
                location = new LatLng(bestResult.getLatitude(), bestResult.getLongitude());
                mGoogleMap.addMarker(new MarkerOptions().position(location).title(address));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        } catch (IOException e) {
            Log.e(getClass().toString(),"Failed in using Geocoder.", e);
            return;
        }

    }
    private void insertRecord() { // 저장 버튼을 누르면 해당 액티비티에 적힌 데이터를 SQL에 저장한다.
        EditText title = findViewById(R.id.title);
        TimePicker startPicker = findViewById(R.id.startpicker);
        TimePicker endPicker = findViewById(R.id.endpicker);
        EditText input = findViewById(R.id.input);
        EditText memo = findViewById(R.id.memo);

        long nOfRows = mDbHelper.insertUserByMethod(String.valueOf(year), String.valueOf(month), String.valueOf(day), title.getText().toString(),
                startPicker.getCurrentHour().toString(), endPicker.getCurrentHour().toString(),
                String.valueOf(location.latitude), String.valueOf(location.longitude), memo.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,nOfRows+" Record Inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Inserted", Toast.LENGTH_SHORT).show();

        finish();

    }

    private void deleteRecord() { // 삭제 버튼을 누르면 해당 데이터에 적힌 데이터를 SQL에서 삭제한다
        Cursor cursor = mDbHelper.getDayUsersBySQL(String.valueOf(year), String.valueOf(month), String.valueOf(day));
        if(hour > 0) {
            cursor = mDbHelper.getHourUsersBySQL(String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(hour));
        }
        cursor.moveToNext();
        long nOfRows = mDbHelper.deleteUserByMethod(cursor.getString(cursor.getColumnIndex(UserContract.Users._ID)));
        if (nOfRows >0)
            Toast.makeText(this,"Record Deleted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setting(){ // 액티비티가 호출되었을 때 이전 프래그먼트에서 전달받은 데이터를 토대로 데이터를 화면에 표시한다.
        EditText title = findViewById(R.id.title);
        TimePicker startPicker = findViewById(R.id.startpicker);
        TimePicker endPicker = findViewById(R.id.endpicker);
        EditText memo = findViewById(R.id.memo);
        Cursor cursor  = mDbHelper.getDayUsersBySQL(String.valueOf(year), String.valueOf(month),
                String.valueOf(day)); // 처음에 커서의 포인터를 해당 년도, 월, 일에 해당하는 부분으로 지정한다.
        if(hour>0)
            cursor  = mDbHelper.getHourUsersBySQL(String.valueOf(year), String.valueOf(month),
                    String.valueOf(day), String.valueOf(hour)); // 만약 시간에 대한 정보가 있을 경우 시간까지 포함하여 포인터를 이동시킨다.
        startPicker.setHour(hour); //데이터를 기반으로 시작시간을 표시한다
        endPicker.setHour(hour+1); // 시작시간에서 한시간 후를 끝나는 시간으로 기본 설정한다.
        //while (cursor.moveToNext()){
        if(cursor.moveToNext()) { //해당 날짜 데이터에 해당하는 스케줄의 제목과 메모를 가져와 작성한다.
            title.setText(cursor.getString(cursor.getColumnIndex(UserContract.Users.SCHEDULE_TITLE)));
            memo.setText(cursor.getString(cursor.getColumnIndex(UserContract.Users.MEMO)));
        }
        //}
    }
}