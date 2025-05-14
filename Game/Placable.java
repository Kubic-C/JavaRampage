package Game;

import javax.swing.Icon;

/**
 * The Placable class represents an item that can be placed in the game world.
 * It extends the Item class and adds functionality for handling its dimensions and collision behavior.
 */
public class Placable extends Item {
    private IVec2D m_dim;
    private boolean m_collide;

    /**
     * Constructs a Placable item with the specified icon, default uses, prefab entity, dimensions, and collision behavior.
     *
     * @param icon The icon representing the Placable item.
     * @param defaultUses The default number of uses for the item.
     * @param prefab The entity prefab associated with the item.
     * @param dim The dimensions of the Placable item.
     * @param collide A boolean indicating whether the Placable item is collidable.
     */
    public Placable(Icon icon, int defaultUses, Entity prefab, IVec2D dim, boolean collide) {
        super(icon, Item.PlacableType, prefab, defaultUses);
        m_dim = dim;
        m_collide = collide;
    }

    /**
     * Returns the dimensions of the Placable item.
     *
     * @return The dimensions of the Placable item as an IVec2D object.
     */
    IVec2D getDim() {
        return m_dim;
    }

    /**
     * Checks if the Placable item is collidable.
     *
     * @return True if the item is collidable, otherwise false.
     */
    boolean isCollidable() {
        return m_collide;
    }
}