package dev.shadowsoffire.apotheosis.adventure.affix.salvaging;

import dev.shadowsoffire.placebo.menu.MenuUtil;
import dev.shadowsoffire.placebo.menu.SimplerMenuProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class SalvagingTableBlock extends Block implements EntityBlock {

    public SalvagingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimplerMenuProvider<>(pLevel, pPos, SalvagingMenu::new);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return MenuUtil.openGui(player, pos, SalvagingMenu::new);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack pStack, BlockGetter pLevel, List<Component> list, TooltipFlag pFlag) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SalvagingTableTile(pPos, pState);
    }

    @Override
    @Deprecated
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() == this && newState.getBlock() == this) return;
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof SalvagingTableTile salvTile) {
            for (int i = 0; i < salvTile.output.getSlots().size(); i++) {
                popResource(world, pos, salvTile.output.getStackInSlot(i));
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }
}