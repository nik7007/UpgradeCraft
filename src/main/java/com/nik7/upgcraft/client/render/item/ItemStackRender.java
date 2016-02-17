package com.nik7.upgcraft.client.render.item;


import com.nik7.upgcraft.block.BlockUpgCContainerOrientable;
import com.nik7.upgcraft.block.BlockUpgCTank;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.tileentities.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemStackRender extends TileEntityItemStackRenderer {

    private UpgCtileentityWoodenFluidTank woodenTank = new UpgCtileentityWoodenFluidTank();
    private UpgCtileentityClayFluidTank clayTank = new UpgCtileentityClayFluidTank();
    private UpgCtileentityFluidFurnace fluidFurnace = new UpgCtileentityFluidFurnace();
    private UpgCtileentityFluidInfuser fluidInfuser = new UpgCtileentityFluidInfuser();
    private TileEntityItemStackRenderer instance;

    public ItemStackRender(TileEntityItemStackRenderer instance) {
        this.instance = instance;
    }

    public void renderByItem(ItemStack itemStack) {
        Block block = Block.getBlockFromItem(itemStack.getItem());

        if (block instanceof BlockUpgCTank) {
            UpgCtileentityFluidTank tank;
            if (block == ModBlocks.blockUpgCWoodenFluidTank)
                tank = woodenTank;
            else if (block == ModBlocks.blockUpgCClayFluidTank)
                tank = clayTank;
            else {
                this.instance.renderByItem(itemStack);
                return;
            }

            if (itemStack.hasTagCompound()) {
                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound());
                tank.drain(EnumFacing.NORTH, tank.getFluidAmount(), true);
                if (fluidStack != null)
                    tank.fill(EnumFacing.NORTH, fluidStack, true);
                else {
                    tank.drain(EnumFacing.NORTH, tank.getFluidAmount(), true);
                }
            } else {
                tank.drain(EnumFacing.NORTH, tank.getFluidAmount(), true);
            }

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
