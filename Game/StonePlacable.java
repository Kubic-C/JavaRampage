package Game;

/**
 * Represents a placable stone object in the game world.
 * The stone has a sprite, health, and a terrain cost associated with it.
 */
public class StonePlacable extends Placable {

    /**
     * Constructor for the StonePlacable class. Initializes the stone entity and its components.
     * 
     * @param world The entity world where the stone will be placed.
     */
    public StonePlacable(EntityWorld world) {
        super(ImageLoader.createImageIcon("images/stone.png", "stone"), 50, null, new IVec2D(1, 1), true);

        Entity e = world.create();
        SpriteComponent sprite = new SpriteComponent();
        sprite.image = ImageLoader.createImage("images/stone.png");
        e.add(sprite);

        ArrowComponent arrow = new ArrowComponent();
        arrow.terrainCost = 5;
        e.add(arrow);

        e.add(new HealthComponent());
        e.disable();
        setPrefab(e);
    }
}