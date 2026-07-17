package com.github.maxos.shitSalary.vault;

import com.github.maxos.shitSalary.ShitSalary;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class VaultHook {

    private final ShitSalary plugin;
    private Economy economy;

    public VaultHook(ShitSalary plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        economy = null;
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().info("Vault не найден!");
            return;
        }
        try {
            RegisteredServiceProvider<Economy> rsp =
                    plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                plugin.getLogger().warning("У вас не установлено ни одного плагина на экономику!");
                return;
            }
            economy = rsp.getProvider();
            plugin.getLogger().info("Успешно подключена экономика: " + economy.getName());
        } catch (Throwable t) {
            plugin.getLogger().warning("Неудачное подключение к экономике: " + t.getMessage());
            economy = null;
        }
    }

    public boolean isEnabled() {
        return economy != null;
    }

    public boolean giveMoney(OfflinePlayer p, double amount) {
        if (economy == null) {
            return false;
        }
        EconomyResponse response = economy.depositPlayer(p, amount);
        return response != null && response.transactionSuccess();
    }

    public String format(double amount, String fallbackFormat) {
        if (economy != null) {
            try {
                return economy.format(amount);
            } catch (Throwable ignored) {}
        }
        return fallbackFormat.replace("{amount}", trimAmount(amount));
    }

    private static String trimAmount(double amount) {
        if (amount == Math.rint(amount) && !Double.isInfinite(amount)) {
            return Long.toString((long) amount);
        }
        return Double.toString(amount);
    }
}
