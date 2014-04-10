/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.gui;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Mercurio
 */
public class MapNetwork {
    
    private final MapData map;
    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    
    public MapNetwork(MapData map, String hostname, int port) throws IOException {
        this.map = map;
        this.socket = new Socket(hostname, port);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }
    
    /**
     * Gives the path between two intersections formed by s1+cs1 and s2+cs2
     * @param s1 street 1
     * @param cs1 cross-street 1
     * @param s2 street 2
     * @param cs2 cross-street 2
     * @return the path
     * @throws IOException 
     */
    public List<MapWay> getPath(String s1, String cs1, String s2, String cs2) throws IOException {
        List<MapWay> path = new ArrayList<>();
        synchronized (output) {
            output.println("PATH");
            output.println(s1 + "\t" + cs1 + "\t" + s2 + "\t" + cs2);
        }
        synchronized (input) {
            if (input.readLine().equals("BEGIN PATH")) {
             String line = input.readLine();
                while (!line.equals("END")) {
                    String[] lineWords = line.split("\t");
                    path.add(new MapWay(lineWords[0], lineWords[1], Double.parseDouble(lineWords[2]), Double.parseDouble(lineWords[3]), lineWords[4], Double.parseDouble(lineWords[5]), Double.parseDouble(lineWords[6]), lineWords[7]));
                    line = input.readLine();
                }
            }
        }
        return path;
    }
    
    /**
     * Gets the path between the points closest to (lat1, lng1) and (lat2, lng2).
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return the path
     * @throws IOException 
     */
    public List<MapWay> getPath(double lat1, double lng1, double lat2, double lng2) throws IOException {
        List<MapWay> path = new ArrayList<>();
        synchronized (output) {
            output.println("POINTPATH");
            output.println(lat1 + "\t" + lng1 + "\t" + lat2 + "\t" + lng2);
        }
        synchronized (input) {
            if (input.readLine().equals("BEGIN PATH")) {
             String line = input.readLine();
                while (!line.equals("END")) {
                    String[] lineWords = line.split("\t");
                    path.add(new MapWay(lineWords[0], lineWords[1], Double.parseDouble(lineWords[2]), Double.parseDouble(lineWords[3]), lineWords[4], Double.parseDouble(lineWords[5]), Double.parseDouble(lineWords[6]), lineWords[7]));
                    line = input.readLine();
                }
            }
        }
        return path;
    }
    
    /**
     * Gets every way that has at least one end inside the box whose top left corner is (topLat, topLng) and bottom right corner is (botLat, botLng).
     * @param topLat
     * @param topLng
     * @param botLat
     * @param botLng
     * @return
     * @throws IOException 
     */
    public List<MapWay> getAllBetween(double topLat, double topLng, double botLat, double botLng) throws IOException {
        List<MapWay> block = new ArrayList<>();
        synchronized (output) {
            output.println("BLOCK");
            output.println(topLat + "\t" + topLng + "\t" + botLat + "\t" + botLng);
        }
        synchronized (input) {
            if (input.readLine().equals("BEGIN BLOCK")) {
             String line = input.readLine();
                while (!line.equals("END")) {
                    String[] lineWords = line.split("\t");
                    block.add(new MapWay(lineWords[0], lineWords[1], Double.parseDouble(lineWords[2]), Double.parseDouble(lineWords[3]), lineWords[4], Double.parseDouble(lineWords[5]), Double.parseDouble(lineWords[6]), lineWords[7]));
                    line = input.readLine();
                }
            }
        }
        return block;
    }
    
    public List<String> getSuggestions(String word) throws IOException {
        List<String> suggestions = new ArrayList<>();
        synchronized (output) {
            output.println("SUGGESTIONS");
            output.println(word);
        }
        synchronized (input) {
            if (input.readLine().equals("BEGIN SUGGESTIONS")) {
             String line = input.readLine();
                while (!line.equals("END")) {
                    suggestions.add(line);
                    line = input.readLine();
                }
            }
        }
        return suggestions;
    }
}
