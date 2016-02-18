package com.gmail.tracebachi.EntityTracker.Commands;

import com.gmail.tracebachi.EntityTracker.ParseUtil;
import com.gmail.tracebachi.EntityTracker.PlayerListener;
import com.gmail.tracebachi.EntityTracker.AreaCounter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.tracebachi.EntityTracker.EntityTracker.BAD;
import static com.gmail.tracebachi.EntityTracker.EntityTracker.GOOD;

/**
 * Created by Trace Bachi (BigBossZee) on 8/19/2015.
 */
public class TrackTileCommand implements TabExecutor
{
    private final PlayerListener listener;

    public TrackTileCommand(PlayerListener listener)
    {
        this.listener = listener;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        String lowerArg = strings[0].toLowerCase();

        return Arrays.asList(Material.values()).stream()
            .filter(material -> material.name().toLowerCase().startsWith(lowerArg))
            .map(Enum::name)
            .collect(Collectors.toList());
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

        Player player = (Player) sender;

        if(args.length >= 1)
        {
            Material material = Material.valueOf(args[0].toUpperCase());
            if(material == null)
            {
                sender.sendMessage(BAD + args[0] + " is not a valid tile type.");
                return true;
            }

            Integer displaySize = 3;
            if(args.length >= 2)
            {
                Integer tempDisplaySize = ParseUtil.parseInteger(args[1]);
                if(tempDisplaySize == null || tempDisplaySize <= 0)
                {
                    sender.sendMessage(BAD + "Number must be greater than 0.");
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
            sender.sendMessage(BAD + "/tracktile <type> <display size>");
        }
        return true;
    }

    private void runTrackCommand(Player player, Material material, int listDisplaySize)
    {
        List<AreaCounter> counterList = new ArrayList<>();

        for(Chunk chunk : player.getWorld().getLoadedChunks())
        {
            for(BlockState state : chunk.getTileEntities())
            {
                if(state.getType() == material)
                {
                    AreaCounter counter = new AreaCounter(state.getX(), state.getY(), state.getZ());
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

        player.sendMessage(GOOD + "Report for " + material + ":");
        for(int i = 0; i < listDisplaySize && i < counterList.size(); ++i)
        {
            AreaCounter counter = counterList.get(i);
            player.sendMessage(ChatColor.GRAY + " #" + ChatColor.WHITE + (i + 1) + ChatColor.GRAY +
                " : " + counter.toString());
        }
    }
}
