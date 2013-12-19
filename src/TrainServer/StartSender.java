/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainServer;

import javax.swing.Timer;

/**
 * This class creates a thread and starts it This class is used to start the
 * Train Server.
 *
 * @author Mikael Kofod, Sune Heide
 */
public class StartSender {

    TrainServer tsThread;
    Timer simTime;
    BusSimulation busSim;

    public static void main(String[] args) {
        StartSender ss = new StartSender();
        ss.startThreads();
    }

    /**
     * Initializes a thread and starts it.
     */
    public void startThreads() {
        busSim = new BusSimulation();
        tsThread = new TrainServer(busSim);
        simTime = new Timer(0, busSim);
        simTime.setDelay(5000);
        simTime.start();
        tsThread.start();
    }
}
