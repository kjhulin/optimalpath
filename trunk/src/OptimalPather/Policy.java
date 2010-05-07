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
import java.text.*;
public abstract class Policy {
    public abstract void computePolicy();
    DecimalFormat df = new DecimalFormat("00.0");
    static double alpha = .9;
    double gamma =.9;
    int sizeX;
    int sizeY;
    int iterations;
    State[][] Q;

    ArrayList<point> goals = new ArrayList<point>();


    public State getQ(int x, int y) {
            return Q[x][y];
    }
    /**
         * Enumerator class for Actions.
         * Each action is described by a change in location and
         * corresponding string arrow representation (unicode arrows)
         * These actions are: LEFT, RIGHT, UP, and DOWN
         */
        public enum Action{
            LEFT (-1,0,"<"),
            RIGHT (1,0,">"),
            UP (0,-1,"^"),
            DOWN(0,1,"v");

            int dx;
            int dy;
            String dir = "NULL";
            Action(int a, int b, String s){
                dx = a;
                dy = b;
                dir = s;
            }
            public String toString(){
               return dir;
            }

        }
        /**
         * State class for storing each state.
         * A state is defined by a coordinate.  Each state has a set of
         * Q values, one for each valid action.
         *
         * Provides methods for retrieving attributes that correspond
         * to the best action for the state.
         */
        public static class State{
            boolean interesting = false;
            int posX;
            int posY;

            //For GA
            Action staticAction = null;
            Double staticReward = null;


            HashMap<Action,Double> reward = new HashMap<Action,Double>();
            public State(int x, int y){
                posX = x;
                posY = y;
                for(Action a : Action.values()){
                    reward.put(a,0.0);
                }

            }
            public Action maxAction(){
                if(staticAction!=null){return staticAction;}
                Action max = null;
                for(Action a : Action.values()){
                    if(max==null || reward.get(a)>reward.get(max)){
                        max = a;
                    }
                }
                return max;
            }
            public String maxString(){
                if(maxReward()!=0){
                    return maxAction().toString();
                }else{
                    return "-";
                }
            }
            public double maxReward(){
                if(staticAction!=null){return staticReward;}
                double max = 0;
                for(Action a : Action.values()){
                    if(max==0 || reward.get(a)>max){
                        max = reward.get(a);
                    }
                }
                return max;
            }
            public String toString(){
                return maxString();
            }
        }
        //Class for storing the location and associated reward for goal/penaly squares
    static class point{
        int x;
        int y;
        int reward;
        int amount;
        boolean agent;
        public point(int a, int b){x=a;y=b;}
        public point(int a, int b, int c){x=a;y=b;reward = c;}
        public point(int a, int b, int c, int d, boolean ag){
        	x=a;
        	y=b;
        	reward = c;
        	amount = d;
        	agent = ag;
        }
        public point clone(){
            point r = new point(x,y,reward,amount,agent);
            return r;
        }
        @Override
        public boolean equals(Object o){
                point p = (point)o;
                return p.x==x&&p.y==y;
        }

        public String toString() {
        	return "(" + x + ", " + y + ")";
        }

    }
     /**
     * Returns a string containing "c" repeated "t" times
     * @param c
     * @param t
     * @return
     */
    public String rep(String c, int t){
        String s = "";
        for(int i = 0; i < t; i++){s+=c;}
        return s;
    }

    public void printQs() {
        //Print Q-Values
        System.out.println("Q Values:");

         System.out.println(rep("-",sizeX*6+1));
        for(int i = 0; i < sizeY; i++){
            for(int j = 0; j < sizeX; j++){
                    if(isGoal(j,i)!=null){
                    System.out.print("| G ");
                }
                else if (isAgentHome(j,i) != null) {
                    System.out.print("| A ");
                } else{
                    String t = "    " + df.format(Q[j][i].maxReward());
                    t = t.substring(t.length()-5);
                    System.out.print("|"+ t );


                }
            }

            System.out.println("|");
            System.out.println(rep("-",sizeX*6+1));
        }
    }


    /**
     * Determines if a square is a goal space and returns the corresponding
     * point object if it is.  Returns null if it is not a goal space
     * @param a
     * @param b
     * @return
     */
    public point isGoal(int a, int b){
        for(point p : goals){
            if(p.x==a&&p.y==b && !p.agent){
                //System.out.println("AT GOAL");
                return p;
            }
        }
        return null;
    }
    public point isAgentHome(int a, int b) {
            for(point p : goals) {
                    if(p.x ==a && p.y == b && p.agent) {
                            return p;
                    }
            }
            return null;
    }
    public String toString(ArrayList<Agent> ag) {
            //Print the optimal policy using arrows to depict square traversal
            String s = "Optimal Policy:\n";
            s+=rep("-",sizeX*4+1)+"\n";
            for(int i = 0; i < sizeY; i++){
                for(int j = 0; j < sizeX; j++){
                    if(isGoal(j,i)!=null){
                        s+="| G ";
                    } else if (isAgent(j,i,ag)) {
                    	s+="| * ";
                    } else if (isAgentHome(j,i) != null) {
                    	s+="| A ";
                    } else{
                        s+="| "+Q[j][i].maxString() + " ";
                    }
                }

                s+="|\n";
                s+=rep("-",sizeX*4+1)+"\n";
            }
            return s;
        }
        boolean isAgent(int a, int b, ArrayList<Agent> agents) {
                    for(Agent ag : agents) {
                        
                            if(ag.location.x == a && ag.location.y == b) {
                                    return true;
                            }
                    }
                    return false;
            }
}
