# LiteConfig

LiteConfig is a simple configuration system that uses JSON files to store data in a specific directory. ✨

## Installation

1. Add the `LiteConfig.java` file to your project.
2. Make sure you have `org.json` as a dependency.

## Usage
You can use jitpack to integrate LiteConfig in your maven or gradle project.
```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.unordentlich</groupId>
            <artifactId>LiteConfig</artifactId>
            <version>-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

If you prefer to use local library management, please check out the releases. 🔥

### Initialization
Before using LiteConfig, you need to initialize the system:
```java
LiteConfig.initialize("storage/configs", "settings", "user");
```
The first argument represents the directory where the configuration files will be stored. The rest of the arguments are the names of the configuration files.
If you want to store the configurations in the working directory itself, you can leave the first argument empty.

This method creates a configuration directory and loads the specified configuration files.

### Accessing Configuration Values
```java
LiteConfig config = LiteConfig.get("settings");
String username = config.getString("user.name");
int age = config.getInt("user.age");
boolean isActive = config.getBoolean("user.active");
```

### Setting Values
```java
config.set("user.name", "John Doe");
config.set("user.age", 25);
config.set("user.active", true);
```

### Storing and Retrieving Arrays
```java
ArrayList<String> hobbies = new ArrayList<>();
hobbies.add("Reading");
hobbies.add("Programming");
config.set("user.hobbies", hobbies);

ArrayList<String> loadedHobbies = config.getStringArray("user.hobbies");
```

### Deleting Entries
```java
config.remove("user.age");
```

### Checking if an Entry Exists
```java
if (config.contains("user.name")) {
    System.out.println("Username exists");
}
```

## Advantages
- Lightweight and simple
- Easy to use with JSON
- Supports various data types
- Automatically creates and loads configuration files

## License
This project is licensed under the MIT License. Feel free to contribute further additions. :)

