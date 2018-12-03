package ru.gb;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MFU {
    private volatile int queuePrint;
    private volatile int queueScanner;
    private volatile boolean bStop;
    private final Object mon = new Object();
    private ExecutorService executorService;
    public MFU() {
        queuePrint = 0;
        queueScanner = 0;
        bStop = true;
        executorService = Executors.newFixedThreadPool(2);
    }
    public void start() { //запуск принтера и сканера
        bStop = false;;
        executorService.execute(new Runnable() {
            public void run() {
                while(!bStop) {
                    int temp = queuePrint;
                    synchronized (mon) { //если началась печать документа, то печатаются все страницы и только потом возможно сканирование
                        for (int i = 0; i < temp; i++) {
                            System.out.println("Отпечатана " + (i + 1) + "-я страница.");
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        queuePrint -= temp;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        executorService.execute(new Runnable() {
            public void run() {
                while(!bStop) {
                    int temp = queueScanner;
                    synchronized (mon) { ////если начали сканировать документ, то сканируются все страницы и только потом возможна печать
                        for (int i = 0; i < temp; i++) {
                            System.out.println("Отcканирована " + (i + 1) + "-я страница.");
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        queueScanner -= temp;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void stop() { // остановка МФУ
        bStop = true;
        executorService.shutdown();
    }
    public void printDocument(int pages) {
        queuePrint += pages;
    }
    public void scannerDocument(int pages) {
        queueScanner += pages;
    }
}
