package com.yahoo.tracebachi.EntityTracker;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Trace Bachi (BigBossZee) on 5/28/2015.
 */
public class EntityTracker extends JavaPlugin implements Listener
{
    public static final String GOOD = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "!" + ChatColor.DARK_GRAY + "]" +
        ChatColor.GREEN + " Tracker " + ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    public static final String BAD = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "!" + ChatColor.DARK_GRAY + "]" +
        ChatColor.RED + " Tracker " + ChatColor.DARK_GRAY + "[" + ChatColor.RED + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    private TrackCommand trackCommand;
    private TrackSumCommand trackSumCommand;

    @Override
    public void onEnable()
    {
        trackCommand = new TrackCommand();
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
    }
}
