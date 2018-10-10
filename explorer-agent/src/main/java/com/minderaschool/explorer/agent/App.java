package com.minderaschool.explorer.agent;

import com.minderaschool.explorer.client.BeaconMeasure;
import com.minderaschool.explorer.client.Robot;

import javax.swing.*;

/**
 * example of a basic agent
 * implemented using the java interface library.
 */
public class App {

    private Robot robot;

    private String robName;
    private double irSensor0, irSensor1, irSensor2, compass;
    private BeaconMeasure beacon;
    private int ground;
    private boolean collision;

    private State state;

    private int beaconToFollow;

    private enum State {RUN, WAIT, RETURN}

    public static void main(String[] args) {
        for(String arg : args ) {
            System.out.println(arg);
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);

        System.out.println("HELLO!!!");
        String host, robName;
        int pos;
        int arg;

        //default values
        host = "localhost";
        robName = "minder-explorer-agent-1";
        pos = 1;


        // parse command-line arguments
        try {
            arg = 0;
            while (arg < args.length) {
                if (args[arg].equals("-pos")) {
                    if (args.length > arg + 1) {
                        pos = Integer.valueOf(args[arg + 1]).intValue();
                        arg += 2;
                    }
                } else if (args[arg].equals("-robname")) {
                    if (args.length > arg + 1) {
                        robName = args[arg + 1];
                        arg += 2;
                    }
                } else if (args[arg].equals("-host")) {
                    if (args.length > arg + 1) {
                        host = args[arg + 1];
                        arg += 2;
                    }
                } else throw new Exception();
            }
        } catch (Exception e) {
            print_usage();
            return;
        }

        // create client
        App client = new App();

        client.robName = robName;

        // register robot in simulator
        client.robot.initRobot(robName, pos, host);

        // main loop
        client.mainLoop();

    }

    // Constructor
    private App() {
        robot = new Robot();
        beacon = new BeaconMeasure();

        beaconToFollow = 0;
        ground = -1;

        state = State.RUN;
    }

    /**
     * reads a new message, decides what to do and sends action to simulator
     */
    public void mainLoop() {

        while (true) {
            robot.readSensors();
            decide();
        }
    }

    public void wander(boolean followBeacon) {
        System.out.println("Wander!!");
        /*if (irSensor0 > 4.0 || irSensor1 > 4.0 || irSensor2 > 4.0)
            robot.driveMotors(-0.1, +0.1);
        else if (irSensor1 > 0.7) robot.driveMotors(0.15, 0.0);
        else if (irSensor2 > 0.7) robot.driveMotors(0.0, 0.15);
        else if (followBeacon && beacon.beaconVisible && beacon.beaconDir > 20.0)
            robot.driveMotors(0.0, 0.1);
        else if (followBeacon && beacon.beaconVisible && beacon.beaconDir < -20.0)
            robot.driveMotors(0.1, 0.0);
        else robot.driveMotors(0.15, 0.15);*/
        robot.driveMotors(0.15, 0.15);

    }

    /**
     * basic reactive decision algorithm, decides action based on current sensor values
     */
    public void decide() {
        if (robot.isObstacleReady(0))
            irSensor0 = robot.getObstacleSensor(0);
        if (robot.isObstacleReady(1))
            irSensor1 = robot.getObstacleSensor(1);
        if (robot.isObstacleReady(2))
            irSensor2 = robot.getObstacleSensor(2);

        if (robot.isCompassReady())
            compass = robot.getCompassSensor();
        if (robot.isGroundReady())
            ground = robot.getGroundSensor();

        if (robot.isBeaconReady(beaconToFollow))
            beacon = robot.getBeaconSensor(beaconToFollow);

        //System.out.println("Measures: ir0=" + irSensor0 + " ir1=" + irSensor1 + " ir2=" + irSensor2 + "\n");

        //System.out.println(robName + " state " + state);

        switch (state) {
            case RUN:    /* Go */
                if (robot.getVisitingLed()) state = State.WAIT;
                if (ground == 0) {         /* Visit Target */
                    robot.setVisitingLed(true);
                    System.out.println(robName + " visited target at " + robot.getTime() + "\n");
                } else {
                    wander(false);
                }
                break;
            case WAIT: /* set returning led and check that it is on */
                robot.setReturningLed(true);
                if (robot.getVisitingLed()) robot.setVisitingLed(false);
                if (robot.getReturningLed()) state = State.RETURN;

                robot.driveMotors(0.0, 0.0);
                break;
            case RETURN: /* Return to home area */
                robot.setVisitingLed(false);
                robot.setReturningLed(false);
                wander(false);
                break;

        }

        //for(int i=1; i<6; i++)
        //  if(robot.NewMessageFrom(i))
        //      System.out.println("Message: From " + i + " to " + robName + " : \"" + robot.GetMessageFrom(i)+ "\"");

        //robot.Say(robName);

        if (robot.getTime() % 2 == 0) {
            robot.requestIRSensor(0);
            robot.requestIRSensor(1);
            robot.requestIRSensor(2);
            robot.requestGroundSensor();
        } else {
            String[] sensList = {"IRSensor1", "IRSensor2", "Compass", "GPS"};
            robot.requestSensors(sensList);
        }
    }

    static void print_usage() {
        System.out.println("Usage: java jClient [-robname <robname>] [-pos <pos>] [-host <hostname>[:<port>]]");
    }
};

