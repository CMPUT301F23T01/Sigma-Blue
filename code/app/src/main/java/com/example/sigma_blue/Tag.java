package com.example.sigma_blue;

import static android.graphics.Color.WHITE;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Stores information about a single tag.
 */
public class Tag implements Comparable<Tag> {
    private String tagText;
    private Color colour;
    private boolean isChecked = false; // For the TagManager.

    public Tag(String tagText, int colour) {
        this.tagText = tagText;
        this.colour = Color.valueOf(colour);
    }

    public Tag(String tagText, Color colour) {
        this.tagText = tagText;
        this.colour = colour;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    @Override
    public int compareTo(Tag o) {
        return this.tagText.compareTo(o.getTagText());
    }

    @Override
    public int hashCode() {
        return this.tagText.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag otherTag = (Tag) obj;
        return this.tagText.equals(otherTag.getTagText());
    }
}
