package Game;

import java.util.*;

/**
 * Represents a component that manages multiple tilemaps across different layers.
 * It provides functionality to add layers, retrieve tilemaps for specific layers,
 * and check if a tile exists in a given position.
 */
public class TilemapComponent extends Component {
	private final Tilemap[] tilemaps = new Tilemap[2];
	private int layerCount = 2;

	/**
	 * Constructor for the TilemapComponent class.
	 * Initializes two tilemaps in the component, representing different layers.
	 * The default layer count is set to 2.
	 */
	public TilemapComponent() {
	    for (int i = 0; i < tilemaps.length; i++) {
	        tilemaps[i] = new Tilemap();
	    }
	}

	/**
	 * Returns the tilemap for a given layer.
	 * 
	 * @param layer The index of the layer.
	 * @return The Tilemap at the specified layer.
	 * @throws IllegalArgumentException if the specified layer is invalid (greater than or equal to layerCount).
	 */
	public Tilemap getTilemap(int layer) {
		if (layer >= layerCount)
			throw new IllegalArgumentException("Invalid layer: " + layer);
		return tilemaps[layer];
	}

	/**
	 * Returns the tilemap at the bottom (first) layer.
	 * 
	 * @return The tilemap at the bottom layer.
	 */
	public Tilemap getBottomTilemap() {
		return tilemaps[0];
	}

	/**
	 * Returns the tilemap at the top (last) layer.
	 * 
	 * @return The tilemap at the top layer.
	 */
	public Tilemap getTopTilemap() {
		return tilemaps[layerCount - 1];
	}


	/**
	 * Gets the layer index that contains a tile at the given position.
	 * 
	 * @param pos The position to check.
	 * @return The index of the layer that contains the tile, or -1 if no tile exists at that position.
	 */
	public int getTopTilemapWith(IVec2D pos) {
		for (int i = layerCount - 1; i >= 0; i--) {
			if (tilemaps[i].contains(pos)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the total number of layers in the TilemapComponent.
	 * 
	 * @return The count of layers.
	 */
	public int getTilemapCount() {
		return layerCount;
	}

	/**
	 * Returns the class type of this component.
	 * 
	 * @return The class type of the TilemapComponent.
	 */
	@Override
	public Class<?> getType() {
	    return TilemapComponent.class;
	}

	/**
	 * Creates a clone of this TilemapComponent.
	 * 
	 * @return null as the cloning functionality is not implemented.
	 */
	@Override
	public Component clone() {
	    return null;
	}

}