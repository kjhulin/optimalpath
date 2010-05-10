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
import java.io.*;
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            PrintStream os1 = new PrintStream(new FileOutputStream("out.txt"));
            //PrintStream os2 = new PrintStream(new FileOutputStream("out2.txt"));

        // manually set goal points
        Random rand = new Random();
        for(int i = 0; i < 1000; i++){
            ArrayList<point> goals = new ArrayList<point>();
            int width = rand.nextInt(5)+5;
            int height = rand.nextInt(5)+5;
            int numGoals = rand.nextInt(6)+1;
            for(int j = 0; j < numGoals; j++){
                goals.add(new point(rand.nextInt(width),rand.nextInt(height),rand.nextInt(8)*50+50,rand.nextInt(6)*10+10,false));
            }
            ArrayList<point> goals2 = new ArrayList<point>();
            ArrayList<point> goals3 = new ArrayList<point>();
            for(point p : goals){
                goals2.add(p.clone());
                goals3.add(p.clone());
            }
            ArrayList<Agent> ag = new ArrayList<Agent>();
            int numAgents = rand.nextInt(5)+2;
            for(int j = 0; j < numAgents; j++){
                ag.add(new Agent(new point(rand.nextInt(width),rand.nextInt(height),-1,0,true),10));       
            }
            ArrayList<Agent> ag2 = new ArrayList<Agent>();
            ArrayList<Agent> ag3 = new ArrayList<Agent>();
            for(Agent a : ag){
                ag2.add(a.clone());
                ag3.add(a.clone());
            }

            //System.out.println(width + "x" + height + "grid; numGoals:" + numGoals + "; numAgents:" + numAgents);
            ResourceCollector rc1 = new ResourceCollector(new RLPolicy(goals, width, height, 100000),ag);
            ResourceCollector rc2 = new ResourceCollector(new GAPolicy(goals2, width, height, 100000),ag2);
            ResourceCollector rc3 = new ResourceCollector(new HRPolicy(goals3, width, height, 100000),ag3);
            os1.println(width + "," + height + "," + numGoals + "," + numAgents + "," + rc1.run() +"," + rc2.run() + "," + rc3.run());

           
        }
        }catch(Exception e){System.err.println(e.toString());e.printStackTrace(System.err);}
        
        //new ResourceCollector();
    }

}
