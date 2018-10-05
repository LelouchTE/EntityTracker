package com.gmail.tracebachi.EntityTracker.Commands;

import com.gmail.tracebachi.EntityTracker.PlayerListener;
import com.gmail.tracebachi.EntityTracker.AreaCounter;
import com.gmail.tracebachi.EntityTracker.ParseUtil;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.tracebachi.EntityTracker.EntityTracker.BAD;
import static com.gmail.tracebachi.EntityTracker.EntityTracker.GOOD;

/**
 * Created by Trace Bachi (BigBossZee) on 8/19/2015.
 */
public class TrackCommand implements TabExecutor
{
    private final HashMap<String, Class<? extends Entity>> nameToClass = new HashMap<>();
    private final PlayerListener listener;

    public TrackCommand(PlayerListener listener)
    {
        this.listener = listener;

        addType("Item", EntityItem.class);
        addType("XPOrb", EntityExperienceOrb.class);
        addType("ThrownEgg", EntityEgg.class);
        addType("LeashKnot", EntityLeash.class);
        addType("Painting", EntityPainting.class);
        addType("Arrow", EntityArrow.class);
        addType("Snowball", EntitySnowball.class);
        addType("Fireball", EntityLargeFireball.class);
        addType("SmallFireball", EntitySmallFireball.class);
        addType("ThrownEnderpearl", EntityEnderPearl.class);
        addType("EyeOfEnderSignal", EntityEnderSignal.class);
        addType("ThrownPotion", EntityPotion.class);
        addType("ThrownExpBottle", EntityThrownExpBottle.class);
        addType("ItemFrame", EntityItemFrame.class);
        addType("WitherSkull", EntityWitherSkull.class);
        addType("PrimedTnt", EntityTNTPrimed.class);
        addType("FallingSand", EntityFallingBlock.class);
        addType("FireworksRocketEntity", EntityFireworks.class);
        addType("ArmorStand", EntityArmorStand.class);
        addType("Boat", EntityBoat.class);
        addType("MinecartRideable", EntityMinecartRideable.class);
        addType("MinecartChest", EntityMinecartChest.class);
        addType("MinecartFurnace", EntityMinecartFurnace.class);
        addType("MinecartTNT", EntityMinecartTNT.class);
        addType("MinecartHopper", EntityMinecartHopper.class);
        addType("MinecartSpawner", EntityMinecartMobSpawner.class);
        addType("MinecartCommandBlock", EntityMinecartCommandBlock.class);
        addType("Mob", EntityInsentient.class);
        addType("Monster", EntityMonster.class);
        addType("Creeper", EntityCreeper.class);
        addType("Skeleton", EntitySkeleton.class);
        addType("Spider", EntitySpider.class);
        addType("Giant", EntityGiantZombie.class);
        addType("Zombie", EntityZombie.class);
        addType("Slime", EntitySlime.class);
        addType("Ghast", EntityGhast.class);
        addType("PigZombie", EntityPigZombie.class);
        addType("Enderman", EntityEnderman.class);
        addType("CaveSpider", EntityCaveSpider.class);
        addType("Silverfish", EntitySilverfish.class);
        addType("Blaze", EntityBlaze.class);
        addType("LavaSlime", EntityMagmaCube.class);
        addType("EnderDragon", EntityEnderDragon.class);
        addType("WitherBoss", EntityWither.class);
        addType("Bat", EntityBat.class);
        addType("Witch", EntityWitch.class);
        addType("Endermite", EntityEndermite.class);
        addType("Guardian", EntityGuardian.class);
        addType("Pig", EntityPig.class);
        addType("Sheep", EntitySheep.class);
        addType("Cow", EntityCow.class);
        addType("Chicken", EntityChicken.class);
        addType("Squid", EntitySquid.class);
        addType("Wolf", EntityWolf.class);
        addType("MushroomCow", EntityMushroomCow.class);
        addType("SnowMan", EntitySnowman.class);
        addType("Ozelot", EntityOcelot.class);
        addType("VillagerGolem", EntityIronGolem.class);
        addType("EntityHorse", EntityHorse.class);
        addType("Rabbit", EntityRabbit.class);
        addType("Villager", EntityVillager.class);
        addType("EnderCrystal", EntityEnderCrystal.class);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        String lowerArg = strings[0].toLowerCase();

        return nameToClass.keySet().stream()
            .filter(name -> name.startsWith(lowerArg))
            .collect(Collectors.toList());
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
            sender.sendMessage(BAD + "/track <type> <display size>");
            return true;
        }

        Class<? extends Entity> type = nameToClass.get(args[0].toLowerCase());

        if(type == null)
        {
            sender.sendMessage(BAD + args[0] + " is not a valid entity type.");
            return true;
        }

        Integer displaySize = 3;

        if(args.length >= 2)
        {
            Integer tempDisplaySize = ParseUtil.parseInteger(args[1]);

            if(tempDisplaySize == null || tempDisplaySize < 1)
            {
                sender.sendMessage(BAD + "Number must be greater than 0.");
                return true;
            }
            else
            {
                displaySize = tempDisplaySize;
            }
        }

        runTrackCommand(player, type, displaySize);
        return true;
    }

    private void runTrackCommand(Player player, Class<? extends Entity> type, int listDisplaySize)
    {
        List<AreaCounter> counterList = new ArrayList<>();
        CraftWorld craftWorld = (CraftWorld) player.getWorld();

        for(Entity entity : craftWorld.getHandle().entityList)
        {
            if(type.equals(entity.getClass()))
            {
                AreaCounter counter = new AreaCounter(entity.locX, entity.locY, entity.locZ);
                int index = counterList.indexOf(counter);

                if(index >= 0)
                {
                    counterList.get(index).increment();
                }
                else
                {
                    counterList.add(counter);
                }
            }
        }

        Collections.sort(counterList, (o1, o2) -> Integer.compare(o2.getCount(), o1.getCount()));
        listener.put(player.getName(), counterList);

        player.sendMessage(GOOD + "Report for " + type.getSimpleName() + ":");

        for(int i = 0; i < listDisplaySize && i < counterList.size(); ++i)
        {
            AreaCounter counter = counterList.get(i);
            player.sendMessage(ChatColor.GRAY + " #" + ChatColor.WHITE + (i + 1) + ChatColor.GRAY +
                " : " + counter.toString());
        }
    }

    private void addType(String name, Class<? extends Entity> clazz)
    {
        nameToClass.put(name.toLowerCase(), clazz);
    }
}
