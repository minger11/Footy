package environment;

import repast.simphony.context.Context;

/**
 * Sidepoints are used to identify the areas that mark the beginning of out of bounds
 * @author user
 *
 */

public class SidePoint extends BoundaryPoint{

	SidePoint(Context context, int x, int y) {
		super(context, x, y);
	}
}
