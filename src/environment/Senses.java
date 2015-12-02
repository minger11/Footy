package environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector3d;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

/**
 * Senses provide all the information about the model to the brain
 * Operates all the setters on the brain class
 * @author user
 *
 */

public final class Senses {

	private static Parameters params = Utils.getParams();
	
	//Current player
	private static  Player player;
	
	//The full vision, central (depth perceiving) vision and sideVision angles (in degrees NOT radians)
	private static  Double fullVision = 190.00;
	private static  Double depthVision = 114.00;
	private static  Double sideVision = (fullVision - depthVision)/2;
	
	//The central headings of the depth vision, the left side vision and the right side vision
	private static  Double noseHeading;
	private static  Double leftSideVisionHeading;
	private static  Double rightSideVisionHeading;

	private static  double hearingRadius = 300;
	
	
	private Senses(){
	}
	
	
	public static void init(Player p){
		player = p;
		info();
		eyes();
		ears();
		touch();
	}
	
	
	public static void step(Player p){
		player = p;
		eyes();
		ears();
		touch();
	}
	

	//-----------INFO---------------------------------------------------------------------------------//
	
		/**
	 * Everything to do with inherent knowledge
	 */
	private static void info(){
		player.getBrain().setPlayer(player);
		player.getBrain().setMaxSpeed(getMaxSpeed());
		//energy
		//playernumber
		//how many a side
		//how many balls
		//etc
	}
		
	private static int getMaxSpeed(){
			if(player instanceof Easterner){
				return (Integer)params.getValue("attacker_speed");
			}
			if(player instanceof Westerner){
				return (Integer)params.getValue("defender_speed");
			}
			return 1;		
		}

		
		
	//---------------EYES-----------------------------------------------------------------//
	
	/**
	 * Everything to do with vision
	 */
	private static void eyes(){
		setHeadings();
		sendView();
		sendRotations();
	}
	
	private static void sendRotations(){
		player.getBrain().setNoseHeading(noseHeading);
		player.getBrain().setBodyRotation(Utils.absoluteToRelative(player.getRotation(), noseHeading));
	}
	
	/**
	 * Sets the respective vision heading variables. Based on the current heading, full vision angle and depth vision angle
	 */
	private static void setHeadings(){
		//The central headings of the respective visions
		noseHeading = player.getHead().getRotation();
		if(noseHeading>2*Math.PI) noseHeading=noseHeading-2*Math.PI;
		else if(noseHeading<0) noseHeading=2*Math.PI+noseHeading;
		leftSideVisionHeading = noseHeading + ((depthVision*0.0174533)/2)+((sideVision*0.0174533)/2);
		if(leftSideVisionHeading>2*Math.PI) leftSideVisionHeading=leftSideVisionHeading-2*Math.PI;
		rightSideVisionHeading = noseHeading - ((depthVision*0.0174533)/2)-((sideVision*0.0174533)/2);
		if(rightSideVisionHeading<0) rightSideVisionHeading=2*Math.PI+rightSideVisionHeading;
	}
	
	/**
	 * Sends the various object lists to the brain
	 */
	private static void sendView(){
		player.getBrain().setWestTryline(getObjectsInView(WestTryPoint.class));
		player.getBrain().setEastTryline(getObjectsInView(EastTryPoint.class));
		player.getBrain().setPlayers(getObjectsInView(Player.class));
		player.getBrain().setSidelines(getObjectsInView(SidePoint.class));
		player.getBrain().setBalls(getObjectsInView(Ball.class));
	}
	
	/**
	 * Returns a list of all the objects of the given class currently in the players view
	 * @param a - is the class of agent. All agents of this class within view will be returned
	 * @return arraylist of senses objects.
	 */
	private static List<SensesObject> getObjectsInView(Class a){
		List<SensesObject> fresh = new ArrayList<>();
		
		//Iterate through each object
		Iterator<Object> it = player.getContext().getObjects(a).iterator();
		while(it.hasNext()){
			Object agent = it.next();	
			
			//Pass the agent and the list to the createView method
			createView(agent, fresh);
		}
		return fresh;
	}
	
	/**
	 * For a given object, if in the players view, creates a new senses object and adds to the arraylist
	 * Importantly, the senses object is made to ensure any locations passed to the brain are NOT dynamic
	 * @param o - the simpleagent being viewed
	 * @param list - the list of senses objects of the same agent class
	 */
	private static void createView(Object o, List<SensesObject> list){
		
		//Convert the simple agent to the relevant agent type
		SimpleAgent agent;
		if(o instanceof Player)agent = (Player)o;
		else if(o instanceof Ball)agent = (Ball)o;
		else if(o instanceof SidePoint)agent = (SidePoint)o;
		else if(o instanceof WestTryPoint)agent = (WestTryPoint)o;
		else if(o instanceof EastTryPoint)agent = (EastTryPoint)o;
		else agent = null;
		
		//Ensure the agent is not the curent player
		if(o!=player){
			
			//If the agent to be seen is completely unobstructed 
			if(!isObstructed(agent)){
				
				//if the agent is within the depth vision of the player
				if(inView(agent, depthVision, noseHeading)){
					
					//Create depth Senses object
					list.add(new SensesObject(agent,Utils.getVector(agent.getPositionPoint(), player.getPositionPoint()), player, true));
				}
				
				//if the agent to be seen is within the right side vision field of the player
				if(inView(agent, sideVision, leftSideVisionHeading)){
					//Create non-depth Senses object
					list.add(new SensesObject(agent, player, leftSideVisionHeading));
				}
				
				//if the agent to be seen is within the left side vision field of the player
				if(inView(agent, sideVision, rightSideVisionHeading)){
					//Create non-depth Senses object
					list.add(new SensesObject(agent, player, rightSideVisionHeading));
				}
			}
		}
	}
	
	/**
	 * Checks if an agent to be seen is obstructed from the current players view
	 * @param agent - the agent to be viewed
	 * @return true if obstructed and false if not obstructed
	 */
	private static boolean isObstructed(SimpleAgent agent){
		//Iterate through potential obstructors in the environment (the players)
		Iterator<Object> it = player.getContext().getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player obstructor = (Player)it.next();
			
			//If the player is not the current player
			if(obstructor!=player){
				double obstructorRadius = (Integer)params.getValue("body_radius");
				double distToObstructor = Math.abs(player.getSpace().getDistance(player.getPositionPoint(), obstructor.getPositionPoint()));
				
				//Use the length to and radius of the obstruction to derive the obstruction angle in degrees of the object from the player
				double halfObstructionAngle = Math.atan((obstructorRadius)/distToObstructor);
				double fullObstructionAngle = halfObstructionAngle*2;
				double obstructionAngle = fullObstructionAngle * 57.2958;
				
				//Obtain the angle in radians from the player to the obstruction
				Vector3d xAxis = new Vector3d(1.0,0.0,0.0);
				Vector3d vectorToObstruction = Utils.getVector(obstructor.getPositionPoint(), player.getPositionPoint());
				double heading = xAxis.angle(vectorToObstruction);
				if(vectorToObstruction.y<0){
					heading = 2*Math.PI - heading;
				}
				
				//Pass the agent to be seen, the obstruction angle and the heading of the obstruction to the inView method
				if(inView(agent, obstructionAngle, heading)){
					
					//If the agent is within the obstruction zone
					//And the distance to the agent is greater than the dist to the obstruction
					//The agent is obstructed from view
					if(Math.abs(player.getSpace().getDistance(player.getPositionPoint(), agent.getPositionPoint()))>distToObstructor){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if an agent is within a viewing angle based on the viewing heading and the current players position
	 * @param agent - the agent to be viewed
	 * @param angle - the viewing angle (in degrees NOT radians)
	 * @param heading - the direction (centre line) of the view (in radians NOT degrees)
	 * @return true if the agent is within the player view, false if not
	 */
	private static boolean inView(SimpleAgent agent, Double angle, Double heading){
		
		//Total vision angle
		double fieldOfVisionAngle = angle;
		double halfVision = fieldOfVisionAngle/2;
		
		//Half vision in radians
		double headToVisionDiff = halfVision*0.0174533;
		
		//Normalizes a radian angle for the right side of vision based on the head angle
		double rightSide = heading-headToVisionDiff;
		if(rightSide<0) rightSide=2*Math.PI+rightSide;
		//Normalizes a radian angle for the left side of vision based on the head angle
		double leftSide = heading+headToVisionDiff;
		if(leftSide>2*Math.PI) leftSide=leftSide-2*Math.PI;
		
		//Converts the radian angles to gradients
		double rightSlope = Math.tan(rightSide);
		double leftSlope = Math.tan(leftSide);
		
		//gets the y intercept of both lines
		double rightIntercept = player.getPositionPoint().getY()-(rightSlope*player.getPositionPoint().getX());
		double leftIntercept = player.getPositionPoint().getY()-(leftSlope*player.getPositionPoint().getX());
		
		//based on the agents x value, gets the corresponding y values on each line
		double rightLineY = (agent.getPositionPoint().getX()*rightSlope)+rightIntercept;
		double leftLineY = (agent.getPositionPoint().getX()*leftSlope)+leftIntercept;

		//The y value of the agent to be viewed
		double agentY = agent.getPositionPoint().getY();
		
		//Used to return agents within views of ranges between 90 and 180 degrees
		if(fieldOfVisionAngle>90&&fieldOfVisionAngle<=180){
			if(Utils.headingNorth(heading)){
				if(leftSlope<=0&&rightSlope>=0){
					if(agentY>=rightLineY&&agentY>=leftLineY){
						return true;
					}
				} else if(leftSlope<=0&&rightSlope<=0){
					if(agentY>=rightLineY&&agentY>=leftLineY){
						return true;
					}
				} else if(leftSlope>=0&&rightSlope>=0){
					if(agentY>=rightLineY&&agentY>=leftLineY){
						return true;
					}
				} else {
					if(Utils.headingWest(heading)){
						if(agentY<=rightLineY&&agentY>=leftLineY){
							return true;
						}
					} else {
						if(agentY>=rightLineY&&agentY<=leftLineY){
							return true;
						}
					}
				}
			} else {
				if(leftSlope<=0&&rightSlope>=0){
					if(agentY<=rightLineY&&agentY<=leftLineY){
						return true;
					}
				} else if(leftSlope<=0&&rightSlope<=0){
					if(agentY<=rightLineY&&agentY<=leftLineY){
						return true;
					}
				} else if(leftSlope>=0&&rightSlope>=0){
					if(agentY<=rightLineY&&agentY<=leftLineY){
						return true;
					}
				}else {
					if(Utils.headingWest(heading)){
						if(agentY<=rightLineY&&agentY>=leftLineY){
							return true;
						}
					} else {
						if(agentY>=rightLineY&&agentY<=leftLineY){
							return true;
						}
					}
				}
			}
			//Used to return agents within view within a range of 0 to 90 degrees
		} else if(fieldOfVisionAngle>=0&&fieldOfVisionAngle<=90){
			if(Utils.headingNorth(heading)){
				if(leftSlope<=0&&rightSlope>=0){
					if(agentY>=rightLineY&&agentY>=leftLineY){
						return true;
					}
				} else if(leftSlope<=0&&rightSlope<=0){
					if(agentY<=rightLineY&&agentY>=leftLineY){
						return true;
					}
				} else if(leftSlope>=0&&rightSlope>=0){
					if(agentY>=rightLineY&&agentY<=leftLineY){
						return true;
					}
				} else {
					if(Utils.headingWest(heading)){
						if(agentY<=rightLineY&&agentY>=leftLineY){
							return true;
						}
					} else {
						if(agentY>=rightLineY&&agentY<=leftLineY){
							return true;
						}
					}
				}
			} else {
				if(leftSlope<=0&&rightSlope>=0){
					if(agentY<=rightLineY&&agentY<=leftLineY){
						return true;
					}
				} else if(leftSlope<=0&&rightSlope<=0){
					if(agentY>=rightLineY&&agentY<=leftLineY){
						return true;
					}
				} else if(leftSlope>=0&&rightSlope>=0){
					if(agentY<=rightLineY&&agentY>=leftLineY){
						return true;
					}
				}else {
					if(Utils.headingWest(heading)){
						if(agentY<=rightLineY&&agentY>=leftLineY){
							return true;
						}
					} else {
						if(agentY>=rightLineY&&agentY<=leftLineY){
							return true;
						}
					}
				}
			}
			//Used to return all agents within viewing range of an angle between 180 and 270 degrees
		} else if(fieldOfVisionAngle>180&&fieldOfVisionAngle<270){
					if((leftSlope<=0&&rightSlope<=0)||(leftSlope>=0&&rightSlope>=0)){
						if(Utils.headingNorth(heading)){
							if(agentY>=rightLineY||agentY>=leftLineY){
								return true;
							}
						} else {
							if(agentY<=rightLineY||agentY<=leftLineY){
								return true;
							}
						}
					}
					if((leftSlope<=0&&rightSlope>=0)||(leftSlope>=0&&rightSlope<=0)){
						if(Utils.headingWest(heading)){
							if(agentY>=rightLineY||agentY<=leftLineY){
								return true;
							}
						} else {
							if(agentY<=rightLineY||agentY>=leftLineY){
								return true;
							}
						}
					}
		}
		return false;
	}
	
	
	
	//-------------EARS-----------------------------------------------------------------------//
	
	/**
	 * Everything to do with hearing
	 */
	private static void ears(){
		hear();
	}
	
	/**
	 * Checks if new messages have hit the messageboard, recreates and sends the most recent message to the brain
	 */
	private static void hear(){
		
		//If the most recent messageboard message is not the same as the last one processed
		if(MessageBoard.getNewMessage()){
			
			Message message = MessageBoard.getLastMessage();		
			
			//If the message is from the referee, create an official message
			if(message.getOfficial()){
				
				//Put the message into a senses object
				SensesObject lastMess = new SensesObject(message);
				
				//Send the sensesobject to the brain
				player.getBrain().setMessage(lastMess);
				
				//Otherwise create a vector to message
			} else {
				Player sender = (Player)message.getSender();
				if(sender!=player){
					Vector3d vectorToMessage = Utils.getVector(sender.getPositionPoint(), player.getPositionPoint());
					
					//If the length of the vector is less than the hearing radius
					if(vectorToMessage.length()<=hearingRadius){

						//Create a new unofficial message
						Message mess = new Message(false, message.getMessage());
						
						//Put the message into a new senses object
						SensesObject lastMess = new SensesObject(mess, vectorToMessage, player, false);
						
						//Send the sensesobject to the brain
						player.getBrain().setMessage(lastMess);
					}
				}
			}
		}
	}
	
	
	//-----------TOUCH--------------------------------------------------------------------------------//
	
	/**
	 * Everything to do with touch
	 */
	private static void touch(){
		player.getBrain().setHasBall(getHasBall());
	}

	/**
	 * Checks if the current player has the ball
	 * @return true if the player has the ball
	 */
	private static boolean getHasBall(){
		
		//Iterate through the ball objects
		Iterator<Object> it = player.getContext().getObjects(Ball.class).iterator();
		while(it.hasNext()){
			Ball ball = (Ball)it.next();
			Vector3d vectorToBall = Utils.getVector(ball.getPositionPoint(), player.getPositionPoint());
			
			//If the absolute distance of the vector is less than the body radius and ball radius
			if(Math.abs(vectorToBall.length())<=(Integer)params.getValue("body_radius")+(Integer)params.getValue("ball_radius")){
				//And if the balls player is either null or the current player - return true
				//This stops error when two players come together
				if(ball.getPlayer()==null||ball.getPlayer().equals(player)){
					return true;
				}
			}
		}
		return false;
	}
	
}
