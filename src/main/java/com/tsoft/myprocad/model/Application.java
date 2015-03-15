package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.Language;
import com.tsoft.myprocad.util.ContentManager;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Application-wide instance
 *
 * Preferences or Settings for the Application
 * One instance per application
 * Stored in user's home directory
 */
public class Application implements JsonSerializable {
    class LastDir implements JsonSerializable {
        private int contentTypeId;
        public String dir;

        private transient ContentManager.ContentType contentType;

        public LastDir() { }

        public LastDir(int contentTypeId, String dir) {
            this.contentTypeId = contentTypeId;
            this.dir = dir;
        }

        public ContentManager.ContentType getContentType() {
            if (contentType == null) contentType = ContentManager.ContentType.findById(contentTypeId);
            return contentType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LastDir lastDir = (LastDir) o;

            if (contentTypeId != lastDir.contentTypeId) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return contentTypeId;
        }

        @Override
        public void toJson(JsonWriter writer) throws IOException {
            writer
                    .write("contentTypeId", contentTypeId)
                    .write("dir", dir);
        }

        @Override
        public void fromJson(JsonReader reader) throws IOException {
            reader
                    .defInteger("contentTypeId", ((value) -> contentTypeId = value))
                    .defString("dir", ((value) -> dir = value))
                    .read();
        }
    }

    class WindowSize implements JsonSerializable {
        public String windowName;
        private int width, height;

        private transient Dimension dimension;

        public WindowSize() { }

        public WindowSize(String windowName, Dimension dimension) {
            this.windowName = windowName;
            this.width = dimension.width;
            this.height = dimension.height;
        }

        public Dimension getDimension() {
            if (dimension == null) dimension = new Dimension(width, height);
            return dimension;
        }

        public void setDimension(Dimension value) {
            width = value.width;
            height = value.height;
            dimension = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WindowSize that = (WindowSize) o;

            if (!windowName.equals(that.windowName)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return windowName.hashCode();
        }

        @Override
        public void toJson(JsonWriter writer) throws IOException {
            writer
                    .write("windowName", windowName)
                    .write("width", width)
                    .write("height", height);
        }

        @Override
        public void fromJson(JsonReader reader) throws IOException {
            dimension = null;
            reader
                    .defString("windowName", ((value) -> windowName = value))
                    .defInteger("width", ((value) -> width = value))
                    .defInteger("height", ((value) -> height = value))
                    .read();
        }
    }

    private int languageId;
    private Set<LastDir> lastDirs = new HashSet<>();
    private Set<WindowSize> windowSizes = new HashSet<>();
    private boolean isItemCreationToggled;

    private transient Language language;
    private transient static String preferencesFileName;
    private transient static Application application;

    // Don't remove this constructor, it is needed for JsonReader
    private Application() { }

    public static Application getInstance() {
        if (application == null) application = new Application();
        return application;
    }

    public static void clearInstance() { application = null; }

    public Language getLanguage() {
        if (language == null) language = Language.findById(languageId);
        return language;
    }

    public void setLanguage(Language value) {
        languageId = value.getId();
        language = value;
        flush();
    }

    public void setPreferencesFileName(String value) {
        preferencesFileName = value;
    }

    public String getLastDirectory(ContentManager.ContentType contentType) {
        Optional<LastDir> lastDir = lastDirs.stream().filter((e) -> e.getContentType().equals(contentType)).findFirst();
        return lastDir.isPresent() ? lastDir.get().dir : null;
    }

    public void setLastDirectory(ContentManager.ContentType contentType, String dir) {
        lastDirs.add(new LastDir(contentType.getId(), dir));
        flush();
    }

    public Dimension getWindowSize(String title) {
        Optional<WindowSize> windowSize = windowSizes.stream().filter((e) -> e.windowName.equals(title)).findFirst();
        return windowSize.isPresent() ? windowSize.get().getDimension() : null;
    }

    public void setWindowSize(String title, Dimension dimension) {
        windowSizes.add(new WindowSize(title, dimension));
        flush();
    }

    public boolean isItemCreationToggled() {
        return isItemCreationToggled;
    }

    public void setItemCreationToggled(boolean isItemCreationToggled) {
        this.isItemCreationToggled = isItemCreationToggled;
    }

    private void flush() {
        if (preferencesFileName == null) return;

        try (FileOutputStream out = new FileOutputStream(preferencesFileName)) {
            JsonWriter writer = new JsonWriter(out);
            writer.toJson(this);
        } catch (IOException e) {
            e.printStackTrace();

            // working without preferences
            preferencesFileName = null;
        }
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("languageId", languageId)
                .write("lastDirs", lastDirs)
                .write("windowSizes", windowSizes)
                .write("isItemCreationToggled", isItemCreationToggled);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        lastDirs = new HashSet<>();
        windowSizes = new HashSet<>();
        reader
                .defInteger("languageId", ((value) -> languageId = value))
                .defCollection("lastDirs", LastDir::new, ((value) -> lastDirs.add((LastDir)value)))
                .defCollection("windowSizes", WindowSize::new, ((value) -> windowSizes.add((WindowSize)value)))
                .defBoolean("isItemCreationToggled", ((value) -> isItemCreationToggled = value))
                .read();
    }
}
