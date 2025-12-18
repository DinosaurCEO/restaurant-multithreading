package com.example.restaurant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;
public class ClientGenerator implements Runnable{
    private final BlockingQueue<Order> incoming;
    private final int intervalMs;
    private final AtomicLong counter = new AtomicLong();
    private final Random rnd = new Random();
    private volatile boolean running = true;
    public ClientGenerator(BlockingQueue<Order> incoming, int intervalMs){
        this.incoming = incoming;
        this.intervalMs = intervalMs;
    }
    public void run(){
        try{
            while(running){
                long id = counter.incrementAndGet();
                Order.Dish dish = Order.Dish.values()[rnd.nextInt(Order.Dish.values().length)];
                String clientName = "Client-"+id;
                Order o = new Order(id, dish, clientName, null);
                incoming.put(o);
                SafeLogger.log("New client order created id="+id+" dish="+dish+" client="+clientName);
                Thread.sleep(intervalMs);
            }
        }catch(InterruptedException e){}
    }
    public void stop(){running=false;}
}

