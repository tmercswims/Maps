/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.traffic.server;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;
import edu.brown.cs032.tmercuri.ja11.maps.gui.MapWay;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Thomas Mercurio
 */
public class ClientHandler implements Runnable {
    
    private final MapData map;
    private final ClientPool clients;
    private final BufferedReader input;
    private final PrintWriter output;

    /**
     * Constructs a ClientHandler on the given client with the given pool.
     * @param map
     * @param clients a group of other clients to chat with
	 * @param client the client to handle
	 * @throws IOException if the client socket is invalid
	 * @throws IllegalArgumentException if client is null
	 */
    public ClientHandler(MapData map, ClientPool clients, Socket client) throws IOException {
        if (clients == null || client == null) {
			throw new IllegalArgumentException("Cannot accept null arguments.");
		}
        
        this.map = map;
        this.clients = clients;
        this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.output = new PrintWriter(client.getOutputStream(), true);
    }
    
    /**
     * Runs this client, waiting for requests.
     */
    @Override
    public void run() {
        clients.add(this);
        try {
            while (true) {
                String msg = input.readLine();
                parseLine(msg);
            }
        } catch (IOException ex) {
            
        }
    }
    
    /**
     * Sends a line of traffic information to the client.
     * @param trafficInfo to send
     */
    public void sendTrafficInfo(String trafficInfo) {
        synchronized (output) {
            output.println("BEGIN TRAFFIC");
            output.println(trafficInfo);
            output.println("END TRAFFIC");
            output.flush();
        }
    }
    
    /**
     * Sends a bunch of MapWays (as text) that are a path.
     * @param path the list of MapWays that form a path
     */
    public void sendPath(List<MapWay> path) {
        synchronized (output) {
            output.println("BEGIN PATH");
            for (MapWay mw : path) {
                output.println(mw.getID() + " " + mw.getStartID() + " " + mw.getStartLat() + " " + mw.getStartLng() + " " + mw.getEndID() + " " + mw.getEndLat() + " " + mw.getEndLng() + " " + mw.getName());
            }
            output.println("END PATH");
            output.flush();
        }
    }
    
    /**
     * Sends a bunch of MapWays (as text) that are in a block.
     * @param path the list of MapWays in the block
     */
    public void sendBlock(List<MapWay> path) {
        synchronized (output) {
            output.println("BEGIN BLOCK");
            for (MapWay mw : path) {
                output.println(mw.getID() + " " + mw.getStartID() + " " + mw.getStartLat() + " " + mw.getStartLng() + " " + mw.getEndID() + " " + mw.getEndLat() + " " + mw.getEndLng() + " " + mw.getName());
            }
            output.println("END BLOCK");
            output.flush();
        }
    }
    
    private void parseLine(String msg) {
        switch (msg) {
            case "PATH":
                doPath();
                break;
            case "POINTPATH":
                doPointPath();
                break;
            case "BLOCK":
                getBlock();
                break;
        }
    }
    
    private void doPath() {
        try {
            String line = input.readLine();
            String[] lineWords = line.split("\" \"");
            
            List<List<String>> path = map.getPath(lineWords[0].replaceAll("\"", ""), lineWords[1].replaceAll("\"", ""), lineWords[2].replaceAll("\"", ""), lineWords[3].replaceAll("\"", ""));
            List<String> wayIDs = path.get(0);
            
            sendPath(map.wayIDsToMapWays(wayIDs));
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
    
    private void doPointPath() {
        try {
            String line = input.readLine();
            String[] lineWords = line.split(" ");
            
            List<List<String>> path = map.getPath(Double.parseDouble(lineWords[0]), Double.parseDouble(lineWords[1]), Double.parseDouble(lineWords[2]), Double.parseDouble(lineWords[3]));
            List<String> wayIDs = path.get(0);
            
            sendPath(map.wayIDsToMapWays(wayIDs));
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
    
    private void getBlock() {
        try {
            String line = input.readLine();
            String[] lineWords = line.split(" ");
            
            List<MapWay> ways = map.getAllBetween(Double.parseDouble(lineWords[0]), Double.parseDouble(lineWords[1]), Double.parseDouble(lineWords[2]), Double.parseDouble(lineWords[3]));
            
            sendBlock(ways);
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
}
