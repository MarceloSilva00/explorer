package com.minderaschool.explorer.agent;

/**
 * example of a basic agent
 * implemented using the java interface library.
 */
public class App {

    public static void main(String[] args) {

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
        Brain client = new Brain(robName, pos, host);

        // main loop
        client.mainLoop();
    }

    static void print_usage() {
        System.out.println("Usage: java jClient [-robname <robname>] [-pos <pos>] [-host <hostname>[:<port>]]");
    }
};

