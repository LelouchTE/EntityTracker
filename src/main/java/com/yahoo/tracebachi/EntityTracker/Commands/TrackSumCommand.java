package com.yahoo.tracebachi.EntityTracker.Commands;

import com.yahoo.tracebachi.EntityTracker.EntityTrackerPlugin;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.TileEntity;
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
            sender.sendMessage(EntityTrackerPlugin.BAD + "This command can only be run by players.");
            return true;
        }

        if(!sender.hasPermission("EntityTrackerPlugin.Use"))
        {
            sender.sendMessage(EntityTrackerPlugin.BAD +
                "You do not have permission to run that command.");
            return true;
        }

        if(args.length == 0)
        {
            trackNormalEntities((Player) sender);
        }
        else if(args.length >= 1 && args[0].equalsIgnoreCase("tile"))
        {
            trackTileEntities((Player) sender);
        }
        return true;
    }

    private void trackNormalEntities(Player player)
    {
        HashMap<Class<? extends Entity>, Integer> typeCountMap = new HashMap<>();
        CraftWorld craftWorld = (CraftWorld) player.getWorld();

        for(Entity entity : craftWorld.getHandle().entityList)
        {
            Class<? extends Entity> entityClass = entity.getClass();
            Integer count = typeCountMap.get(entityClass);
            typeCountMap.put(entityClass, (count == null) ? 1 : count + 1);
        }

        ArrayList<Map.Entry<Class<? extends Entity>, Integer>> summaryList =
            new ArrayList<>(typeCountMap.entrySet());
        Collections.sort(summaryList, (o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()));

        player.sendMessage(EntityTrackerPlugin.GOOD + "Summary (Type, Amount):");
        for(Map.Entry<Class<? extends Entity>, Integer> entry : summaryList)
        {
            int count = entry.getValue();
            if(count > 0)
            {
                player.sendMessage(ChatColor.GRAY + " " + entry.getKey().getSimpleName() + ": " + ChatColor.WHITE + count);
            }
        }
    }

    private void trackTileEntities(Player player)
    {
        HashMap<Class<? extends TileEntity>, Integer> typeCountMap = new HashMap<>();
        CraftWorld craftWorld = (CraftWorld) player.getWorld();

        for(TileEntity entity : craftWorld.getHandle().tileEntityList)
        {
            Class<? extends TileEntity> tileEntityClass = entity.getClass();
            Integer count = typeCountMap.get(tileEntityClass);
            typeCountMap.put(tileEntityClass, (count == null) ? 1 : count + 1);
        }

        ArrayList<Map.Entry<Class<? extends TileEntity>, Integer>> summaryList =
            new ArrayList<>(typeCountMap.entrySet());
        Collections.sort(summaryList, (o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()));

        player.sendMessage(EntityTrackerPlugin.GOOD + "Summary (Type, Amount):");
        for(Map.Entry<Class<? extends TileEntity>, Integer> entry : summaryList)
        {
            int count = entry.getValue();
            if(count > 0)
            {
                player.sendMessage(ChatColor.GRAY + " " + entry.getKey().getSimpleName() + ": " + ChatColor.WHITE + count);
            }
        }
    }
}
