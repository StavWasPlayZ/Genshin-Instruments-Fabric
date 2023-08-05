package com.cstav.genshinstrument.block.partial;

import java.util.function.Consumer;

import com.cstav.genshinstrument.block.partial.client.IClientArmPoseProvider;
import com.cstav.genshinstrument.networking.ModPacketHandler;
import com.cstav.genshinstrument.networking.OpenInstrumentPacketSender;
import com.cstav.genshinstrument.networking.packets.instrument.NotifyInstrumentClosedPacket;
import com.cstav.genshinstrument.util.ModEntityData;
import com.cstav.genshinstrument.util.ServerUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractInstrumentBlock extends BaseEntityBlock {

    /**
     * @param onOpenRequest A server-side event fired when the player has requested to interact
     * with the instrument.
     * It should should send a packet to the given player for opening this instrument's screen.
     */
    public AbstractInstrumentBlock(Properties pProperties) {
        super(pProperties);

        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER)
            initClientBlockUseAnim((pose) -> clientBlockArmPose = pose);
    }


    // Abstract implementations
    protected abstract OpenInstrumentPacketSender instrumentPacketSender();
    @Override
    public abstract InstrumentBlockEntity newBlockEntity(BlockPos pPos, BlockState pState);

    /**
     * An instance of {@link IClientArmPoseProvider} as defined in {@link AbstractInstrumentBlock#initClientBlockUseAnim}
     */
    private Object clientBlockArmPose;
    protected void initClientBlockUseAnim(final Consumer<ArmPose> consumer) {
        //TODO re-implement once ModArmPose is implemented
        // consumer.accept(ModArmPose.PLAYING_BLOCK_INSTRUMENT);
        consumer.accept(ArmPose.BOW_AND_ARROW);
    }

    @Environment(EnvType.CLIENT)
    public ArmPose getClientBlockArmPose() {
        return (ArmPose)clientBlockArmPose;
    }


    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
            BlockHitResult pHit) {        
        if (pLevel.isClientSide)
                return InteractionResult.CONSUME;


        final BlockEntity be = pLevel.getBlockEntity(pPos);
        if (!(be instanceof InstrumentBlockEntity))
            return InteractionResult.FAIL;

        if (ServerUtil.sendOpenPacket((ServerPlayer)pPlayer, instrumentPacketSender(), pPos)) {
            ((InstrumentBlockEntity)be).users.add(pPlayer.getUUID());
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }


    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        final BlockEntity be = pLevel.getBlockEntity(pPos);
        if (!(be instanceof InstrumentBlockEntity))
            return;


        final InstrumentBlockEntity ibe = (InstrumentBlockEntity)be;

        for (final Player player : pLevel.players()) {
            ibe.users.forEach((user) -> {
                ModEntityData.setInstrumentClosed(pLevel.getPlayerByUUID(user));
                ModPacketHandler.sendToClient(new NotifyInstrumentClosedPacket(user), (ServerPlayer)player);
            });
        }
    }

}