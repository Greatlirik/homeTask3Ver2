import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Pier extends Thread {
    private boolean free=true;
    private int maxCargoContainers = 3;
    private BlockingQueue<Ships> shipsQueue;
    private int pierNumber;
    private Ships currentShip;
    ReentrantLock locker;
    Condition condition;
    public Pier(BlockingQueue<Ships> shipsQueue, int pierNumber, ReentrantLock locker,Condition condition){
        super("Причал "+pierNumber);
        this.shipsQueue =shipsQueue;
        this.pierNumber=pierNumber;
        this.locker=locker;
        this.condition=condition;

    }

    @Override
    public void run() {
        while (canUploadMore()) {

            if (currentShip == null) {
                locker.lock();
                try {;
                        free = true;
                    } finally{
                    locker.unlock();
                }


            }

            else {
                    currentShip.lockerShip.lock();
                    free = false;
                    currentShip.lockerShip.unlock();
                    currentShip.Upload();
                    System.out.println("Корабль " + currentShip.getShipName() + " был разгружен причалом " + pierNumber);
                    maxCargoContainers--;
                currentShip = shipsQueue.poll();
                if (currentShip != null) {
                    System.out.println("Корабль " + currentShip.getShipName() + " из очереди пришвартовался к причалу " + pierNumber + " для разгрузки");
                }
            }
        }
        System.out.println("Причал " + pierNumber + " заполнен");
    }

    private boolean canUploadMore() {
        return maxCargoContainers > 0;
    }
    public void setCurrentShip(Ships currentShip) {
        this.currentShip = currentShip;
    }

    public int getPierNumber() {
        return pierNumber;
    }
    public boolean isFree() {
        return free;
    }
}
