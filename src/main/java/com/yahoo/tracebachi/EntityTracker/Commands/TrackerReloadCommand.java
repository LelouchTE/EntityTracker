package com.yahoo.tracebachi.EntityTracker.Commands;

import com.yahoo.tracebachi.EntityTracker.EntityTrackerPlugin;
import com.yahoo.tracebachi.EntityTracker.LocationCounter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Trace Bachi (BigBossZee) on 10/25/2015.
 */
public class TrackerReloadCommand implements CommandExecutor
{
    private EntityTrackerPlugin plugin;

    public TrackerReloadCommand(EntityTrackerPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(!commandSender.isOp())
        {
            commandSender.sendMessage(ChatColor.RED + "This command can only be run as OP.");
            return true;
        }

        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        LocationCounter.xLength = config.getDouble("X-Length", 24.0);
        LocationCounter.yLength = config.getDouble("Y-Length", 24.0);
        LocationCounter.zLength = config.getDouble("Z-Length", 24.0);
        commandSender.sendMessage(ChatColor.GREEN + "EntityTracker configuration reloaded.");
        return true;
    }
}
