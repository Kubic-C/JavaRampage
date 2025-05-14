package Game;

import javax.swing.Icon;

/**
 * The BigBoom weapon, the most powerful boom of them all. */
public class BigBoom extends Weapon {
	/**
	 * Creates the BigBoom */
	public BigBoom() {
		super(ImageLoader.createImageIcon("images/bigBoom.png", "BigBoom"), 1000, 1.0f, 15.0f, 10, 20.0f);
	}
}