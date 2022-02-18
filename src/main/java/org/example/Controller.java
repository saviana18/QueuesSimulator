package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller implements Runnable {

    @FXML private TextField clients;
    @FXML private TextField queues;
    @FXML private TextField simulationInterval;
    @FXML private TextField minimumArrivalTime;
    @FXML private TextField maximumArrivalTime;
    @FXML private TextField minimumServiceTime;
    @FXML private TextField maximumServiceTime;
    @FXML private TextArea results;

    public int c; //to save the number of clients
    public int q; //to save the number of queues
    public int sim; //to save the simulation interval
    public int atMin; //to save the minimum arrival time
    public int atMax; //to save the maximum arrival time
    public int stMin; //to save the minimum service time
    public int stMax; //to save the maximum service time

    private Scheduler scheduler;
    private ArrayList<Task> tasks;

    public void generateNRandomTasks(int N) {
        Random random = new Random();
        int i;
        for (i = 0; i < N; i++) {
            int randomArrivalTime = random.nextInt(atMax - atMin + 1) + atMin;
            int randomProcessingTime = random.nextInt(stMax - stMin + 1) + stMin;
            Task newRandomTask = new Task(i + 1, randomArrivalTime, randomProcessingTime);
            tasks.add(newRandomTask);
        }
    }

    @Override
    public void run() {
        generateNRandomTasks(c);
        double averageServiceTime = 0, averageWaitingTime = 0;
        int peakHour = 0;
        int currentTime = 0, maxNoTasks = 0;
        try {
            FileWriter fileWriter = new FileWriter("logTestNou.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            while (currentTime < sim) {
                int finalCurrentTime = currentTime;
                Platform.runLater(()->results.appendText("Time " + finalCurrentTime + "\n"));
                bufferedWriter.write("Time " + finalCurrentTime + "\n");

                Iterator<Task> taskIterator = tasks.iterator();
                while (taskIterator.hasNext()) {
                    Task task = taskIterator.next();
                    if (task.getArrivalTime() == currentTime) {
                        scheduler.dispatchTask(task);
                        averageServiceTime += task.getProcessingPeriod();
                        averageWaitingTime += task.getFinishTime();
                        taskIterator.remove();
                    }
                }

                for (Task task : tasks) {
                    Platform.runLater(() -> results.appendText("Waiting clients: " + task.toString()));
                    bufferedWriter.write("Waiting clients: " + task.toString());

                }

                int countForServers = 0;
                for (Server server : scheduler.getServers()) {
                    countForServers += server.getTasks().size();
                    Platform.runLater(()->results.appendText(server.toString() + "\n"));
                    bufferedWriter.write(server.toString() + "\n");

                }

                if (countForServers > maxNoTasks) {
                    maxNoTasks = countForServers;
                    peakHour = currentTime;
                }


                System.out.println(currentTime);

                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }

                currentTime++;
            }

            averageServiceTime /= c;
            averageWaitingTime /= c;
            int finalPeakHour = peakHour;
            int finalMaxNoTasks = maxNoTasks;
            Platform.runLater(() -> results.appendText("Peak hour: " + finalPeakHour + " with a number of: " +
                    finalMaxNoTasks + " tasks" + "\n"));
            double finalAverageServiceTime = averageServiceTime;
            Platform.runLater(() -> results.appendText("Average service time: " + finalAverageServiceTime + "\n"));
            double finalAverageWaitingTime = averageWaitingTime;
            Platform.runLater(() -> results.appendText("Average waiting time: " + finalAverageWaitingTime + "\n"));
            bufferedWriter.write("Peak hour: " + finalPeakHour + " with a number of: " +
                    finalMaxNoTasks + " tasks" + "\n");
            bufferedWriter.write("Average service time: " + finalAverageServiceTime + "\n");
            bufferedWriter.write("Average waiting time: " + finalAverageWaitingTime + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void startSimulation(ActionEvent actionEvent) {
        try {
            c = Integer.parseInt(clients.getText());
            q = Integer.parseInt(queues.getText());
            sim = Integer.parseInt(simulationInterval.getText());
            atMin = Integer.parseInt(minimumArrivalTime.getText());
            atMax = Integer.parseInt(maximumArrivalTime.getText());
            stMin = Integer.parseInt(minimumServiceTime.getText());
            stMax = Integer.parseInt(maximumServiceTime.getText());
        } catch (NumberFormatException exception) {
            results.clear();
            results.setText("Input-urile sunt numere intregi!\n");
        }

        if (atMax < atMin || stMax < stMin || atMax > sim || stMax > sim) {
            results.clear();
            results.setText("Introduceti valori conform cerintei!");
        }

        this.tasks = new ArrayList<Task>();
        Controller.this.scheduler = new Scheduler(q, c);

        Thread thread = new Thread(Controller.this);
        thread.start();

        Timer timer = new Timer();
        TimerTask exitApp = new TimerTask() {
            public void run() {
                System.exit(0);
            }
        };

        timer.schedule(exitApp, new Date(System.currentTimeMillis() + (this.sim + 10) * 1000L));
    }
}
