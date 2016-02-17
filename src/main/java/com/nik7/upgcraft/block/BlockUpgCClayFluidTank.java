package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tileentities.UpgCtileentityClayFluidTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockUpgCClayFluidTank extends BlockUpgCTank {

    public static final PropertyBool IS_HARDENED = PropertyBool.create("isHardened");

    public BlockUpgCClayFluidTank() {
        super(Material.rock, Capacity.SMALL_TANK, "ClayFluidTank");
        this.setHardness(2.8f);
        this.hasSubBlocks = true;
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, TankType.SOLID).withProperty(IS_HARDENED, false));
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        if (this.hasSubBlocks) {
            list.add(new ItemStack(this, 1, TankType.SOLID.getMeta()));
            list.add(new ItemStack(this, 1, TankType.GLASSES.getMeta()));
            list.add(new ItemStack(this, 1, TankType.SOLID.getMeta() + 2));
            list.add(new ItemStack(this, 1, TankType.GLASSES.getMeta() + 2));
        }
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, TYPE, IS_HARDENED);
    }

    @Override
    public int damageDropped(IBlockState state) {
        int oldMeta = super.damageDropped(state);

        if (state.getValue(IS_HARDENED))
            oldMeta += 2;
        return oldMeta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isCocked = false;
        if (meta >= 2) {
            meta -= 2;
            isCocked = true;
        }
        return super.getStateFromMeta(meta).withProperty(IS_HARDENED, isCocked);
    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UpgCtileentityClayFluidTank();
    }
}
