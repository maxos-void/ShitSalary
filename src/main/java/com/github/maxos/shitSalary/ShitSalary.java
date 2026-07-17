package com.github.maxos.shitSalary;

import com.github.maxos.shitSalary.vault.VaultHook;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public final class ShitSalary extends JavaPlugin {

    public static Logger log;

    @Override
    public void onEnable() {

        NamespacedKey nameKey = new NamespacedKey(this, "name-key");
        NamespacedKey amountKey = new NamespacedKey(this, "amount-key");

        log = this.getSLF4JLogger();
        saveDefaultConfig();

        VaultHook vault = new VaultHook(this);
        vault.setup();
        if (!vault.isEnabled()) {
            this.onDisable();
        }
    }

    @Override
    public void onDisable() {
    }
}
