package Game;

/**
 * A component to be used in an Entity-Component-System (ECS) architecture.
 * Represents the position and rotation of an entity in 2D space.
 * Provides utility methods for converting between local and world coordinates.
 */
public class TransformComponent extends Component {

    // Position of the entity in 2D space
    public Vec2D pos = new Vec2D();

    // Rotation of the entity, in radians
    public float rot;

    /**
     * Converts a local point (relative to the entity's position and rotation)
     * to a world point by applying the entity's position and rotation.
     *
     * @param localPoint The local point relative to the entity.
     * @return The corresponding world point.
     */
    Vec2D getWorldPoint(Vec2D localPoint) {
        return localPoint.clone().rotate(rot).add(pos);
    }

    /**
     * Converts a world point (in global coordinates) to a local point relative
     * to the entity's position and rotation.
     *
     * @param worldPoint The point in world coordinates.
     * @return The corresponding local point relative to the entity.
     */
    Vec2D getLocalPoint(Vec2D worldPoint) {
        return worldPoint.clone().sub(pos).rotate(-rot);
    }

    /**
     * Returns the class type of the TransformComponent.
     *
     * @return The class type of TransformComponent.
     */
    @Override
    public Class<?> getType() {
        return TransformComponent.class;
    }

    /**
     * Creates a deep copy (clone) of the TransformComponent.
     *
     * @return A new TransformComponent object with the same position and rotation.
     */
    @Override
    public Component clone() {
        TransformComponent clone = new TransformComponent();
        clone.pos = pos.clone();
        clone.rot = rot;
        return clone;
    }
}