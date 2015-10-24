package com.yahoo.tracebachi.EntityTracker.Listeners;

import com.yahoo.tracebachi.EntityTracker.LocationCounter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Trace Bachi (BigBossZee) on 10/24/2015.
 */
public class TrackListener implements Listener
{
    private final HashMap<String, List<LocationCounter>> nameToCounterList = new HashMap<>();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        nameToCounterList.remove(event.getPlayer().getName());
    }

    public List<LocationCounter> put(String name, List<LocationCounter> toAdd)
    {
        return nameToCounterList.put(name, toAdd);
    }

    public List<LocationCounter> get(String name)
    {
        return nameToCounterList.get(name);
    }

    public List<LocationCounter> remove(String name)
    {
        return nameToCounterList.remove(name);
    }
}
