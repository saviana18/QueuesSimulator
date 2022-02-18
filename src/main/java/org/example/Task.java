package org.example;

public class Task {

    private int clientID;
    private int arrivalTime;
    private int finishTime;
    private int processingPeriod;

    public Task() {

    }

    public Task(int clientID, int arrivalTime, int processingPeriod) {
        this.clientID = clientID;
        this.arrivalTime = arrivalTime;
        this.finishTime = 0;
        this.processingPeriod = processingPeriod;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getProcessingPeriod() {
        return processingPeriod;
    }

    public void setProcessingPeriod(int processingPeriod) {
        this.processingPeriod = processingPeriod;
    }

    @Override
    public String toString() {
        return "(" + this.clientID + ", " + this.arrivalTime + ", " +
                this.processingPeriod + ")" + "\n";
    }
}

