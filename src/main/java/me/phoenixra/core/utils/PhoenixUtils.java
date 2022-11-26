package me.phoenixra.core.utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoenixUtils {
    public static boolean HEX_COLOR_SUPPORT;

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(%([A-Za-z]+)%)");
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("&#([0-9A-Fa-f]{6})");

    static {
        try {
            ChatColor.class.getDeclaredMethod("of", String.class);
            HEX_COLOR_SUPPORT = true;
        } catch (NoSuchMethodException e) {
            HEX_COLOR_SUPPORT = false;
        }
    }

    public static Color decodeHexColor(String value){
        try {
            java.awt.Color color = java.awt.Color.decode(value);
            return Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
        }catch (Exception e){
            e.printStackTrace();
            Bukkit.getLogger().severe("Incorrect HEX code: "+value);
            return Color.WHITE;
        }
    }
    public static List<String> colorFormat(List<String> list) {
        for (int i = 0; i < list.size(); ++i) {
            list.set(i, colorFormat(list.get(i)));
        }
        return list;
    }
    public static String colorFormat(String text){
        if (HEX_COLOR_SUPPORT) {
            Matcher matcher = HEX_COLOR_PATTERN.matcher(text);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
            }
            text = matcher.appendTail(buffer).toString();
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    public static String getProgressBar(long n, long n2, int n3, String string, String string2, String string3) {
        long n4;
        double f = (double)n / (double)n2;
        long n5 = (long)((double)n3 * f);
        long n6 = n3 - n5;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PhoenixUtils.colorFormat(string2));
        for (n4 = 0; n4 < n5; ++n4) {
            stringBuilder.append(string);
        }
        stringBuilder.append(PhoenixUtils.colorFormat(string3));
        for (n4 = 0; n4 < n6; ++n4) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    public static Color parseColor(String s){
        Color color = switch (s.toLowerCase()) {
            case "aqua" -> Color.AQUA;
            case "red" -> Color.RED;
            case "green" -> Color.GREEN;
            case "blue" -> Color.BLUE;
            case "fuchsia" -> Color.FUCHSIA;
            case "gray" -> Color.GRAY;
            case "lime" -> Color.LIME;
            case "maroon" -> Color.MAROON;
            case "navy" -> Color.NAVY;
            case "olive" -> Color.OLIVE;
            case "orange" -> Color.ORANGE;
            case "purple" -> Color.PURPLE;
            case "silver" -> Color.SILVER;
            case "teal" -> Color.TEAL;
            case "yellow" -> Color.YELLOW;
            default -> Color.WHITE;
        };
        return color;
    }
    public static List<Entity> getNearbyEntities(Location location, EntityType entityType, int radius) {
        ArrayList<Entity> arrayList = new ArrayList<>();

        for (Entity entity : location.getWorld().getEntities()) {
            if(entityType!=null&&!entity.getType().equals(entityType)) continue;
            if (!isInsideArea(location, entity.getLocation(), radius)) continue;
            arrayList.add(entity);
        }
        return arrayList;
    }
    public static boolean isInsideArea(Location center, Location point, int radius) {
        int x1 = center.getBlockX();
        int z1 = center.getBlockZ();
        int x2 = point.getBlockX();
        int z2 = point.getBlockZ();
        return x2 < x1 + radius && z2 < z1 + radius && x2 > x1 - radius && z2 > z1 - radius;
    }

    public static Class<?> getNMSClass( String pack, String name) {
        String className;

        if (getServerVersion() < 17) className = "net.minecraft.server"+ getNMSVersion() + name;
        else className = "net.minecraft." + pack + '.' + name;

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new InternalError("Failed to get NMS class " + className + ". Probably, your currently using unsupported server version", e);
        }
    }
    public static int getServerVersion(){
        return Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].split("_")[1]);
    }
    public static String getNMSVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
