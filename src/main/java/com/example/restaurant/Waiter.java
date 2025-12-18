package com.example.restaurant;
import java.util.concurrent.BlockingQueue;
public class Waiter implements Runnable{
    private final String name;
    private final BlockingQueue<Order> incoming;
    private final BlockingQueue<Order> kitchenQueue;
    public Waiter(String name, BlockingQueue<Order> incoming, BlockingQueue<Order> kitchenQueue){
        this.name = name;
        this.incoming = incoming;
        this.kitchenQueue = kitchenQueue;
    }
    public void run(){
        try{
            while(true){
                Order incomingOrder = incoming.take();
                Order order = new Order(incomingOrder.getId(), incomingOrder.getDish(), incomingOrder.getClientName(), name);
                SafeLogger.log(name+" accepted order "+order.getId()+" from "+order.getClientName());
                kitchenQueue.put(order);
                SafeLogger.log(name+" placed order "+order.getId()+" to kitchen");
                Order ready = order.getReadyFuture().get();
                SafeLogger.log(name+" received ready order "+ready.getId()+" dish="+ready.getDish()+" delivering to "+ready.getClientName());
                deliver(ready);
            }
        }catch(InterruptedException e){
        }catch(Exception e){
            SafeLogger.log(name+" terminated with error "+e.getMessage());
        }
    }
    private void deliver(Order o){
        SafeLogger.log(name+" delivered order "+o.getId()+" to "+o.getClientName());
    }
}
