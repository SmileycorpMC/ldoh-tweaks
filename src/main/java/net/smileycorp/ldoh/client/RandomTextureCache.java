package net.smileycorp.ldoh.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class RandomTextureCache implements ISelectiveResourceReloadListener {

    public static final RandomTextureCache INSTANCE = new RandomTextureCache();

    private Map<ResourceLocation, List<ResourceLocation>> CACHE = Maps.newHashMap();

    private RandomTextureCache() {}

    private void init(ResourceLocation loc) {
        List<ResourceLocation> list = Lists.newArrayList();
        String domain = loc.getResourceDomain();
        String path = loc.getResourcePath().replace("textures", "mcpatcher").replace("entity", "mob")
                .replace("entities", "mob").replace(".png", "");
        IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
        int i = 1;
        while (true) {
            try {
                ResourceLocation checkedTexture = new ResourceLocation(domain, path+(i == 1 ? "" : i)+".png");
                rm.getResource(checkedTexture);
                list.add(checkedTexture);
                i++;
            } catch (Exception e) {
               break;
            }
        }
        CACHE.put(loc, list);
    }

    public ResourceLocation getLoc(ResourceLocation loc, Entity entity) {
        if (!CACHE.containsKey(loc)) init(loc);
        List<ResourceLocation> list = CACHE.get(loc);
        if (list.isEmpty()) return loc;
        else return list.get((int)(entity.getUniqueID().getLeastSignificantBits() & 0x7FFFFFFFL) % list.size());
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager, Predicate<IResourceType> predicate) {
        if (predicate.test(VanillaResourceType.TEXTURES)) {
            for (ResourceLocation loc : CACHE.keySet()) init(loc);
        }
    }
}
