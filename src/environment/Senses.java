package environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector3d;

import org.apache.commons.lang3.RandomStringUtils;

import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.SimpleCartesianAdder;

/**
 * Senses provide all the information about the model to the brain
 * Operates all the setters on the brain class
 * @author user
 *
 */

public class Senses {

	//Current player
	private Player player;
	
	//Various object lists to be sent to the brain as vision
	private List<SensesObject> players;
	private List<SensesObject> tryline;
	private List<SensesObject> sidelines;
	private List<SensesObject> balls;
	
	//The full vision, central (depth perceiving) vision and sideVision angles (in degrees NOT radians)
	Double fullVision = 190.00;
	Double depthVision = 114.00;
	Double sideVision = (fullVision - depthVision)/2;
	
	//The central headings of the depth vision, the left side vision and the right side vision
	Double noseHeading;
	Double leftSideVisionHeading;
	Double rightSideVisionHeading;
	
	Message lastMessage;
	double hearingRadius = 300;
	
	Senses(Player player){
		this.player = player; 
	}
	
	
	protected void init(){
		setHeadings();
		getObjectLists();
		setObjectLists();
		player.brain.setSpace(makeSpace());
		player.brain.setPosition(getPosition());
		player.brain.setPlayer(player);
		player.brain.setMaxSpeed(getMaxSpeed());
		player.brain.setHasBall(getHasBall());
	}
	
	protected void step(){
		hear();
		setHeadings();
		getObjectLists();
		setObjectLists();
		player.brain.setPosition(getPosition());
		player.brain.setHasBall(getHasBall());
	}
	

	public void hear(){
		Message message = getMessage();
		if(getMessage()!=lastMessage){
			lastMessage = message;
			if(message.sender instanceof Referee){
				Message mess = new Message(true, message.getMessage());
				player.brain.setLastMessage(mess);
			} else {
				Player sender = (Player)message.sender;
				if(sender!=player){
					Vector3d vectorToMessage = getVector(sender.movement.currentPosition, player.movement.currentPosition);
					if(vectorToMessage.length()<=hearingRadius){
						Double angle = getAngle(vectorToMessage);
						Message mess = new Message(false, message.getMessage(), angle);
						player.brain.setLastMessage(mess);
					}
				}
			}
		}
	}
	
	/**
	 * Takes two ndpoints and returns vector to the target
	 * @param referencePoint
	 * @param targetPoint
	 * @return
	 */
	public Vector3d getVector(Vector3d targetPoint, Vector3d referencePoint){
		Vector3d vectorToTarget = new Vector3d();
		vectorToTarget.sub(targetPoint, referencePoint);
		return vectorToTarget;
	}
	
	/**
	 * Takes a vector and returns its angle in radians
	 * @param referencePoint
	 * @param targetPoint
	 * @return
	 */
	public Double getAngle(Vector3d vectorToTarget){
		Vector3d xAxis = new Vector3d(1.0,0.0,0.0);
		double heading = xAxis.angle(vectorToTarget);
		if(vectorToTarget.y<0){
			heading = 2*Math.PI - heading;
		}
		return heading;
	}
	
	public Message getMessage(){
		Message message = lastMessage;
			Iterator<Object> itera = player.context.getObjects(MessageBoard.class).iterator();
			if(itera.hasNext()){
				MessageBoard mb = (MessageBoard)itera.next();
				if(mb.getLastMessage()!=null){
					message = mb.getLastMessage();
				}
			}
		return message;
	}
	
	/**
	 * Returns a list that represents the players in view and their present(static) positions
	 * sets the list as an internal variable
	 * At this stage returns all points in context, needs work
	 * @return
	 */
	public List<SensesObject> getPlayers(){
		List<SensesObject> fresh = new ArrayList<>();
		Iterator<Object> it = player.context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			if(!this.player.equals(player)){
				createView(player, fresh);
			}
		}
		players = fresh;
		return fresh;
	}
	
	/**
	 * For a given object, if in the players view, creates a new senses object and adds to the arraylist
	 * Importantly, the senses object is made to ensure any locations passed to the brain are NOT dynamic
	 * @param o - the simpleagent being viewed
	 * @param list - the list of senses objects of the same agent class
	 */
	public void createView(Object o, List<SensesObject> list){
		
		//Convert the simple agent to the relevant agent type
		SimpleAgent agent;
		if(o instanceof Player)agent = (Player)o;
		else if(o instanceof Ball)agent = (Ball)o;
		else if(o instanceof SidePoint)agent = (SidePoint)o;
		else if(o instanceof TryPoint)agent = (TryPoint)o;
		else agent = null;
		
		//Ensure the agent is not the curent player
		if(o!=player){
			
			//If the agent to be seen is completely unobstructed 
			if(!isObstructed(agent)){
				
				//if the agent is within the depth vision of the player
				if(inView(agent, depthVision, noseHeading)){
					//Create depth Senses object
					list.add(new SensesObject(agent,agent.currentPosition));
				}
				
				//if the agent to be seen is within the right side vision field of the player
				if(inView(agent, sideVision, leftSideVisionHeading)){
					//Create non-depth Senses object
					list.add(new SensesObject(agent, leftSideVisionHeading));
				}
				
				//if the agent to be seen is within the left side vision field of the player
				if(inView(agent, sideVision, rightSideVisionHeading)){
					//Create non-depth Senses object
					list.add(new SensesObject(agent, rightSideVisionHeading));
				}
			}
		}
	}

	/**
	 * Checks if the current player has the ball
	 * @return true if the player has the ball
	 */
	public boolean getHasBall(){
		
		//Iterate through the ball objects
		Iterator<Object> it = player.context.getObjects(Ball.class).iterator();
		while(it.hasNext()){
			Ball ball = (Ball)it.next();
			Vector3d vectorToBall = new Vector3d();
			
			//Get the vector from the current player to the ball
			vectorToBall.sub(ball.movement.currentPosition, player.movement.currentPosition);
			
			//If the absolute distance of the vector is less than the body radius and ball radius
			if(Math.abs(vectorToBall.length())<=(Integer)player.params.getValue("body_radius")+(Integer)player.params.getValue("ball_radius")){
				//And if the balls player is either null or the current player - return true
				//This stops error when two players come together
				if(ball.player==null||ball.player.equals(player)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Creates the various object lists to be sent to the brain
	 */
	public void getObjectLists(){
		tryline = getObjectsInView(TryPoint.class);
		players = getObjectsInView(Player.class);
		//sidelines = getObjectsInView(SidePoint.class);
		balls = getObjectsInView(Ball.class);
	}
	
	/**
	 * Sends the various object lists to the brain
	 */
	public void setObjectLists(){
		player.brain.setTryline(tryline);
		player.brain.setPlayers(players);
		//player.brain.setSidelines(sidelines);
		player.brain.setBalls(balls);
	}
	
	/**
	 * Returns a list of all the objects of the given class currently in the players view
	 * @param a - is the class of agent. All agents of this class within view will be returned
	 * @return arraylist of senses objects.
	 */
	public List<SensesObject> getObjectsInView(Class a){
		List<SensesObject> fresh = new ArrayList<>();
		
		//Iterate through each object
		Iterator<Object> it = player.context.getObjects(a).iterator();
		while(it.hasNext()){
			Object agent = it.next();	
			
			//Pass the agent and the list to the createView method
			createView(agent, fresh);
		}
		return fresh;
	}
	
	/**
	 * Sets the respective vision heading variables. Based on the current heading, full vision angle and depth vision angle
	 */
	public void setHeadings(){
		//The central headings of the respective visions
		noseHeading = player.movement.currentHeadAngle;
		if(noseHeading>2*Math.PI) noseHeading=noseHeading-2*Math.PI;
		else if(noseHeading<0) noseHeading=2*Math.PI+noseHeading;
		leftSideVisionHeading = noseHeading + ((depthVision*0.0174533)/2)+((sideVision*0.0174533)/2);
		if(leftSideVisionHeading>2*Math.PI) leftSideVisionHeading=leftSideVisionHeading-2*Math.PI;
		rightSideVisionHeading = noseHeading - ((depthVision*0.0174533)/2)-((sideVision*0.0174533)/2);
		if(rightSideVisionHeading<0) rightSideVisionHeading=2*Math.PI+rightSideVisionHeading;
	}
	
	/**
	* Creates the internal projection space to be used by the brain
	 * @return
	 */
	public ContinuousSpace<Object> makeSpace(){
		String random = RandomStringUtils.randomAlphanumeric(17).toUpperCase();
		ContinuousSpaceFactory spaceFactory = 
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace(random, player.context, 
						new SimpleCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.StrictBorders(),
						(Integer)player.params.getValue("display_width"), (Integer)player.params.getValue("display_height"));
		return space;
	}
	
	
	/**
	 * Checks if an agent to be seen is obstructed from the current players view
	 * @param agent - the agent to be viewed
	 * @return true if obstructed and false if not obstructed
	 */
	public boolean isObstructed(SimpleAgent agent){
		//Iterate through potential obstructors in the environment (the players)
		Iterator<Object> it = player.context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player obstructor = (Player)it.next();
			
			//If the player is not the current player
			if(obstructor!=player){
				double obstructorRadius = (Integer)player.params.getValue("body_radius");
				double distToObstructor = Math.abs(player.space.getDistance(player.currentPosition, obstructor.currentPosition));
				
				//Use the length to and radius of the obstruction to derive the obstruction angle in degrees of the object from the player
				double halfObstructionAngle = Math.atan((obstructorRadius)/distToObstructor);
				double fullObstructionAngle = halfObstructionAngle*2;
				double obstructionAngle = fullObstructionAngle * 57.2958;
				
				//Obtain the angle in radians from the player to the obstruction
				Vector3d xAxis = new Vector3d(1.0,0.0,0.0);
				Vector3d vectorToObstruction = new Vector3d();
				vectorToObstruction.sub(obstructor.movement.currentPosition, player.movement.currentPosition);
				double heading = xAxis.angle(vectorToObstruction);
				if(vectorToObstruction.y<0){
					heading = 2*Math.PI - heading;
				}
				
				//Pass the agent to be seen, the obstruction angle and the heading of the obstruction to the inView method
				if(inView(agent, obstructionAngle, heading)){
					
					//If the agent is within the obstruction zone
					//And the distance to the agent is greater than the dist to the obstruction
					//The agent is obstructed from view
					if(Math.abs(player.space.getDistance(player.currentPosition, agent.currentPosition))>distToObstructor){
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
	public boolean inView(SimpleAgent agent, Double angle, Double heading){
		
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
		double rightIntercept = player.currentPosition.getY()-(rightSlope*player.currentPosition.getX());
		double leftIntercept = player.currentPosition.getY()-(leftSlope*player.currentPosition.getX());
		
		//based on the agents x value, gets the corresponding y values on each line
		double rightLineY = (agent.currentPosition.getX()*rightSlope)+rightIntercept;
		double leftLineY = (agent.currentPosition.getX()*leftSlope)+leftIntercept;

		//The y value of the agent to be viewed
		double agentY = agent.currentPosition.getY();
		
		//Used to return agents within views of ranges between 90 and 180 degrees
		if(fieldOfVisionAngle>90&&fieldOfVisionAngle<=180){
			if(headingNorth(heading)){
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
					if(headingWest(heading)){
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
					if(headingWest(heading)){
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
			if(headingNorth(heading)){
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
					if(headingWest(heading)){
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
					if(headingWest(heading)){
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
						if(headingNorth(heading)){
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
						if(headingWest(heading)){
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
	
	/**
	 * Determines whether the given heading is west (right)
	 * @param heading - the heading in radians (NOT degrees)
	 * @return true if the heading is left(west) and false if the heading is right(east)
	 */
	public boolean headingWest(Double heading){
		if(heading>=Math.PI*.5&&heading<=Math.PI*1.5){
			return true;
		}
		return false;
	}	
		
	
	/**
	 * Determines whether the given heading is up (north)
	 * @param heading - the heading in radians (NOT degrees)
	 * @return true if the heading is up(north) and false if the heading is down(south)
	 */
	public boolean headingNorth(Double heading){
		if(heading<Math.PI){
			return true;
		}
		return false;
	}

	
	/**
	 * Returns the position of a simple agent object only if it is in the list variable
	 * @param o
	 * @return
	 */
	public NdPoint getPosition(SimpleAgent o){
		if(o instanceof Player){
			if(players.contains((Player)o)){
				return o.getPosition();
			}
		}
		if(o instanceof TryPoint){
			if(tryline.contains((TryPoint)o)){
				return o.getPosition();
			}
		}
			return null;
	}
	
	public NdPoint getPosition(){
		return player.getPosition();
	}

	public int getMaxSpeed(){
		if(player instanceof Attacker){
			return (Integer)player.params.getValue("attacker_speed");
		}
		if(player instanceof Defender){
			return (Integer)player.params.getValue("defender_speed");
		}
		return 1;		
	}
	
}
