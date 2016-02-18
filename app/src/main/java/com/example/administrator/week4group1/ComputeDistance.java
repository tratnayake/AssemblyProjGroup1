package com.example.administrator.week4group1;

import java.util.Arrays;

/**
 * Created by A00802338 on 2/4/2016.
 */
public class ComputeDistance {

    public static final float R = 637100f;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {



        float[][] coord = new float[][] {{0f,1f},{1f,1f},{2f,1f},{3f,1f}};


        System.out.println(Arrays.deepToString(coord));


        float[][] output = new float[coord.length][coord.length];

        //For each element in the array, record the distances
        for (int i = 0; i < coord.length; i++) {


            float lat1 = coord[i][0];
            float lon1 = coord[i][1];

            float phi1 = (float) Math.toRadians((double)coord[i][0]);

            //The amount of points in this array
            for (int j = 0; j < coord.length; j++) {
                float lat2 = coord[j][0];

                float phi2 = (float) Math.toRadians((double)coord[j][0]);
                float lon2 = coord[j][1];

                float deltaLat = (float) Math.toRadians(lat2 - lat1);
                float deltaLon = (float) Math.toRadians(lon2 - lon1);



                float a = (float) (Math.sin((double) deltaLat / 2) * Math.sin((double) deltaLat / 2)
                        + Math.cos((double) phi1) * (double) Math.cos(phi2)
                        * Math.sin((double) deltaLon / 2) * Math.sin((double) deltaLon / 2));

                float c = (float)( 2 * Math.atan2(Math.sqrt((double)a), Math.sqrt((1- (double) a))));

                float d = R * c;

                output[i][j] = d;

            }


        }

        System.out.println(Arrays.deepToString(output));



    }

}
