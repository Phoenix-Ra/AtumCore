package me.phoenixra.atum.core.placeholders;

import me.phoenixra.atum.core.utils.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextReplacer {
    private final HashMap<String, AtumPlaceholder> placeholders=new HashMap<>();


    public TextReplacer registerPlaceholder(@NotNull AtumPlaceholder placeholder){
        placeholders.put(placeholder.getPlaceholder(),placeholder);
        return this;
    }

    public String replace(@NotNull Player player,@NotNull String text){
        for(AtumPlaceholder placeholder: placeholders.values()){
            while (text.contains("%"+placeholder.getPlaceholder()+"_")){
                String arg = text.split("%" + placeholder.getPlaceholder() + "_")[1].split("%")[0];
                text = text.replace("%"+placeholder.getPlaceholder()+"_"+arg+"%", placeholder.replace(player,arg));
            }
            while (text.contains("%"+placeholder.getPlaceholder()+"%")){
                text = text.replace("%"+placeholder.getPlaceholder()+"%", placeholder.replace(player, null));
            }
        }
        return StringUtils.format(text);
    }
    public List<String> replace(@NotNull Player player,@NotNull List<String> list){
        List<String> newList=new ArrayList<>();
        list.forEach(s -> newList.add(replace(player,s)));
        return newList;
    }
    public void unregisterAll(){
        placeholders.clear();
    }
}
