package com.github.maxos.shitSalary.config;

import com.github.maxos.shitSalary.ShitSalary;
import com.github.maxos.shitSalary.paid.PaidEntity;
import com.github.maxos.shitSalary.paid.PaidType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Set;

import static com.github.maxos.shitSalary.ShitSalary.log;

public class CfgParser {

    private FileConfiguration cfg;

    public CfgParser(FileConfiguration cfg) {
        this.cfg = cfg;
    }

    public EnumMap<EntityType, PaidEntity> parse() {

        EnumMap<EntityType, PaidEntity> paidEntities = new EnumMap<>(EntityType.class);

        ConfigurationSection entitiesSection = cfg.getConfigurationSection("entities");
        if (entitiesSection != null) {

            Set<String> entityIds =  entitiesSection.getKeys(false);
            for (String s : entityIds) {
                parseEntity(entitiesSection.getConfigurationSection(s), paidEntities);
            }

        }

        return paidEntities;
    }

    private void parseEntity(ConfigurationSection entitySection, EnumMap<EntityType, PaidEntity> paidEntities) {
        if (entitySection != null) {

            String entityId = entitySection.getString(entitySection.getName(), "");
            EntityType type = getEntityType(entityId);
            if (type == null) {
                return;
            }

            ConfigurationSection priceSection = entitySection.getConfigurationSection("price");
            double minPay = 0;
            double maxPay = 0;
            if (priceSection != null) {
                minPay = priceSection.getDouble("min", 0);
                maxPay = priceSection.getDouble("max", 0);
            }

            String paidTypeId = entitySection.getString("type-withdrawal", "virtual");
            PaidType paidType = getPaidType(paidTypeId);
            paidEntities.put(type, new PaidEntity(minPay, maxPay, paidType));

        }
    }

    @Nullable
    private EntityType getEntityType(String entityId) {
        try {
            return EntityType.valueOf(entityId);
        } catch (IllegalArgumentException e) {
            log.error("Некорретный ID энтити > {}", entityId);
            return null;
        }
    }

    @NotNull
    private PaidType getPaidType(String id) {
        try {
            return PaidType.valueOf(id.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Некорретный type-withdrawal > {}. Принудительно установлен VIRTUAL", id);
            return PaidType.VIRTUAL;
        }
    }

}
