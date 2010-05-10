/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OptimalPather;

import OptimalPather.Policy.*;
import java.util.*;
class ResourceCollector {


    	Policy policy;
    	//ArrayList<point> goals = new ArrayList<point>();
    	ArrayList<Agent> agents = new ArrayList<Agent>();
    	//could pass any of the above as arguments for extensibility
    	public ResourceCollector (Policy ppp, ArrayList<Agent> al) {

                agents = al;
                policy = ppp;
    		
    	}
        public int run(){
            //add the location of agent home points with negative reward
    		addAgentPoints();


    		policy.computePolicy();
    		//System.out.println(policy.toString(agents));
    		//printAgentLocs();
                int count=0;
                int maxCount = 10000;
    		while(resourcesRemain()){
                    count++;
                        if(count>maxCount){
                        //    System.out.println("Maximum iterations exceeded.  Terminating.");
                            break;
                        }

    			//System.out.println("Moving agents...");
    			for(int i = 0; i < agents.size(); i++) {
    				Agent a = agents.get(i);
    				point newPoint = new point(a.location.x, a.location.y, 0, 0, false);

    				Action maxAction;

    				if(a.isFull)	{ //Carrying resources
    					if(a.isHome(newPoint)) {
    						agents.get(i).isFull = false;
    						//System.out.println("Agent " + i + " dropped off resources.");
    						// potentially keep track of what goes into the sink
    					} else {
    						agents.get(i).moveToHome();
    					}

    				} else { //Looking for resources
                                   // System.out.println(a.location.x + " " + a.location.y);
                                    try{
    					maxAction = policy.Q[a.location.x][a.location.y].maxAction();
                                        newPoint.x += maxAction.dx;
    					newPoint.y += maxAction.dy;
                                    }catch(Exception e){
                                        //for(Agent aaa : agents){
                                        //    System.out.println(aaa.toString());
                                        //}
                                        //System.out.println(policy.toString(new ArrayList<Agent>()));
                                        count = maxCount;
                                    }
    					
    					if(isGoal(newPoint)) {
    						agents.get(i).isFull = true;
    						//if empty, calculate policy
    						if(nomResources(newPoint, a.capacity)) { //this resource is expired
    							//System.out.println("Resource at " + newPoint.toString() + " is depleted. Recomputing policy");
    							if(policy instanceof GAPolicy){
                                                            policy = new GAPolicy(policy.goals, policy.sizeX, policy.sizeY, policy.iterations);
                                                        }else if(policy instanceof RLPolicy){
                                                            policy = new RLPolicy(policy.goals, policy.sizeX, policy.sizeY, policy.iterations);

                                                        }else if(policy instanceof HRPolicy){
                                                            policy = new HRPolicy(policy.goals, policy.sizeX, policy.sizeY, policy.iterations);

                                                        }
                                                        //System.out.println(policy.getClass().getName());
                                                        //policy =  policy.getClass().newInstance();
                                                        //policy
                                                         //       (policy.goals, policy.sizeX, policy.sizeY, policy.iterations);
                                                        //policy = policy.getClass();//  //easier than wiping the
    							policy.computePolicy();
    						}

    					} else {
    						agents.get(i).location = newPoint;
    					}
    				}

    			}
    			//System.out.println(policy.toString(agents));
    			//printAgentLocs();
    			//System.out.println("__________________");

                }
                return count;
        }
    	boolean isGoal(point x) {
            for(point p : policy.goals){
                if(p.x==x.x && p.y==x.y && !p.agent){
                    return true;
                }
            }
            return false;
    	}

    	boolean isAgent(int a, int b) {
    		for(Agent ag : agents) {
    			if(ag.location.x == a && ag.location.y == b) {
    				return true;
    			}
    		}
    		return false;
    	}

    	boolean nomResources(point loc, int cap) {
    		for(int i = 0; i < policy.goals.size(); i++) {
    			if(loc.x == policy.goals.get(i).x && loc.y == policy.goals.get(i).y) {
    				policy.goals.get(i).reward = policy.goals.get(i).reward - cap;
    				//System.out.println(policy.goals.get(i).reward + " remaining at " + policy.goals.get(i).toString());
    				if(policy.goals.get(i).reward <= 0) {
    					policy.goals.remove(i);
    					return true;
    				}
    			}
    		}

    		return false;
    	}

    	boolean resourcesRemain() {
               // System.out.println("Resources: " + policy.goals.size());
    		int sum = 0;

    		for(point a : policy.goals){
    			sum += a.amount;
    		}
    		if(sum != 0) {
    			return true;
    		} else {
    			return false;
    		}
    	}

    	void addAgentPoints() {
    		for(Agent a : agents) {
    			policy.goals.add(a.home);
    		}
    	}

        /*public void printPolicy() {
            //Print the optimal policy using arrows to depict square traversal
            System.out.println("Optimal Policy:");
            System.out.println(policy.rep("-",policy.sizeX*4+1));
            for(int i = 0; i < yMax; i++){
                for(int j = 0; j < xMax; j++){
                    if(isGoal(new point(j,i))){
                        System.out.print("| G ");
                    } else if (isAgent(j,i)) {
                    	System.out.print("| * ");
                    } else if (policy.isAgentHome(j,i) != null) {
                    	System.out.print("| A ");
                    } else{
                        System.out.print("| "+policy.Q[j][i].maxString() + " ");
                    }
                }

                System.out.println("|");
                System.out.println(policy.rep("-",xMax*4+1));
            }
        }*/

    	void printAgentLocs() {
    		for(int i = 0; i < agents.size(); i++) {
    			System.out.println("Agent " + i + " at " + agents.get(i).location.toString());
    		}
    	}
    }

    class Agent {
    	int capacity;
    	boolean isFull;
    	point home;
    	point location;

    	public Agent (point h, int c) {
    		home = h;
    		location = h;
    		capacity = c;
    		isFull = false;
    	}
        public String toString(){
            return "Home:" + home.toString() + " Location:" + location.toString();
        }
        public Agent clone(){
            return new Agent(home.clone(),capacity);
        }
    	int distFromHome() {
    		return (int)(Math.abs(home.x-location.x) + Math.abs(home.y-location.y));
    	}

    	void moveToHome() {

    		if (location.x > home.x) {
    			location.x--;
    			return;
    		} else if ( location.x < home.x) {
    			location.x++;
    			return;
    		}

    		if(location.y > home.y) {
    			location.y--;
    			return;
    		} else if (location.y < home.y) {
    			location.y++;
    			return;
    		}

    	}

    	boolean isHome(point test) {
    		if(test.x == home.x && test.y == home.y) { return true;}
    		return false;
    	}

    }
