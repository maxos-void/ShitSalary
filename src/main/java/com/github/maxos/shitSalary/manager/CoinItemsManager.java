package com.github.maxos.shitSalary.manager;

import com.github.maxos.shitSalary.ShitSalary;
import com.github.maxos.shitSalary.vault.VaultHook;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import static org.bukkit.persistence.PersistentDataType.*;

public class CoinItemsManager {

    private final NamespacedKey nameKey;
    private final NamespacedKey amountKey;
    private final ShitSalary plugin;
    private final VaultHook vault;

    public CoinItemsManager(NamespacedKey nameKey, NamespacedKey amountKey, ShitSalary plugin, VaultHook vault) {
        this.nameKey = nameKey;
        this.amountKey = amountKey;
        this.plugin = plugin;
        this.vault = vault;
    }

    private final HashMap<UUID, HashSet<UUID>> playerToCoins = new HashMap<>();

    public void spawnCoins(Location loc, double amount, Player p) {
        World w = loc.getWorld();
        double oneAmount = amount / 3;

        UUID playerUUID = p.getUniqueId();
        HashSet<UUID> oldCoins = playerToCoins.get(playerUUID);
        if (oldCoins != null) {
            forcePickup(oldCoins, playerUUID);
        }

        HashSet<UUID> coins = new HashSet<>();

        for (int a = 0; a < 3; a++) {
            Item i = w.dropItemNaturally(loc, new ItemStack(Material.EMERALD));
            i.setCustomName("§e" + oneAmount);
            i.setCustomNameVisible(true);

            PersistentDataContainer pdc = i.getPersistentDataContainer();
            pdc.set(nameKey, STRING, p.getUniqueId().toString());
            pdc.set(amountKey, DOUBLE, amount);
            coins.add(i.getUniqueId());
        }

        playerToCoins.put(playerUUID, coins);
        timer(playerUUID);
    }

    private void timer(UUID playerUUID) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            HashSet<UUID> entities = playerToCoins.get(playerUUID);
            if (entities != null) {
                forcePickup(entities, playerUUID);
            }
        }, 100L);
    }

    public void pickup(Player p, Item i, PlayerAttemptPickupItemEvent e) {
        UUID pUUID = p.getUniqueId();
        PersistentDataContainer pdc = i.getPersistentDataContainer();
        String owner = pdc.get(nameKey, STRING);
        if (owner == null) {
            return;
        }
        if (owner.contains(pUUID.toString())) {

            HashSet<UUID> coins = playerToCoins.get(pUUID);
            if (coins != null) {
                forcePickup(coins, pUUID);
                return;
            }
        }
        e.setCancelled(true);
    }

    private void forcePickup(HashSet<UUID> oldCoins, UUID playerUUID) {
        playerToCoins.remove(playerUUID);
        boolean isPay = false;
        for (UUID entityUUID : oldCoins) {
            Entity e = Bukkit.getEntity(entityUUID);
            if (e != null) {
                if (!isPay) {
                    PersistentDataContainer pdc = e.getPersistentDataContainer();
                    double amount = pdc.getOrDefault(amountKey, DOUBLE, 0.0);
                    vault.giveMoney(Bukkit.getOfflinePlayer(playerUUID), amount);
                    isPay = true;
                }
                e.remove();
            }
        }
    }

}
