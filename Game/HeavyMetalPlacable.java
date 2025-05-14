package Game;

/**
 * Represents a placable object of type "Heavy Metal" in the game.
 */
public class HeavyMetalPlacable extends Placable {

    /**
     * Constructor that initializes the "Heavy Metal" placable object.
     * 
     * @param world The `EntityWorld` where the entity will be created.
     */
    public HeavyMetalPlacable(EntityWorld world) {
		super(ImageLoader.createImageIcon("images/heavyMetal.png", "stone"), 5, null, new IVec2D(1, 1), true);
		Entity e = world.create();
		SpriteComponent sprite = new SpriteComponent();
		sprite.image = ImageLoader.createImage("images/heavyMetal.png");
		e.add(sprite);
		ArrowComponent arrow = new ArrowComponent();
		arrow.terrainCost = 50;
		e.add(arrow);
		HealthComponent health = new HealthComponent();
		health.health = 500;
		e.add(health);
		e.disable();
		setPrefab(e);
	}
}