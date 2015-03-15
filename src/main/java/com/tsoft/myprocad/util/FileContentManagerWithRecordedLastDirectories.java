package com.tsoft.myprocad.util;

import com.tsoft.myprocad.model.Application;

import java.io.File;

/**
 * A file content manager that records the last directories for each content
 * in Java preferences.
 */
public class FileContentManagerWithRecordedLastDirectories extends FileContentManager {
    public FileContentManagerWithRecordedLastDirectories() {
        super();
    }

    @Override
    protected File getLastDirectory(ContentType contentType) {
        String directoryPath = null;
        if (contentType != null) {
            directoryPath = Application.getInstance().getLastDirectory(contentType);
        }

        if (directoryPath == null) {
            // get default value
            directoryPath = Application.getInstance().getLastDirectory(null);
        }

        if (directoryPath != null) {
            File directory = new File(directoryPath);
            if (directory.isDirectory()) {
                return directory;
            }
        }
        return null;
    }

    @Override
    protected void setLastDirectory(ContentType contentType, File directory) {
        // Last directories are not recorded in user preferences since there's no need of portability
        // from a computer to an other
        if (directory == null) return;

        String directoryPath = directory.getAbsolutePath();
        if (contentType != null) {
            Application.getInstance().setLastDirectory(contentType, directoryPath);
        }

        if (directoryPath != null) {
            // set default value
            Application.getInstance().setLastDirectory(ContentType.DEFAULT, directoryPath);
        }
    }
}
