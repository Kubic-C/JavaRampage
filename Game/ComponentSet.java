package Game;

import java.util.*;


/**
 * Represents a set of unique integer component IDs, where each component ID is stored in sorted order.
 * Provides methods for adding, removing, and checking membership of components.
 */
public class ComponentSet {
    private ArrayList<Integer> m_set;

    /**
     * Creates a new, empty ComponentSet.
     */
    public ComponentSet() {
        m_set = new ArrayList<Integer>();
    }

    /**
     * Creates a new ComponentSet initialized with the given list of component IDs.
     *
     * @param set The list of component IDs to initialize the set with.
     */
    public ComponentSet(ArrayList<Integer> set) {
        m_set = set;
    }

    /**
     * Returns a shallow copy of the current set.
     *
     * @return A new ArrayList containing all the elements of this set.
     */
    ArrayList<Integer> copySet() {
        ArrayList<Integer> copy = new ArrayList<Integer>();
        copy.addAll(m_set);
        return copy;
    }

    /**
     * Checks if the set contains the given component ID.
     *
     * @param id The component ID to check for.
     * @return true if the set contains the component ID, false otherwise.
     */
    public boolean contains(int id) {
        return Collections.binarySearch(m_set, id) >= 0;
    }

    /**
     * Adds the given component ID to the set, ensuring the set remains sorted.
     * If the component ID already exists in the set, it will not be added again.
     *
     * @param id The component ID to add.
     * @return A new ComponentSet with the added component ID, or the current set if the ID was already present.
     */
    public ComponentSet add(int id) {
        ArrayList<Integer> set = copySet();
        int index = Collections.binarySearch(set, id);
        if (index >= 0) {
            return new ComponentSet(m_set); // ID already present, return current set.
        }
        index = -index - 1;
        set.add(index, id);
        return new ComponentSet(set);
    }

    /**
     * Removes the given component ID from the set.
     * If the component ID does not exist in the set, the set remains unchanged.
     *
     * @param id The component ID to remove.
     * @return A new ComponentSet with the component ID removed, or the current set if the ID was not found.
     */
    public ComponentSet remove(int id) {
        ArrayList<Integer> set = copySet();
        int index = Collections.binarySearch(set, id);
        if (index < 0) {
            return new ComponentSet(m_set); // ID not found, return current set.
        }
        set.remove(index);
        return new ComponentSet(set);
    }

    /**
     * Calculates the hash code for the set based on its contents.
     *
     * @return The hash code for the set.
     */
    @Override
    public int hashCode() {
        int hash = 1;
        for (int x : m_set) {
            hash = 31 * hash + Integer.hashCode(x);
        }
        return hash;
    }

    /**
     * Compares this set to another object for equality.
     *
     * @param other The object to compare with.
     * @return true if the other object is a ComponentSet and contains the same component IDs, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) 
            return true;
        if (!(other instanceof ComponentSet)) 
            return false;
        ComponentSet o = (ComponentSet) other;
        return m_set.equals(o.m_set); 
    }

    /**
     * Checks if this set is a subset of another set.
     * A set A is a subset of set B if all elements of A are also in B.
     *
     * @param other The other ComponentSet to check against.
     * @return true if this set is a subset of the other set, false otherwise.
     */
    public boolean subsetOf(ComponentSet other) {
        for (int id : m_set) {
            if (Collections.binarySearch(other.m_set, id) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if this set is a superset of another set.
     * A set A is a superset of set B if all elements of B are also in A.
     *
     * @param other The other ComponentSet to check against.
     * @return true if this set is a superset of the other set, false otherwise.
     */
    public boolean supersetOf(ComponentSet other) {
        return other.subsetOf(this);
    }

    /**
     * Returns a string representation of the set.
     *
     * @return A string containing the list of component IDs in the set.
     */
    @Override
    public String toString() {
        return m_set.toString();
    }
}