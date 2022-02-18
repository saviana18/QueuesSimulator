package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int queueID;

    public Server() {

    }

    public Server(int queueID) {

        //initialize
        this.tasks = new ArrayBlockingQueue<>(Scheduler.maxClientsPerServer);
        this.waitingPeriod = new AtomicInteger(0);
        this.queueID = queueID;
    }

    public void addTask(Task task) {

        //add task to queue
        tasks.add(task);
        //compute task's finish time
        task.setFinishTime(waitingPeriod.intValue() + task.getArrivalTime() + task.getProcessingPeriod());
        //increment waitingTime
        waitingPeriod.addAndGet(task.getProcessingPeriod());
    }

    public void run() {

        Task task;
        while (true) {
            try {
                //take next task from queue
                task = this.tasks.peek();
                if (task != null) {

                    //stop the thread for a time equal with the task's processing period
                    Thread.sleep(1000L * task.getProcessingPeriod());

                    //decrement waiting period
                    this.waitingPeriod.addAndGet(-task.getProcessingPeriod());

                    //remove tasks' head
                    this.tasks.poll();
                }

            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public void setTasks(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public int getQueueID() {
        return queueID;
    }

    public void setQueueID(int queueID) {
        this.queueID = queueID;
    }

    @Override
    public String toString() {

        String serverString = "Queue #" + this.queueID;

        for(Task task : this.tasks) {
            serverString = serverString + "\n" + task.toString();
        }

        if(serverString.equals("Queue #" + this.queueID)) {
            serverString = serverString + "\n" + "Closed" + "\n";
        }
        return serverString;
    }
}