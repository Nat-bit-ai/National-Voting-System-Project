package service;

public class OfflineSyncService {
    private Thread worker;

    public void start() {
        worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(30_000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "offline-sync-service");
        worker.setDaemon(true);
        worker.start();
    }
}
