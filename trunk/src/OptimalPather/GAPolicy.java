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
import java.text.*;

public class GAPolicy extends Policy{

    ArrayList<datum> population = null;
    datum bestSoFar = null;
    int popSize = 100;
    //static int sizeX = 10;
    //static int sizeY = 10;
    //ArrayList<point> goals = new ArrayList<point>();

    public GAPolicy(ArrayList<point> p, int xx, int yy, int iters){
        
        goals=p;
        sizeX = xx;
        sizeY = yy;
        iterations = iters;

        
       

    }
 
    public ArrayList<datum> initialize(int n){
        ArrayList<datum> ret = new ArrayList<datum>();
        for(int i = 0; i < n; i++){
            ret.add(datum.getRandom(sizeX, sizeY, goals));
        }
        return ret;
    }
    public datum breed(datum a, datum b){
        datum ret = new datum(sizeX, sizeY, goals);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                ret.dat[i][j] = new State(i,j);
                ret.dat[i][j].staticAction = mutate(a.dat[i][j].staticAction,b.dat[i][j].staticAction);
            }
        }
        return ret;
    }
    public datum breed2(datum a, datum b){
        datum ret = new datum(sizeX, sizeY, goals);
        Random rr = new Random();
        int aa = rr.nextInt(a.dat.length);

        for(int i = 0; i <sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                ret.dat[i][j] = new State(i,j);
                if(sizeY < aa){
                    ret.dat[i][j].staticAction = a.dat[i][j].staticAction;
                }else{
                    ret.dat[i][j].staticAction = b.dat[i][j].staticAction;
                }
            }
        }
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                int v = rr.nextInt(1000);
                if(v > 995){
                   switch(v){
                        case 996: ret.dat[i][j].staticAction= Action.LEFT;
                        case 997: ret.dat[i][j].staticAction= Action.RIGHT;
                        case 998: ret.dat[i][j].staticAction= Action.UP;
                        case 999: ret.dat[i][j].staticAction= Action.DOWN;
                    }
                }
            }
        }
        return ret;
    }
    public Action mutate(Action a, Action b){
        String r = "";
        Random rr = new Random();
        int i = rr.nextInt(100);
        if(i<48)
            return a;
        else if(i<96)
            return b;
        else{
            switch(i){
                case 96: return Action.LEFT;
                case 97: return Action.RIGHT;
                case 98: return Action.UP;
                case 99: return Action.DOWN;
            }
        }
        return null;

    }

    @Override
    public void computePolicy() {
        population = initialize(popSize);
        System.out.println("Created initial population");
        int maxCtr = 0;
        double lastMax = -1;
        int counter = 0;

        while(true){
            counter ++;
            maxCtr++;
            datum bestThisRound = Collections.max(population);
            if(bestSoFar==null||bestThisRound.fitness()>bestSoFar.fitness()){
                bestSoFar = bestThisRound;
            }
            double maxVal = bestThisRound.fitness();
            ArrayList<datum> newPop = new ArrayList<datum>();
            Random r = new Random();
            while(!population.isEmpty()){//Deterministic tournament selection
                ArrayList<datum> tmp = new ArrayList<datum>();
                for(int j = 0; !population.isEmpty()&&j < 8; j++){
                    tmp.add(population.remove(r.nextInt(population.size())));

                }
                newPop.add(Collections.max(tmp));
            }
            population = newPop;
            while(population.size()<popSize){
                //population.add(breed2(population.get(r.nextInt(population.size())),population.get(r.nextInt(population.size()))));
               population.add(breed(population.get(r.nextInt(population.size())),population.get(r.nextInt(population.size()))));
            }
            double t = bestSoFar.fitness();
            //.out.println(counter + ":" +t);
            if(t > lastMax){
                System.out.println(counter + ":" + t);
                //System.out.println(bestSoFar.toString());
                lastMax = t;
                maxCtr = 0;
            }
            if(maxCtr>sizeX*sizeY*10){
                break;
            }
        }

        System.out.println("Converged after " + counter + " iterations!");

        Q = bestSoFar.dat;
        System.out.println(toString());

    }

     static class datum implements Comparable {
         ArrayList<point> goals;
        //static String[] domain = new String[]{"<",">","v","^"};
        double gamma = .9;
        //HashMap<point,Double> vals = new HashMap<point,Double>();
        int sizeX;
        int sizeY;
        State[][] dat;
        double savedFitness = -999;
        public datum(int sizeX, int sizeY, ArrayList<point> g){
            goals = g;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            dat = new State[sizeX][sizeY];
        }

        public static datum getRandom(int sizeX, int sizeY, ArrayList<point> goals){

            Random rand = new Random();
            datum ret = new datum(sizeX, sizeY,goals);

            ret.dat = new State[sizeX][sizeY];
            for(int i = 0; i < sizeX; i++){
                for(int j = 0; j < sizeY; j++){
                    ret.dat[i][j]= new State(i,j);
                    ret.dat[i][j].staticAction=Action.values()[rand.nextInt(Action.values().length)];
                }
            }

            return ret;
        }


        /*
        public String printValues(){
            String s = "";
            for(int i = 0;i < sizeY; i++){
                for(int j = 0; j < sizeX; j++){
                    if(isGoal(new point(j,i))!=null)
                        s+="|  G  ";
                    else{
                        String t = "    " + df.format(dat[j][i].staticReward);
                        t = t.substring(t.length()-5);
                        s+="|"+ t ;

                    }
                }
                s+="\n";
            }
            return s;
        }
         * */

        public double fitness(){
            if(savedFitness!=-999){
                return savedFitness;
            }
            double ret = 0;
            for(int i = 0; i < sizeX; i++){
                for(int j = 0; j < sizeY; j++){
                    ret += calcFVal(i,j);
                }

            }
            savedFitness=ret;
            return ret;
        }
        public double calcFVal(int i, int j){
            
            LinkedList<State> v = new LinkedList<State>();
            State curr = dat[i][j];
            double penalty = -10;
  
            while(true){
                
                if(curr.staticReward!=null){
                    if(curr.staticReward==penalty){
                        while(!v.isEmpty()){
                            v.pop().staticReward = penalty;
                        }
                    }
                    else{
                        int count = 1;
                        while(!v.isEmpty()){
                            v.pop().staticReward = Math.pow(gamma,count)*curr.staticReward;
                            //System.out.print(df.format(dat[p.x][p.y].staticReward) + " " );
                            count++;

                        }
                        //System.out.println();
                    }    
                    return dat[i][j].staticReward;
                    
                }
                
                

                //System.out.println(curr.toString());
                
                v.push(curr);
                if(isGoal(curr.posX, curr.posY)!=null){
                    point p = isGoal(curr.posX,curr.posY);
                    double g = p.reward;
                    int count = 0;
                    while(!v.isEmpty()){
                        v.pop().staticReward = Math.pow(gamma,count)*g;
                       // System.out.println( dat[p.x][p.y].staticReward);
                        count++;
                    }
                    return dat[i][j].staticReward;
                }
                
                int dx = curr.staticAction.dx;
                int dy = curr.staticAction.dy;
                if(curr.posX+dx<0||curr.posX+dx>sizeX-1||curr.posY+dy<0||curr.posY+dy>sizeY-1){
                    while(!v.isEmpty()){
                        v.pop().staticReward = penalty;
                    }
                    return penalty;
                }
                State next = dat[curr.posX+dx][curr.posY+dy];

                if(v.contains(next)){
                    while(!v.isEmpty()){
                        v.pop().staticReward = penalty;
                    }
                    return penalty;
                }else{

                    curr = next;
                }
                
            }

        }

        public point isGoal(int a, int b){
            for(point p : goals){
                if(p.x==a&&p.y==b)
                    return p;
            }
            return null;
        }
        public int compareTo(Object o) {
            datum d = (datum)o;
            double v1 = fitness();
            double v2 = d.fitness();
            //System.out.println(v1 + " " + v2);
            return v1>v2?1:v1<v2?-1:0;
        }
        

    }
    

}



