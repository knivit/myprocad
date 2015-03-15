package com.tsoft.myprocad.util;

import com.tsoft.myprocad.l10n.L10;

public interface ContentManager {
    public enum ContentType {
        DEFAULT(0, "Default", ""),
        MY_PRO_CAD(1, L10.get(L10.MYPROCAD_FILES), ".mpc"),
        PDF(2, L10.get(L10.PDF_FILES), ".pdf"),
        OBJ(3, L10.get(L10.OBJ_FILES), ".obj");

        private int id;
        private String description;
        private String defaultFileExtension;

        ContentType(int id, String description, String defaultFileExtension) {
            this.id = id;
            this.description = description;
            this.defaultFileExtension = defaultFileExtension;
        }

        public int getId() { return id; }

        public String getDescription() { return description; }

        public String getDefaultFileExtension() { return defaultFileExtension; }

        public static ContentType findById(int id) {
            for (ContentType contentType : values()) {
                if (contentType.getId() == id) return contentType;
            }
            return null;
        }
    }

    /**
     * Returns the content location chosen by user with an open content dialog.
     * @return the chosen content location or <code>null</code> if user canceled its choice.
     */
    public abstract String showOpenDialog(String dialogTitle, ContentType contentType);

    /**
     * Returns the content location chosen by user with a save content dialog.
     * If the returned location already exists, this method should have confirmed
     * if the user wants to overwrite it before return.
     * @return the chosen content location or <code>null</code> if user canceled its choice.
     */
    public abstract String showSaveDialog(String dialogTitle, ContentType contentType, String location);
}