package com.example.restaurant;
import java.util.concurrent.CompletableFuture;
public class Order {
    public enum Dish {SOUP, STEAK, SALAD}
    private final long id;
    private final Dish dish;
    private final String clientName;
    private final String waiterName;
    private final CompletableFuture<Order> readyFuture = new CompletableFuture<>();
    private volatile long createdAt = System.currentTimeMillis();
    public Order(long id, Dish dish, String clientName, String waiterName){
        this.id=id;
        this.dish=dish;
        this.clientName=clientName;
        this.waiterName=waiterName;
    }
    public long getId(){return id;}
    public Dish getDish(){return dish;}
    public String getClientName(){return clientName;}
    public String getWaiterName(){return waiterName;}
    public CompletableFuture<Order> getReadyFuture(){return readyFuture;}
    public long getCreatedAt(){return createdAt;}
}
