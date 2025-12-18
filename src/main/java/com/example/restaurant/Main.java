package com.example.restaurant;
import java.util.concurrent.*;
public class Main {
    public static void main(String[] args) throws Exception {
        int waiters = 3;
        int cooks = 3;
        int clientIntervalMs = 400;
        int runtimeSeconds = 20;
        if(args.length>=1) waiters=Integer.parseInt(args[0]);
        if(args.length>=2) cooks=Integer.parseInt(args[1]);
        if(args.length>=3) clientIntervalMs=Integer.parseInt(args[2]);
        if(args.length>=4) runtimeSeconds=Integer.parseInt(args[3]);
        BlockingQueue<Order> kitchenQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Order> incomingOrders = new LinkedBlockingQueue<>();
        Kitchen kitchen = new Kitchen(kitchenQueue, cooks);
        kitchen.start();
        ExecutorService waitersPool = Executors.newFixedThreadPool(waiters);
        for(int i=0;i<waiters;i++){
            Waiter w = new Waiter("Waiter-"+(i+1), incomingOrders, kitchenQueue);
            waitersPool.submit(w);
        }
        ClientGenerator clients = new ClientGenerator(incomingOrders, clientIntervalMs);
        Thread cg = new Thread(clients);
        cg.start();
        SafeLogger.log("Restaurant started with waiters="+waiters+" cooks="+cooks);
        Thread.sleep(runtimeSeconds * 1000L);
        clients.stop();
        cg.join();
        SafeLogger.log("Stopping accepting new orders");
        Thread.sleep(1000);
        waitersPool.shutdownNow();
        waitersPool.awaitTermination(5, TimeUnit.SECONDS);
        kitchen.stop();
        SafeLogger.log("Restaurant stopped");
    }
}
