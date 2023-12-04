package com.example.sigma_blue.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the selected tab in the edit/details fragments.
 */
public enum TabSelected
{
    Details (0), Tags (1), Photos (2);

    private final int position;
    TabSelected(int position)
    {
        this.position = position;
    }

    private static final Map<Integer, TabSelected> map = new HashMap<>(values().length, 1);
    static {
        for (TabSelected c : values()) map.put(c.position(), c);
    }

    public static TabSelected of(int position) {
        TabSelected result = map.get(position);
        if (result == null) {
            throw new IllegalArgumentException("Position out of bounds. Must be within 0-2");
        }
        return result;
    }

    public int position() { return position; }
}
