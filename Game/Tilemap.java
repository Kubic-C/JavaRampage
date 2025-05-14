package Game;

import java.util.*;

/**
 * A class representing a tilemap where tiles are stored and managed.
 */
public class Tilemap {
    private Map<IVec2D, Tile> m_tiles;
    
    /**
     * Constructs a new Tilemap object and initializes the tile storage.
     */
    public Tilemap() {
        m_tiles = new HashMap<IVec2D, Tile>();
    }

    /**
     * Checks if the tilemap contains a tile at the specified position.
     * 
     * @param key The position of the tile.
     * @return `true` if the tilemap contains a tile at the given position, `false` otherwise.
     */
    public boolean contains(IVec2D key) {
        return m_tiles.containsKey(key);
    }

    /**
     * Finds and retrieves the tile at the specified position.
     * 
     * @param key The position of the tile.
     * @return The tile at the given position, or `null` if no tile exists at that position.
     */
    public Tile find(IVec2D key) {
        return m_tiles.get(key);
    }

    /**
     * Returns the set of all tile positions (keys) in the tilemap.
     * 
     * @return A set of tile positions in the tilemap.
     */
    public Set<IVec2D> getKeys() {
        return m_tiles.keySet();
    }

    /**
     * Checks if a tile can be placed at the specified position with the given dimensions.
     * 
     * @param pos The position to check for placement.
     * @param dim The dimensions of the tile.
     * @return `true` if the tile can be placed, `false` if it would overlap existing tiles.
     */
    public boolean canPlace(IVec2D pos, IVec2D dim) {
        for(int y = pos.y; y < pos.y + dim.y; y++) {
            for(int x = pos.x; x < pos.x + dim.x; x++) {
                if(contains(pos))
                    return false;
            }
        }
        return true;
    }

    /**
     * Erases all tiles in the specified area defined by position and dimensions.
     * 
     * @param pos The position of the top-left corner of the area.
     * @param dim The dimensions of the area to erase.
     */
    public void eraseAll(IVec2D pos, IVec2D dim) {
        for(int y = pos.y; y < pos.y + dim.y; y++) {
            for(int x = pos.x; x < pos.x + dim.x; x++) {
                if(contains(pos))
                    erase(pos);
            }
        }
    }

    /**
     * Inserts a new tile with the specified properties into the tilemap.
     * 
     * @param world The physics world to attach a rigid body to.
     * @param worldTrans The world transform used to determine position.
     * @param pos The position at which to insert the tile.
     * @param dim The dimensions of the tile.
     * @param e The entity to associate with the tile.
     * @param isCollidable Whether the tile should be collidable.
     * @return `true` if the tile was successfully inserted, `false` if it could not be placed.
     */
    public boolean insert(PhysicsWorld world, TransformComponent worldTrans, IVec2D pos, IVec2D dim, Entity e, boolean isCollidable) {
        if(!canPlace(pos, dim))
            return false;
        
        Tile tile = new Tile();
        tile.isCollidable = isCollidable;
        tile.isMain = true;
        if(dim.x != 1 || dim.y != 1)
            tile.isMulti = true;
        tile.entity = e;
        tile.data1 = dim.x;
        tile.data2 = dim.y;
        
        if(e != null) {
            TileBoundComponent tileBound = new TileBoundComponent();
            tileBound.tilemap = this;
            tileBound.pos = pos.clone();
            e.add(tileBound);
        }

        if(tile.isMulti) {
            Tile subTile = new Tile();
            tile.isMain = false;
            tile.isMulti = true;
            tile.entity = null;
            tile.body = null;
            tile.data1 = pos.x;
            tile.data2 = pos.y;
            
            for(int y = pos.y; y < pos.y + dim.y; y++) {
                for(int x = pos.x; x < pos.x + dim.x; x++) {
                    IVec2D subTilePos = new IVec2D(x, y);
                    if(subTilePos.equals(pos))
                        continue;
                    
                    m_tiles.put(subTilePos, subTile);
                }
            }
        }
        
        if(tile.isCollidable) {
            RigidBody rb = world.create(worldTrans.getWorldPoint(getLocalTileCenter(pos, dim)), 0, true);
            Vec2D hs = new Vec2D(Tile.sideLength * dim.x, Tile.sideLength * dim.y);
            Rectangle rect = new Rectangle(50, hs.x, hs.y);
            rb.attachFixture(rect);
            rb.setUserData(e);
            
            tile.body = rb;
        }
        
        m_tiles.put(pos, tile);
        
        return true;
    }

    /**
     * Erases the tile at the specified position.
     * 
     * @param pos The position of the tile to erase.
     * @return The entity associated with the erased tile, or a new entity if no tile existed.
     */
    public Entity erase(IVec2D pos) {
        if(!contains(pos))
            return new Entity(0, null);
        
        Tile tile = find(pos);
        if(tile.isMulti && !tile.isMain)
            return erase(tile.getPos());
        
        IVec2D dim = tile.getSize();
        for(int i = pos.x; i < pos.x + dim.x; i++)
            for(int j = pos.y; j < pos.y + dim.y; j++) {
                if(tile.body != null)
                    tile.body.markForDeletion();
                if(tile.entity != null)
                    tile.entity.world().destroy(tile.entity.id());
                m_tiles.remove(pos);
            }
        
        return tile.entity;
    }

    /**
     * Calculates the local center of a tile based on its position and dimensions.
     * 
     * @param tilePos The position of the tile.
     * @param dim The dimensions of the tile.
     * @return The local center of the tile.
     */
    static public Vec2D getLocalTileCenter(IVec2D tilePos, IVec2D dim) {
        return new Vec2D(tilePos.x, tilePos.y).scale(Tile.sideLength).add(new Vec2D(dim.x, dim.y).scale(Tile.sideLength * 0.5f));
    }

    /**
     * Retrieves the nearest tile position for a given world position.
     * 
     * @param localPos The world position to find the nearest tile for.
     * @return The position of the nearest tile in the tilemap.
     */
    static public IVec2D getNearestTile(Vec2D localPos) {
        return new IVec2D((int)Math.floor(localPos.x / Tile.sideLength), (int)Math.floor(localPos.y / Tile.sideLength));
    }
}