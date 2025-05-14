package Game;

/**
 * Represents an arrow component used in pathfinding or direction-based logic.
 * Stores directional data, movement cost, terrain cost, and generation tracking.
 */
public class ArrowComponent extends Component {
	public Vec2D dir = new Vec2D(0);
	public float cost = Float.MAX_VALUE;
	public float terrainCost = 0;
	public int gen = 0;

	/**
	 * Returns the type of this component.
	 *
	 * @return the class object representing ArrowComponent
	 */
	@Override
	public Class<?> getType() {
		// TODO Auto-generated method stub
		return ArrowComponent.class;
	}

	/**
	 * Creates a deep copy of this ArrowComponent instance.
	 *
	 * @return a cloned ArrowComponent with copied values
	 */
	@Override
	public Component clone() {
		ArrowComponent clone = new ArrowComponent();
		clone.dir = dir.clone();
		clone.cost = cost;
		clone.terrainCost = terrainCost;
		clone.gen = gen;

		return clone;
	}
}