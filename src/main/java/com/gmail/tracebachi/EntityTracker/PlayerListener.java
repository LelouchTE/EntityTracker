package com.gmail.tracebachi.EntityTracker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Trace Bachi (BigBossZee) on 10/24/2015.
 */
public class PlayerListener implements Listener
{
    private final HashMap<String, List<AreaCounter>> nameToCounterList = new HashMap<>();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        nameToCounterList.remove(event.getPlayer().getName());
    }

    public List<AreaCounter> put(String name, List<AreaCounter> toAdd)
    {
        return nameToCounterList.put(name, toAdd);
    }

    public List<AreaCounter> get(String name)
    {
        return nameToCounterList.get(name);
    }
}
