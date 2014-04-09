/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.traffic.server;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author Thomas Mercurio
 */
public class TrafficServer {
    
    private final MapData map;
    private final ServerSocket socket;
    private final ClientPool clients;
    private final Executor tPool;
    
    /**
     * Makes a new TrafficServer
     * @param ways the ways filename
     * @param nodes the nodes filename
     * @param index the index filename
     * @param serverPort the port that the server should start on
     * @throws IOException 
     */
    public TrafficServer(String ways, String nodes, String index, int serverPort) throws IOException {
        if (serverPort <= 1024) {
			throw new IllegalArgumentException("Ports under 1024 are reserved!");
		}
        
        this.map = new MapData(ways, nodes, index);
        this.socket = new ServerSocket(serverPort);
        this.clients = new ClientPool();
        this.tPool = Executors.newFixedThreadPool(9);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length != 6) {
                throw new IllegalArgumentException("Usage: trafficServer <ways> <nodes> <index> <hostname> <traffic port> <server port>");
            }
            
            TrafficServer trafficServer = new TrafficServer(args[0], args[1], args[2], Integer.parseInt(args[5]));
            
            trafficServer.go(args[3], Integer.parseInt(args[4]));
        } catch (IllegalArgumentException | IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
    
    private void go(String trafficBotHostname, int trafficBotPort) {
        tPool.execute(new TrafficBotListener(clients, trafficBotHostname, trafficBotPort));
        
        try {
            Socket clientConnection = socket.accept();
		    System.out.println("Connected to a client.");
		    tPool.execute(new ClientHandler(map, clients, clientConnection));
		} catch (IOException ex) {
		    System.out.println("ERROR: " + ex.getMessage());
        }
    }
}
