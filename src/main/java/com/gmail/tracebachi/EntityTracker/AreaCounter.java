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

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Trace Bachi (BigBossZee) on 5/30/2015.
 */
public class AreaCounter
{
    private final double x;
    private final double y;
    private final double z;

    private int count;

    public AreaCounter(double x, double y, double z)
    {
        this(x, y, z, 1);
    }

    public AreaCounter(double x, double y, double z, int count)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
    }

    public int getCount()
    {
        return count;
    }

    public int increment()
    {
        return (count += 1);
    }

    public Location getLocation(World world)
    {
        return new Location(world, ((int) x) + 0.5, ((int) y) + 0.5, ((int) z) + 0.5);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof AreaCounter)
        {
            AreaCounter other = (AreaCounter) obj;

            return Math.abs(x - other.x) <= EntityTracker.xLength() &&
                Math.abs(y - other.y) <= EntityTracker.yLength() &&
                Math.abs(z - other.z) <= EntityTracker.zLength();
        }
        return false;
    }

    @Override
    public Object clone()
    {
        return new AreaCounter(x, y, z, count);
    }

    @Override
    public String toString()
    {
        return count + " near ( " + (int) x + " , " + (int) y + " , " + (int) z + " )";
    }
}
