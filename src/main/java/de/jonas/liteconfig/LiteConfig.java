package de.jonas.liteconfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple configuration system that uses JSON files to store data in a specific directory.
 *
 * @author jonask
 * @version 1.0
 */
public class LiteConfig {
    private static final HashMap<String, LiteConfig> configs = new HashMap<>();
    private static String configurationDirectory = "";

    private final File file;
    private JSONObject config;

    private LiteConfig(String fileName) {
        this.file = new File(getPathname(fileName, false));
        createFileIfMissing();
        load();
    }

    /**
     * Get the JSON object of the root configuration.
     *
     * @return Root configuration JSON object.
     */
    public JSONObject getConfig() {
        return config;
    }

    /**
     * Get a string field from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return The string value of the field.
     */
    public String getString(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        return lastNode.getString(field);
    }

    /**
     * Get am integer field from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return The integer value of the field.
     */
    public int getInt(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        return lastNode.getInt(field);
    }

    /**
     * Get a double field from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return The double value of the field.
     */
    public double getDouble(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        return lastNode.getDouble(field);
    }

    /**
     * Get a boolean field from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return The boolean value of the field.
     */
    public boolean getBoolean(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        return lastNode.getBoolean(field);
    }

    /**
     * Get a JSON object from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return The JSON object of the field.
     */
    public Object getObject(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        return lastNode.get(field);
    }

    /**
     * Get a String array from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return The String array of the field.
     */
    public ArrayList<String> getStringArray(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        ArrayList<String> list = new ArrayList<>();
        lastNode.getJSONArray(field).forEach(o -> list.add((String) o));
        return list;
    }

    /**
     * Get an integer array from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return The integer array of the field.
     */
    public ArrayList<Integer> getIntArray(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        ArrayList<Integer> list = new ArrayList<>();
        lastNode.getJSONArray(field).forEach(o -> list.add((Integer) o));
        return list;
    }

    /**
     * Get a double array from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return The double array of the field.
     */
    public ArrayList<Double> getDoubleArray(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        ArrayList<Double> list = new ArrayList<>();
        lastNode.getJSONArray(field).forEach(o -> list.add((Double) o));
        return list;
    }

    /**
     * Get a boolean array from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return The boolean array of the field.
     */
    public ArrayList<Boolean> getBooleanArray(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        ArrayList<Boolean> list = new ArrayList<>();
        lastNode.getJSONArray(field).forEach(o -> list.add((Boolean) o));
        return list;
    }

    /**
     * Get a JSON object array from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return The JSON object array of the field.
     */
    public ArrayList<JSONObject> getJsonObjectArray(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        ArrayList<JSONObject> list = new ArrayList<>();
        lastNode.getJSONArray(field).forEach(o -> list.add((JSONObject) o));
        return list;
    }

    /**
     * Get an object array from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return The object array of the field.
     */
    public ArrayList<Object> getObjectArray(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        ArrayList<Object> list = new ArrayList<>();
        lastNode.getJSONArray(field).forEach(list::add);
        return list;
    }

    /**
     * Set a field in the configuration with a value of any type.
     * <br>
     * Sub-fields can be accessed by separating points.
     * <br><br>
     * You can also set JSON objects and primitive types.
     * <br>
     * For arrays, please use {@link #set(String, ArrayList)}.
     *
     * @param key   The key of the field (sub-fields are seperated by a point).
     * @param value The value of the field.
     */
    public void set(String key, Object value) {
        String[] nodes = key.split("\\.");
        JSONObject current = config;
        for (int i = 0; i < nodes.length - 1; i++) {
            if (!current.has(nodes[i])) {
                current.put(nodes[i], new JSONObject());
            }
            current = current.getJSONObject(nodes[i]);
        }
        current.put(nodes[nodes.length - 1], value);
        save();
    }

    /**
     * Store an array of any type in the configuration.
     * <br>
     * Arrays may also contain JSON objects to store complex data.
     *
     * @param key   The key of the field (sub-fields are seperated by a point).
     * @param value The array of the field.
     */
    public void set(String key, ArrayList<?> value) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        JSONArray array = new JSONArray();
        value.forEach(array::put);
        lastNode.put(field, array);
        save();
    }

    /**
     * Remove a field from the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     */
    public void remove(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        lastNode.remove(field);
        save();
    }

    /**
     * Clear the configuration.
     */
    public void clear() {
        config = new JSONObject();
        save();
    }

    /**
     * Delete the configuration file.
     */
    public void delete() {
        file.delete();
    }

    /**
     * Check if a field exists in the configuration.
     *
     * @param key The key of the field (sub-fields are seperated by a point).
     * @return Whether the field exists.
     */
    public boolean contains(String key) {
        JSONObject lastNode = getDestinationNode(key);
        String field = key.split("\\.")[key.split("\\.").length - 1];
        return lastNode.has(field);
    }

    private void createFileIfMissing() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create file " + file.getName());
            }
        }
    }

    private void load() {
        if (!file.exists()) {
            return;
        }

        try {
            String content = Files.readString(file.toPath());
            try {
                this.config = new JSONObject(content);
            } catch (Exception e) {
                this.config = new JSONObject();
                save();
                System.out.println("Created new configuration file " + file.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file " + file.getName());
        }
    }

    private void save() {
        try {
            Files.writeString(file.toPath(), config.toString(4));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file " + file.getName());
        }
    }

    private JSONObject getDestinationNode(String key) {
        String[] nodes = key.split("\\.");
        JSONObject current = config;
        for (int i = 0; i < nodes.length - 1; i++) {
            current = current.getJSONObject(nodes[i]);
        }
        return current;
    }

    /**
     * Initialize the LiteConfig system with a configuration directory and all expected configurations in it.
     * <br><br>
     * If the <code>configurationDirectory</code> starts with a slash, it will be treated as an absolute path
     * (from root directory). Otherwise, it will be treated as a relative path (from the working directory).
     * If you want to use the normal working directory, just pass an empty string.
     *
     * @param configurationDirectory The directory where the configurations are stored.
     * @param configNames            The names of the configurations (<b>without</b> a file prefix).
     */
    public static void initialize(String configurationDirectory, String... configNames) {
        LiteConfig.configurationDirectory = !configurationDirectory.endsWith("/") ? configurationDirectory
                : configurationDirectory.substring(0, configurationDirectory.length() - 1);
        for (String configName : configNames) {
            String cfName = configName.contains(".") ? configName.split("\\.")[0] : configName;
            configs.put(cfName, new LiteConfig(cfName));
        }
    }

    /**
     * Get a configuration by its name. You can access the read-/write-functions of the desired configuration through
     * this method.
     *
     * @param configName The name of the configuration.
     * @return The configuration object.
     */
    public static LiteConfig get(String configName) {
        LiteConfig config = configs.get(configName);
        if (config == null) {
            throw new RuntimeException("Configuration " + configName + " does not exist");
        }
        return config;
    }

    /**
     * Get the absolute path of the configuration directory (from root).
     *
     * @return The absolute path of the configuration directory.
     */
    public static String getAbsoluteConfigPath() {
        return getPathname(null, true);
    }

    /**
     * Get the relative path of the configuration directory (from working directory).
     *
     * @return The relative path of the configuration directory.
     */
    public static String getRelativeConfigPath() {
        return getPathname(null, false);
    }

    private static String getPathname(String fileName, boolean absolute) {
        String pathname = configurationDirectory + (configurationDirectory.endsWith("/")
                || configurationDirectory.isEmpty() || fileName == null ? "" : "/");
        if (fileName != null) {
            pathname += fileName + (fileName.endsWith(".json") ? "" : ".json");
        }
        return absolute ? new File(pathname).getAbsolutePath() : pathname;
    }
}