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
	
	/**
	 * Takes two vectors and returns vector to the target
	 * @param referencePoint
	 * @param targetPoint
	 * @return
	 */
	public Vector3d getVector(NdPoint targetPoint, NdPoint referencePoint){
		Vector3d targetVector = new Vector3d(targetPoint.getX(), targetPoint.getY(),0.0);
		Vector3d referenceVector = new Vector3d(referencePoint.getX(), referencePoint.getY(),0.0);
		Vector3d vectorToTarget = new Vector3d();
		vectorToTarget.sub(targetVector, referenceVector);
		return vectorToTarget;
	}
		
}
