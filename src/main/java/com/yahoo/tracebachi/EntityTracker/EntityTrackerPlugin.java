package com.yahoo.tracebachi.EntityTracker;

import com.yahoo.tracebachi.EntityTracker.Commands.TrackCommand;
import com.yahoo.tracebachi.EntityTracker.Commands.TrackSumCommand;
import com.yahoo.tracebachi.EntityTracker.Listeners.TrackListener;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
    private TrackSumCommand trackSumCommand;

    @Override
    public void onEnable()
    {
        trackListener = new TrackListener();
        trackCommand = new TrackCommand(trackListener);
        getCommand("track").setExecutor(trackCommand);
        getServer().getPluginManager().registerEvents(trackCommand, this);

        trackSumCommand = new TrackSumCommand();
        getCommand("tracksum").setExecutor(trackSumCommand);
    }

    @Override
    public void onDisable()
    {
        trackSumCommand = null;
        trackCommand = null;
        trackListener = null;
    }
}
