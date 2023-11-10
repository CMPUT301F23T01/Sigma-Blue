package com.example.sigma_blue;

/**
 * Enumeration for the names used in database
 */
public enum DatabaseNames {
    PRIMARY_COLLECTION("SigmaBlue"), ITEMS_COLLECTION("Item"),
    TAGS_COLLECTION("Tag"), ACCOUNT_INFO_COLLECTION("AccountInfo"),
    USER_INFO_DOCUMENT("UserInfo");

    private String name;

    /**
     * Constructor method
     */
    DatabaseNames(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
