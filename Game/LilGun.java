package Game;

/**
 * The LilGun.
 */
public class LilGun extends Weapon {

    /**
     * Constructs a new LilGun.
     */
    public LilGun() {
        super(ImageLoader.createImageIcon("images/lilGun.png", "lilBoom"), Integer.MAX_VALUE, 0.25f, 5.0f, 1, 4.0f);
    }
}