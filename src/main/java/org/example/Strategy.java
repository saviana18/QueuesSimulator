package org.example;

import java.util.List;

public class Strategy {

    public void addTask(List<Server> servers, Task task){
        int minimumWaitingTime = Integer.MAX_VALUE;
        int indexServer = -1;
        for (Server server: servers) {
            if (server.getTasks().size() < Scheduler.maxClientsPerServer) {
                if (server.getWaitingPeriod().intValue() < minimumWaitingTime) {
                    minimumWaitingTime = server.getWaitingPeriod().intValue();
                    indexServer = servers.indexOf(server);
                }
            }
        }

        if (indexServer != -1) {
            servers.get(indexServer).addTask(task);
        }
    }
}