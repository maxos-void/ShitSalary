package com.github.maxos.shitSalary.manager;

import com.github.maxos.shitSalary.paid.PaidEntity;
import com.github.maxos.shitSalary.paid.PaidType;
import com.github.maxos.shitSalary.random.RandomPay;
import com.github.maxos.shitSalary.vault.VaultHook;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.EnumMap;

public class MurderManager {

    private final EnumMap<EntityType, PaidEntity> paidEntities;
    private final VaultHook vault;
    private final RandomPay randomPay = new RandomPay();
    private final CoinItemsManager coinsManager;

    public MurderManager(EnumMap<EntityType, PaidEntity> paidEntities, VaultHook vault, CoinItemsManager coinsManager) {
        this.paidEntities = paidEntities;
        this.vault = vault;
        this.coinsManager = coinsManager;
    }

    private void reload(EnumMap<EntityType, PaidEntity> paidEntities) {
        this.paidEntities.clear();
        this.paidEntities.putAll(paidEntities);
    }

    public void onMurder(Player p, LivingEntity e) {
        PaidEntity paidEntity = paidEntities.get(e.getType());
        double amount = getAmount(paidEntity.minPay(), paidEntity.maxPay());
        if (!physicalDrop(paidEntity, amount, p, e)) {
            pay(p, amount);
        }
    }

    private void pay(Player p, double amount) {
        vault.giveMoney(p, amount);
    }

    private boolean physicalDrop(PaidEntity paidEntity, double amount, Player p, LivingEntity e) {
        if (paidEntity.paidType() == PaidType.PHYSICAL) {
            coinsManager.spawnCoins(e.getLocation().clone(), amount, p);
            return true;
        } else {
            return false;
        }
    }

    private double getAmount(double min, double max) {
        return randomPay.getRandomPay(min, max);
    }

}
