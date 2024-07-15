package org.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Team teams[] = new Team[3];

        for (int i = 0; i < teams.length; i++) {
            teams[i] = new Team(i + 1);
        }

        Race race = new Race(1000, teams);

        race.start();
        race.printResults();
        System.exit(0);
    }
}
