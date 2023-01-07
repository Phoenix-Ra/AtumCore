package me.phoenixra.atum.core.utils;

import org.bukkit.Bukkit;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ClassUtils {
    public static <P> Set<Path> getPathsIn(P plugin,String path, Predicate<? super Path> filter) {
        Set<Path> files = new LinkedHashSet<>();
        String packagePath = path.replace(".", "/");

        try {
            URI uri = plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            FileSystem fileSystem = FileSystems.newFileSystem(URI.create("jar:" + uri), Collections.emptyMap());
            files = Files.walk(fileSystem.getPath(packagePath)).
                    filter(Objects::nonNull).
                    filter(filter).
                    collect(Collectors.toSet());
            fileSystem.close();
        } catch (URISyntaxException | IOException ex) {
            Bukkit.getLogger().
                    log(Level.WARNING, "An error occurred while trying to load files: " + ex.getMessage(), ex);
        }

        return files;
    }
    public static <P> Set<Class<?>> getClasses(P plugin,String packageName) {
        Set<Class<?>> classes = new LinkedHashSet<>();

        Predicate<? super Path> filter = entry -> {
            String path = entry.getFileName().toString();
            return !path.contains("$") && path.endsWith(".class");
        };

        for (Path filesPath : getPathsIn(plugin, packageName, filter)) {
            // Compatibility with different Java versions
            String path = filesPath.toString();
            if (path.charAt(0) == '/') {
                path = path.substring(1);
            }

            String fileName = path.replace("/", ".").split(".class")[0];

            try {
                Class<?> clazz1 = Class.forName(fileName);
                classes.add(clazz1);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return classes;
    }
    public static <P, T> Set<Class<? extends T>> getSubTypesOf(P plugin, String packageName, Class<?> type) {
        return getClasses(plugin,packageName).stream().
                filter(type::isAssignableFrom).
                map(aClass -> ((Class<? extends T>) aClass)).
                collect(Collectors.toSet());
    }
}
