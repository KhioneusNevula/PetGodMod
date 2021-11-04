package com.gm910.petgodmod.blocks;

import javax.annotation.Nullable;

import com.gm910.petgodmod.init.ItemInit;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockBase extends Block {

	public BlockBase(BlockBehaviour.Properties properties, @Nullable String itemName) {
		super(properties);
		if (itemName != null)
			ItemInit.ITEMS.register(itemName, () -> new BlockItem(this, new Item.Properties()));
	}

}
