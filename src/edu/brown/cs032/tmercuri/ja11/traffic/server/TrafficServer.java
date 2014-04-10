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
public class TrafficServer extends Thread {
    
    private final MapData map;
    private final ServerSocket socket;
    private final ClientPool clients;
    private final Executor tPool;
    private final String trafficBotHostname;
    private final int trafficBotPort;
    
    /**
     * Makes a new TrafficServer.
     * @param ways the ways filename
     * @param nodes the nodes filename
     * @param index the index filename
     * @param serverPort the port that the server should start on
     * @param trafficBotHostname the hostname that the traffic bot is on
     * @param trafficBotPort the port that the traffic bot is on
     * @throws IOException if an I/O error occurs
     */
    public TrafficServer(String ways, String nodes, String index, int serverPort, String trafficBotHostname, int trafficBotPort) throws IOException {
        if (serverPort <= 1024) {
			throw new IllegalArgumentException("Ports under 1024 are reserved!");
		}
        
        this.map = new MapData(ways, nodes, index);
        this.socket = new ServerSocket(serverPort);
        this.clients = new ClientPool();
        this.tPool = Executors.newFixedThreadPool(9);
        this.trafficBotHostname = trafficBotHostname;
        this.trafficBotPort = trafficBotPort;
    }

    /**
     * Runs this server.
     */
    @Override
    public void run() {
        tPool.execute(new TrafficBotListener(clients, trafficBotHostname, trafficBotPort));
        
        try {
            Socket clientConnection = socket.accept();
		    System.out.println("Connected to a client.");
		    tPool.execute(new ClientHandler(map, clients, clientConnection));
		} catch (IOException ex) {
		    System.out.println("ERROR: " + ex.getMessage());
        }
    }
    
    /**
     * Stops waiting for connections, closes all connected clients, and closes this server's ServerSocket.
     * @throws IOException if the socket is invalid
     */
    public void kill() throws IOException {
        clients.killall();
        socket.close();
    }
}
