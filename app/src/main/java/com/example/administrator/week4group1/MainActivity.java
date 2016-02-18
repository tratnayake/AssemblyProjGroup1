package com.example.administrator.week4group1;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.Manifest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static {
        //System.loadLibrary("mysum");
        System.loadLibrary("nativeComputeDistances");
    }

    private native int mysum(int a, int b);

    private native float[] nativeComputeDistances(float[] coords);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        //Custom lab 1 <code></code>

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            TextView txtView = (TextView) findViewById(R.id.text_id);

            //Create an array list to hold all the points
            List<List<Float>> startingPoints = new ArrayList<>(1000);

            //For each of those elements, take in an arrayList.
            for(int i = 0; i < 1000; i++)  {
                startingPoints.add(new ArrayList<Float>());
            }

            for(int i=1; i<1000; i++){
                startingPoints.get(i).add((float) Math.random() * 1000);
                startingPoints.get(i).add((float) Math.random() * 1000);
            }

            System.out.println(startingPoints.toString());
            float[][] coord = new float[][] {{0f,1f},{1f,1f},{2f,1f},{3f,1f}};
            //float[] coord = new float[]{0f, 1f, 1f, 1f, 2f, 1f, 3f, 1f};

            Debug.startMethodTracing("ComputeDistance3");
            float[][] results = ComputeDistance(coord);
            //float[] results = nativeComputeDistances(coord);
            Debug.stopMethodTracing();
            txtView.setText("The results are " + Arrays.toString(results));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static float[][] ComputeDistance(float[][] coord) {

        final float R = 637100f;


        System.out.println(Arrays.deepToString(coord));


        float[][] output = new float[coord.length][coord.length];

        //For each element in the array, record the distances
        for (int i = 0; i < coord.length; i++) {


            float lat1 = coord[i][0];
            float lon1 = coord[i][1];

            float phi1 = (float) Math.toRadians((double) coord[i][0]);

            //The amount of points in this array
            for (int j = 0; j < coord.length; j++) {
                float lat2 = coord[j][0];

                float phi2 = (float) Math.toRadians((double) coord[j][0]);
                float lon2 = coord[j][1];

                float deltaLat = (float) Math.toRadians(lat2 - lat1);
                float deltaLon = (float) Math.toRadians(lon2 - lon1);


                float a = (float) (Math.sin((double) deltaLat / 2) * Math.sin((double) deltaLat / 2)
                        + Math.cos((double) phi1) * (double) Math.cos(phi2)
                        * Math.sin((double) deltaLon / 2) * Math.sin((double) deltaLon / 2));

                float c = (float) (2 * Math.atan2(Math.sqrt((double) a), Math.sqrt((1 - (double) a))));

                float d = R * c;

                output[i][j] = d;

            }


        }

        return output;


    }
}
