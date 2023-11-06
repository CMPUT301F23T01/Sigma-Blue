package com.example.sigma_blue;

public enum DatabaseNames {
    PRIMARY_COLLECTION("SigmaBlue"), ITEMS("Item"), TAGS("Tag"),
    PWDPATH("AccountInfo");

    private String name;
    private DatabaseNames(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
