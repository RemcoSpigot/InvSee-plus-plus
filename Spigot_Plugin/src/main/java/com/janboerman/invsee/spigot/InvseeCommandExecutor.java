package com.janboerman.invsee.spigot;

import com.janboerman.invsee.spigot.api.InvseeAPI;
import com.janboerman.invsee.spigot.api.MainSpectatorInventory;
import com.janboerman.invsee.spigot.perworldinventory.PerWorldInventorySeeApi;
import com.janboerman.invsee.spigot.perworldinventory.PwiCommandArgs;
import com.janboerman.invsee.utils.Either;
import me.ebonjaeger.perworldinventory.data.ProfileKey;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

class InvseeCommandExecutor implements CommandExecutor {

    private final InvseePlusPlus plugin;

    InvseeCommandExecutor(InvseePlusPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return false;
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        String playerNameOrUUID = args[0];
        boolean isUuid;
        UUID uuid = null;
        try {
            uuid = UUID.fromString(playerNameOrUUID);
            isUuid = true;
        } catch (IllegalArgumentException e) {
            isUuid = false;
        }

        InvseeAPI api = plugin.getApi();
        CompletableFuture<Optional<MainSpectatorInventory>> future = null;

        if (args.length > 1 && api instanceof PerWorldInventorySeeApi) {
            String pwiArgument = args[1];
            PerWorldInventorySeeApi pwiApi = (PerWorldInventorySeeApi) api;

            Either<String, PwiCommandArgs> either = PwiCommandArgs.parse(pwiArgument, pwiApi.getHook());
            if (either.isLeft()) {
                player.sendMessage(ChatColor.RED + either.getLeft());
                return true;
            }

            PwiCommandArgs pwiOptions = either.getRight();
            CompletableFuture<Optional<UUID>> uuidFuture = isUuid
                    ? CompletableFuture.completedFuture(Optional.of(uuid))
                    : pwiApi.fetchUniqueId(playerNameOrUUID);

            final boolean finalIsUuid = isUuid;
            future = uuidFuture.thenCompose(optId -> {
                if (optId.isPresent()) {
                    UUID uniqueId = optId.get();
                    ProfileKey profileKey = PwiCommandArgs.toProfileKey(uniqueId, pwiOptions, pwiApi.getHook());
                    String playerName = finalIsUuid ? "InvSee++ Player" : playerNameOrUUID;
                    return pwiApi.spectateInventory(uniqueId, playerName, playerName + "'s inventory", profileKey);
                } else {
                    return CompletableFuture.completedFuture(Optional.empty());
                }
            });
        }

        if (future == null) {
            //No PWI argument - just continue with the regular method
            if (isUuid) {
                //TODO UUID -> name conversion
                future = api.spectateInventory(uuid, "InvSee++ Player", playerNameOrUUID + "'s inventory");
            } else {
                future = api.spectateInventory(playerNameOrUUID, playerNameOrUUID + "'s inventory");
            }
        }

        future.whenComplete((optionalSpectatorInv, throwable) -> {
            if (throwable == null) {
                optionalSpectatorInv.ifPresentOrElse(player::openInventory, () -> player.sendMessage(ChatColor.RED + "Player " + playerNameOrUUID + " does not exist."));
            } else {
                player.sendMessage(ChatColor.RED + "An error occurred while trying to open " + playerNameOrUUID + "'s inventory.");
                plugin.getLogger().log(Level.SEVERE, "Error while trying to create main-inventory spectator inventory", throwable);
            }
        });

        return true;
    }

}
