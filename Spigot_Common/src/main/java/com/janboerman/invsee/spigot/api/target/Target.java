package com.janboerman.invsee.spigot.api.target;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface Target {

    public static Target byUsername(String username) {
        return new UsernameTarget(username);
    }

    public static Target byUniqueId(UUID uniqueId) {
        return new UniqueIdTarget(uniqueId);
    }

    public static Target byPlayer(Player player) {
        return new PlayerTarget(player);
    }

}

