package environment;

/**
 * A class representing physical laws.
 * Potentially have various copies of this class 
 * One copy could be used by the model
 * Other copies could be used by players to represent their interpretation of physical laws
 * Would need to use a level of truth parameter in the constructor (overload) for subjective copies
 * Could also include uncertainty in knowledge of own ability and knowledge of opponents ability to represent this
 * Movements should be passed here first, could also pass movements to a MODERATOR type class
 * @author user
 *
 */

public class Physics {

	MotionVector desiredMotion;
	MotionVector currentMotion;
	
	Physics(MotionVector currentMotion, MotionVector desiredMotion){
		this.desiredMotion = desiredMotion;
		this.currentMotion = currentMotion;		
	}
	
	protected MotionVector updateMotion(){
		return desiredMotion;
	}
	
}
