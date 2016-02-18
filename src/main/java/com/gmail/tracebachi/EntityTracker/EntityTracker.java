package com.gmail.tracebachi.EntityTracker;

import com.gmail.tracebachi.EntityTracker.Commands.TrackCommand;
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
    private TrackCommand trackCommand;
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

        trackCommand = new TrackCommand(playerListener);
        getCommand("track").setExecutor(trackCommand);

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
        trackCommand = null;
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
