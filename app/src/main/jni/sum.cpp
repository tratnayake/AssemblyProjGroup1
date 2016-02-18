#include <jni.h>
#include <stdlib.h>
#include <math.h>
#include <android/log.h>

#define EARTH_RADIUS 6371000

#define LAT 0
#define LON 1

float *getDistances(float *, int);
float getSingleDistance(float *, float*);
float toRadians(float);

extern "C"
JNIEXPORT jint JNICALL Java_com_example_administrator_week4group1_MainActivity_mysum(JNIEnv *env,
jobject thisObj, jint a, jint b){
    return a + b;
}
/*
extern "C"
JNIEXPORT jfloatArray JNICALL Java_com_example_administrator_week4group1_MainActivity_nativeComputeDistances(
        JNIEnv *env, jobject thisObj, jobjectArray originalPoints) {

    // get the number of points in the array
    int numPoints = env->GetArrayLength(originalPoints);

    // allocate space for the points
    float **points = (float**)malloc(sizeof(float*) * numPoints);

    // for each float array object
    for(int i = 0; i < numPoints; i++){

        // get the single float array
        jfloatArray onePoint = (jfloatArray) env->GetObjectArrayElement(originalPoints, 0);

        // get the floats from the array
        points[i] = env->GetFloatArrayElements(onePoint, 0);
    }

    float **distances = getDistances(points, numPoints);

    jfloatArray jdistances = env->NewFloatArray(numPoints * numPoints);
    for(int i = 0; i < numPoints; i++){
        env->SetFloatArrayRegion(jdistances, numPoints * i, numPoints * (i+1), distances[i]);
    }

    return jdistances;

}
*/
extern "C"
JNIEXPORT jfloatArray JNICALL Java_com_example_administrator_week4group1_MainActivity_nativeComputeDistances(
        JNIEnv *env, jobject thisObj, jfloatArray originalPoints) {

    __android_log_print(ANDROID_LOG_ERROR, "log", "Entered nativeComputeDistances");

    // get number of points
    int length = env->GetArrayLength(originalPoints);
    int numPoints = length / 2;

    __android_log_print(ANDROID_LOG_ERROR, "log", "Got array of %d points", numPoints);

    float *points = env->GetFloatArrayElements(originalPoints, 0);

    __android_log_print(ANDROID_LOG_ERROR, "log", "Got points array");

    float *distances = getDistances(points, numPoints);

    jfloatArray jdistances = env->NewFloatArray(numPoints * numPoints);
    env->SetFloatArrayRegion(jdistances, 0, numPoints * numPoints, distances);

    return jdistances;

}

float *getDistances(float *points, int numPoints){

    float *distances = (float*)malloc(sizeof(float) * numPoints * numPoints);

    for(int i = 0; i < numPoints; i++){
        for(int j = i + 1; j < numPoints; j++) {
            __android_log_print(ANDROID_LOG_ERROR, "log", "Comparing points %d and %d", i, j);
            distances[j + (i * numPoints)] = distances[i + (j * numPoints)] =
                    getSingleDistance(&points[i * 2], &points[j * 2]);
        }
    }

    return distances;
}
/*
float **getDistances(float **points, int numPoints) {

    int i, j = 0;

    float **distances = (float **)malloc(sizeof(float*) * numPoints);

    for(i = 0; i < numPoints; i++){
        distances[i] = (float *)malloc(sizeof(float) * numPoints);

        for(j = 0; j < numPoints; j++){
            distances[i][j] = 0;
        }
    }

    for(i=0; i < numPoints; i++){
        for(j = i + 1; j < numPoints; j++){
            distances[i][j] = distances[j][i] = getSingleDistance(points[i], points[j]);
        }
    }

    return distances;
}
*/
float getSingleDistance(float *a, float *b){
    float latA = toRadians(a[LAT]);
    float latB = toRadians(b[LAT]);

    float deltaLat = toRadians(b[LON] - a[LAT]);
    float deltaLon = toRadians(b[LON] - a[LON]);

    float fA = powf(sinf(deltaLat / 2), 2) +
               cosf(latA) *
               cosf(latB) *
               powf(sinf(deltaLon / 2), 2);

    float fC = 2 * atan2f(sqrtf(fA), sqrtf(1 - fA));

    return EARTH_RADIUS * fC;

}

inline float toRadians(float degrees) {
    return (degrees * M_PI / 180);
}