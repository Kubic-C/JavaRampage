package Game;

/**
 * Represents a tile in the game world. A tile may have a rigid body, an associated entity, and different properties
 * such as collision, size, and position.
 */
public class Tile {
	static final public float sideLength = 20.0f;
	
	public boolean isCollidable = false;
	public boolean isMain = true;
	public boolean isMulti = false;
	
	public RigidBody body;
	public Entity entity;
	
	public int data1; // Could be width or main tile pos x
	public int data2; // Could be height or main tile pos y
	
    /**
     * Returns the position of the tile when it is a multi-tile (non-main). The position is represented by a 2D vector.
     * 
     * @return The position of the tile as an `IVec2D` object.
     * @throws AssertionError if the tile is not a multi-tile and non-main.
     */
    IVec2D getPos() {
        assert isMulti && !isMain;
        return new IVec2D(data1, data2);
    }

    /**
     * Returns the size of the tile when it is the main tile. The size is represented by a 2D vector.
     * 
     * @return The size of the tile as an `IVec2D` object.
     * @throws AssertionError if the tile is not the main tile.
     */
    IVec2D getSize() {
        assert isMain;
        return new IVec2D(data1, data2);
    }
}