package com.gmail.tracebachi.EntityTracker.Commands;

import net.minecraft.server.v1_12_R1.Entity;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.*;

import static com.gmail.tracebachi.EntityTracker.EntityTracker.BAD;
import static com.gmail.tracebachi.EntityTracker.EntityTracker.GOOD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.WHITE;

/**
 * Created by Trace Bachi (BigBossZee) on 8/19/2015.
 */
public class TrackSumCommand implements TabExecutor
{
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        return Arrays.asList("entity", "tile");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(BAD + "This command can only be run by players.");
            return true;
        }

        if(!sender.hasPermission("EntityTracker.Use"))
        {
            sender.sendMessage(BAD + "You do not have permission to run that command.");
            return true;
        }

        if(args.length < 1)
        {
            sender.sendMessage(GOOD + "/tracksum <entity|tile>");
        }
        else if(args[0].equalsIgnoreCase("entity"))
        {
            trackNormalEntities((Player) sender);
        }
        else if(args[0].equalsIgnoreCase("tile"))
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

        player.sendMessage(GOOD + "Summary (Type, Amount):");

        for(Map.Entry<Class<? extends Entity>, Integer> entry : summaryList)
        {
            int count = entry.getValue();
            if(count > 0)
            {
                player.sendMessage(GRAY +
                    " " + entry.getKey().getSimpleName() +
                    ": " + WHITE + count);
            }
        }
    }

    private void trackTileEntities(Player player)
    {
        HashMap<Material, Integer> typeCountMap = new HashMap<>();

        for(Chunk chunk : player.getWorld().getLoadedChunks())
        {
            for(BlockState state : chunk.getTileEntities())
            {
                Material material = state.getType();
                Integer count = typeCountMap.get(material);
                typeCountMap.put(material, (count == null) ? 1 : count + 1);
            }
        }

        ArrayList<Map.Entry<Material, Integer>> summaryList = new ArrayList<>(typeCountMap.entrySet());
        Collections.sort(summaryList, (o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()));

        player.sendMessage(GOOD + "Summary (Type, Amount):");

        for(Map.Entry<Material, Integer> entry : summaryList)
        {
            int count = entry.getValue();
            if(count > 0)
            {
                player.sendMessage(GRAY +
                    " " + entry.getKey().toString() +
                    ": " + WHITE + count);
            }
        }
    }
}
