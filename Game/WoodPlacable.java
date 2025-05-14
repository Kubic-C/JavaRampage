package Game;

/**
 * Represents a placable wooden object in the game.
 */
public class WoodPlacable extends Placable {
    /**
     * Constructor to create a new WoodPlacable object with default properties and components.
     * It initializes the object with a wood texture, health, and a terrain cost, and creates
     * the corresponding entity in the world with necessary components like sprite and health.
     *
     * @param world The entity world where the object is placed.
     */
	public WoodPlacable(EntityWorld world) {
		super(ImageLoader.createImageIcon("images/wood.png", "wood"), 30, null, new IVec2D(1, 1), true);
		Entity e = world.create();
		SpriteComponent sprite = new SpriteComponent();
		sprite.image = ImageLoader.createImage("images/wood.png");
		e.add(sprite);
		ArrowComponent arrow = new ArrowComponent();
		arrow.terrainCost = 2;
		e.add(arrow);
		HealthComponent health = new HealthComponent();
		health.health = 40;
		e.add(health);
		e.disable();
		setPrefab(e);
	}
}