package com.example.sigma_blue;

/**
 * Interface that enforces methods that are required for a container to be
 * adaptable to the android BaseAdapter
 */
public interface IAdaptable<E> {
    public int getCount();
    public E getItem(int position);
    public int getItemId(int position);
}
