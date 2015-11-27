package environment;

import java.io.IOException;
import java.net.URL;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

/**
 * Sets out the look and appearance of the background and agents
 * @author user
 *
 */
public class SimpleAgentStyle extends DefaultStyleOGL2D{

	private ShapeFactory2D factory;
	
	//The scales of the various images
	private float playerScale = 0.3f;
	private float fieldScale = 1f;
	private float headScale = 0.3f;
	private float ballScale = 0.1f;
	
	/**
	 * Creates the shapefactory to be used to create shapes
	 */
	@Override
	public void init(ShapeFactory2D x){
		factory = x;
		super.init(factory);
	}
	
	/**
	 * Gets the various images used in the simulation
	 */
	@Override
	public VSpatial getVSpatial(Object o, saf.v3d.scene.VSpatial spatial) {
		if(spatial == null){
			if (o instanceof Attacker){
				try{
					URL url = getClass().getResource("/maroon.png");
					return(factory.createImage(url.getPath()));
					}
				catch (IOException e) {
				    System.err.println("Couldnt get player images!");
				}
			}
			if (o instanceof Defender){
				try{
					URL url = getClass().getResource("/baby_blue.png");
					return factory.createImage(url.getPath());
					}
				catch (IOException e) {
				    System.err.println("Couldnt get player images!");
				}
			}
			if (o instanceof Field){
				try{
					URL	url = getClass().getResource("/field.png");
					return factory.createImage(url.getPath());
					}
				catch (IOException e) {
				    System.err.println("Couldnt get field image!");
				}
			}
			if (o instanceof Head){
				try{
					URL	url = getClass().getResource("/head.png");
					return factory.createImage(url.getPath());
				}
				catch (IOException e) {
				    System.err.println("Couldnt get head image!");
				}
			}
			if (o instanceof Ball){
				try{
					URL	url = getClass().getResource("/ball.png");
					return factory.createImage(url.getPath());
				}
				catch (IOException e) {
				    System.err.println("Couldnt get ball image!");
				}
			}
			return null;
		}
		return spatial;
	}
	
	/**
	 * Sets the rotation of the agent shapes, retrieves this from the model
	 */
	@Override
	public float getRotation(Object o) {
		if (o instanceof Player){
			double angle = ((Player) o).getRotation();
			float heading = 360-(float)(angle*57.2958);
			return heading;
		}
		if (o instanceof Head){
			double angle = ((Head) o).getRotation();
			float heading = 360-(float)(angle*57.2958);
			return heading;
		}
		if (o instanceof Ball){
			double angle = ((Ball) o).getRotation();
			float heading = 360-(float)(angle*57.2958);
			return heading;
		}
		return 0;
	  }

	/**
	 * Sets the scale of the agents
	 */
	@Override
	public float getScale(Object o) {
		if (o instanceof Player)
			return playerScale;
		if (o instanceof Field)
			return fieldScale;
		if (o instanceof Head)
			return headScale;
		if (o instanceof Ball)
			return ballScale;
		return 1f;
	}
}

