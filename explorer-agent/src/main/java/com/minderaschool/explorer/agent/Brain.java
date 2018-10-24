package com.minderaschool.explorer.agent;

import com.minderaschool.explorer.client.Robot;

public class Brain {
    private Robot robot;
    private String robName;
    private double irSensor0, irSensor1, irSensor2, compass;

    enum State {WAIT, SCAN, TURN_RIGHT}

    enum Direction {RIGHT, BOTTOM, LEFT, TOP}

    private Square map[][] = new Square[22][42]; //Y X
    private State currentState;
    private Direction currentDirection;
    private int ground;
    private double startTurnTime;
    private final double TIME_TO_ROTATE = 38.0;
    private int X = 21, Y = 11;

    public Brain(String robName, int pos, String host) {
        robot = new Robot();
        ground = -1;
        currentDirection = getCurrentDirection();
        System.out.println(currentDirection);
        currentState = State.SCAN;
        this.robName = robName;
        robot.initRobot(robName, pos, host); // register robot in simulator
    }


    public Direction getCurrentDirection() {
        if (compass < 40 && compass > -40) {// RIGHT
            return Direction.RIGHT;
        } else if (compass < 130 && compass > 70) {//TOP
            return Direction.TOP;
        } else if (compass < -160 && compass > 160) {//LEFT
            return Direction.LEFT;
        } else {//BOTTOM
            return Direction.BOTTOM;
        }
    }

    public void mainLoop() {
        robot.readSensors();
        double time = robot.getTime();
        System.out.println(time + ": Waiting to start");
        while (time <= 0.0001 && time >= -0.0001) {
            // do nothing, just wait until it starts
            robot.readSensors();
            time = robot.getTime();
            System.out.print(".");
            System.out.flush();
        }
        System.out.println(time + ": Started");
        while (true) {
            robot.readSensors();
            decide();
        }
    }


    private void getSensors() {
        if (robot.isObstacleReady(0))
            irSensor0 = robot.getObstacleSensor(0);
        if (robot.isObstacleReady(1))
            irSensor1 = robot.getObstacleSensor(1);
        if (robot.isObstacleReady(2))
            irSensor2 = robot.getObstacleSensor(2);
        if (robot.isCompassReady())
            compass = robot.getCompassSensor();
        // if (robot.isBumperReady())
        //    bumper = robot.getBumperSensor();
        if (robot.isGroundReady())
            ground = robot.getGroundSensor();
        // if (robot.isBeaconReady(beaconToFollow))
        //    beacon = robot.getBeaconSensor(beaconToFollow);
    }

    private void requestSensors() {
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

    public void decide() {
        getSensors();

        double currentTime = robot.getTime();
        currentDirection = getCurrentDirection();
        System.out.println(currentTime + ": " + currentState + " DIR: "+ currentDirection);

        switch (currentState) {
            case SCAN:
                System.out.println();
                scan();
                System.out.println();
                break;
            case TURN_RIGHT:
                //System.out.println("robot.driveMotors(0.02, -0.02);");
                robot.driveMotors(0.02, -0.02);

                if (startTurnTime < 0) {
                    startTurnTime = currentTime;
                }

                System.out.println(currentTime - startTurnTime + " > " + TIME_TO_ROTATE);
                if (currentTime - startTurnTime > TIME_TO_ROTATE) {
                    //System.out.println("Changing state to WAIT");
                    // stop rotating

                    currentState = State.SCAN;
                    startTurnTime = -1.0;
                }
                break;
        }
        //System.out.println(currentSquare.toString());
        //System.out.println("RIGHT= " + currentSquare.isRight() + " TOP= " + currentSquare.isTop() + " LEFT= " + currentSquare.isLeft() + " BOTTOM= "+ currentSquare.isBottom());
        if(map[11][21].getCount()==4){
            robot.finish();

        }
        requestSensors();
    }


    public void scan() {
        if (compass < 30 && compass > -30) {// RIGHT
            if (irSensor0 > 1.5) {
                map[Y][X].setRight(true);
                map[Y][X].incCount();
                //System.out.println("RIGHT= TRUE");
            }
        } else if (compass < 120 && compass > 60) {//TOP
            if (irSensor0 > 1.5) {
                map[Y][X].setTop(true);
                map[Y][X].incCount();
                //System.out.println("TOP= TRUE");
            }
        } else if (compass < -150 && compass > 150) {//LEFT
            if (irSensor0 > 1.5) {
                map[Y][X].setLeft(true);
                map[Y][X].incCount();
                //System.out.println("LEFT= TRUE");
            }
        } else if (compass > -120 && compass < -60) {//BOTTOM
            if (irSensor0 > 1.5) {
                map[Y][X].setBottom(true);
                map[Y][X].incCount();
                //System.out.println("BOTTOM= TRUE");
            }
        }
        System.out.println(map[Y][X].getCount());
        currentState = State.TURN_RIGHT;
    }

}