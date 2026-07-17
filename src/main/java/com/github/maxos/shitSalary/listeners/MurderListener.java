package com.github.maxos.shitSalary.listeners;

import com.github.maxos.shitSalary.vault.VaultHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MurderListener implements Listener {

    private final VaultHook eco;

    public MurderListener(VaultHook eco) {
        this.eco = eco;
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        Player p = e.getEntity().getKiller();
        if (p != null) {
            eco.giveMoney(p, 100.0);
        }

    }

}
