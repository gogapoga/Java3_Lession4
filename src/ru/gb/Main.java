package ru.gb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
	    //Задание №1
        printLetters();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Задание №2
        writeFile1("text1.txt"); //по заданию неясно важен ли порядок записи потоками в файл, поэтому сделал 2 метода
                                         //writeFile1-соблюдает порядок, writeFile2-порядок записи равновероятен
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writeFile2("text2.txt");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Задание №3
        MFU mfu = new MFU();
        mfu.start();
        mfu.printDocument(10);
        mfu.scannerDocument(10);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mfu.printDocument(5);
        mfu.scannerDocument(3);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mfu.stop();
    }

    public static void printLetters() {
        PrintLettersClass w = new PrintLettersClass();
        Thread threadA = new Thread(() -> {
            w.printA();
        });
        Thread threadB = new Thread(() -> {
            w.printB();
        });
        Thread threadC = new Thread(() -> {
            w.printC();
        });
        threadC.start();
        threadA.start();
        threadB.start();
    }

    public static void writeFile1(String fileName) {
        WriteFileClass w = new WriteFileClass(fileName);
        if(w.init()) {
            Thread thread1 = new Thread(() -> {
                w.writeThread1();
            });
            Thread thread2 = new Thread(() -> {
                w.writeThread2();
            });
            Thread thread3 = new Thread(() -> {
                w.writeThread3();
                w.close();
            });
            thread1.start();
            thread2.start();
            thread3.start();

        } else System.out.println("Ошибка открытия файла!");


    }

    public static void writeFile2(String fileName) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName, false);
            ExecutorService executorService = Executors.newFixedThreadPool(3);
            executorService.execute(new Runnable() {
                public void run() {
                    for (int i = 0; i < 10; i++) {
                        try {
                            fout.write(("Thread №1 записал в файл!\n").getBytes());
                            System.out.println("Thread №1 записал в файл!");
                            Thread.sleep(20);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            executorService.execute(new Runnable() {
                 public void run() {
                     for (int i = 0; i < 10; i++) {
                         try {
                             fout.write(("Thread №2 записал в файл!\n").getBytes());
                             System.out.println("Thread №2 записал в файл!");
                             Thread.sleep(20);
                        } catch (IOException e) {
                             e.printStackTrace();
                        } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                     }
                 }
            });
            executorService.execute(new Runnable() {
                 public void run() {
                     for (int i = 0; i < 10; i++) {
                         try {
                             fout.write(("Thread №3 записал в файл!\n").getBytes());
                             System.out.println("Thread №3 записал в файл!");
                             Thread.sleep(20);
                         } catch (IOException e) {
                             e.printStackTrace();
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                     }
                 }
            });
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fout.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
