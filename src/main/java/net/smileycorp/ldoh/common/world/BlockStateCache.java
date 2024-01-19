package net.smileycorp.ldoh.common.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BlockStateCache {

    private static final Map<PosKey, WeakReference<IBlockState>> CACHE = new HashMap<>();

    public static IBlockState getBlockState(World world, BlockPos pos) {
        if (world.isOutsideBuildHeight(pos)) return Blocks.AIR.getDefaultState();
        if (world.isRemote) return world.getBlockState(pos);
        WeakReference<IBlockState> ref = CACHE.get(pos);
        if (ref == null) {
            Chunk chunk = world.getChunkFromBlockCoords(pos);
            CACHE.put(PosKey.of(pos), new WeakReference<>(chunk.getBlockState(pos)));
            ref = CACHE.get(pos);
        }
        if (ref == null) return Blocks.AIR.getDefaultState();
        IBlockState state = ref.get();
        return state == null ? Blocks.AIR.getDefaultState() : state;
    }

    public static IBlockState getBlockState(IBlockAccess access, BlockPos pos) {
        return access instanceof World ? getBlockState((World) access, pos) : access.getBlockState(pos);
    }

    public static void clear() {
        CACHE.clear();
    }

    private static class PosKey {

        private final int x, y, z;

        private PosKey(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PosKey) return x == ((PosKey) o).x && y == ((PosKey) o).y && z == ((PosKey) o).z;
            if (o instanceof Vec3i) return x == ((Vec3i) o).getX() && y == ((Vec3i) o).getY() && z == ((Vec3i) o).getZ();
            return false;
        }

        @Override
        public int hashCode() {
            return x << 12 + y << 6 + z;
        }

        private static PosKey of(BlockPos pos) {
            return new PosKey(pos.getX(), pos.getY(), pos.getZ());
        }

    }
}
