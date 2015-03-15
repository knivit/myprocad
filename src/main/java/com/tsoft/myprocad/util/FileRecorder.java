package com.tsoft.myprocad.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import com.tsoft.myprocad.model.Project;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonWriter;

public class FileRecorder {
    private static final Logger logger = Logger.getLogger(FileRecorder.class.getName());

    public boolean writeProject(Project project, String fileName) {
        File file = new File(fileName);
        if (file.exists() && !file.canWrite()) {
            logger.severe("Can't write over file " + fileName);
            return false;
        }

        try {
            File tempFile = OperatingSystem.createTemporaryFile("save", ".myprocad");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                JsonWriter writer = new JsonWriter(fos);
                writer.toJson(project);
            }

            Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.severe("Can't save project " + fileName + ". " + ex.getMessage());
        }
        return false;
    }

    public Project readProject(String fileName) {
        try (FileInputStream in = new FileInputStream(fileName)) {
            JsonReader reader = new JsonReader(in);
            Project project = new Project();
            project.fromJson(reader);
            return project;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.severe("Can't read project from " + fileName + ". " + ex.getMessage());
        }
        return null;
    }
}
