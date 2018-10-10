package com.minderaschool.explorer.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Robot {
    private DatagramSocket socket;
    private InetAddress address;
    private XmlParser parser;
    private int port;
    private Measures values;
    private Parameters params;

    public Robot() {
        try {
            this.socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.parser = new XmlParser();
    }

    public boolean initRobot(String robotName, int robotId, String address) {
        this.sendInitAndParseReply(address, "<Robot Id=\"" + robotId + "\" Name=\"" + robotName + "\"/>");
        return true;
    }

    public boolean initRobot(String robotName, int robotId, double[] angles, String address) {
        StringBuilder data = new StringBuilder("<Robot Id=\"" + robotId + "\" Name=\"" + robotName + "\" />");

        for(int i = 0; i < angles.length; ++i) {
            data.append("  <IRSensor Id=\"").append(i).append("\" Angle=\"").append(angles[i]).append("\" /> ");
        }

        data.append("</Robot>");
        this.sendInitAndParseReply(address, data.toString());
        return true;
    }

    public void readSensors() {
        byte[] buffer = new byte[4096];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            this.socket.receive(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SensorHandler sensorHandler = new SensorHandler(this.params.nBeacons);
        this.parser.parse(packet.getData(), packet.getLength() - 1, sensorHandler);
        this.values = sensorHandler.getValues();
    }

    public double getTime() {
        return (double)this.values.time;
    }

    public boolean isObstacleReady(int sensorIndex) {
        return this.values.IRSensorReady[sensorIndex];
    }

    public double getObstacleSensor(int sensorIndex) {
        return this.values.IRSensor[sensorIndex];
    }

    public boolean isBeaconReady(int beaconIndex) {
        return this.values.beaconReady[beaconIndex];
    }

    public BeaconMeasure getBeaconSensor(int beaconIndex) {
        return this.values.beacon[beaconIndex];
    }

    public boolean isCompassReady() {
        return this.values.compassReady;
    }

    public double getCompassSensor() {
        return this.values.compass;
    }

    public boolean isGroundReady() {
        return this.values.groundReady;
    }

    public int getGroundSensor() {
        return this.values.ground;
    }

    public boolean isBumperReady() {
        return this.values.collisionReady;
    }

    public boolean newMessageFrom(int robotIndex) {
        if (this.values.hearMessage[robotIndex - 1] == null) {
            return false;
        } else {
            return !this.values.hearMessage[robotIndex - 1].equals("");
        }
    }

    public boolean getBumperSensor() {
        return this.values.collision;
    }

    public String getMessageFrom(int robotIndex) {
        return this.values.hearMessage[robotIndex - 1];
    }

    public boolean isGPSReady() {
        return this.values.gpsReady;
    }

    public double getX() {
        return this.values.gpsData.x;
    }

    public double getY() {
        return this.values.gpsData.y;
    }

    public double getDir() {
        return this.values.gpsData.dir;
    }

    public void requestCompassSensor() {
        this.send("<Actions> <SensorRequests Compass=\"Yes\"/> </Actions>");
    }

    public void requestGroundSensor() {
        this.send("<Actions> <SensorRequests Ground=\"Yes\"/> </Actions>");
    }

    public void requestIRSensor(int sensorIndex) {
        this.send("<Actions> <SensorRequests IRSensor" + sensorIndex + "=\"Yes\"/> </Actions>");
    }

    public void requestBeaconSensor(int beaconSensorIndex) {
        this.send("<Actions> <SensorRequests Beacon" + beaconSensorIndex + "=\"Yes\"/> </Actions>");
    }

    public void requestSensors(String[] sensors) {
        StringBuilder data = new StringBuilder("<Actions> <SensorRequests ");

        for (String sensor : sensors) {
            data.append(sensor).append("=\"Yes\" ");
        }

        data.append("/> </Actions>");
        this.send(data.toString());
    }

    public boolean getStartButton() {
        return this.values.startBut;
    }

    public boolean getStopButton() {
        return this.values.stopBut;
    }

    public boolean getVisitingLed() {
        return this.values.visitingLed;
    }

    public boolean getReturningLed() {
        return this.values.returningLed;
    }

    public boolean getFinished() {
        return this.values.endLed;
    }

    public void driveMotors(double leftMotor, double rightMotor) {
        this.send("<Actions LeftMotor=\"" + leftMotor + "\" RightMotor=\"" + rightMotor + "\"/>");
    }

    public void say(String message) {
        this.send("<Actions><say><![CDATA[" + message + "]]></say></Actions>");
    }

    public void setReturningLed(boolean returningLed) {
        this.send("<Actions LeftMotor=\"0\" RightMotor=\"0\" ReturningLed=\"" + (returningLed ? "On" : "Off") + "\"/>");
    }

    public void setVisitingLed(boolean visitingLed) {
        this.send("<Actions LeftMotor=\"0\" RightMotor=\"0\" VisitingLed=\"" + (visitingLed ? "On" : "Off") + "\"/>");
    }

    public void finish() {
        this.send("<Actions LeftMotor=\"0\" RightMotor=\"0\" EndLed=\"On\"/>");
    }

    public int getCycleTime() {
        return this.params.cycleTime;
    }

    public int getFinalTime() {
        return this.params.simTime;
    }

    public int getKeyTime() {
        return this.params.keyTime;
    }

    public double getNoiseObstacleSensor() {
        return this.params.obstacleNoise;
    }

    public double getNoiseBeaconSensor() {
        return this.params.beaconNoise;
    }

    public double getNoiseCompassSensor() {
        return this.params.compassNoise;
    }

    public double getNoiseMotors() {
        return this.params.motorsNoise;
    }

    public int getNumberOfBeacons() {
        return this.params.nBeacons;
    }

    private void sendInitAndParseReply(String address, String data) {
        this.port = 6000;
        byte[] buffer = data.getBytes();
        int index = address.indexOf(":");
        if (index != -1) {
            this.port = Integer.valueOf(address.substring(index + 1));
            address = address.substring(0, index);
        }

        try {
            this.address = InetAddress.getByName(address);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.address, this.port);

        try {
            this.socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);

        try {
            this.socket.receive(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ReplyHandler replyHandler = new ReplyHandler();
        this.parser.parse(packet.getData(), packet.getLength() - 1, replyHandler);
        this.params = replyHandler.getParams();
        this.port = packet.getPort();
    }

    private void send(String data) {
        byte[] buffer = data.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.address, this.port);

        try {
            this.socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

