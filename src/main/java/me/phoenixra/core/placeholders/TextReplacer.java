package me.phoenixra.core.placeholders;

import me.phoenixra.core.utils.PhoenixUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextReplacer {
    private final HashMap<String, PlaceholderTask> placeholders=new HashMap<>();


    public TextReplacer registerPlaceholder(@NotNull PlaceholderTask placeholder){
        placeholders.put(placeholder.getPlaceholder(),placeholder);
        return this;
    }

    public String replace(@NotNull Player player,@NotNull String text){
        for(PlaceholderTask task: placeholders.values()){
            if(text.contains("%"+task.getPlaceholder()+"_")) {
                String arg = text.split("%" + task.getPlaceholder() + "_")[1].split("%")[0];
                text = text.replace("%"+task.getPlaceholder()+"_"+arg+"%", task.getReplacement(player,arg));
            }else if(text.contains("%"+task.getPlaceholder()+"%")){
                text = text.replace("%"+task.getPlaceholder()+"%", task.getReplacement(player, null));
            }
        }
        return PhoenixUtils.colorFormat(text);
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
