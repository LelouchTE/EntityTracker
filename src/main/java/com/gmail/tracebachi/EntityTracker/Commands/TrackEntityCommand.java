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
package com.gmail.tracebachi.EntityTracker.Commands;

import com.gmail.tracebachi.EntityTracker.AreaCounter;
import com.gmail.tracebachi.EntityTracker.ParseUtil;
import com.gmail.tracebachi.EntityTracker.PlayerListener;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_10_R1.*;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.tracebachi.EntityTracker.EntityTracker.BAD;
import static com.gmail.tracebachi.EntityTracker.EntityTracker.GOOD;
import static net.md_5.bungee.api.ChatColor.GRAY;
import static net.md_5.bungee.api.ChatColor.WHITE;

/**
 * Created by Trace Bachi (BigBossZee) on 8/19/2015.
 */
public class TrackEntityCommand implements TabExecutor
{
    private final HashMap<String, Class<? extends Entity>> nameToClass = new HashMap<>();
    private final PlayerListener listener;

    public TrackEntityCommand(PlayerListener listener)
    {
        this.listener = listener;

        addType(EntityItem.class, "Item");
        addType(EntityExperienceOrb.class, "XPOrb");
        addType(EntityAreaEffectCloud.class, "AreaEffectCloud");

        addType(EntityEgg.class, "ThrownEgg");
        addType(EntityLeash.class, "LeashKnot");
        addType(EntityPainting.class, "Painting");
        addType(EntityTippedArrow.class, "Arrow");
        addType(EntitySnowball.class, "Snowball");
        addType(EntityLargeFireball.class, "Fireball");
        addType(EntitySmallFireball.class, "SmallFireball");
        addType(EntityEnderPearl.class, "ThrownEnderpearl");
        addType(EntityEnderSignal.class, "EyeOfEnderSignal");
        addType(EntityPotion.class, "ThrownPotion");
        addType(EntityThrownExpBottle.class, "ThrownExpBottle");
        addType(EntityItemFrame.class, "ItemFrame");
        addType(EntityWitherSkull.class, "WitherSkull");

        addType(EntityTNTPrimed.class, "PrimedTnt");
        addType(EntityFallingBlock.class, "FallingSand");
        addType(EntityFireworks.class, "FireworksRocketEntity");
        addType(EntitySpectralArrow.class, "SpectralArrow");
        addType(EntityShulkerBullet.class, "ShulkerBullet");
        addType(EntityDragonFireball.class, "DragonFireball");

        addType(EntityArmorStand.class, "ArmorStand");

        addType(EntityBoat.class, "Boat");
        addType(EntityMinecartRideable.class, EntityMinecartAbstract.EnumMinecartType.RIDEABLE.b());
        addType(EntityMinecartChest.class, EntityMinecartAbstract.EnumMinecartType.CHEST.b());
        addType(EntityMinecartFurnace.class, EntityMinecartAbstract.EnumMinecartType.FURNACE.b());
        addType(EntityMinecartTNT.class, EntityMinecartAbstract.EnumMinecartType.TNT.b());
        addType(EntityMinecartHopper.class, EntityMinecartAbstract.EnumMinecartType.HOPPER.b());
        addType(EntityMinecartMobSpawner.class, EntityMinecartAbstract.EnumMinecartType.SPAWNER.b());
        addType(EntityMinecartCommandBlock.class, EntityMinecartAbstract.EnumMinecartType.COMMAND_BLOCK.b());

        addType(EntityInsentient.class, "Mob");
        addType(EntityMonster.class, "Monster");

        addType(EntityCreeper.class, "Creeper");
        addType(EntitySkeleton.class, "Skeleton");
        addType(EntitySpider.class, "Spider");
        addType(EntityGiantZombie.class, "Giant");
        addType(EntityZombie.class, "Zombie");
        addType(EntitySlime.class, "Slime");
        addType(EntityGhast.class, "Ghast");
        addType(EntityPigZombie.class, "PigZombie");
        addType(EntityEnderman.class, "Enderman");
        addType(EntityCaveSpider.class, "CaveSpider");
        addType(EntitySilverfish.class, "Silverfish");
        addType(EntityBlaze.class, "Blaze");
        addType(EntityMagmaCube.class, "LavaSlime");
        addType(EntityEnderDragon.class, "EnderDragon");
        addType(EntityWither.class, "WitherBoss");
        addType(EntityBat.class, "Bat");
        addType(EntityWitch.class, "Witch");
        addType(EntityEndermite.class, "Endermite");
        addType(EntityGuardian.class, "Guardian");
        addType(EntityShulker.class, "Shulker");

        addType(EntityPig.class, "Pig");
        addType(EntitySheep.class, "Sheep");
        addType(EntityCow.class, "Cow");
        addType(EntityChicken.class, "Chicken");
        addType(EntitySquid.class, "Squid");
        addType(EntityWolf.class, "Wolf");
        addType(EntityMushroomCow.class, "MushroomCow");
        addType(EntitySnowman.class, "SnowMan");
        addType(EntityOcelot.class, "Ozelot");
        addType(EntityIronGolem.class, "VillagerGolem");
        addType(EntityHorse.class, "EntityHorse");
        addType(EntityRabbit.class, "Rabbit");
        addType(EntityPolarBear.class, "PolarBear");

        addType(EntityVillager.class, "Villager");

        addType(EntityEnderCrystal.class, "EnderCrystal");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        String lowerArg = strings[0].toLowerCase();

        return nameToClass.keySet().stream()
            .filter(name -> name.contains(lowerArg))
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
            sender.sendMessage(BAD + "/trackentity <type> <display size>");
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
            Location loc = counter.getLocation(player.getWorld());
            TextComponent clickableResult = new TextComponent(TextComponent.fromLegacyText(
                GRAY + " #" +
                WHITE + (i + 1) +
                GRAY + " : " + counter.toString()));

            clickableResult.setClickEvent(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/tppos " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ()));

            player.spigot().sendMessage(clickableResult);
        }
    }

    private void addType(Class<? extends Entity> clazz, String name)
    {
        nameToClass.put(name.toLowerCase(), clazz);
    }
}
