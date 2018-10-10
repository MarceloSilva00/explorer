package com.minderaschool.explorer.client;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

class SensorHandler extends DefaultHandler {
    private Measures values;
    private int nBeacons;
    private String activeTag;
    private int hearFrom;

    public SensorHandler(int nBeacons) {
        this.nBeacons = nBeacons;
        this.activeTag = "";
        this.hearFrom = 0;
    }

    Measures getValues() {
        return this.values;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        this.activeTag = qName;
        if (this.values == null) {
            this.values = new Measures(this.nBeacons);
        }

        String attributeValue1;
        if (qName.equals("Measures")) {
            if (attributes != null) {
                attributeValue1 = attributes.getValue("Time");
                if (attributeValue1 != null) {
                    this.values.time = Integer.valueOf(attributeValue1);
                }
            }
        } else {
            String attributeValue2;
            String attributeValue3;
            if (qName.equals("Sensors")) {
                if (attributes != null) {
                    attributeValue1 = attributes.getValue("Compass");
                    if (attributeValue1 != null) {
                        this.values.compassReady = true;
                        this.values.compass = Double.valueOf(attributeValue1);
                    }

                    attributeValue2 = attributes.getValue("Ground");
                    if (attributeValue2 != null) {
                        this.values.groundReady = true;
                        this.values.ground = Integer.valueOf(attributeValue2);
                    }

                    attributeValue3 = attributes.getValue("Collision");
                    if (attributeValue3 != null) {
                        this.values.collisionReady = true;
                        this.values.collision = attributeValue3.equals("Yes");
                    }
                }
            } else {
                int sensorId;
                switch (qName) {
                    case "IRSensor":
                        if (attributes != null) {
                            attributeValue1 = attributes.getValue("Id");
                            if (attributeValue1 != null) {
                                sensorId = Integer.valueOf(attributeValue1);
                                attributeValue3 = attributes.getValue("Value");
                                if (attributeValue3 != null) {
                                    this.values.IRSensorReady[sensorId] = true;
                                    this.values.IRSensor[sensorId] = Double.valueOf(attributeValue3);
                                }
                            }
                        }
                        break;
                    case "BeaconSensor":
                        if (attributes != null) {
                            attributeValue1 = attributes.getValue("Id");
                            if (attributeValue1 != null) {
                                sensorId = Integer.valueOf(attributeValue1);
                                attributeValue3 = attributes.getValue("Value");
                                if (attributeValue3 != null) {
                                    this.values.beaconReady[sensorId] = true;
                                    if (!attributeValue3.equals("NotVisible")) {
                                        this.values.beacon[sensorId].beaconDir = Double.valueOf(attributeValue3);
                                        this.values.beacon[sensorId].beaconVisible = true;
                                    } else {
                                        this.values.beacon[sensorId].beaconVisible = false;
                                    }
                                } else {
                                    this.values.beaconReady[sensorId] = false;
                                    this.values.beacon[sensorId].beaconVisible = false;
                                    this.values.beacon[sensorId].beaconDir = 0.0D;
                                }
                            }
                        }
                        break;
                    case "GPS":
                        if (attributes != null) {
                            attributeValue1 = attributes.getValue("X");
                            if (attributeValue1 != null) {
                                this.values.gpsData.x = Double.valueOf(attributeValue1);
                                this.values.gpsReady = true;
                            }

                            attributeValue2 = attributes.getValue("Y");
                            if (attributeValue2 != null) {
                                this.values.gpsData.y = Double.valueOf(attributeValue2);
                            }

                            attributeValue3 = attributes.getValue("Dir");
                            if (attributeValue3 != null) {
                                this.values.gpsData.dir = Double.valueOf(attributeValue3);
                            }
                        }
                        break;
                    case "Leds":
                        if (attributes != null) {
                            attributeValue1 = attributes.getValue("EndLed");
                            if (attributeValue1 != null) {
                                this.values.endLed = attributeValue1.equals("On");
                            }

                            attributeValue2 = attributes.getValue("ReturningLed");
                            if (attributeValue2 != null) {
                                this.values.returningLed = attributeValue2.equals("On");
                            }

                            attributeValue3 = attributes.getValue("VisitingLed");
                            if (attributeValue3 != null) {
                                this.values.visitingLed = attributeValue3.equals("On");
                            }
                        }
                        break;
                    case "Buttons":
                        if (attributes != null) {
                            attributeValue1 = attributes.getValue("Start");
                            if (attributeValue1 != null) {
                                this.values.startBut = attributeValue1.equals("On");
                            }

                            attributeValue2 = attributes.getValue("Stop");
                            if (attributeValue2 != null) {
                                this.values.stopBut = attributeValue2.equals("On");
                            }
                        }
                        break;
                    case "Message":
                        attributeValue1 = attributes.getValue("From");
                        if (attributeValue1 != null) {
                            this.hearFrom = Integer.valueOf(attributeValue1);
                        }
                        break;
                }
            }
        }

    }

    public void endElement(String uri, String localName, String qName) {
        this.activeTag = "";
    }

    public void characters(char[] chars, int start, int length) {
        if (this.activeTag.equals("Message")) {
            if (this.values.hearMessage[this.hearFrom - 1] == null) {
                this.values.hearMessage[this.hearFrom - 1] = new String(chars, start, length);
            } else {
                this.values.hearMessage[this.hearFrom - 1] = this.values.hearMessage[this.hearFrom - 1] + new String(chars, start, length);
            }
        }

    }
}

