package com.example.sigma_blue;

public class Tag implements Comparable<Tag>{
    private String tagText;

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
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
