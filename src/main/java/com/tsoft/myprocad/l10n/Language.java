package com.tsoft.myprocad.l10n;

public enum Language {
    // Order is important, as L10 resources relies on it
    ENGLISH(1),
    RUSSIAN(2);

    private int id;

    Language(int id) {
        this.id = id;
    }

    public int getId() { return id; }

    public static Language findById(int id) {
        for (Language language : values()) {
            if (language.getId() == id) return language;
        }
        return null;
    }
}
