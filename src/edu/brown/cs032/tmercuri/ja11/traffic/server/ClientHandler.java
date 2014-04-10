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
    private final Socket client;
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
        this.client = client;
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
            output.println("END");
            output.flush();
        }
    }
    
    /**
     * Sends a message to the client that the server has quit.
     */
    public void sendQuit() {
        synchronized (output) {
            output.println("QUIT");
        }
    }
    
    private void sendPath(List<MapWay> path) {
        synchronized (output) {
            output.println("BEGIN PATH");
            for (MapWay mw : path) {
                output.println(mw.getID() + "\t" + mw.getStartID() + "\t" + mw.getStartLat() + "\t" + mw.getStartLng() + "\t" + mw.getEndID() + "\t" + mw.getEndLat() + "\t" + mw.getEndLng() + "\t" + mw.getName());
            }
            output.println("END");
            output.flush();
        }
    }
    
    private void sendBlock(List<MapWay> block) {
        synchronized (output) {
            output.println("BEGIN BLOCK");
            for (MapWay mw : block) {
                output.println(mw.getID() + "\t" + mw.getStartID() + "\t" + mw.getStartLat() + "\t" + mw.getStartLng() + "\t" + mw.getEndID() + "\t" + mw.getEndLat() + "\t" + mw.getEndLng() + "\t" + mw.getName());
            }
            output.println("END");
            output.flush();
        }
    }
    
    private void sendSuggestions(List<String> suggestions) {
        synchronized (output) {
            output.println("BEGIN SUGGESTIONS");
            for (String sug : suggestions) {
                output.println(sug);
            }
            output.println("END");
            output.flush();
        }
    }
    
    /**
	 * Close this socket and its related streams.
	 * @throws IOException Passed up from socket
	 */
	public void kill() throws IOException {
	    clients.remove(this);
		input.close();
		output.close();
		client.close();
	}
    
    private void parseLine(String msg) {
        switch (msg) {
            case "PATH":
                getPath();
                break;
            case "POINTPATH":
                getPointPath();
                break;
            case "BLOCK":
                getBlock();
                break;
            case "SUGGESTIONS":
                getSuggestions();
                break;
        }
    }
    
    private void getPath() {
        try {
            String line = input.readLine();
            String[] lineWords = line.split("\t");
            
            List<List<String>> path = map.getPath(lineWords[0], lineWords[1], lineWords[2], lineWords[3]);
            List<String> wayIDs = path.get(0);
            
            sendPath(map.wayIDsToMapWays(wayIDs));
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
    
    private void getPointPath() {
        try {
            String line = input.readLine();
            String[] lineWords = line.split("\t");
            
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
            String[] lineWords = line.split("\t");
            
            List<MapWay> ways = map.getAllBetween(Double.parseDouble(lineWords[0]), Double.parseDouble(lineWords[1]), Double.parseDouble(lineWords[2]), Double.parseDouble(lineWords[3]));
            
            sendBlock(ways);
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
    
    private void getSuggestions() {
        try {
            String line = input.readLine().trim();
            
            List<String> suggs = map.getSuggestions(line);
            
            sendSuggestions(suggs);
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
}
