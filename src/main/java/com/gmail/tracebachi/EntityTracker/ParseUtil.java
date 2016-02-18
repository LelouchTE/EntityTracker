package com.gmail.tracebachi.EntityTracker;

/**
 * Created by Trace Bachi (tracebachi@gmail.com, BigBossZee) on 2/17/16.
 */
public interface ParseUtil
{
    static Integer parseInteger(String str)
    {
        try
        {
            return Integer.parseInt(str);
        }
        catch(NumberFormatException ex)
        {
            return null;
        }
    }
}
