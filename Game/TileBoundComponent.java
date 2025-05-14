package Game;

/**
 * Represents a component that binds a tile to an entity, including the tilemap and the tile position.
 */
public class TileBoundComponent extends Component {
    
    /**
     * The tilemap to which this component belongs.
     */
    public Tilemap tilemap;
    
    /**
     * The position of the tile in the tilemap.
     */
    public IVec2D pos;

    /**
     * Returns the type of this component (TileBoundComponent).
     * 
     * @return The class type of this component (`TileBoundComponent.class`).
     */
    @Override
    public Class<?> getType() {
        return TileBoundComponent.class;
    }

    /**
     * Clones this component. Since cloning is not implemented, this method returns `null`.
     * 
     * @return `null` as cloning is not supported in this implementation.
     */
    @Override
    public Component clone() {
        return null;
    }

    /**
     * Removes this component from the entity and cleans up its associated tilemap and tile.
     * This involves removing the entity from the tilemap, erasing the tile, and destroying the associated entity.
     */
    @Override
    public void onRemove() {
        if (tilemap != null) {
            Tilemap tm = tilemap;
            tilemap = null;

            // Find the entity associated with the tile and erase the tile from the tilemap
            Entity e = tm.find(pos).entity;
            tm.find(pos).entity = null;
            tm.erase(pos);
            
            // Destroy the entity
            e.world().destroy(e.id());
        }
    }
}