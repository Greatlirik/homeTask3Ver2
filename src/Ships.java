import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ships extends Thread {
    private boolean cargoOff = false;
    private int shipName;
    ReentrantLock lockerShip;
    Condition conditionShip;
    private List<Pier> piers;
    private BlockingQueue<Ships> shipsQueue;
    private int timeToUpload;
    public Ships(int timeToUpload, List<Pier> piers, BlockingQueue<Ships> shipsQueue, int shipName,ReentrantLock lockerShip,Condition conditionShip) {
        super("Корабль " + shipName);
        this.timeToUpload=timeToUpload;
        this.shipName=shipName;
        this.shipsQueue=shipsQueue;
        this.piers=piers;
        this.lockerShip=lockerShip;
        this.conditionShip=conditionShip;

    }

    @Override
    public void run() {

        boolean isApplied = false;

        for (Pier piers : piers) {
            if (piers.isFree()) {
                synchronized (piers) {
                    if (piers.isFree()) {
                        piers.setCurrentShip(this);
                        System.out.println("Корабль " + shipName + " нашел свободный причал " + piers.getPierNumber() + " и пришвартовался");
                        isApplied = true;
                        piers.notify();


                    }
                }
                break;
            } else {
                continue;
            }
        }

        if (!isApplied) {
            shipsQueue.offer(this);
            synchronized (this) {
                try {
                    this.wait(3000 + new Random().nextInt(3000));

                if (!cargoOff) {
                    shipsQueue.remove(this);
                    System.out.println("Корабль " + shipName + " остался с грузом");
                }
                    } catch (InterruptedException e) {
                        Thread.currentThread()
                                .interrupt();
                        e.printStackTrace();
                    }
                }

            }

        if (!cargoOff) {
            System.out.println("Корабль " + shipName + " уплыл");
        }

    }
    public void Upload() {
        synchronized (this) {
            try {
                Thread.sleep(timeToUpload);
                cargoOff = true;
                this.notify();

            } catch (InterruptedException e) {
                Thread.currentThread()
                        .interrupt();
                e.printStackTrace();
            }
        }
    }



    public int getShipName() {
        return shipName;
    }
}
