/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainServer;

import RMI.IRemoteMySQL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * This class is used for when the Train Server needs Database access.
 *
 * @author Mikael Kofod, Sune Heide
 */
public class TrainServerDB {

    Statement stmt;
    Registry registry;
    IRemoteMySQL rp;

    /**
     * We run the connection() method to establish a connection to the database.
     */
    public TrainServerDB() {
        connection();
    }

    /**
     * This method creates the connection to the database which we need to be
     * able to access the data which is stored.
     */
    public void connection() {
        try {
            //Get reference to rmi registry server
            registry = LocateRegistry.getRegistry("[2001:878:91e:4:216:3eff:fe1a:c]", 1337);
            //Lookup server object
            rp = (IRemoteMySQL) registry.lookup("MySQL");
        } catch (Exception e) {
            Logger.getLogger(TrainServerDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * This method requires 2 parameters which are used to withdraw data from
     * the database and checks if the found values are correct.
     *
     * @param ssn
     * @param password
     * @return int
     */
    public boolean loginPDA(String ssn, String password) {

        try {
            return rp.loginPDA(ssn, password);
        } catch (Exception e) {
            Logger.getLogger(TrainServerDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    /**
     * This method creates a row in the Travels table which is used to store the
     * journeys of customers, A few columns are created as null to work together
     * with another method.
     *
     * @param ssn
     * @param vehicle
     * @param stime
     * @param splace
     */
    public void createTravel(String ssn, String vehicle, String stime, String splace) {
        try {
            rp.createTravel(ssn, vehicle, stime, splace);

        } catch (Exception e) {
            Logger.getLogger(TrainServerDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * This method finds the previously created row for the correct customer and
     * updates the data with new content.
     *
     * @param ssn
     * @param etime
     * @param eplace
     * @param price
     */
    public void updateTravel(String ssn, String etime, String eplace, double price) {
        try {
            rp.updateTravel(ssn, etime, eplace, price);

        } catch (Exception e) {
            Logger.getLogger(TrainServerDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
