package com.yahoo.tracebachi.EntityTracker.Commands;

import com.yahoo.tracebachi.EntityTracker.EntityTrackerPlugin;
import com.yahoo.tracebachi.EntityTracker.Listeners.TrackListener;
import com.yahoo.tracebachi.EntityTracker.LocationCounter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Trace Bachi (BigBossZee) on 8/19/2015.
 */
public class TrackTileCommand implements CommandExecutor
{
    private final TrackListener listener;

    public TrackTileCommand(TrackListener listener)
    {
        this.listener = listener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(EntityTrackerPlugin.BAD + "This command can only be run by players.");
            return true;
        }

        if(!sender.hasPermission("EntityTracker.Use"))
        {
            sender.sendMessage(EntityTrackerPlugin.BAD + "You do not have permission to run that command.");
            return true;
        }

        Player player = (Player) sender;

        if(args.length >= 2 && args[0].equalsIgnoreCase("tp"))
        {
            Integer index = parseInteger(args[1]);
            if(index == null || index <= 0)
            {
                sender.sendMessage(EntityTrackerPlugin.BAD + "Number must be greater than 0.");
                return true;
            }

            runTrackTpCommand(player, index);
        }
        else if(args.length >= 1)
        {
            Material material = Material.valueOf(args[0].toUpperCase());
            if(material == null)
            {
                sender.sendMessage(EntityTrackerPlugin.BAD + args[0] + " is not a valid tile type.");
                return true;
            }

            Integer displaySize = 3;
            if(args.length >= 2)
            {
                Integer tempDisplaySize = parseInteger(args[1]);
                if(tempDisplaySize == null || tempDisplaySize <= 0)
                {
                    sender.sendMessage(EntityTrackerPlugin.BAD + "Number must be greater than 0.");
                    return true;
                }
                else
                {
                    displaySize = tempDisplaySize;
                }
            }

            runTrackCommand(player, material, displaySize);
        }
        else
        {
            sender.sendMessage(EntityTrackerPlugin.BAD + "Commands:");
            sender.sendMessage(ChatColor.GRAY + "  /tracktile [type] [display size]");
            sender.sendMessage(ChatColor.GRAY + "  /tracktile tp [number]");
            return true;
        }
        return true;
    }

    private void runTrackTpCommand(Player player, int listIndex)
    {
        List<LocationCounter> locList = listener.get(player.getName());

        if(locList != null)
        {
            if(listIndex > locList.size() || listIndex < 1)
            {
                player.sendMessage(EntityTrackerPlugin.BAD +
                    "Invalid index. It must be between 1 ~ " + locList.size());
            }
            else
            {
                LocationCounter counter = locList.get(listIndex - 1);
                Location location = counter.getLocation(player.getWorld());

                player.sendMessage(EntityTrackerPlugin.GOOD +
                    "Teleporting to #" + listIndex);
                player.teleport(location);
            }
        }
        else
        {
            player.sendMessage(EntityTrackerPlugin.BAD +
                "No track list found. Re-run the /track command to build it.");
        }
    }

    private void runTrackCommand(Player player, Material material, int listDisplaySize)
    {
        List<LocationCounter> counterList = new ArrayList<>();

        for(Chunk chunk : player.getWorld().getLoadedChunks())
        {
            for(BlockState state : chunk.getTileEntities())
            {
                if(state.getType() == material)
                {
                    LocationCounter counter = new LocationCounter(state.getX(), state.getY(), state.getZ());
                    int index = counterList.indexOf(counter);

                    if(index >= 0)
                    {
                        counterList.get(index).increment();
                    }
                    else
                    {
                        counterList.add(counter);
                    }
                }
            }
        }

        Collections.sort(counterList, (o1, o2) -> Integer.compare(o2.getCount(), o1.getCount()));
        listener.put(player.getName(), counterList);

        player.sendMessage(EntityTrackerPlugin.GOOD + "Report for " + material + ":");
        for(int i = 0; i < listDisplaySize && i < counterList.size(); ++i)
        {
            LocationCounter counter = counterList.get(i);
            player.sendMessage(ChatColor.GRAY + " #" + ChatColor.WHITE + (i + 1) + ChatColor.GRAY +
                " : " + counter.toString());
        }
    }

    private Integer parseInteger(String str)
    {
        try
        {
            return Integer.parseInt(str);
        }
        catch(NumberFormatException ex)
        {
            return null;
        }
    }
}
