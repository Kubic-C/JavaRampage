package Game;

import javax.swing.JButton;

/**
 * Represents a slot in an inventory system.
 * Each slot holds an item, displays a button with item details, 
 * and manages the number of uses available for that item.
 */
public class Slot {
	private int m_slot;
	private Item m_item;
	private JButton m_btn;
	private int m_uses;
	
    /**
     * Constructs a Slot with a given button and slot index.
     * 
     * @param btn The button that represents the slot in the GUI.
     * @param slot The index of the slot.
     */
    public Slot(JButton btn, int slot) {
        m_slot = slot;
        m_btn = btn;
    }
	
    /**
     * Retrieves the button associated with the slot.
     * 
     * @return The JButton that represents the slot in the GUI.
     */
    public JButton getButton() {
        return m_btn;
    }

    /**
     * Retrieves the index of the slot.
     * 
     * @return The index of the slot.
     */
    public int getSlot() {
        return m_slot;
    }

    /**
     * Retrieves the item currently stored in the slot.
     * 
     * @return The item in the slot, or null if the slot is empty.
     */
    public Item getItem() {
        return m_item;
    }

    /**
     * Sets the item to be placed in the slot.
     * Also updates the button's icon and text to reflect the item and its uses.
     * 
     * @param item The item to be placed in the slot.
     */
    public void setItem(Item item) {
        m_item = item;
        if (item != null) {
            m_uses = item.getDefaultUses();  // Set the default number of uses for the item
            m_btn.setIcon(m_item.getIcon()); // Set the button's icon based on the item's icon
            m_btn.setText("" + m_uses);      // Set the button's text to the number of uses
        } else {
            m_btn.setIcon(null);             // Clear the button's icon if no item
        }
    }
	
    /**
     * Retrieves the number of uses left for the item in the slot.
     * 
     * @return The number of uses.
     */
    public int getUses() {
        return m_uses;
    }

    /**
     * Decreases the number of uses available for the item in the slot and updates the button text.
     */
    public void used() {
        m_uses--;
        m_btn.setText("" + m_uses);  // Update the button's text to reflect remaining uses
    }

    /**
     * Checks if the item in the slot should be removed (i.e., if the number of uses is 0 or less).
     * 
     * @return true if the item should be removed, false otherwise.
     */
    public boolean shouldRemove() {
        return m_uses <= 0;
    }
}