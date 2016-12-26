package com.nik7.upgcraft.waila;


import com.nik7.upgcraft.block.BlockBasicFunnel;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Reference;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public class WailaFunnelHandler extends WailaFluidTankHandler {

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        super.getWailaBody(itemStack, currenttip, accessor, config);

        if (accessor.getBlock() == ModBlocks.blockBasicFunnel) {
            if (accessor.getBlockState().getValue(BlockBasicFunnel.BURNED)) {
                //currenttip.add("");
                currenttip.add(TextFormatting.DARK_RED + I18n.translateToLocal("tooltip." + Reference.RESOURCE_PREFIX + "funnel.burned"));
            }
        }

        return currenttip;

    }


}
