package com.klinbee.keenlib.placementmodifiers;

import com.klinbee.keenlib.registration.TypedCodec;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Recreation of the vanilla version, with a few differences:
 * 1. No Bedrock check
 * 2. Does a single pass for the chunk and caches the y-values of the layers
 */
public class CountOnEveryLayerPlacement extends PlacementModifier {
    public static final Codec<CountOnEveryLayerPlacement> CODEC = IntProvider.codec(0, 256)
            .fieldOf("count")
            .xmap(CountOnEveryLayerPlacement::new, placementModifier -> placementModifier.countProvider)
            .codec();

    public static PlacementModifierType<?> TYPE;

    public static final TypedCodec<PlacementModifierType<?>> TYPED_CODEC = new TypedCodec<>("count_on_every_layer",  (PlacementModifierType<CountOnEveryLayerPlacement>) () -> CODEC);

    private final IntProvider countProvider;

    private CountOnEveryLayerPlacement(IntProvider countProvider) {
        this.countProvider = countProvider;
    }

    public static CountOnEveryLayerPlacement of(IntProvider countProvider) {
        return new CountOnEveryLayerPlacement(countProvider);
    }

    public static CountOnEveryLayerPlacement of(int count) {
        return of(ConstantInt.of(count));
    }

    private static final short INVALID_LAYER = -32768;

    private static volatile List<short[]> LAYERS = new ArrayList<>();
    private static volatile short MAX_LAYER = INVALID_LAYER;
    private static volatile long PREV_CHUNK = ChunkPos.INVALID_CHUNK_POS;

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource random, BlockPos pos) {
        Stream.Builder<BlockPos> blockPositions = Stream.builder();

        int x = pos.getX();
        int z = pos.getZ();

        {
            long currChunk = ChunkPos.asLong(pos);
            if (ChunkPos.asLong(pos) != PREV_CHUNK) {
                populateLayers(placementContext,
                        x,
                        z
                );
                PREV_CHUNK = currChunk;
            }
        }

        for (short layer = 0; layer < MAX_LAYER; layer++) {
            int currLayerCount = countProvider.sample(random);
            for (int i = 0; i < currLayerCount; i++) {
                int offsetX = random.nextInt(16);
                int offsetZ = random.nextInt(16);
                int newY = LAYERS.get(layer)[offsetX + (offsetZ << 4)];
                if (newY != INVALID_LAYER) {
                    blockPositions.add(new BlockPos(
                            x + offsetX,
                            newY,
                            z + offsetZ)
                    );
                }
            }
        }

        return blockPositions.build();
    }

    private void populateLayers(PlacementContext placementContext, int baseX, int baseZ) {
        resetLayers();

        short maxLayer = INVALID_LAYER;

        short minY = (short) placementContext.getMinBuildHeight();
        short maxY = (short) placementContext.getLevel().getMaxBuildHeight();

        for (int offsetX = 0; offsetX < 16; offsetX++) {
            for (int offsetZ = 0; offsetZ < 16; offsetZ++) {
                short currLayer = 0;
                BlockPos.MutableBlockPos prevPos = new BlockPos.MutableBlockPos(
                        baseX + offsetX,
                        minY,
                        baseZ + offsetZ
                );
                BlockState prevBlockState = placementContext.getBlockState(prevPos);
                for (short y = (short) (minY + 1); y < maxY; y++) {
                    prevPos.setY(y);
                    BlockState currBlockState = placementContext.getBlockState(prevPos);
                    if (!isEmpty(prevBlockState) && isEmpty(currBlockState)) {
                        // Ensure we have enough layers
                        while (LAYERS.size() <= currLayer) {
                            short[] newLayer = new short[256];
                            Arrays.fill(newLayer, INVALID_LAYER);
                            LAYERS.add(newLayer);
                        }
                        LAYERS.get(currLayer)[offsetX + (offsetZ << 4)] = y;
                        currLayer++;
                        maxLayer = (short) Math.max(currLayer, maxLayer);
                    }
                    prevBlockState = currBlockState;
                }
            }
        }
        MAX_LAYER = maxLayer;
    }

    private void resetLayers() {
        MAX_LAYER = INVALID_LAYER;
        for (short[] layer : LAYERS) {
            Arrays.fill(layer, INVALID_LAYER);
        }
    }

    @Override
    public PlacementModifierType<?> type() {
        return TYPE;
    }

    private static boolean isEmpty(BlockState blockState) {
        return blockState.isAir() || blockState.is(Blocks.WATER) || blockState.is(Blocks.LAVA);
    }
}