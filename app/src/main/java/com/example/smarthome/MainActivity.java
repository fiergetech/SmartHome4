package com.example.smarthome;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements AntaresHTTPAPI.OnResponseListener {

    private  Button refreshpintu;
    private  Button refreshlampu;
    private  Button refreshpompa;

    private Button on;
    private Button off;

    private Button on1;
    private Button off1;

    private Button on2;
    private Button off2;

    private TextView txtData;

    private String TAG = "ANTARES-API1";
    private String TAG1 = "ANTARES-API2";
    private String TAG2 = "ANTARES-API3";
    private AntaresHTTPAPI antaresAPIHTTP;
    private String dataDevice;

    private AntaresHTTPAPI antaresAPIHTTP1;
    private String dataDevice1;

    private AntaresHTTPAPI antaresAPIHTTP2;
    private String dataDevice2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Inisialisasi UI yang digunakan di aplikasi --- //
        on = (Button) findViewById(R.id.on);
        off = (Button) findViewById(R.id.off);
        on1 = (Button) findViewById(R.id.on1);
        off1 = (Button) findViewById(R.id.off1);
        on2 = (Button) findViewById(R.id.on2);
        off2 = (Button) findViewById(R.id.off2);
        refreshpintu = (Button) findViewById(R.id.refreshpintu);
        refreshlampu = (Button) findViewById(R.id.refreshlampu);
        refreshpompa = (Button) findViewById(R.id.refreshpompa);
        txtData = (TextView) findViewById(R.id.txtData);

        // --- Inisialisasi API Antares --- //
        //antaresAPIHTTP = AntaresHTTPAPI.getInstance();
        antaresAPIHTTP = new AntaresHTTPAPI();
        antaresAPIHTTP.addListener(this);

        antaresAPIHTTP1 = new AntaresHTTPAPI();
        antaresAPIHTTP1.addListener(this);

        antaresAPIHTTP2 = new AntaresHTTPAPI();
        antaresAPIHTTP2.addListener(this);

        refreshpintu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.getLatestDataofDevice("e542272e6af150eb:7022af482cfba0e5", "KontrollerLampu", "PintuUtama");
            }
        });

        refreshlampu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP1.getLatestDataofDevice("e542272e6af150eb:7022af482cfba0e5", "KontrollerLampu", "LampuTeras");
            }
        });

        refreshpompa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP2.getLatestDataofDevice("e542272e6af150eb:7022af482cfba0e5", "KontrollerLampu", "PompaAir");
            }
        });


        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("STATUS PINTU");

                myRef.setValue(1);
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("STATUS PINTU");

                myRef.setValue(0);
            }
        });

        on1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("STATUS LAMPU TERAS");

                myRef.setValue(1);
            }
        });

        off1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("STATUS LAMPU TERAS");

                myRef.setValue(0);
            }
        });

        on2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("STATUS LAMPU ATAS");

                myRef.setValue(1);
            }
        });

        off2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("STATUS LAMPU ATAS");

                myRef.setValue(0);
            }
        });

    }


    @Override
    public void onResponse(AntaresResponse antaresResponse) {
        // --- Cetak hasil yang didapat dari ANTARES ke System Log --- //
        //Log.d(TAG,antaresResponse.toString());
        Log.d(TAG, Integer.toString(antaresResponse.getRequestCode()));
        if (antaresResponse.getRequestCode() == 0) {
            try {
                JSONObject body = new JSONObject(antaresResponse.getBody());
                dataDevice = body.getJSONObject("m2m:cin").getString("con");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtData.setText(dataDevice);
                    }
                });
                Log.d(TAG, dataDevice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG1, Integer.toString(antaresResponse.getRequestCode()));
        if (antaresResponse.getRequestCode() == 0) {
            try {
                JSONObject body = new JSONObject(antaresResponse.getBody());
                dataDevice1 = body.getJSONObject("m2m:cin").getString("con");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtData.setText(dataDevice1);
                    }
                });
                Log.d(TAG1, dataDevice1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG2, Integer.toString(antaresResponse.getRequestCode()));
        if (antaresResponse.getRequestCode() == 0) {
            try {
                JSONObject body = new JSONObject(antaresResponse.getBody());
                dataDevice2 = body.getJSONObject("m2m:cin").getString("con");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtData.setText(dataDevice2);
                    }
                });
                Log.d(TAG2, dataDevice2);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}