package Game;

/**
 * A component used in an Entity-Component-System (ECS) architecture.
 * This component holds a reference to a RigidBody and its associated world data.
 */
public class RigidBodyComponent extends Component {
    
    /**
     * The RigidBody associated with this component.
     */
    public RigidBody body;
    
    /**
     * Constructs a new RigidBodyComponent with the given RigidBody.
     * 
     * @param bodyId The RigidBody to associate with this component.
     */
    public RigidBodyComponent(RigidBody bodyId) {
        body = bodyId;
    }

    /**
     * Called when the component is removed.
     * Destroys the associated RigidBody in the world and nullifies the reference.
     */
    @Override
    public void onRemove() {
        body.getWorld().destroy(body.id());
        body = null;
    }
    
    /**
     * Returns the class type of this component.
     * 
     * @return The class type of RigidBodyComponent.
     */
    @Override
    public Class<?> getType() {
        return RigidBodyComponent.class;
    }

    /**
     * Clones this component. Currently not implemented.
     * 
     * @return null as cloning is not supported in this implementation.
     */
    @Override
    public Component clone() {
        return null;
    }
}