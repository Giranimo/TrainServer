/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Kristian Jacobsen, Mikael Kofod
 */
/**
 * @param args the command line arguments
 */
public class BusSimulation implements ActionListener {

    String[] BusRoute = {"Gentofte", "Bagsværd", "Frederikssund", "Albertslund", "Vanløse"};
    int[] Delays = {9000, 9000, 9000, 9000, 9000};
    int position = 0;
    String BusNow = "Gentofte";
    String BusBefore;

    /**
     * @param args the command line arguments
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        BusBefore = BusNow;
        BusNow = BusRoute[position];
        position = (position + 1) % BusRoute.length;
        ((Timer) e.getSource()).setDelay(Delays[position]);
        System.out.println("from " + BusBefore + " to " + BusNow);
    }
}
