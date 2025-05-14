package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Set;

/**
 * Renders the tiles of a tilemap onto the screen by processing each entity
 * that has a TransformComponent and TilemapComponent. This system is responsible
 * for rendering tile layers and their associated entities' sprite representations.
 */
public class TilemapRenderSystem extends EntitySystem {
    /**
     * Constructor for the TilemapRenderSystem.
     * Initializes the system to work on entities that have a TransformComponent and a TilemapComponent.
     *
     * @param world The EntityWorld to retrieve components from.
     */
	TilemapRenderSystem(EntityWorld world) {
		super(world, new ComponentSet()
				.add(world.component(TransformComponent.class))
				.add(world.component(TilemapComponent.class)));
	}

    /**
     * Unused
     * 
     * @param tick unused
     * @param deltaTime unused
     */
	@Override
	public void run(int tick, float deltaTime) {}


    /**
     * Draws the tilemap layers onto the provided Graphics2D object.
     * Iterates through each entity that has both a TransformComponent and TilemapComponent,
     * rendering each tile and its corresponding sprite based on the entity's transform.
     * 
     * @param gfx The Graphics2D object used to draw the tilemap to the screen.
     */
	public void draw(Graphics2D gfx) {		
		each(new Runnable() {
			@Override
			public void each(Entity e) {
				//System.out.println(e.id());
				TransformComponent transform = e.get(TransformComponent.class);
				TilemapComponent tilemapLayers = e.get(TilemapComponent.class);
				
				for(int i = 0; i < tilemapLayers.getTilemapCount(); i++) {
					Tilemap tilemap = tilemapLayers.getTilemap(i);
					
					for(IVec2D pos : tilemap.getKeys()) {
						Tile tile = tilemap.find(pos);

						IVec2D size = tile.getSize();
						Vec2D hs = new Vec2D(size.x * Tile.sideLength, size.y * Tile.sideLength).scale(0.5f);
						Vec2D worldPos = transform.getWorldPoint(Tilemap.getLocalTileCenter(pos, tile.getSize()).sub(hs));
						Vec2D worldDim = transform.getWorldPoint(Tilemap.getLocalTileCenter(pos, tile.getSize()).add(hs));
						
						int x[] = { (int)worldPos.x, (int)worldDim.x, (int)worldDim.x, (int)worldPos.x };
						int y[] = { (int)worldPos.y, (int)worldPos.y, (int)worldDim.y, (int)worldDim.y };
					
						if(tile.entity != null && tile.entity.has(SpriteComponent.class)) {
							Image image = tile.entity.get(SpriteComponent.class).image;
							
							gfx.setColor(Color.CYAN);
							gfx.drawImage(image, x[0], y[0], x[2], y[2], 0, 0, 32, 32, null);
						}
					}
				}	
			}
		});
	}
}