package com.github.maxos.shitSalary.listeners;

import com.github.maxos.shitSalary.manager.CoinItemsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

public class PickupListener implements Listener {

    private final CoinItemsManager coinsManager;

    public PickupListener(CoinItemsManager coinsManager) {
        this.coinsManager = coinsManager;
    }

    @EventHandler
    public void onPickupCoins(PlayerAttemptPickupItemEvent e) {
        coinsManager.pickup(e.getPlayer(), e.getItem(), e);
    }

}
