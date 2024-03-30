package im.ahmadabdullah.catfishing;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public final class CatFishing extends JavaPlugin implements Listener {
    private final Random random = new Random();
    private final String PREFIX = "[CatFishing] ";
    private final String catchMessage = ChatColor.YELLOW + "You caught a cat!";
    private final double catChance = 0.1;
    private final Sound celebrationSound = Sound.ENTITY_FIREWORK_ROCKET_LAUNCH;
    private final Particle celebrationParticle = Particle.FIREWORKS_SPARK;
    private final PotionEffect[] celebrationEffects = {
            new PotionEffect(PotionEffectType.SPEED, 20 * 10, 2),
            new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 2),
            new PotionEffect(PotionEffectType.LUCK, 20 * 10, 2)
    };

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info(PREFIX + "Plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(PREFIX + "Plugin disabled!");
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && random.nextDouble() < catChance) {
            Player player = event.getPlayer();
            Location playerLocation = player.getLocation();
            Cat cat = (Cat) player.getWorld().spawnEntity(playerLocation, EntityType.CAT);
            cat.setAI(true);
            player.playSound(playerLocation, celebrationSound, 1.0f, 1.0f);
            player.getWorld().spawnParticle(celebrationParticle, playerLocation, 100);
            for (PotionEffect effect : celebrationEffects) {
                player.addPotionEffect(effect);
            }
            player.sendMessage(ChatColor.GREEN + PREFIX + catchMessage);
            event.setExpToDrop(0);
            event.setCancelled(true);
        }
    }
}
