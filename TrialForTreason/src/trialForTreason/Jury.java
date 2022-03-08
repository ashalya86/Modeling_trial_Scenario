/**
 * 
 */
package trialForTreason;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

/**
 * @author nick
 * 
 */
public class Jury {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private boolean moved;
	private int energy;
	float propability_of_conviction;
	float propability_of_non_conviction;

	public Jury(ContinuousSpace<Object> space, Grid<Object> grid, int energy, float propability_of_conviction, float propability_of_non_conviction) {
		this.space = space;
		this.grid = grid;
		this.energy = energy;
		this.propability_of_conviction = propability_of_conviction;
		this.propability_of_non_conviction = propability_of_non_conviction;
	}

	@ScheduledMethod(start = 2, interval = 1)
	public void step() {
		// get the grid location of this Zombie
		GridPoint pt = grid.getLocation(this);

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<Citizen> nghCreator = new GridCellNgh<Citizen>(grid, pt,
				Citizen.class, 1, 1);
		List<GridCell<Citizen>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

		GridPoint pointWithMostHumans = null;
		int maxCount = -1;
		for (GridCell<Citizen> cell : gridCells) {
			if (cell.size() > maxCount) {
				pointWithMostHumans = cell.getPoint();
				maxCount = cell.size();
			}
		}
//		moveTowards(pointWithMostHumans);
//		infect();
		if (energy > 20) {
			System.out.println("I'm a jury member");
			makedecision(this.propability_of_conviction, this.propability_of_non_conviction);
		}	
	}
	
	public void makedecision(float propability_of_conviction, float propability_of_non_conviction) {
			if ((propability_of_conviction - propability_of_non_conviction) > 0.5) {
				System.out.println("Convict Leocrates");
			}else {
				System.out.println("Do not convict Leocrates");	
				}
			}
}
