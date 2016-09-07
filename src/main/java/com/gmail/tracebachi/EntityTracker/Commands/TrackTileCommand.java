/*
 * This file is part of EntityTracker.
 *
 * DeltaRedis is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EntityTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EntityTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gmail.tracebachi.EntityTracker.Commands;

import com.gmail.tracebachi.EntityTracker.AreaCounter;
import com.gmail.tracebachi.EntityTracker.ParseUtil;
import com.gmail.tracebachi.EntityTracker.PlayerListener;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.Location;
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
import static net.md_5.bungee.api.ChatColor.GRAY;
import static net.md_5.bungee.api.ChatColor.WHITE;

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

        return Arrays.stream(Material.values())
            .filter(material -> material.name().toLowerCase().contains(lowerArg))
            .map(Enum::name)
            .map(String::toLowerCase)
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

        if(args.length < 1)
        {
            sender.sendMessage(BAD + "/tracktile <type> <display size>");
            return true;
        }

        Material material;

        try
        {
            material = Material.valueOf(args[0].toUpperCase());
        }
        catch(IllegalArgumentException e)
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
            Location loc = counter.getLocation(player.getWorld());
            TextComponent clickableResult = new TextComponent(TextComponent.fromLegacyText(
                GRAY + " #" +
                WHITE + (i + 1) +
                GRAY + " : " + counter.toString()));

            clickableResult.setClickEvent(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/tppos " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ()));

            player.spigot().sendMessage(clickableResult);
        }
    }
}
