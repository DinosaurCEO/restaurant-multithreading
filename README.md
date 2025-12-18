# Restaurant Multithreading Simulation

Многопоточная симуляция работы ресторана: клиенты, официанты и кухня работают параллельно.

## Как использовать

### Компиляция
```bash
cd src/main/java
javac $(find . -name "*.java")

### Запуск
```java -cp src/main/java com.example.restaurant.Main [waiters] [cooks] [clientIntervalMs] [runtimeSeconds]

