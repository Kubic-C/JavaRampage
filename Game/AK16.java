package Game;

import javax.swing.Icon;

/**
 * A Weapon representing a fiction AK 16*/
public class AK16 extends Weapon {
	/**
	 * Creates an AK16 with its default firing speed and bullet size. */
	public AK16() {
		super(ImageLoader.createImageIcon("images/basicGun.png", "AK16"), 1000, 0.1f, 5.0f, 1, 5);
	}
}