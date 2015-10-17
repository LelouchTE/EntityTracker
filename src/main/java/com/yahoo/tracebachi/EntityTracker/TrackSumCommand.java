package com.yahoo.tracebachi.EntityTracker;

import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Trace Bachi (BigBossZee) on 8/19/2015.
 */
public class TrackSumCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(EntityTracker.BAD + "This command can only be run by players.");
            return true;
        }

        if(!sender.hasPermission("EntityTracker.Use"))
        {
            sender.sendMessage(EntityTracker.BAD +
                "You do not have permission to run that command.");
            return true;
        }

        Player player = (Player) sender;
        HashMap<Class<? extends Entity>, Integer> typeCountMap = new HashMap<>();
        CraftWorld craftWorld = (CraftWorld) player.getWorld();

        for(Entity entity : craftWorld.getHandle().entityList)
        {
            Class<? extends Entity> entityClass = entity.getClass();
            Integer count = typeCountMap.get(entityClass);
            if(count == null)
            {
                typeCountMap.put(entityClass, 1);
            }
            else
            {
                typeCountMap.put(entityClass, count + 1);
            }
        }

        ArrayList<Map.Entry<Class<? extends Entity>, Integer>> summaryList =
            new ArrayList<>(typeCountMap.entrySet());
        Collections.sort(summaryList, (o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()));

        player.sendMessage(EntityTracker.GOOD + "Summary (Type, Amount):");

        for(Map.Entry<Class<? extends Entity>, Integer> entry : summaryList)
        {
            int count = entry.getValue();
            if(count > 0)
            {
                player.sendMessage(ChatColor.GRAY + " " + entry.getKey().getSimpleName() + ": " + ChatColor.WHITE + count);
            }
        }
        return true;
    }
}
