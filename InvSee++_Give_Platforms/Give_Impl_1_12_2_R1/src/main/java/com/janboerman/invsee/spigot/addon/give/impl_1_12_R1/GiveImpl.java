package com.janboerman.invsee.spigot.addon.give.impl_1_12_R1;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import com.janboerman.invsee.spigot.addon.give.common.NeditImpl;
import me.nullicorn.nedit.type.NBTCompound;
import me.nullicorn.nedit.type.NBTList;

import java.util.Map.Entry;

public class GiveImpl extends NeditImpl {

    public static final GiveImpl INSTANCE = new GiveImpl();

    private GiveImpl() {
    }

    @Override
    protected org.bukkit.inventory.ItemStack applyTag(org.bukkit.inventory.ItemStack stack, NBTCompound tag) {
        var nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound nmsTag = convert(tag);
        nmsStack.setTag(nmsTag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    private static NBTBase convert(Object o) {
        //no way to instantiate NBTTagEnd from outside the net.minecraft.server package without using reflection hacks.
        if (o instanceof Byte) return new NBTTagByte((Byte) o);
        if (o instanceof Short) return new NBTTagShort((Short) o);
        if (o instanceof Integer) return new NBTTagInt((Integer) o);
        if (o instanceof Long) return new NBTTagLong((Long) o);
        if (o instanceof Float) return new NBTTagFloat((Float) o);
        if (o instanceof Double) return new NBTTagDouble((Double) o);
        if (o instanceof byte[]) return new NBTTagByteArray((byte[]) o);
        if (o instanceof String) return new NBTTagString((String) o);
        if (o instanceof NBTList) return convert((NBTList) o);
        if (o instanceof NBTCompound) return convert((NBTCompound) o);
        if (o instanceof int[]) return new NBTTagIntArray((int[]) o);
        if (o instanceof long[]) return new NBTTagLongArray((long[]) o);

        throw new RuntimeException("Cannot convert " + o + " to its nbt-equivalent");
    }

    private static NBTTagCompound convert(NBTCompound tag) {
        NBTTagCompound compoundTag = new NBTTagCompound();
        for (Entry<String, Object> entry : tag.entrySet()) {
            compoundTag.set(entry.getKey(), convert(entry.getValue()));
        }
        return compoundTag;
    }

    private static NBTTagList convert(NBTList tag) {
        NBTTagList listTag = new NBTTagList();
        for (Object o : tag) {
            listTag.add(convert(o));
        }
        return listTag;
    }

}
