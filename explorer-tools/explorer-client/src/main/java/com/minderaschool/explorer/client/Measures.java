package com.minderaschool.explorer.client;

class Measures {
    int time;
    double compass;
    boolean compassReady;
    BeaconMeasure[] beacon;
    boolean[] beaconReady;
    boolean collision;
    boolean collisionReady;
    int ground;
    boolean groundReady;
    double[] IRSensor;
    boolean[] IRSensorReady;
    boolean endLed;
    boolean returningLed;
    boolean visitingLed;
    boolean startBut;
    boolean stopBut;
    GpsMeasure gpsData;
    boolean gpsReady;
    String[] hearMessage;

    Measures(int totalBeaconLeds) {
        compassReady = collisionReady = groundReady = gpsReady = false;
        IRSensor = new double[4];
        IRSensorReady = new boolean[4];

        for(int i = 0; i < 4; i++) {
            IRSensorReady[i] = false;
        }

        beacon = new BeaconMeasure[totalBeaconLeds];
        beaconReady = new boolean[totalBeaconLeds];

        for(int i = 0; i < totalBeaconLeds; i++) {
            beacon[i] = new BeaconMeasure();
            beaconReady[i] = false;
        }

        gpsData = new GpsMeasure();
        hearMessage = new String[5];
    }
}

