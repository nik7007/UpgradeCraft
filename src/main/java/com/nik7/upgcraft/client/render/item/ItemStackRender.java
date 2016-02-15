package com.nik7.upgcraft.client.render.item;


import com.nik7.upgcraft.block.BlockUpgCContainerOrientable;
import com.nik7.upgcraft.block.BlockUpgCTank;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidInfuser;
import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
import com.nik7.upgcraft.tileentities.UpgCtileentityWoodenFluidTank;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemStackRender extends TileEntityItemStackRenderer {

    private UpgCtileentityWoodenFluidTank tank = new UpgCtileentityWoodenFluidTank();
    private UpgCtileentityFluidFurnace fluidFurnace = new UpgCtileentityFluidFurnace();
    private UpgCtileentityFluidInfuser fluidInfuser = new UpgCtileentityFluidInfuser();
    private TileEntityItemStackRenderer instance;

    public ItemStackRender(TileEntityItemStackRenderer instance) {
        this.instance = instance;
    }

    public void renderByItem(ItemStack itemStack) {
        Block block = Block.getBlockFromItem(itemStack.getItem());

        if (block instanceof BlockUpgCTank) {

            tank.setBlockType(block);
            tank.setMetadata(itemStack.getMetadata());

            TileEntityRendererDispatcher.instance.renderTileEntityAt(tank, 0.0d, 0.0d, 0.0d, 0.0f);

        } else if (block instanceof BlockUpgCContainerOrientable) {

            UpgCtileentityInventoryFluidHandler fluidMachine;

            if (block == ModBlocks.blockUpgCFluidFurnace)
                fluidMachine = fluidFurnace;
            else if (block == ModBlocks.blockUpgCFluidInfuser)
                fluidMachine = fluidInfuser;
            else {
                this.instance.renderByItem(itemStack);
                return;
            }

            fluidMachine.setBlockType(block);
            fluidMachine.setMetadata(block.getMetaFromState(block.getDefaultState().withProperty(BlockUpgCContainerOrientable.FACING, EnumFacing.SOUTH)));


            TileEntityRendererDispatcher.instance.renderTileEntityAt(fluidMachine, 0.0d, 0.0d, 0.0d, 0.0f);

        } else
            this.instance.renderByItem(itemStack);
    }
}
