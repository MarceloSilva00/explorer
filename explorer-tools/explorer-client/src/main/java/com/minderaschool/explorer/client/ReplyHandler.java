package com.minderaschool.explorer.client;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

class ReplyHandler extends DefaultHandler {
    private Parameters params;

    ReplyHandler() {
    }

    Parameters getParams() {
        return this.params;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (this.params == null) {
            this.params = new Parameters();
        }

        String attributeValue;
        if (qName.equals("Reply") && attributes != null) {
            attributeValue = attributes.getValue("Status");
            if (attributeValue != null) {
                if (!attributeValue.equals("Ok")) {
                    System.out.println("Status: Refused");
                    System.exit(1);
                } else {
                    System.out.println("Status: Ok");
                }
            }
        }

        if (qName.equals("Parameters") && attributes != null) {
            attributeValue = attributes.getValue("SimTime");
            if (attributeValue != null) {
                this.params.simTime = Integer.valueOf(attributeValue);
            }

            attributeValue = attributes.getValue("CycleTime");
            if (attributeValue != null) {
                this.params.cycleTime = Integer.valueOf(attributeValue);
            }

            attributeValue = attributes.getValue("CompassNoise");
            if (attributeValue != null) {
                this.params.compassNoise = Double.valueOf(attributeValue);
            }

            attributeValue = attributes.getValue("BeaconNoise");
            if (attributeValue != null) {
                this.params.beaconNoise = Double.valueOf(attributeValue);
            }

            attributeValue = attributes.getValue("ObstacleNoise");
            if (attributeValue != null) {
                this.params.obstacleNoise = Double.valueOf(attributeValue);
            }

            attributeValue = attributes.getValue("MotorsNoise");
            if (attributeValue != null) {
                this.params.motorsNoise = Double.valueOf(attributeValue);
            }

            attributeValue = attributes.getValue("KeyTime");
            if (attributeValue != null) {
                this.params.keyTime = Integer.valueOf(attributeValue);
            }

            attributeValue = attributes.getValue("NBeacons");
            if (attributeValue != null) {
                this.params.nBeacons = Integer.valueOf(attributeValue);
            }
        }

    }

    public void endElement(String uri, String localName, String qName) {
    }
}

