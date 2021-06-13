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
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(schedule.this);
        Intent intent = getIntent();
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
        mDbHelper = new DBHelper(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //getLastLocation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setting();
        }
        Button getAddressButton = (Button) findViewById(R.id.find);
        getAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();
            }
        });
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertRecord();
            }
        });
        Button remove = (Button)findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
            }
        });
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    final int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 0;
    Location mLastLocation;
    private void getLastLocation() {
        // 1. 위치 접근에 필요한 권한 검사 및 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    schedule.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
                    REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
            );
            return;
        }

        // 2. Task<Location> 객체 반환
        Task task = mFusedLocationClient.getLastLocation();

        // 3. Task가 성공적으로 완료 후 호출되는 OnSuccessListener 등록
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // 4. 마지막으로 알려진 위치(location 객체)를 얻음.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(schedule.this);
                if (location != null) {
                    mLastLocation = location;
                    //updateUI();
                } else
                    Toast.makeText(getApplicationContext(),
                            "No location detected",
                            Toast.LENGTH_SHORT)
                            .show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Cursor cursor  =mDbHelper.getDayUsersBySQL(String.valueOf(year), String.valueOf(month), String.valueOf(day));
        mGoogleMap = googleMap;
        location = new LatLng(37.5817891, 127.008175);
        while(cursor.moveToNext()){
            lat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(UserContract.Users.LAT)));
            lng = Double.parseDouble(cursor.getString(cursor.getColumnIndex(UserContract.Users.LNG)));
            location = new LatLng(lat, lng);
        }
        googleMap.addMarker(new MarkerOptions().position(location));
        // move the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    private void getAddress() {
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
    private void insertRecord() {
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

    private void deleteRecord() {
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
    private void setting(){
        EditText title = findViewById(R.id.title);
        TimePicker startPicker = findViewById(R.id.startpicker);
        TimePicker endPicker = findViewById(R.id.endpicker);
        EditText memo = findViewById(R.id.memo);
        Cursor cursor  = mDbHelper.getDayUsersBySQL(String.valueOf(year), String.valueOf(month), String.valueOf(day));
        if(hour>0)
            cursor  = mDbHelper.getHourUsersBySQL(String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(hour));
        startPicker.setHour(hour);
        endPicker.setHour(hour+1);
        while (cursor.moveToNext()){
            title.setText(cursor.getString(cursor.getColumnIndex(UserContract.Users.SCHEDULE_TITLE)));
            memo.setText(cursor.getString(cursor.getColumnIndex(UserContract.Users.MEMO)));
        }
    }
}