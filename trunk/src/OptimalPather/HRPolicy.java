/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OptimalPather;
import java.util.*;
/**
 *
 * @author Kevin Hulin
 */
public class HRPolicy extends Policy {

    public HRPolicy(ArrayList<point> p, int xx, int yy, int iters){
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

    @Override
    public void computePolicy() {
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                Q[i][j].staticAction = computePolicy(i,j);
                Q[i][j].staticReward = 1.0;
                if(isInteresting(i,j)) Q[i][j].interesting = true;
            }
        }
    }
    public boolean isInteresting(int i, int j){
        Action a = Q[i][j].staticAction;
        if(Q[i+a.dx][j+a.dy].interesting) return true;
        State up; State down; State left; State right;
        if(i>0) left = Q[i-1][j]; else left = null;
        if(i<sizeX-1) right = Q[i+1][j]; else right = null;
        if(j>0) up = Q[i][j-1]; else up = null;
        if(j<sizeY-1) down = Q[i][j+1]; else down = null;
        if(up!=null&&isGoal(up.posX,up.posY)!=null)return true;
        if(down!=null&&isGoal(down.posX,down.posY)!=null)return true;
        if(left!=null&&isGoal(left.posX,left.posY)!=null)return true;
        if(right!=null&&isGoal(right.posX,right.posY)!=null)return true;
        return false;
    }
    public Action computePolicy(int i,int j){
        State up; State down; State left; State right;
        if(i>0) left = Q[i-1][j]; else left = null;
        if(i<sizeX-1) right = Q[i+1][j]; else right = null;
        if(j>0) up = Q[i][j-1]; else up = null;
        if(j<sizeY-1) down = Q[i][j+1]; else down = null;
        HashMap<State,Action> hm = new HashMap<State,Action>();


        hm.put(up,Action.UP); hm.put(down,Action.DOWN); hm.put(left,Action.LEFT); hm.put(right,Action.RIGHT);
        for(State s : hm.keySet()){
            if(s != null && isGoal(s.posX,s.posY)!=null)return hm.get(s);
        }
        for(State s : hm.keySet()){
            if(s != null && s.interesting) return hm.get(s);
        }
        if(up == null) return Action.DOWN;
        if(down == null) return Action.UP;
        if(left == null) return Action.RIGHT;
        if(right == null) return Action.LEFT;

        return Action.DOWN;


    }

}
