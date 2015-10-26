package com.yahoo.tracebachi.EntityTracker;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Trace Bachi (BigBossZee) on 5/30/2015.
 */
public class LocationCounter
{
    public static double xLength = 24.0D;
    public static double yLength = 24.0D;
    public static double zLength = 24.0D;

    private double x;
    private double y;
    private double z;
    private int count;

    public LocationCounter(double x, double y, double z)
    {
        this(x, y, z, 1);
    }

    public LocationCounter(double x, double y, double z, int count)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
    }

    public Location getLocation(World world)
    {
        return new Location(world, ((int) x) + 0.5, y, ((int) z) + 0.5);
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public void increment()
    {
        this.count++;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof LocationCounter)) { return false; }

        return nearEnough((LocationCounter) obj);
    }

    @Override
    public String toString()
    {
        return count + " near ( " + (int) x + " , " + (int) y + " , " + (int) z + " )";
    }

    private boolean nearEnough(LocationCounter counter)
    {
        return Math.abs(x - counter.x) <= xLength &&
            Math.abs(y - counter.y) <= yLength &&
            Math.abs(z - counter.z) <= zLength;
    }
}
