package Game;

/**
 * Abstract base class representing an element in a tree structure.
 * The class provides functionality to track whether the element has been queried 
 * and also defines the requirement for subclasses to implement the method for 
 * obtaining the axis-aligned bounding box (AABB) of the element.
 */
public abstract class TreeElement {

    // Flag to track whether the element has been queried
    private boolean queried = false;

    /**
     * Sets the queried status of the element.
     *
     * @param q The queried status to set (true if queried, false otherwise).
     */
    public void setQueriedStatus(boolean q) {
        queried = q;
    }

    /**
     * Checks whether the element has been queried.
     *
     * @return true if the element has been queried, false otherwise.
     */
    public boolean hasQueried() {
        return queried;
    }

    /**
     * Abstract method that must be implemented by subclasses to return the 
     * axis-aligned bounding box (AABB) of the tree element.
     *
     * @return The AABB representing the spatial bounds of the element.
     */
    public abstract AABB getAABB();
}