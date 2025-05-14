package Game;

import java.awt.Graphics2D;

/**
 * The Rectangle class is a subclass of Fixture that represents a rectangular shape 
 * with a specific mass, width, and height. 
 */
public class Rectangle extends Fixture {
    private float m_w, m_h;

    /**
     * Constructs a Rectangle with the specified mass, width, and height.
     * 
     * @param mass The mass of the rectangle.
     * @param w The width of the rectangle.
     * @param h The height of the rectangle.
     */
    public Rectangle(float mass, float w, float h) {
        super(mass);
        
        m_w = w;
        m_h = h;
    }

    /**
     * Calculates the corner points of the rectangle after applying a rotation and 
     * offset transformation.
     * 
     * @param offset The position to which the rectangle is offset.
     * @param rot The rotation angle (in radians) to apply to the rectangle.
     * @return An array of Vec2D objects representing the corner points of the rectangle.
     */
    public Vec2D[] getPoints(Vec2D offset, float rot) {
        float hw = m_w / 2;
        float hh = m_h / 2;
        
        Vec2D corners[] = {
            new Vec2D(-hw, -hh),
            new Vec2D( hw, -hh),
            new Vec2D( hw,  hh),
            new Vec2D(-hw,  hh),
        };
        
        for (int i = 0; i < corners.length; i++) {
            corners[i].rotate(rot);
            corners[i].add(offset);
        }
        
        return corners;
    }

    /**
     * Calculates and returns the Axis-Aligned Bounding Box (AABB) of the rectangle 
     * after applying the specified rotation.
     * 
     * @param rot The rotation angle (in radians) to apply to the rectangle.
     * @return The AABB that bounds the rotated rectangle.
     */
    @Override
    public AABB getAABB(float rot) {
        float hw = m_w / 2;
        float hh = m_h / 2;
        
        Vec2D corners[] = {
            new Vec2D(-hw, -hh).rotate(rot),
            new Vec2D( hw, -hh).rotate(rot),
            new Vec2D( hw,  hh).rotate(rot),
            new Vec2D(-hw,  hh).rotate(rot),
        };

        Vec2D min = new Vec2D(Float.MAX_VALUE, Float.MAX_VALUE);
        Vec2D max = new Vec2D(-Float.MAX_VALUE, -Float.MAX_VALUE);
        for (int i = 0; i < corners.length; i++) {
            if (corners[i].x < min.x) {
                min.x = corners[i].x;
            }
            if (corners[i].y < min.y) {
                min.y = corners[i].y;
            }
            
            if (corners[i].x > max.x) {
                max.x = corners[i].x;
            }
            if (corners[i].y > max.y) {
                max.y = corners[i].y;
            }
        }

        return new AABB(new Vec2D(min.x, max.y), new Vec2D(max.x, min.y));
    }

    /**
     * Returns the type of this fixture, which is a rectangle.
     * 
     * @return An integer representing the type of this fixture (RECTANGLE_FIXTURE).
     */
    @Override
    public int getType() {
        return RECTANGLE_FIXTURE;
    }

    /**
     * Draws the rectangle on the provided Graphics2D object, applying the specified
     * position and rotation.
     * 
     * @param gfx The Graphics2D object used for drawing.
     * @param pos The position at which to draw the rectangle.
     * @param angle The rotation angle (in radians) to apply to the rectangle.
     */
    @Override
    public void draw(Graphics2D gfx, Vec2D pos, float angle) {
        Vec2D points[] = getPoints(pos, angle);
        
        for (int i = 0; i < points.length; i++) {
            Vec2D curPoint = points[i];
            Vec2D nextPoint = points[(i + 1) % points.length];
            
            gfx.drawLine((int)curPoint.x, (int)curPoint.y, (int)nextPoint.x, (int)nextPoint.y);
        }
    }
}