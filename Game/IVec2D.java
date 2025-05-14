package Game;

/**
 * The IVec2D class represents a 2D integer vector with various mathematical operations.
 */
public class IVec2D {
    public int x;
    public int y;

    /**
     * Constructs a new IVec2D with both x and y set to 0.
     */
    public IVec2D() {
        x = y = 0;
    }

    /**
     * Constructs a new IVec2D with both x and y set to the specified value.
     * 
     * @param s The value to set for both x and y.
     */
    public IVec2D(int s) {
        x = y = s;
    }

    /**
     * Constructs a new IVec2D with the specified x and y values.
     * 
     * @param x The x-coordinate of the vector.
     * @param y The y-coordinate of the vector.
     */
    public IVec2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds another IVec2D vector to this vector and returns the result.
     * 
     * @param other The other vector to add.
     * @return This vector after the addition.
     */
    public IVec2D add(IVec2D other) {
        x += other.x;
        y += other.y;

        return this;
    }

    /**
     * Subtracts another IVec2D vector from this vector and returns the result.
     * 
     * @param other The other vector to subtract.
     * @return This vector after the subtraction.
     */
    public IVec2D sub(IVec2D other) {
        x -= other.x;
        y -= other.y;

        return this;
    }

    /**
     * Rotates this vector by the specified angle (in radians) and returns the result.
     * 
     * @param angle The angle (in radians) by which to rotate the vector.
     * @return This vector after the rotation.
     */
    public IVec2D rotate(int angle) {
        int oldX = x, oldY = y;

        x = (int) (oldX * Math.cos(angle) - oldY * Math.sin(angle));
        y = (int) (oldX * Math.sin(angle) + oldY * Math.cos(angle));

        return this;
    }

    /**
     * Scales this vector by the specified scale factor and returns the result.
     * 
     * @param scale The scale factor to multiply the vector by.
     * @return This vector after the scaling.
     */
    public IVec2D scale(int scale) {
        x *= scale;
        y *= scale;

        return this;
    }

    /**
     * Computes and returns the dot product of this vector with another vector.
     * 
     * @param other The other vector to compute the dot product with.
     * @return The dot product of the two vectors.
     */
    public int dot(IVec2D other) {
        return x * other.x + y * other.y;
    }

    /**
     * Computes and returns the length (magnitude) of this vector.
     * 
     * @return The length of the vector.
     */
    public int length() {
        return (int) Math.sqrt(length2());
    }

    /**
     * Computes and returns the squared length of this vector (without taking the square root).
     * 
     * @return The squared length of the vector.
     */
    public int length2() {
        return x * x + y * y;
    }

    /**
     * Creates a new IVec2D that is a copy of this vector.
     * 
     * @return A new IVec2D with the same x and y values.
     */
    public IVec2D clone() {
        return new IVec2D(x, y);
    }

    /**
     * Checks if this vector is equal to another object.
     * 
     * @param other The object to compare with.
     * @return True if the object is an IVec2D and its x and y values are equal to this vector's x and y values, otherwise false.
     */
    @Override
    public boolean equals(Object other) {
        IVec2D otherv = (IVec2D) other;
        return x == otherv.x && y == otherv.y;
    }

    /**
     * Computes and returns the angle (in radians) of this vector with respect to the x-axis.
     * 
     * @return The angle of the vector in radians.
     */
    public int angle() {
        return (int) Math.atan2(y, x);
    }

    /**
     * Normalizes this vector, making its length equal to 1, and returns the result.
     * 
     * @return This vector after normalization.
     */
    public IVec2D normal() {
        int l = length();

        x /= l;
        y /= l;

        return this;
    }

    /**
     * Returns a string representation of this vector in the format "x:y".
     * 
     * @return A string representing the vector.
     */
    @Override
    public String toString() {
        return x + ":" + y;
    }

    /**
     * Returns a hash code value for this vector.
     * 
     * @return The hash code value for the vector.
     */
    @Override
    public int hashCode() {
        return x |  y << 16;
    }
}