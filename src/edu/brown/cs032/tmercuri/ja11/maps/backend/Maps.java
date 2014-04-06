/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.backend;

import edu.brown.cs032.tmercuri.ja11.maps.gui.MapsFrame;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Thomas Mercurio
 */
public class Maps {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean useGUI = false;
        MapsFrame GUI = null;
        String waysFilename = "";
        String nodesFilename = "";
        String IndexFilename = "";
        
        try {
            // check argument numbers
            if (args.length != 3 && args.length != 4) {
                    throw new RuntimeException("Usage: maps [--gui] <ways> <nodes> <index>");
            }

            if (args.length == 4) {
                if (args[0].equals("--gui")) {
                    useGUI = true;
                    waysFilename = args[1];
                    nodesFilename = args[2];
                    IndexFilename = args[3];
                } else {
                    throw new RuntimeException("Usage: maps [--gui] <ways> <nodes> <index>");
                }
            } else {
                waysFilename = args[0];
                nodesFilename = args[1];
                IndexFilename = args[2];
            }
            
            if (useGUI) {
                GUI = new MapsFrame(null);
            }
            MapData map = new MapData(waysFilename, nodesFilename, IndexFilename);
            if (GUI != null) {
                GUI.setMap(map);
            } else {
                commandLineInterface(map);
            }
       } catch (IOException /*| RuntimeException*/ ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
    
    private static void commandLineInterface(MapData map) throws IOException {
        Scanner s = new Scanner(System.in);
        while (s.hasNextLine()) {
            List<List<String>> path;
            String line = s.nextLine().replaceAll("\\s+", " ");
            // got an empty line
            if (line.equals(" ") || line.equals("")) {
                break;
            }
            // in quotes, assume we are given cross streets
            if (line.startsWith("\"")) {
                String[] lineWords = line.split("\" \"");
                // invalid input
                if (lineWords.length != 4) {
                    throw new IllegalArgumentException("bad input");
                }
                path = map.getPath(lineWords[0].replaceAll("\"", ""), lineWords[1].replaceAll("\"", ""), lineWords[2].replaceAll("\"", ""), lineWords[3].replaceAll("\"", ""));
            } // otherwise, lats and lngs
            else {
                String[] lineWords = line.split(" ");
                if (lineWords.length != 4) {
                    throw new IllegalArgumentException("bad input");
                }
                path = map.getPath(Double.parseDouble(lineWords[0]), Double.parseDouble(lineWords[1]), Double.parseDouble(lineWords[2]), Double.parseDouble(lineWords[3]));
            }
            printPath(path);
        }
    }
    
    private static void printPath(List<List<String>> path) {
        // Lists for edges and nodes on the path
        List<String> edges = path.get(0);
        List<String> nodes = path.get(1);
        String sourceID = path.get(2).get(0);
        String targetID = path.get(2).get(1);
        
        // if the first node is not the given source or the target and source are the same, then a path doesn't exist
        if (!nodes.get(0).equals(sourceID) || targetID.equals(sourceID)) {
            System.out.println(sourceID + " -/- " + targetID);
        } // print the path, one connection at a time
        else {
            for (int i=1; i<nodes.size(); i++) {
                System.out.println(nodes.get(i-1) + " -> " + nodes.get(i) + " : " + edges.get(i));
            }
        }
    }
}
