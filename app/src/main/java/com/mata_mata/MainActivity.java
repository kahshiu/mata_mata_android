package com.mata_mata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private Button btn_map;
    private Button btn_location;
    private Button btn_call;
    private Button btn_sms;
    private TextView text_lat;
    private TextView text_lng;
    private LocationManager loc_man;
    private String provider;

    // permission code
    int MY_PERMISSION_REQUEST_COARSE_LOCATION = 100;
    int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    int MY_PERMISSION_REQUEST_CALL = 102;
    int MY_PERMISSION_REQUEST_SMS = 103;

    // location tracking variables
    private final long MIN_DISTANCE = 0;
    private final long MIN_TIME = 0;
    private boolean isDeniedFine = true;
    private boolean isDeniedCoarse = true;
    private boolean isDeniedCall = true;
    private boolean isDeniedSms = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // targeted visuals
        btn_map = findViewById(R.id.btn_map);
        btn_location = findViewById(R.id.btn_location);
        btn_call = findViewById(R.id.btn_call);
        btn_sms = findViewById(R.id.btn_sms);
        text_lat = findViewById(R.id.textbox_lat);
        text_lng = findViewById(R.id.textbox_lng);
        loc_man = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        isDeniedCoarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_DENIED;
        isDeniedFine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_DENIED;

        if(isDeniedCoarse) { ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION }, MY_PERMISSION_REQUEST_COARSE_LOCATION ); }
        if(isDeniedFine) { ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, MY_PERMISSION_REQUEST_FINE_LOCATION ); }
        if(!isDeniedCoarse && !isDeniedFine ) loc_man.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,this);

        // event handlers
        btn_map.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(),ActivityMap.class);
               startActivity(intent);
           }
        });

        btn_location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                isDeniedCoarse = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_DENIED;
                isDeniedFine = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_DENIED;

                if(isDeniedCoarse) { ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION }, MY_PERMISSION_REQUEST_COARSE_LOCATION ); }
                if(isDeniedFine) { ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, MY_PERMISSION_REQUEST_FINE_LOCATION ); }

                Criteria criteria = new Criteria();
                provider = loc_man.getBestProvider(criteria, false);
                Location loc = loc_man.getLastKnownLocation(provider);
                text_lat.setText( String.valueOf(loc.getLatitude()) );
                text_lng.setText( String.valueOf(loc.getLongitude()) );
            }
        });

        btn_sms.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                isDeniedSms = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS ) == PackageManager.PERMISSION_DENIED;
                if(isDeniedSms) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS );
                }
                text_lat.setText( String.valueOf(isDeniedSms) );
                try {
                   SmsManager sms_man = SmsManager.getDefault();
                   sms_man.sendTextMessage("+601111196956",null, "hello from android", null, null );
                    //sms_man.sendTextMessage("+60126221766",null, "hello from android", null, null );
                   // Intent intentSms = new Intent(Intent.ACTION_VIEW);
                   // intentSms.putExtra("address", "+60126221766");
                   // intentSms.putExtra("sms_body", "something");
                   // intentSms.setType("vnd.android-dir/mms-sms");
                   // startActivity(intentSms);

               } catch (Exception e){
                   text_lat.setText("error");
               }

               //Intent intentCall = new Intent(Intent.SMS);
               //intentCall.setData(Uri.parse("tel:+60126221766"));
               //text_lat.setText( String.valueOf(isDeniedCall) );
               //startActivity(intentCall);

            }
        });

        btn_call.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                isDeniedCall = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE ) == PackageManager.PERMISSION_DENIED;
                if(isDeniedCall) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.CALL_PHONE}, MY_PERMISSION_REQUEST_SMS );
                }
                Intent intentCall = new Intent(Intent.ACTION_CALL);
                intentCall.setData(Uri.parse("tel:+60126221766"));
                text_lat.setText( String.valueOf(isDeniedCall) );
                startActivity(intentCall);

            }
        });
    }

    @Override
    public void onLocationChanged(Location loc) {
        text_lat.setText(String.valueOf("hit"));
        //text_lat.setText(String.valueOf(loc.getLatitude()));
        //text_lng.setText(String.valueOf(loc.getLongitude()));
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
