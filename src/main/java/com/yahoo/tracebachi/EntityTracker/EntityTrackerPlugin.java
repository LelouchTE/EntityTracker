package com.yahoo.tracebachi.EntityTracker;

import com.yahoo.tracebachi.EntityTracker.Commands.TrackCommand;
import com.yahoo.tracebachi.EntityTracker.Commands.TrackSumCommand;
import com.yahoo.tracebachi.EntityTracker.Commands.TrackTileCommand;
import com.yahoo.tracebachi.EntityTracker.Commands.TrackerReloadCommand;
import com.yahoo.tracebachi.EntityTracker.Listeners.TrackListener;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by Trace Bachi (BigBossZee) on 5/28/2015.
 */
public class EntityTrackerPlugin extends JavaPlugin implements Listener
{
    public static final String GOOD =
        ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "!" + ChatColor.DARK_GRAY + "]" +
        ChatColor.GREEN + " Tracker " +
        ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    public static final String BAD =
        ChatColor.DARK_GRAY + "[" + ChatColor.RED + "!" + ChatColor.DARK_GRAY + "]" +
        ChatColor.RED + " Tracker " +
        ChatColor.DARK_GRAY + "[" + ChatColor.RED + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    private TrackListener trackListener;
    private TrackCommand trackCommand;
    private TrackTileCommand trackTileCommand;
    private TrackSumCommand trackSumCommand;
    private TrackerReloadCommand reloadCommand;

    @Override
    public void onLoad()
    {
        File file = new File(getDataFolder(), "config.yml");
        if(!file.exists()) { saveConfig(); }
    }

    @Override
    public void onEnable()
    {
        reloadConfig();
        LocationCounter.xLength = getConfig().getDouble("X-Length", 24.0);
        LocationCounter.yLength = getConfig().getDouble("Y-Length", 24.0);
        LocationCounter.zLength = getConfig().getDouble("Z-Length", 24.0);

        trackListener = new TrackListener();
        getServer().getPluginManager().registerEvents(trackListener, this);

        trackCommand = new TrackCommand(trackListener);
        getCommand("track").setExecutor(trackCommand);

        trackTileCommand = new TrackTileCommand(trackListener);
        getCommand("tracktile").setExecutor(trackTileCommand);

        trackSumCommand = new TrackSumCommand();
        getCommand("tracksum").setExecutor(trackSumCommand);

        reloadCommand = new TrackerReloadCommand(this);
        getCommand("trackreload").setExecutor(reloadCommand);
    }

    @Override
    public void onDisable()
    {
        reloadCommand = null;
        trackSumCommand = null;
        trackTileCommand = null;
        trackCommand = null;
        trackListener = null;
    }
}
