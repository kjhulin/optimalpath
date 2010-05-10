package OptimalPather;

/**
 * CS 6375 - Machine Learning
 * Dr. Liu
 * Homework 6 Question 4 - Programming Assignment
 * Optimal Policy and Q value iteration
 *
 * Language: Java 1.6.0_16
 * Compile with "javac Main.java"
 * Arguments:
 * None
 *
 * About the code:
 * Implementation of the provided pseudo code that uses Q-Learning in order
 * to learn the optimal policy in a grid, provided a goal space.  This
 * program defines a grid as an "Optimal Policy" object, requiring arguments
 * for goal location(s), grid size, and number of iterations.
 * Learning rate values alpha and gamma are both hard coded at 0.9, however
 * these values can be adjusted as the developer likes.
 *
 * Note: Grid positions are 0 indexed, positive, and (0,0) is in the top
 * left corner (keeping with java programming convention)
 *
 * Going Further:
 * After getting the code working to the specs of the problem described in
 * the homework, I wanted to explore the algorithms ability to accurately
 * produce optimal policies in a more complex grid.  I extended my program
 * to allow for multiple "goal" spaces (terminal squares with positive
 * reward values) and multiple "penalty" spaces (terminal squares with
 * negative reward values).  As would be expected, the program directs
 * traffic with higher reach to the higher reward squares and completely
 * avoids penalty squares.
 */

import java.util.*;
import java.text.*;
import java.lang.Math;
import OptimalPather.Policy.*;

/**
 *
 * @author Kevin Hulin and Eric Martin
 */




public class RLPolicy extends Policy{


    /**
     * @param args the command line arguments
     */
    public RLPolicy(ArrayList<point> p, int xx, int yy, int iters) {
        sizeX = xx;
        sizeY = yy;
        iterations = iters;
        Q = new State[sizeX][sizeY];
        goals = p;
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                Q[i][j] = new State(i,j);
            }
        }
        

    }
    public void computePolicy() {
        int cx = 0;
        int cy = 0;

        Random rand = new Random();
        for(int t = 0; t < iterations; t++){
          // System.out.println(iter + ":" + cx + "," + cy);
          alpha = 1/(double)(t+1);
            double r = rand.nextDouble();
            Action a = null;
            if(r <= .25){
                if(cx==0) continue;
                else
                   a = Action.LEFT;
            }else if(r <= .5){
                if(cx==sizeX-1) continue;
                else
                    a = Action.RIGHT;
            }else if(r <= .75){
                if(cy==0) continue;
                else
                    a = Action.UP;
            }else if(r <= 1){
                if(cy==sizeY-1) continue;
                else
                   a = Action.DOWN;
            }else{
                System.out.println("ERROR");
            }

            int nx = cx + a.dx;
            int ny = cy + a.dy;

            double maxQ = 0;
            double reward = 0;

            point pp;
            if((pp=isGoal(nx,ny))!=null){
                reward = pp.reward;
                maxQ = 0;
            }else{
                reward = 0;
                maxQ = Q[nx][ny].maxReward();
            }
            Q[cx][cy].reward.put(a, (1-alpha)*Q[cx][cy].reward.get(a) + alpha * (reward + gamma * maxQ));

            if(isGoal(nx,ny) != null) continue;

            cx = nx;
            cy = ny;
        }

    }


   
}



