package com.example.ttmlm;

import com.example.ttmlm.init.CoalVariants;
import com.example.ttmlm.init.IngotVariants;
import com.example.ttmlm.init.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TTMLM.MOD_ID)
public class TTMLM
{
    public static final String MOD_ID = "ttmlm";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    //ITEM GROUP FOR CREATIVE TAB
    public static final ItemGroup ITEM_GROUP_BLOCK = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.mutableAlloyOre);
        }

        @Override
        public ITextComponent getDisplayName() {
            return new StringTextComponent("Variants Blocks");
        }
    };

    public static final ItemGroup ITEM_GROUP_TOOL = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(IngotVariants.BLAZING_ALLOY.getAxeItem());
        }
        @Override
        public ITextComponent getDisplayName() {
            return new StringTextComponent("Variants Tools");
        }
    };

    public static final ItemGroup ITEM_GROUP_COMBAT = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(IngotVariants.ENDER_ALLOY.getSwordItem());
        }
        @Override
        public ITextComponent getDisplayName() {
            return new StringTextComponent("Variants Combat");
        }
    };

    public static final ItemGroup ITEM_GROUP_RESOURCES = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(CoalVariants.FREEZING_CARBON.getCoalVariantItem());
        }
        @Override
        public ITextComponent getDisplayName() {
            return new StringTextComponent("Variants Resources");
        }
    };

    public TTMLM() {
        DistExecutor.safeRunForDist(
                () -> SideProxy.Client::new,
                () -> SideProxy.Server::new
        );
    }

    public static String getVersion() {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById(MOD_ID);
        if(o.isPresent()){
            return o.get().getModInfo().getVersion().toString();
        }
        return "NONE";
    }

    public static boolean isDevBuild() {
        return "NONE".equals(getVersion());
    }

    public static ResourceLocation getID(String path){
        return new ResourceLocation(MOD_ID, path);
    }

    public static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, T entry, String registryKey)
    {
        entry.setRegistryName(new ResourceLocation(TTMLM.MOD_ID, registryKey));
        registry.register(entry);
        return entry;
    }

}
