package Game;

/**
 * Represents a generic component that can be attached to an entity.
 * Subclasses should implement specific behavior for each type of component.
 */
public abstract class Component {

	/**
	 * Returns the type of the component.
	 * 
	 * @return The class type of the component.
	 */
	public abstract Class<?> getType();

	/**
	 * Creates a clone of the component.
	 * 
	 * @return A new instance that is a copy of the current component.
	 */
	public abstract Component clone();

	/**
	 * Callback method called when the component is removed from an entity.
	 * Default implementation does nothing. Can be overridden by subclasses.
	 */
	public void onRemove() {}
}