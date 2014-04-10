/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.traffic.server;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Thomas Mercurio
 */
public class Traffic {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length != 6) {
                throw new IllegalArgumentException("Usage: trafficServer <ways> <nodes> <index> <hostname> <traffic port> <server port>");
            }
            
            TrafficServer trafficServer = new TrafficServer(args[0], args[1], args[2], Integer.parseInt(args[5]), args[3], Integer.parseInt(args[4]));
            
            trafficServer.start();
            
            Scanner scanner = new Scanner(System.in);
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.length() == 0 || line.equalsIgnoreCase("exit")) {
                    trafficServer.kill();
                    System.exit(0);
                }
            }
        } catch (IllegalArgumentException | IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
}
