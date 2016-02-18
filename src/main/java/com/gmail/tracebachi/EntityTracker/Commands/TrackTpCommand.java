package com.gmail.tracebachi.EntityTracker.Commands;

import com.gmail.tracebachi.EntityTracker.PlayerListener;
import com.gmail.tracebachi.EntityTracker.AreaCounter;
import com.gmail.tracebachi.EntityTracker.ParseUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.gmail.tracebachi.EntityTracker.EntityTracker.BAD;
import static com.gmail.tracebachi.EntityTracker.EntityTracker.GOOD;

/**
 * Created by Trace Bachi (BigBossZee) on 8/19/2015.
 */
public class TrackTpCommand implements CommandExecutor
{
    private final PlayerListener listener;

    public TrackTpCommand(PlayerListener listener)
    {
        this.listener = listener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(BAD + "This command can only be run by players.");
            return true;
        }

        if(!sender.hasPermission("EntityTracker.Use"))
        {
            sender.sendMessage(BAD + "You do not have permission to run that command.");
            return true;
        }

        Player player = (Player) sender;

        if(args.length < 1)
        {
            sender.sendMessage(BAD + "/tracktp <number>");
        }
        else
        {
            Integer index = ParseUtil.parseInteger(args[0]);

            if(index == null || index <= 0)
            {
                sender.sendMessage(BAD + "Number must be greater than 0.");
                return true;
            }

            List<AreaCounter> locList = listener.get(player.getName());

            if(locList == null)
            {
                player.sendMessage(BAD + "No track list found. Rerun a track command to build it.");
                return true;
            }

            if(index > locList.size() || index < 1)
            {
                player.sendMessage(BAD + "Invalid index. It must be between 1 ~ " + locList.size());
                return true;
            }

            AreaCounter counter = locList.get(index - 1);
            Location location = counter.getLocation(player.getWorld());

            player.sendMessage(GOOD + "Teleporting to #" + index);
            player.teleport(location);
        }

        return true;
    }
}
