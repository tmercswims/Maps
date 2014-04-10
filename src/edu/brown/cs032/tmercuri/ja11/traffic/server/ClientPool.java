/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.traffic.server;

import java.io.IOException;
import java.util.LinkedList;

/**
 * A pool of clients for sending traffic information to them all.
 * @author Thomas Mercurio
 */
public class ClientPool {
    private final LinkedList<ClientHandler> clients;
	
	/**
	 * Initialize a new ClientPool.
	 */
	public ClientPool() {
		clients = new LinkedList<>();
	}
	
	/**
	 * Add a new client to the chat room.
	 * @param client to add
	 */
	public synchronized void add(ClientHandler client) {
		clients.add(client);
	}
	
	/**
	 * Remove a client from the pool. Only do this if you intend to clean up
	 * that client later.
	 * @param client to remove
	 * @return true if the client was removed, false if they were not there
	 */
	public synchronized boolean remove(ClientHandler client) {
		return clients.remove(client);
	}
	
	/**
	 * Send a line of traffic info to all clients in the pool
	 * @param trafficInfo to send
	 */
	public synchronized void broadcastTrafficInfo(String trafficInfo) {
		for (ClientHandler client : clients) {
			client.sendTrafficInfo(trafficInfo);
		}
	}
    
    /**
     * Sends a server quit message to all clients.
     */
    public synchronized void broadcastQuitMessage() {
        for (ClientHandler client : clients) {
            client.sendQuit();
        }
    }
    
    /**
	 * Close all ClientHandlers and empty the pool
	 */
	public synchronized void killall() {
		broadcastQuitMessage();

		for (ClientHandler client : clients) {
			try {
				client.kill();
			} catch (IOException e) {
				// There's nothing we can do here.
			}
		}

		clients.clear();
	}
}
