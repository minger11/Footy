package environment;

/**
 * Contains simple information related to motion
 * @author user
 *
 */
public class MotionVector{
	
	double velocity;
	double angle;
	double acceleration;
	
	MotionVector(double velocity, double angle, double acceleration){
		this.velocity = velocity;
		this.angle = angle;
		this.acceleration = acceleration;
	}
	
	MotionVector(){
	}
	
	public double getVelocity(){
		return velocity;
	}
	public double getAngle(){
		return angle;
	}
	public double getAcceleration(){
		return acceleration;
	}
	protected void setVelocity(Double x){
		velocity = x;
	}
	protected void setAngle(Double x){
		angle = x;
	}
	protected void setAcceleration(Double x){
		acceleration = x;
	}
}
