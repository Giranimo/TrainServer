/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the systems Train Server which simulates a server that is
 * placed in a vehicle, The class is used to communicate with the corresponding
 * PDA program.
 *
 * @author Michael Kofod, Sune Heide
 */
public class TrainServer extends Thread {

    int multiPort = 5000;
    int uniPort = multiPort + 1;
    int uniPort2 = uniPort + 1;
    UnicasterThread urt;
    TrainServerDB tsDB;
    BusSimulation busSim;
    String[] split;
    HashMap pingMap;
    String vehicle = "500S";

    /**
     * This constructor initializes objects which are needed for further usage
     * It also starts the other thread which is needed to run the program.
     */
    public TrainServer(BusSimulation busSim) {
        this.busSim = busSim;
        urt = new UnicasterThread();
        pingMap = new HashMap();
        tsDB = new TrainServerDB();
        threadStart();
    }

    /**
     * Constructor did not like to have a thread start, so a method was created
     * to do it and call it instead
     *
     * This method starts the UniReceiveThread thread.
     */
    public void threadStart() {
        urt.start();
    }

    /**
     * This run() method starts the Thread which does nothing else but multicast
     * the same message every 2 second.
     */
    @Override
    public void run() {

        try {
            String group = "239.192.100.100";
            InetAddress multiAddress;

            multiAddress = InetAddress.getByName(group);
            MulticastSocket socket = new MulticastSocket();
            socket.joinGroup(multiAddress);

            String sendData = "HEJ " + multiAddress.getHostAddress() + ":" + multiPort;
            byte[] data = sendData.getBytes();
            DatagramPacket pack = new DatagramPacket(data, data.length, multiAddress, multiPort);

            while (true) {
                socket.send(pack);
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            Logger.getLogger(TrainServer.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * This Thread receives and sends Unicast to the PDA's which answer's the
     * Multicast that is being spammed out, It will check in the Database and
     * decide out from the return what information needs to be sent back.
     */
    public class UnicasterThread extends Thread {

        @Override
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket(uniPort2);

                byte buf[] = new byte[2048];
                DatagramPacket pack = new DatagramPacket(buf, buf.length);

                while (true) {
                    socket.receive(pack);
                    handleUniPack(pack);
//                    System.out.println("Vi har modtaget unicast " + split[1]);
                }
            } catch (Exception e) {
                Logger.getLogger(UnicasterThread.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }

        /**
         * Depending on the message in the unicast received it will send a
         * specific answer after checking the Database whether the credentials
         * received exists and whether or not the customer is already logged on.
         *
         * @param pack
         */
        public void handleUniPack(DatagramPacket pack) {
            InetAddress address;

            String retrieved = new String(pack.getData(), 0, pack.getLength());
            split = retrieved.split(" ");
            address = pack.getAddress();

            pack.setData(new byte[2048]);

            switch (split[0]) {
                case "LOGIN":
                    //Accepted i databasen - Check username, password og balance mindre end 0
                    if (tsDB.loginPDA((split[1]), split[2])) {
                        if (!pingMap.containsKey(split[1])) {
                            sendUnicast("ACCEPTED " + split[1], address);
                            System.out.println("Vi har modtaget LOGIN og sender ACCEPTED - " + split[1]);
                            pingMap.put(split[1], true);
                            PingTimer pt = new PingTimer(split[1]);
                            pt.start();
                            tsDB.createTravel(split[1], vehicle, currentTime(split[1]), busSim.BusNow);
                        } else { // Accepted i databasen men exists i Hashmap
                            sendUnicast("DECLINED " + split[1], address);
                            System.out.println("Personen er allerede logget ind, sender DECLINED - " + split[1]);
                        }
                    } else //Declined i databasen
                    {
                        sendUnicast("DECLINED " + split[1], address);
                        System.out.println("Vi har modtaget LOGIN og sender DECLINED - " + split[1]);
                        pingMap.remove(split[1]);
                    }
                    break;
                case "PING":
                    pingMap.put(split[1], true);
                    break;
            }
        }

        /**
         * This method is used in the UniReceiveThread and is used to send out a
         * unicast with a message the other program can understand.
         *
         * @param msg
         * @param IPaddress
         */
        public void sendUnicast(String msg, InetAddress IPaddress) {
            try {
                DatagramSocket socket = new DatagramSocket();

                byte[] buf = msg.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, IPaddress, uniPort);
                socket.send(packet);

            } catch (Exception e) {
                Logger.getLogger(UnicasterThread.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    /**
     * This method returns the current date and time of the computer in a
     * specific Format.
     *
     * @return String
     */
    public String currentTime(String name) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        String DateNow = sdf.format(date) + " " + sdf2.format(date);
        System.out.println(name + " er gået på/af " + vehicle + " " + sdf.format(date) + " " + sdf2.format(date));
//        System.out.println(split[1] + " er steget på transportmiddel " + sdf.format(date) + " " + sdf2.format(date));
        return DateNow;
    }

    /**
     * PingTimer class is a thread that ensures the customer has pinged within 5
     * seconds.
     */
    public class PingTimer extends Thread {

        String username;

        /**
         * We initialize the String username and set it to the owner of the PDA
         * which we accepted, This is to ensure consistency on the who the
         * pingtimer works with.
         *
         * @param username
         */
        public PingTimer(String username) {
            this.username = username;
        }

        /**
         * This run() method is a basic timer which first checks the hashmap if
         * the customer has pinged, If the customer has pinged within the 5
         * seconds of the timer it will run again and it will continue doing so
         * untill the customer has not pinged.
         */
        @Override
        public void run() {
            boolean isRunning = true;
            while (isRunning) {
                try {
                    Thread.sleep(5000);
                    System.out.println("PingTimer har talt til 5 " + username);
                    if (checkPing()) {
                        isRunning = false;
                        tsDB.updateTravel(username, currentTime(username), busSim.BusNow, 100);
//                        endTIME(username);
                        pingMap.remove(username);
                    }
                } catch (Exception e) {
                    Logger.getLogger(PingTimer.class.getName()).log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }

        /**
         * Every time the customer pings, his value is set to true This method
         * checks if the customer has pinged, if he has it will change it to
         * false.
         *
         * @return boolean
         */
        public boolean checkPing() {
            if (pingMap.get(username) == true) {
                pingMap.put(username, false);
                return false;
            } else {
                return true;
            }
        }
    }
}