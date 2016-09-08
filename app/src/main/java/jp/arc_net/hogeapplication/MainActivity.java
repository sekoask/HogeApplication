package jp.arc_net.hogeapplication;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {
    /**
     * DB
     */
    private SQLiteDatabase hogeDb;
    /**
     * LocationManager
     */
    private LocationManager locationManager;
    private int REQUEST_CODE_GPS_PERMISSION = 0x01;

    /**
     * 緯度（南北）
     */
    private TextView latView;
    private double latitude;

    /**
     * 経度（東西）
     */
    private TextView longView;
    private double longitude;
    /**
     * 高度
     */
    private TextView altView;
    private double altitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button nextBtn = (Button) findViewById(R.id.button);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                EditText editText = (EditText) findViewById(R.id.editText);
                if (editText.getText().length() != 0) {
                    intent.putExtra("INPUT_TEXT", editText.getText().toString());
                    ContentValues values = new ContentValues();
                    values.put("message", editText.getText().toString());
                    values.put("insert_tm", System.currentTimeMillis());
                    values.put("latitude", latitude);
                    values.put("longitude", longitude);
                    values.put("altitude", altitude);
                    hogeDb.insert("hoge_message", "null", values);
                } else {
                    intent.putExtra("INPUT_TEXT", "書き込みなし");
                }
                startActivity(intent);
            }
        });

        Button viewBtn = (Button) findViewById(R.id.viewButton);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("INPUT_TEXT", "表示のみ");
                startActivity(intent);
            }
        });

        HogeSqlLiteOpenHelper hogeDbHelper = new HogeSqlLiteOpenHelper(getApplicationContext());
        hogeDb = hogeDbHelper.getWritableDatabase();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        latView = (TextView) findViewById(R.id.latView);
        longView = (TextView) findViewById(R.id.longView);
        altView = (TextView) findViewById(R.id.altView);
    }

    @Override
    protected void onPause() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                super.onPause();
                return;
            }
            locationManager.removeUpdates(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (locationManager != null) {
            // 引数は通知までの最小時間間隔(ミリ秒)と最小距離間隔(メートル)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_GPS_PERMISSION);
                super.onResume();
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        StringBuilder latText = new StringBuilder();
        latText.append("緯度:");
        latText.append(latitude);
        latView.setText(latText.toString());
        longitude = location.getLongitude();
        StringBuilder longText = new StringBuilder();
        longText.append("経度:");
        longText.append(longitude);
        longView.setText(longText.toString());
        altitude = location.getAltitude();
        StringBuilder altText = new StringBuilder();
        altText.append("高度:");
        altText.append(altitude);
        altView.setText(altText.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
