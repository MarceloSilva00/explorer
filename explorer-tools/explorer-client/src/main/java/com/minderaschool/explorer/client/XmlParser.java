package com.minderaschool.explorer.client;

import java.io.ByteArrayInputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

class XmlParser {
    XmlParser() {
    }

    public void parse(byte[] buffer, int length, DefaultHandler defaultHandler) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        try {
            if (buffer[buffer.length - 1] == 0) {
                buffer[buffer.length - 1] = 32;
            }

            SAXParser saxParser = saxParserFactory.newSAXParser();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer, 0, length);
            saxParser.parse(inputStream, defaultHandler);
        } catch (SAXParseException e) {
            System.out.println("\n** Parsing error, line " + e.getLineNumber() + ", uri " + e.getSystemId());
            System.out.println("   " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }
}

