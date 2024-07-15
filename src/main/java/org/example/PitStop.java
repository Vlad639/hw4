package org.example;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

@Log4j2
public class PitStop extends Thread {

    PitWorker[] workers = new PitWorker[4];

    private final Semaphore semaphore = new Semaphore(1, true);

    private volatile F1Cars car;

    @Getter
    private final CyclicBarrier barrier = new CyclicBarrier(5);

    public PitStop() {
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new PitWorker(i, this);
            workers[i].start();
        }
    }

    public void pitline(F1Cars f1Cars) throws InterruptedException, BrokenBarrierException {
        semaphore.acquire();
        log.info("Боллид {} прибыл на питстоп", f1Cars.getName());
        car = f1Cars;
        barrier.await(); // Ждём, пока будут заменены все шины

        car = null;
        log.info("Боллид {} покинул питстоп", f1Cars.getName());
        semaphore.release();
    }


    @Override
    public void run() {
        while(!isInterrupted()){
            //синхронизируем поступающие болиды и работников питстопа при необходимости
        }
    }

    public F1Cars getCar() {
        return car;
    }
}
