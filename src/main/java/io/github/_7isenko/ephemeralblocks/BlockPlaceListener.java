package io.github._7isenko.ephemeralblocks;

import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftMagicNumbers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BlockPlaceListener implements Listener {
    private List<Material> safeList = parseConfigList();
    private long ticksToBreak = EphemeralBlocks.config.getLong("timeToBreak") * 20;

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (safeList.contains(event.getBlockPlaced().getType()))
            return;
        Bukkit.getScheduler().runTaskLater(EphemeralBlocks.plugin, () -> {
            try {
                if (event.getBlockPlaced().getType() == Material.AIR)
                    return;
                Block block = CraftMagicNumbers.getBlock(event.getBlockPlaced().getType());
                SoundEffectType set = block.getBlockData().getStepSound();
                Field f = set.getClass().getDeclaredField("X");
                f.setAccessible(true);
                SoundEffect se = (SoundEffect) f.get(set);
                Location location = event.getBlockPlaced().getLocation();
                BlockPosition bp = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                ((CraftWorld) event.getPlayer().getWorld()).getHandle().playSound(null, bp, se, SoundCategory.BLOCKS, 1f, 1f);

            } catch (Exception e) {
                // ok fuck it
            }
            event.getBlockPlaced().breakNaturally();
        }, ticksToBreak);
    }

    private List<Material> parseConfigList() {
        List<String> strings = EphemeralBlocks.config.getStringList("unbreakableBlocks");
        List<Material> materials = new ArrayList<>();
        strings.forEach((s -> materials.add(Material.valueOf(s.toUpperCase()))));
        return materials;
    }
}
