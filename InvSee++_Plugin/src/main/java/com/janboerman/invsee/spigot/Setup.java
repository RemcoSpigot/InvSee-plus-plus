package com.janboerman.invsee.spigot;

import com.janboerman.invsee.spigot.api.OfflinePlayerProvider;
import com.janboerman.invsee.spigot.internal.InvseePlatform;
import com.janboerman.invsee.spigot.internal.MappingsVersion;
import com.janboerman.invsee.spigot.internal.NamesAndUUIDs;
import com.janboerman.invsee.spigot.internal.OpenSpectatorsCache;
import com.janboerman.invsee.spigot.api.Scheduler;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

public interface Setup {

    public InvseePlatform platform();

    public default OfflinePlayerProvider offlinePlayerProvider() {
        return OfflinePlayerProvider.Dummy.INSTANCE;
    }

    public static Setup setup(Plugin plugin, Scheduler scheduler, NamesAndUUIDs lookup, OpenSpectatorsCache cache) {
        final Server server = plugin.getServer();
        final String serverClassName = server.getClass().getName();

        if ("org.bukkit.craftbukkit.v1_8_R3.CraftServer".equals(serverClassName)) {
            return new Impl_1_8_8(plugin, lookup, scheduler, cache);
        } else if ("org.bukkit.craftbukkit.v1_12_R1.CraftServer".equals(serverClassName)) {
            return new Impl_1_12_2(plugin, lookup, scheduler, cache);
        } else if ("org.bukkit.craftbukkit.v1_15_R1.CraftServer".equals(serverClassName)) {
            return new Impl_1_15_2(plugin, lookup, scheduler, cache);
        } else if ("org.bukkit.craftbukkit.v1_16_R3.CraftServer".equals(serverClassName)) {
            return new Impl_1_16_5(plugin, lookup, scheduler, cache);
        } else if ("org.bukkit.craftbukkit.v1_17_R1.CraftServer".equals(serverClassName)) {
            switch (MappingsVersion.getMappingsVersion(server)) {
                case MappingsVersion._1_17_1:
                    return new Impl_1_17_1(plugin, lookup, scheduler, cache);
            }
        } else if ("org.bukkit.craftbukkit.v1_18_R2.CraftServer".equals(serverClassName)) {
            switch (MappingsVersion.getMappingsVersion(server)) {
                case MappingsVersion._1_18_2:
                    return new Impl_1_18_2(plugin, lookup, scheduler, cache);
            }
        } else if ("org.bukkit.craftbukkit.v1_19_R1.CraftServer".equals(serverClassName)) {
            switch (MappingsVersion.getMappingsVersion(server)) {
                case MappingsVersion._1_19_2:
                    return new Impl_1_19_2(plugin, lookup, scheduler, cache);
            }
        } else if ("org.bukkit.craftbukkit.v1_19_R2.CraftServer".equals(serverClassName)) {
            switch (MappingsVersion.getMappingsVersion(server)) {
                case MappingsVersion._1_19_3:
                    return new Impl_1_19_3(plugin, lookup, scheduler, cache);
            }
        } else if ("org.bukkit.craftbukkit.v1_19_R3.CraftServer".equals(serverClassName)) {
            switch (MappingsVersion.getMappingsVersion(server)) {
                case MappingsVersion._1_19_4:
                    return new Impl_1_19_4(plugin, lookup, scheduler, cache);
            }
        }

        if (server.getClass().getSimpleName().equals("CraftServer")) {
            throw new RuntimeException("Unsupported CraftBukkit version. Please run on one of [1.8.8, 1.12.2, 1.15.2, 1.16.5, 1.17.1, 1.18.2, 1.19.3, 1.19.4]. Are you running the latest InvSee++?");
        } else {
            throw new RuntimeException("Unsupported server software. Please run on (a fork of) CraftBukkit.");
        }
    }

}

//we use separate classes per implementation, to prevent classloading of an incorrect version.
//previously, the Setup#setup(Plugin) method tried to load all implementation classes, even before any of them was needed.

class Impl_1_8_8 extends SetupImpl {
    Impl_1_8_8(Plugin plugin, NamesAndUUIDs lookup, Scheduler scheduler, OpenSpectatorsCache cache) {
        super(new com.janboerman.invsee.spigot.impl_1_8_R3.InvseeImpl(plugin, lookup, scheduler, cache), new com.janboerman.invsee.spigot.impl_1_8_R3.KnownPlayersProvider(plugin, scheduler));
    }
}

class Impl_1_12_2 extends SetupImpl {
    Impl_1_12_2(Plugin plugin, NamesAndUUIDs lookup, Scheduler scheduler, OpenSpectatorsCache cache) {
        super(new com.janboerman.invsee.spigot.impl_1_12_R1.InvseeImpl(plugin, lookup, scheduler, cache), new com.janboerman.invsee.spigot.impl_1_12_R1.KnownPlayersProvider(plugin, scheduler));
    }
}

class Impl_1_15_2 extends SetupImpl {
    Impl_1_15_2(Plugin plugin, NamesAndUUIDs lookup, Scheduler scheduler, OpenSpectatorsCache cache) {
        super(new com.janboerman.invsee.spigot.impl_1_15_R1.InvseeImpl(plugin, lookup, scheduler, cache), new com.janboerman.invsee.spigot.impl_1_15_R1.KnownPlayersProvider(plugin, scheduler));
    }
}

class Impl_1_16_5 extends SetupImpl {
    Impl_1_16_5(Plugin plugin, NamesAndUUIDs lookup, Scheduler scheduler, OpenSpectatorsCache cache) {
        super(new com.janboerman.invsee.spigot.impl_1_16_R3.InvseeImpl(plugin, lookup, scheduler, cache), new com.janboerman.invsee.spigot.impl_1_16_R3.KnownPlayersProvider(plugin, scheduler));
    }
}

class Impl_1_17_1 extends SetupImpl {
    Impl_1_17_1(Plugin plugin, NamesAndUUIDs lookup, Scheduler scheduler, OpenSpectatorsCache cache) {
        super(new com.janboerman.invsee.spigot.impl_1_17_1_R1.InvseeImpl(plugin, lookup, scheduler, cache), new com.janboerman.invsee.spigot.impl_1_17_1_R1.KnownPlayersProvider(plugin, scheduler));
    }
}

class Impl_1_18_2 extends SetupImpl {
    Impl_1_18_2(Plugin plugin, NamesAndUUIDs lookup, Scheduler scheduler, OpenSpectatorsCache cache) {
        super(new com.janboerman.invsee.spigot.impl_1_18_2_R2.InvseeImpl(plugin, lookup, scheduler, cache), new com.janboerman.invsee.spigot.impl_1_18_2_R2.KnownPlayersProvider(plugin, scheduler));
    }
}

class Impl_1_19_2 extends SetupImpl {
    Impl_1_19_2(Plugin plugin, NamesAndUUIDs lookup, Scheduler scheduler, OpenSpectatorsCache cache) {
        super(new com.janboerman.invsee.spigot.impl_1_19_2_R1.InvseeImpl(plugin, lookup, scheduler, cache), new com.janboerman.invsee.spigot.impl_1_19_2_R1.KnownPlayersProvider(plugin, scheduler));
    }
}

class Impl_1_19_3 extends SetupImpl {
    Impl_1_19_3(Plugin plugin, NamesAndUUIDs lookup, Scheduler scheduler, OpenSpectatorsCache cache) {
        super(new com.janboerman.invsee.spigot.impl_1_19_3_R2.InvseeImpl(plugin, lookup, scheduler, cache), new com.janboerman.invsee.spigot.impl_1_19_3_R2.KnownPlayersProvider(plugin, scheduler));
    }
}

class Impl_1_19_4 extends SetupImpl {
    Impl_1_19_4(Plugin plugin, NamesAndUUIDs lookup, Scheduler scheduler, OpenSpectatorsCache cache) {
        super(new com.janboerman.invsee.spigot.impl_1_19_4_R3.InvseeImpl(plugin, lookup, scheduler, cache), new com.janboerman.invsee.spigot.impl_1_19_4_R3.KnownPlayersProvider(plugin, scheduler));
    }
}

//

class SetupImpl implements Setup {

    private final InvseePlatform platform;
    private final OfflinePlayerProvider offlinePlayerProvider;

    SetupImpl(InvseePlatform platform, OfflinePlayerProvider offlinePlayerProvider) {
        this.platform = platform;
        this.offlinePlayerProvider = offlinePlayerProvider;
    }

    @Override
    public InvseePlatform platform() {
        return platform;
    }

    @Override
    public OfflinePlayerProvider offlinePlayerProvider() {
        return offlinePlayerProvider;
    }
}