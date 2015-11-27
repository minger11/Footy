package environment;

import javax.vecmath.Vector3d;
import repast.simphony.space.continuous.NdPoint;

public class Utils {

	/**
	 * A utility class. A toolbox for common operations across the model
	 */
	Utils(){
		
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
	 * Takes two vectors and returns vector to the target
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
	
	/**
	 * Calculates the relative angle based on two absolute angles in radians
	 * @param absoluteTarget - the targets angle in radians
	 * @param absoluteReference - the reference angle in radians
	 * @return - the relative angle in radians from the references perspective
	 */
	public double absoluteToRelative(double absoluteTarget, double absoluteReference){
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
	public double RelativeToAbsolute(double relativeAngle, double absoluteReference){
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
	
	public Vector3d getVector(NdPoint targetPoint, NdPoint referencePoint){
		Vector3d targetVector = new Vector3d(targetPoint.getX(), targetPoint.getY(),0.0);
		Vector3d referenceVector = new Vector3d(referencePoint.getX(), referencePoint.getY(),0.0);
		Vector3d vectorToTarget = new Vector3d();
		vectorToTarget.sub(targetVector, referenceVector);
		return vectorToTarget;
	}
	
	/**
	 * Takes in an angle in radians and a distance before giving back the vector
	 * @param angle - absolute angle to target in radians
	 * @param hypotenuse - the distance to the target
	 * @return - an absolute vector 
	 */
	public Vector3d getVector(double angle, double hypotenuse){
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
	
}
