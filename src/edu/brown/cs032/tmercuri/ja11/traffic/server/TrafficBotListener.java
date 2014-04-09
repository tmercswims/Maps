/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.traffic.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Thomas Mercurio
 */
public class TrafficBotListener implements Runnable {
    
    private final ClientPool clients;
    private BufferedReader input;
    
    public TrafficBotListener(ClientPool clients, String hostname, int port) {
        this.clients = clients;
        Socket bot;
        try {
            bot = new Socket(hostname, port);
            input = new BufferedReader(new InputStreamReader(bot.getInputStream()));
        } catch (UnknownHostException ex) {
            System.err.println("ERROR: Unknown host: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String trafficInfo = input.readLine();
                clients.broadcastTrafficInfo(trafficInfo);
            } catch (IOException ex) {
                System.err.println("ERROR: " + ex.getMessage());
            }
        }
    }
    
}
