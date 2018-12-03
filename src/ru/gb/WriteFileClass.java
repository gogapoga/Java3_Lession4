package ru.gb;

import java.io.FileOutputStream;
import java.io.*;

public class WriteFileClass {
    private String fileName;
    private FileOutputStream fout;
    private volatile int currentThread;
    public WriteFileClass(String fileName) {
        this.fileName = fileName;
        fout = null;
        currentThread = 0;
    }
    public  boolean init() {
        try {
            fout = new FileOutputStream(fileName, false);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public  void close() {
        try {
            fout.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeThread1() {
        synchronized (fout) {
            try {
                for(int i = 0; i < 10; i++) {
                    while (currentThread != 0) {
                        fout.wait();
                    }
                    try {
                        fout.write(("Thread №1 записал в файл!\n").getBytes());
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread №1 записал в файл!");
                    Thread.sleep(20);
                    currentThread = 1;
                    fout.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void writeThread2() {
        synchronized (fout) {
            try {
                for(int i = 0; i < 10; i++) {
                    while (currentThread != 1) {
                        fout.wait();
                    }
                    try {
                        fout.write(("Thread №2 записал в файл!\n").getBytes());
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread №2 записал в файл!");
                    Thread.sleep(20);
                    currentThread = 2;
                    fout.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void writeThread3() {
        synchronized (fout) {
            try {
                for(int i = 0; i < 10; i++) {
                    while (currentThread != 2) {
                        fout.wait();
                    }
                    try {
                        fout.write(("Thread №3 записал в файл!\n").getBytes());
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread №3 записал в файл!");
                    Thread.sleep(20);
                    currentThread = 0;
                    fout.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
