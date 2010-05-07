/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OptimalPather;

/**
 *
 * @author Kevin Hulin
 */
import java.util.*;
import OptimalPather.Policy.*;
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // manually set goal points
        ArrayList<point> goals = new ArrayList<point>();
        goals.add(new point(0,0, 50, 50, false));
        goals.add(new point(4,4,100, 30, false));
        goals.add(new point(0,4,150, 20,false));
        goals.add(new point(4,0,200,10,false));
        ArrayList<point> goals2 = new ArrayList<point>();
        ArrayList<point> goals3 = new ArrayList<point>();
        for(point p : goals){
            goals2.add(p.clone());
            goals3.add(p.clone());
        }
        //ResourceCollector rc1 = new ResourceCollector(new RLPolicy(goals, 5, 5, 100000));
       // ResourceCollector rc2 = new ResourceCollector(new GAPolicy(goals2, 5, 5, 100000));
        ResourceCollector rc3 = new ResourceCollector(new HRPolicy(goals2, 5, 5, 100000));

       
        
        //new ResourceCollector();
    }

}
