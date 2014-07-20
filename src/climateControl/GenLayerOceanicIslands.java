package climateControl;
import climateControl.utils.IntRandomizer;
import climateControl.utils.RandomIntUser;
import genLayerPack.GenLayer;
import genLayerPack.IntCache;
/**
 *
 * @author Zeno410
 */
public class GenLayerOceanicIslands extends GenLayer{

    private final IntRandomizer passer = new IntRandomizer() {
        public int nextInt(int range) {
            return GenLayerOceanicIslands.this.nextInt(range);
        }
    };

    private final int milleFill;
    private final RandomIntUser island;
    public GenLayerOceanicIslands(long p_i45480_1_, GenLayer p_i45480_3_, int milleFill, final int islandValue){
        super(p_i45480_1_);
        this.parent = p_i45480_3_;
        this.milleFill = milleFill;
        // using a constant island value so create a RandomIntUser that ignores the random and returns the value
        this.island = new RandomIntUser() {
            public int value(IntRandomizer randomizer) {
                return islandValue;
            }
        };
    }

    public GenLayerOceanicIslands(long p_i45480_1_, GenLayer p_i45480_3_, int milleFill, RandomIntUser island){
        super(p_i45480_1_);
        this.parent = p_i45480_3_;
        this.milleFill = milleFill;
        this.island = island;
    }

    public GenLayerOceanicIslands(long p_i45480_1_, GenLayer p_i45480_3_, int percentFilled){
        this( p_i45480_1_,  p_i45480_3_,  percentFilled,1);
    }

    /**
     * Every pixel of "water" (value 0) has percentFilled chance of being made land
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public int[] getInts(int par1, int par2, int par3, int par4) {
        int i1 = par1 - 1;
        int j1 = par2 - 1;
        int k1 = par3 + 2;
        int l1 = par4 + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        for (int i2 = 0; i2 < par4; ++i2){
            for (int j2 = 0; j2 < par3; ++j2){
                int k2 = aint[j2 + 1 + (i2 + 1 - 1) * (par3 + 2)];
                int l2 = aint[j2 + 1 + 1 + (i2 + 1) * (par3 + 2)];
                int i3 = aint[j2 + 1 - 1 + (i2 + 1) * (par3 + 2)];
                int j3 = aint[j2 + 1 + (i2 + 1 + 1) * (par3 + 2)];
                int k3 = aint[j2 + 1 + (i2 + 1) * k1];
                aint1[j2 + i2 * par3] = k3;
                this.initChunkSeed((long)(j2 + par1), (long)(i2 + par2));

                if (k3 <= 0 && k2 <= 0 && l2 <= 0 && i3 <= 0 && j3 <= 0 && this.nextInt(1000) < milleFill){
                    aint1[j2 + i2 * par3] = island.value(passer);
                }
            }
        }

        return aint1;
    }
}