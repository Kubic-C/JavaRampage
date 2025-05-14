package Game;

import java.awt.Graphics2D;

/**
 * An axis-aligned-bounding-box */
public class AABB {
	Vec2D bl, tr;
	
	/**
	 * Creates a bounding box, starting at (x, y), and extending positively with dimensions (w, h) 
	 * @param x the x position of the AABB
	 * @param y the y position of the AABB
	 * @param w the width of the AABB
	 * @param h the height of the AABB */
	public AABB(float x, float y, float w, float h) {
		this(new Vec2D(x, y + h), new Vec2D(x + w, y));
	}
	
	/**
	 * Creates a AABB with corners bl and tr, where bl < tr
	 * @param bl The bottom left corner of the AABB
	 * @param tr The top right corner of the AABB */
	public AABB(Vec2D bl, Vec2D tr) {
		this.bl = bl;
		this.tr = tr;
	}
	
	/**
	 * Translates a AABB by trans
	 * @param trans The amount to translate by
	 * @return This AABB, so that calls may be chained together. */
	public AABB translate(Vec2D trans) {
		bl.add(trans);
		tr.add(trans);
		
		return this;
	}
	
	/**
	 * Debug draws the AABB 
	 * @param gfx the graphics to draw on */
	public void draw(Graphics2D gfx) {
		int w = (int)(tr.x - bl.x);
		int h = (int)(bl.y - tr.y);
		
		gfx.drawRect((int)bl.x, (int)tr.y, w, h);
	}
	
	/**
	 * @param other the other aabb
	 * @return Does this AABB contain other? */
	public boolean contains(AABB other) {
		return bl.x <= other.bl.x && other.tr.x <= tr.x &&
			   bl.y <= other.bl.y && other.tr.y < tr.y;
	}
	
	/**
	 * @param point A point
	 * @return Does this AABB contain point ? */
	public boolean contains(Vec2D point) {
		return bl.x <= point.x && point.x <= tr.x &&
			   bl.y <= point.y && point.y < tr.y;
	}
	
	/**
	 * @param other the other aabb
	 * @return Does this AABB intersect other? */
	public boolean intersects(AABB b) {
	  return (bl.x <= b.tr.x && tr.x >= b.bl.x) &&
	         (tr.y <= b.bl.y && bl.y >= b.tr.y);
	}
	
	/**
	 * Rotates the corners of this AABB to create an OBB. This AABB
	 * will be unmodified.
	 * @param angle The angle to rotate by
	 * @return the vertices of this AABB rotated by angle */
	public Vec2D[] rotate(float angle) {
		Vec2D points[] = {
			bl,
			new Vec2D(bl.y, tr.x),
			tr,
			new Vec2D(bl.x, tr.y)
		};
		
		for(int i = 0; i < points.length; i++) {
			points[i].rotate(angle);
		}
		
		return points;
	}
}