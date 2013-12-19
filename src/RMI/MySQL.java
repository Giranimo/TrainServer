package RMI;

import java.io.*;

/**
 * MySQL class implements Serialable to support mashalling
 *
 * @author Sune Heide
 */
public class MySQL implements Serializable {

    public int balance, admin;
    public String ssn, password, fname, sname, mail, phoneNumber, address;

    public MySQL() {
    }

    public MySQL(String cpr, String password, String fName, String sName, String mail, String address, int balance, int admin) {

        this.ssn = cpr;
        this.password = password;
        this.fname = fName;
        this.sname = sName;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.balance = balance;
        this.admin = admin;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getcpr() {
        return ssn;
    }

    public void setcpr(String cpr) {
        this.ssn = cpr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return fname;
    }

    public void setFirstName(String firstName) {
        this.fname = firstName;
    }

    public String getLastName() {
        return sname;
    }

    public void setLastName(String lastName) {
        this.sname = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
