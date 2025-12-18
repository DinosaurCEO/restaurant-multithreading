package com.example.restaurant;
import java.util.concurrent.*;
public class Kitchen {
    private final BlockingQueue<Order> kitchenQueue;
    private final ExecutorService cooks;
    private final Thread dispatcher;
    private volatile boolean running = true;
    public Kitchen(BlockingQueue<Order> kitchenQueue, int cooksCount){
        this.kitchenQueue = kitchenQueue;
        this.cooks = Executors.newFixedThreadPool(cooksCount);
        this.dispatcher = new Thread(this::dispatchLoop);
    }
    public void start(){dispatcher.start();}
    private void dispatchLoop(){
        try{
            while(running || !kitchenQueue.isEmpty()){
                Order order = kitchenQueue.poll(200, TimeUnit.MILLISECONDS);
                if(order==null) continue;
                cooks.submit(() -> prepare(order));
            }
        }catch(InterruptedException e){}
    }
    private void prepare(Order o){
        try{
            long start = System.currentTimeMillis();
            int delay = cookTimeFor(o.getDish());
            SafeLogger.log("Cook started order "+o.getId()+" dish="+o.getDish()+" by "+o.getWaiterName());
            Thread.sleep(delay);
            SafeLogger.log("Cook finished order "+o.getId()+" dish="+o.getDish());
            o.getReadyFuture().complete(o);
        }catch(InterruptedException e){
            o.getReadyFuture().completeExceptionally(e);
        }
    }
    private int cookTimeFor(Order.Dish d){
        switch(d){
            case SOUP: return 800;
            case SALAD: return 400;
            case STEAK: return 1200;
            default: return 500;
        }
    }
    public void stop(){
        running=false;
        dispatcher.interrupt();
        cooks.shutdown();
        try{ cooks.awaitTermination(5, TimeUnit.SECONDS); }catch(Exception e){}
    }
}

