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
package com.gmail.tracebachi.EntityTracker;

import com.gmail.tracebachi.EntityTracker.Commands.TrackEntityCommand;
import com.gmail.tracebachi.EntityTracker.Commands.TrackSumCommand;
import com.gmail.tracebachi.EntityTracker.Commands.TrackTileCommand;
import com.gmail.tracebachi.EntityTracker.Commands.TrackTpCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Trace Bachi (BigBossZee) on 5/28/2015.
 */
public class EntityTracker extends JavaPlugin implements Listener
{
    private static double xLength = 24.0;
    private static double yLength = 24.0;
    private static double zLength = 24.0;

    public static final String GOOD = ChatColor.translateAlternateColorCodes('&',
        "&8[&a!&8]&a Tracker &8[&a!&8] &7");
    public static final String BAD = ChatColor.translateAlternateColorCodes('&',
        "&8[&c!&8]&c Tracker &8[&c!&8] &7");

    private TrackTpCommand trackTpCommand;
    private TrackEntityCommand trackEntityCommand;
    private TrackTileCommand trackTileCommand;
    private TrackSumCommand trackSumCommand;
    private PlayerListener playerListener;

    @Override
    public void onLoad()
    {
        saveDefaultConfig();
    }

    @Override
    public void onEnable()
    {
        reloadLengths();

        playerListener = new PlayerListener();
        getServer().getPluginManager().registerEvents(playerListener, this);

        trackTpCommand = new TrackTpCommand(playerListener);
        getCommand("tracktp").setExecutor(trackTpCommand);

        trackEntityCommand = new TrackEntityCommand(playerListener);
        getCommand("trackentity").setExecutor(trackEntityCommand);

        trackTileCommand = new TrackTileCommand(playerListener);
        getCommand("tracktile").setExecutor(trackTileCommand);

        trackSumCommand = new TrackSumCommand();
        getCommand("tracksum").setExecutor(trackSumCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(command.getName().equalsIgnoreCase("trackreload"))
        {
            if(!sender.hasPermission("EntityTracker.Use"))
            {
                sender.sendMessage(ChatColor.RED + "This command can only be run as OP.");
                return true;
            }

            reloadLengths();
            sender.sendMessage(ChatColor.GREEN + "EntityTracker configuration reloaded.");
        }

        return true;
    }

    @Override
    public void onDisable()
    {
        trackSumCommand = null;
        trackTileCommand = null;
        trackEntityCommand = null;
        trackTpCommand = null;
        playerListener = null;
    }

    public static double xLength()
    {
        return xLength;
    }

    public static double yLength()
    {
        return yLength;
    }

    public static double zLength()
    {
        return zLength;
    }

    private void reloadLengths()
    {
        reloadConfig();
        xLength = getConfig().getDouble("X-Length", 24.0);
        yLength = getConfig().getDouble("Y-Length", 24.0);
        zLength = getConfig().getDouble("Z-Length", 24.0);
    }
}
