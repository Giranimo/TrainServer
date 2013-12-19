package RMI;

import java.rmi.*;
import java.util.ArrayList;

/**
 * IRemoteProvince interface
 *
 * @author Sune Heide
 */
public interface IRemoteMySQL extends Remote {
    //applet

    public int save(MySQL p) throws RemoteException;

    public int update(MySQL p) throws RemoteException;

    public int delete(String ssn) throws RemoteException;

    public ArrayList<ArrayList<String>> findAll(String table) throws RemoteException;

    public ArrayList tableCustomerSearch(String searchText) throws RemoteException;

    public ArrayList tableTravelsSearch(String searchText) throws RemoteException;

    public ArrayList<String> columnNames(String table) throws RemoteException;

    public int login(String ssn, String password) throws RemoteException;

    public int userEditUser(MySQL p) throws RemoteException;

    public int registerCreateUser(MySQL p) throws RemoteException;
    //CalcServer

    public String getRoute(String first, String end) throws RemoteException;

    public double getPrice(String first, String end) throws RemoteException;
    //TrainServer

    public boolean loginPDA(String ssn, String password) throws RemoteException;

    public void createTravel(String ssn, String vehicle, String stime, String splace) throws RemoteException;

    public void updateTravel(String ssn, String etime, String eplace, double price) throws RemoteException;
}
