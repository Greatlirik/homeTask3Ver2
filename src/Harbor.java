import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Harbor {
    private List<Pier> piers=new ArrayList<Pier>();
    private BlockingQueue<Ships> shipsQueue = new LinkedBlockingQueue<Ships>();
    public static void main(String[] args) throws InterruptedException {

        Harbor harbor = new Harbor();
        ReentrantLock locker = new ReentrantLock();
        Condition condition = locker.newCondition();

        for (int i = 1; i < 3; i++) {

            Pier ps = new Pier(harbor.shipsQueue, i,locker,condition);
            harbor.piers.add(ps);

        }

        harbor.piers.forEach(Thread::start);
        for (int i = 1; i < 12; i++) {

            new Ships(500 + new Random().nextInt(50), harbor.piers, harbor.shipsQueue, i,locker,condition).start();
        }

    }
}

