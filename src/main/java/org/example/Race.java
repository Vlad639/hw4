package org.example;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Log4j2
public class Race {

    @Getter
    private final long distance;

    @Getter
    private final CountDownLatch start;

    @Getter
    private final CountDownLatch finish;

    private final List<F1Cars> participantCars = new ArrayList<>();

    private final List<Team> teams = new ArrayList<>();

    public Race(long distance, Team[] participantCars) {
        this.distance = distance;
        teams.addAll(List.of(participantCars));
        int carsNumbers = teams.stream().mapToInt(team -> team.getCars().length).sum();
        start = new CountDownLatch(carsNumbers + 1);
        finish = new CountDownLatch(carsNumbers);
    }

    /**
     * Запускаем гонку
     */
    public void start() throws InterruptedException {
        for (Team team : teams) {
            team.prepareRace(this);
        }

        log.info("Ждём, пока все машины приедут на стартовую позицию");
        while (start.getCount() > 1) {
            Thread.sleep(100);
        }

        //даём команду на старт гонки
        start.countDown();
        log.info("Старт гонки!");

        //блокируем поток до завершения гонки
        finish.await();
    }


    //Регистрируем участников гонки
    public void register(F1Cars participantCar) {
        participantCars.add(participantCar);
    }

    public void printResults() {
        participantCars.sort(F1Cars::compareTo);
        log.info("Результат гонки:");
        int position = 1;
        for (F1Cars participant : participantCars) {
            log.info("Позиция: {}, боллид: {}, время: {}", position++, participant.getName(), participant.getTime());
        }
    }
}
