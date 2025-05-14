package Game;

/**
 * A 2D vector class representing a point or direction in a 2D space.
 * Provides methods for common vector operations like addition, subtraction, scaling, rotation,
 * and calculating the dot product and length of the vector.
 */
public class Vec2D {
    public float x;
    public float y;

    /**
     * Default constructor initializing the vector to (0, 0).
     */
    public Vec2D() {
        x = y = 0;
    }

    /**
     * Constructor initializing both x and y to the same value.
     *
     * @param s The value to assign to both x and y.
     */
    public Vec2D(float s) {
        x = y = s;
    }

    /**
     * Constructor initializing the vector to a specific x and y value.
     *
     * @param x The x coordinate of the vector.
     * @param y The y coordinate of the vector.
     */
    public Vec2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds another vector to this vector.
     *
     * @param other The vector to add.
     * @return This vector after the addition.
     */
    public Vec2D add(Vec2D other) {
        x += other.x;
        y += other.y;
        return this;
    }

    /**
     * Subtracts another vector from this vector.
     *
     * @param other The vector to subtract.
     * @return This vector after the subtraction.
     */
    public Vec2D sub(Vec2D other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    /**
     * Rotates the vector by a given angle.
     *
     * @param angle The angle to rotate the vector by, in radians.
     * @return This vector after the rotation.
     */
    public Vec2D rotate(float angle) {
        float oldX = x, oldY = y;
        x = (float) (oldX * Math.cos(angle) - oldY * Math.sin(angle));
        y = (float) (oldX * Math.sin(angle) + oldY * Math.cos(angle));
        return this;
    }

    /**
     * Scales the vector by a given factor.
     *
     * @param scale The scale factor to apply to the vector.
     * @return This vector after the scaling.
     */
    public Vec2D scale(float scale) {
        x *= scale;
        y *= scale;
        return this;
    }

    /**
     * Calculates the dot product of this vector and another vector.
     *
     * @param other The other vector.
     * @return The dot product of this vector and the other vector.
     */
    public float dot(Vec2D other) {
        return x * other.x + y * other.y;
    }

    /**
     * Calculates the length (magnitude) of the vector.
     *
     * @return The length of the vector.
     */
    public float length() {
        return (float) Math.sqrt(length2());
    }

    /**
     * Calculates the squared length (magnitude squared) of the vector.
     *
     * @return The squared length of the vector.
     */
    public float length2() {
        return x * x + y * y;
    }

    /**
     * Creates a new vector that is a copy of this vector.
     *
     * @return A new vector that is a copy of this vector.
     */
    public Vec2D clone() {
        return new Vec2D(x, y);
    }

    /**
     * Checks if two vectors are equal (i.e., if both their x and y components are equal).
     *
     * @param other The other object to compare.
     * @return true if the vectors are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vec2D)) return false;
        Vec2D otherv = (Vec2D) other;
        return x == otherv.x && y == otherv.y;
    }

    /**
     * Calculates the angle (in radians) of the vector from the positive x-axis.
     *
     * @return The angle of the vector in radians.
     */
    public float angle() {
        return (float) Math.atan2(y, x);
    }

    /**
     * Normalizes the vector (i.e., makes its length 1 while maintaining its direction).
     *
     * @return This vector after it has been normalized.
     */
    public Vec2D normal() {
        float l = length();
        x /= l;
        y /= l;
        return this;
    }

    /**
     * Provides a string representation of the vector in the form "x:y".
     *
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return x + ":" + y;
    }
}