package Game;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Represents a core component that manages items that can be purchased and placed in slots.
 * The component displays buttons for each item, tracks their costs and purchases, and updates the score.
 */
public class CoreComponent extends Component {
	private class PaidItem {
		public int cost;
		public int baseCost;
		public Item item;
		public int purchaseCount;
		public JButton button;
	}
	
	private ArrayList<PaidItem> m_items;
	private int m_curSlot;
	private Thread m_scoreViewerUpdater;
	
    private static boolean containsComponent(JPanel panel, java.awt.Component component) {
        for (java.awt.Component comp : panel.getComponents()) {
            if (comp == component) {
            	System.out.println("Already containerd");
                return true;
            }
        }
        return false;
    }
	
    /**
     * Constructs a new CoreComponent with an empty list of items.
     */
	public CoreComponent() {
		m_items = new ArrayList<PaidItem>();
	}
	
    /**
     * Updates the given panel with buttons for each item, as well as the current score.
     * Each item button, when clicked, attempts to place the item in a free slot and deducts the cost from the score.
     *
     * @param parent  The parent JPanel where items and the score label are added.
     * @param width   The number of columns to display items in the grid.
     * @param slots   The available slots where items can be placed.
     * @param score   The current score, which is updated when items are purchased.
     */
	public void updatePanel(JPanel parent, int width, Slot[] slots, AtomicInteger score) {
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridy = 3;
		constraint.gridx = 0;

		for (int i = 0; i < m_items.size(); i++) {
			PaidItem item = m_items.get(i);
			if (containsComponent(parent, item.button))
				continue;

			JButton itemGUI = new JButton(item.cost + "$");
			itemGUI.setIcon(item.item.getIcon());

			constraint.fill = GridBagConstraints.NONE;
			parent.add(itemGUI, constraint);

			item.button = itemGUI;

			itemGUI.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (item.cost > score.get())
						return;

					for (int slotI = 0; slotI < slots.length; slotI++) {
						if (slots[slotI].getItem() == null) {
							score.addAndGet(-item.cost);
							slots[slotI].setItem(item.item);
							item.purchaseCount++;
							item.cost = (int)(item.baseCost * (5 + Math.sqrt(item.purchaseCount)));
							item.button.setText(item.cost + "$");
							break;
						}
					}
				}
			});

			constraint.gridx++;
			if (constraint.gridx >= width) {
				constraint.gridx = 0;
				constraint.gridy++;
			}
		}
		
		if(m_scoreViewerUpdater != null && m_scoreViewerUpdater.isAlive())
			m_scoreViewerUpdater.interrupt();
		
		m_scoreViewerUpdater = new Thread(new Runnable() {
			@Override
			public void run() {
				JLabel label = new JLabel();
				label.setPreferredSize(new Dimension(200, 25));
				label.setAlignmentY(java.awt.Component.BOTTOM_ALIGNMENT);
				label.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
				label.setForeground(Color.BLACK);
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = -1;
				gbc.gridy = -1;
				parent.add(label, gbc);
				
				while(!Thread.interrupted())
					label.setText("Current Score: " + score);
				
				parent.remove(label);
				System.out.println("Closing thread");
			}
		});
		m_scoreViewerUpdater.start();
	}
	
    /**
     * Adds a new item to the available paid items list.
     *
     * @param cost The initial cost of the item.
     * @param item The item to be added.
     */
	public void addItem(int cost, Item item) {
		PaidItem paidItem = new PaidItem();
		paidItem.baseCost = cost;
		paidItem.cost = cost;
		paidItem.item = item;
		m_items.add(paidItem);
	}

    /**
     * Returns the class type of the component.
     *
     * @return The class type of this component.
     */
	@Override
	public Class<?> getType() {
		return CoreComponent.class;
	}

    /**
     * Creates a deep clone of the CoreComponent.
     *
     * @return A new instance of CoreComponent with the same items as the original.
     */
	@Override
	public Component clone() {
		CoreComponent clone = new CoreComponent();
		for(PaidItem item : m_items) {
			PaidItem itemClone = new PaidItem();
			itemClone.cost = item.cost;
			itemClone.item = item.item; 
			clone.m_items.add(itemClone);
		}
		clone.m_curSlot = m_curSlot;
		return clone;
	}
}