package cc.cassian.immersiveminimaps.compat;


import net.fabricmc.loader.api.FabricLoader;

public class ModCompat {
    /**
     * Accessories - used for detecting overlays in accessory slots.
     * Multiplatform
     */
    public static final boolean ACCESSORIES = FabricLoader.getInstance().isModLoaded("accessories");
    
    /**
     * Trinkets - used for detecting overlays in accessory slots.
     * Fabric
     */
    public static final boolean TRINKETS = FabricLoader.getInstance().isModLoaded("trinkets");

    /**
     * Trinkets (Updated) - used for detecting overlays in accessory slots.
     * Multiplatform
     */
    public static final boolean TRINKETS_UPDATED = FabricLoader.getInstance().isModLoaded("trinkets_updated");

    /**
     * Immersive Overlays
     */
    public static final boolean IMMERSIVE_OVERLAYS = FabricLoader.getInstance().isModLoaded("immersiveoverlays");
}
