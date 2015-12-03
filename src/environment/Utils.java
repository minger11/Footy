package environment;

import java.util.Iterator;

import javax.vecmath.Vector3d;

import repast.simphony.space.continuous.NdPoint;

/**
 * A utility class. A toolbox for common operations across the model
 * @author user
 *
 */

public final class Utils {

	private Utils(){
		
	}
	
	/**
	 * Determines whether the given heading is west (right)
	 * @param heading - the heading in radians (NOT degrees)
	 * @return true if the heading is left(west) and false if the heading is right(east)
	 */
	public static boolean headingWest(Double heading){
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
	public static boolean headingNorth(Double heading){
		if(heading<Math.PI){
			return true;
		}
		return false;
	}
	
	/**
	 * Takes two vectors and returns vector to the target
	 * @param referencePoint
	 * @param targetPoint
	 * @return
	 */
	public static Vector3d getVector(Vector3d targetPoint, Vector3d referencePoint){
		Vector3d vectorToTarget = new Vector3d();
		vectorToTarget.sub(targetPoint, referencePoint);
		return vectorToTarget;
	}
	
	/**
	 * Checks an angle to ensure it is witin the bounds of a radian, updates if neccessary
	 * @param angle - the angle to be checked
	 * @return the updated angle
	 */
	public static double checkRadian(double angle){
		
		//If the angle is greater than the radian limit
		if(angle>2*Math.PI) angle=angle-2*Math.PI;
		
		//If the angle is less than the radian limit
		else if(angle<0) angle=2*Math.PI+angle;
		
		return angle;
	}
	
	/**
	 * Takes a vector and returns its angle in radians
	 * @param referencePoint
	 * @param targetPoint
	 * @return
	 */
	public static double getAngle(Vector3d vectorToTarget){
		Vector3d xAxis = new Vector3d(1.0,0.0,0.0);
		double heading = xAxis.angle(vectorToTarget);
		if(vectorToTarget.y<0){
			heading = 2*Math.PI - heading;
		}
		return heading;
	}
	
	/**
	 * Calculates the relative angle based on two absolute angles in radians
	 * @param absoluteTarget - the targets angle in radians
	 * @param absoluteReference - the reference angle in radians
	 * @return - the relative angle in radians from the references perspective
	 */
	public static double absoluteToRelative(double absoluteTarget, double absoluteReference){
		double relativeAngle = absoluteTarget - absoluteReference;
		//if got the long angle
		if(relativeAngle>Math.PI){
			relativeAngle=-(2*Math.PI-relativeAngle);
		}
		if(relativeAngle<-Math.PI){
			relativeAngle=(2*Math.PI+relativeAngle);
		}
		return relativeAngle;
	}

	/**
	 * Calculates the absolute angle of a relative angle based on an absolute reference
	 * @param relativeAngle - the relative angle from the reference angle in radians
	 * @param absoluteReference - the absolute angle of the reference point viewing the relative angle in radians
	 * @return the relative angle in radians
	 */
	public static double RelativeToAbsolute(double relativeAngle, double absoluteReference){
		double absoluteAngle = absoluteReference + relativeAngle;
		//if got the long angle
		if(absoluteAngle<0){
			absoluteAngle=2*Math.PI+absoluteAngle;
		}
		if(absoluteAngle>2*Math.PI){
			absoluteAngle=(absoluteAngle-2*Math.PI);
		}
		return absoluteAngle;
}
	
	/**
	 * Returns the vector from a reference ndPoint to a target ndPoint
	 * @param targetPoint 
	 * @param referencePoint
	 * @return Vector3d
	 */
	public static Vector3d getVector(NdPoint targetPoint, NdPoint referencePoint){
		
		//recreate the ndpoints as vectors
		Vector3d targetVector = new Vector3d(targetPoint.getX(), targetPoint.getY(),0.0);
		Vector3d referenceVector = new Vector3d(referencePoint.getX(), referencePoint.getY(),0.0);
		Vector3d vectorToTarget = new Vector3d();
		
		//create and return a vector between the two vectors
		vectorToTarget.sub(targetVector, referenceVector);
		return vectorToTarget;
	}
	
	/**
	 * Takes in an angle in radians and a distance before giving back the vector
	 * @param angle - absolute angle to target in radians
	 * @param hypotenuse - the distance to the target
	 * @return - an absolute vector 
	 */
	public static Vector3d getVector(double angle, double hypotenuse){
		Vector3d vector = new Vector3d();
		
		//The x and y values of the future vector
		double x;
		double y;
		
		//The opposite and adjacent angles of the triangle
		double opp;
		double adj;
		
		double theAngle = angle;
		while(theAngle>Math.PI*.5){
			theAngle = theAngle - Math.PI*.5;
		}
		opp = Math.sin(theAngle)*hypotenuse;
		adj = Math.cos(theAngle)*hypotenuse;
		
		if(angle==0||angle==2*Math.PI){
			x = hypotenuse;
			y=0;
		} else if(Math.PI*.5>angle&&angle>0){
			x = adj;
			y = opp;
		} else if(angle==Math.PI*.5){
			x = 0;
			y = hypotenuse;
			
		} else if(Math.PI>angle&&angle>Math.PI*.5){
			x = -opp;
			y = adj;
		} else if(angle==Math.PI){
			x = -hypotenuse;
			y=0;
		} else if(Math.PI*1.5>angle&&angle>Math.PI){
			x = -adj;
			y = -opp;
		} else if(angle==Math.PI*1.5){
			x = 0;
			y = -hypotenuse;
			
		} else if(Math.PI*2>angle&&angle>Math.PI*1.5){
			x = opp;
			y = -adj;
		} else {
			x=0;
			y=0;
		}
		vector.set(x,y,0.0);
		return vector;
		
	}
	
	/**
	 * Checks if an agent is within a viewing angle based on the viewing heading and the current players position
	 * @param agent - the agent to be viewed
	 * @param angle - the viewing angle (in degrees NOT radians)
	 * @param heading - the direction (centre line) of the view (in radians NOT degrees)
	 * @return true if the agent is within the player view, false if not
	 */
	public static boolean inView(SimpleAgent agent, Player player, Double angle, Double heading){
		
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
	
	/**
	 * Checks if a heading is within view based on the viewing angle and the heading of the view
	 * @param headingToObject - the heading (radians) to the object to be viewed
	 * @param angle - the width of the viewing angle (degrees)
	 * @param heading - the heading of the centre of the view
	 */
	public static boolean inView(double headingToObject, Double angle, Double heading){
		
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
		
		//special condition
		if(rightSide>leftSide){
			if(headingToObject>=rightSide||headingToObject<=leftSide){
				return true;
			}
			//regular condition
		} else if(leftSide>rightSide){
			if(headingToObject>=rightSide&&headingToObject<=leftSide){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the current player has the ball
	 * @return true if the player has the ball
	 */
	public static boolean hasBall(Player player){
		
		//iterate through the balls
		Iterator<Object> it = Params.context.getObjects(Ball.class).iterator();
		Ball ball;
		if(it.hasNext()){
			ball = (Ball)it.next();
			
			//create a vector from the player to the ball
				Vector3d vectorToBall = Utils.getVector(ball.getPositionVector(), player.getPositionVector());
				
				//If the ball is close enough to the player and within angle of the players arms
				if((Math.abs(vectorToBall.length())<=(Params.playerRadius+0.01))&&
						Utils.inView(ball, player, Params.armsAngle, player.getArms().getRotation())){

					//If the ball's player is either the player or null
					if(ball.getPlayer()==null||ball.getPlayer().equals(player)){
						return true;			
					}
				}	
		}
		return false;
	}
	
	/**
	 * Returns the ball if the player has one
	 * @return the ball if the player has the ball
	 */
	public static Ball getBall(Player player){
		
		//iterate through the balls
		Iterator<Object> it = Params.context.getObjects(Ball.class).iterator();
		Ball ball;
		if(it.hasNext()){
			ball = (Ball)it.next();
			
				//create a vector from the player to the ball
				Vector3d vectorToBall = Utils.getVector(ball.getPositionVector(), player.getPositionVector());
				
				//If the ball is close enough to the player and within angle of the players arms
				if((Math.abs(vectorToBall.length())<=(Params.playerRadius+.01)&&
						Utils.inView(ball, player, Params.armsAngle, player.getArms().getRotation()))){
					
					//If the ball's player is either the player or null
					if(ball.getPlayer()==null||ball.getPlayer().equals(player)){
						return ball;			
					}
				}	
		}
		return null;
	}
}
