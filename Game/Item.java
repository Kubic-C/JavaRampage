package Game;

import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The Item class represents an item in the game, which can have different types,
 * associated icon, and a prefab object with a default usage count.
 */
public class Item {
    public static int InvalidType = 0;
    public static int PlacableType = 1;
    public static int HoldableType = 2;

    private Icon m_icon;
    private int m_type = InvalidType;
    private Object m_prefab;
    private int m_defaultUses;

    /**
     * Constructs an Item with a specified icon, type, prefab, and default uses.
     * 
     * @param icon The icon associated with the item.
     * @param type The type of the item (e.g., placable, holdable).
     * @param prefab The prefab object associated with the item.
     * @param defaultUses The default number of uses for the item.
     */
    public Item(Icon icon, int type, Object prefab, int defaultUses) {
        m_icon = icon;
        m_type = type;
        m_prefab = prefab;
        m_defaultUses = defaultUses;
    }

    /**
     * Gets the icon associated with this item.
     * 
     * @return The icon of the item.
     */
    public Icon getIcon() {
        return m_icon;
    }

    /**
     * Gets the type of the item.
     * 
     * @return The type of the item (e.g., placable, holdable).
     */
    public int getType() {
        return m_type;
    }

    /**
     * Gets the prefab object associated with this item.
     * 
     * @return The prefab object of the item.
     */
    public Object getPrefab() {
        return m_prefab;
    }

    /**
     * Sets the prefab object associated with this item.
     * 
     * @param prefab The new prefab object to associate with this item.
     */
    public void setPrefab(Object prefab) {
        m_prefab = prefab;
    }

    /**
     * Gets the default number of uses for this item.
     * 
     * @return The default number of uses.
     */
    public int getDefaultUses() {
        return m_defaultUses;
    }
}