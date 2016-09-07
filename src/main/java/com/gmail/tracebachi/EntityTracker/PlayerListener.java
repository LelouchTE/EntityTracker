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
