package org.example;

import java.util.ArrayList;

public class Scheduler {

    public static int maxClientsPerServer;
    private static ArrayList<Server> serverList;
    private final Strategy strategy = new Strategy();

    public Scheduler(int maxServers, int maxClientsPerServer) {
        Scheduler.maxClientsPerServer = maxClientsPerServer;
        serverList = new ArrayList<Server>(maxClientsPerServer);
        for(int i = 1; i <= maxServers; i++) {
            Server newServer = new Server(i);
            serverList.add(newServer);
            Thread thread = new Thread(newServer);
            thread.start();
        }
    }

    public void dispatchTask(Task task) {
        strategy.addTask(serverList, task);
    }

    public ArrayList<Server> getServers() {
        return serverList;
    }
}