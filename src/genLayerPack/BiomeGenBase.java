
package genLayerPack;

import java.util.Set;


public class BiomeGenBase
{
    protected static final BiomeGenBase.Height height_Default = new BiomeGenBase.Height(0.1F, 0.2F);
    protected static final BiomeGenBase.Height height_ShallowWaters = new BiomeGenBase.Height(-0.5F, 0.0F);
    protected static final BiomeGenBase.Height height_Oceans = new BiomeGenBase.Height(-1.0F, 0.1F);
    protected static final BiomeGenBase.Height height_DeepOceans = new BiomeGenBase.Height(-1.8F, 0.1F);
    protected static final BiomeGenBase.Height height_LowPlains = new BiomeGenBase.Height(0.125F, 0.05F);
    protected static final BiomeGenBase.Height height_MidPlains = new BiomeGenBase.Height(0.2F, 0.2F);
    protected static final BiomeGenBase.Height height_LowHills = new BiomeGenBase.Height(0.45F, 0.3F);
    protected static final BiomeGenBase.Height height_HighPlateaus = new BiomeGenBase.Height(1.5F, 0.025F);
    protected static final BiomeGenBase.Height height_MidHills = new BiomeGenBase.Height(1.0F, 0.5F);
    protected static final BiomeGenBase.Height height_Shores = new BiomeGenBase.Height(0.0F, 0.025F);
    protected static final BiomeGenBase.Height height_RockyWaters = new BiomeGenBase.Height(0.1F, 0.8F);
    protected static final BiomeGenBase.Height height_LowIslands = new BiomeGenBase.Height(0.2F, 0.3F);
    protected static final BiomeGenBase.Height height_PartiallySubmerged = new BiomeGenBase.Height(-0.2F, 0.1F);


    public static final TempCategory oceanic = TempCategory.OCEAN;
    public static final TempCategory cold = TempCategory.COLD;
    public static final TempCategory medium = TempCategory.MEDIUM;
    public static final TempCategory warm = TempCategory.WARM;
    /**
     * An array of all the biomes, indexed by biome id.
     */
    private static final BiomeGenBase[] biomeList = new BiomeGenBase[256];
    //public static final Set explorationBiomesList = Sets.newHashSet();
    public static final BiomeGenBase ocean = (new BiomeGenBase(0,oceanic));
    public static final BiomeGenBase plains = (new BiomeGenBase(1,medium));
    public static final BiomeGenBase desert = (new BiomeGenBase(2,warm));
    public static final BiomeGenBase extremeHills = (new BiomeGenBase(3,medium));
    public static final BiomeGenBase forest = (new BiomeGenBase(4,medium));
    public static final BiomeGenBase taiga = (new BiomeGenBase(5,medium));
    public static final BiomeGenBase swampland = (new BiomeGenBase(6,medium));
    public static final BiomeGenBase river = (new BiomeGenBase(7,null));
    public static final BiomeGenBase hell = (new BiomeGenBase(8,null));
    /**
     * Is the biome used for sky world.
     */
    public static final BiomeGenBase sky = (new BiomeGenBase(9,null));
    public static final BiomeGenBase frozenOcean = (new BiomeGenBase(10,cold));
    public static final BiomeGenBase frozenRiver = (new BiomeGenBase(11,cold));
    public static final BiomeGenBase icePlains = (new BiomeGenBase(12,cold));
    public static final BiomeGenBase iceMountains = (new BiomeGenBase(13,cold));
    public static final BiomeGenBase mushroomIsland = (new BiomeGenBase(14,medium));
    public static final BiomeGenBase mushroomIslandShore = (new BiomeGenBase(15,medium));
    /**
     * Beach biome.
     */
    public static final BiomeGenBase beach = (new BiomeGenBase(16,medium));
    /**
     * Desert Hills biome.
     */
    public static final BiomeGenBase desertHills = (new BiomeGenBase(17,warm));
    /**
     * Forest Hills biome.
     */
    public static final BiomeGenBase forestHills = (new BiomeGenBase(18,medium));
    /**
     * Taiga Hills biome.
     */
    public static final BiomeGenBase taigaHills = (new BiomeGenBase(19,medium));
    /**
     * Extreme Hills Edge biome.
     */
    public static final BiomeGenBase extremeHillsEdge = (new BiomeGenBase(20,medium));
    /**
     * Jungle biome identifier
     */
    public static final BiomeGenBase jungle = (new BiomeGenBase(21,medium));
    public static final BiomeGenBase jungleHills = (new BiomeGenBase(22,medium));
    public static final BiomeGenBase jungleEdge = (new BiomeGenBase(23,medium));
    public static final BiomeGenBase deepOcean = (new BiomeGenBase(24,oceanic));
    public static final BiomeGenBase stoneBeach = (new BiomeGenBase(25,medium));
    public static final BiomeGenBase coldBeach = (new BiomeGenBase(26,cold));
    public static final BiomeGenBase birchForest = (new BiomeGenBase(27,medium));
    public static final BiomeGenBase birchForestHills = (new BiomeGenBase(28,medium));
    public static final BiomeGenBase roofedForest = (new BiomeGenBase(29,medium));
    public static final BiomeGenBase coldTaiga = (new BiomeGenBase(30,cold));
    public static final BiomeGenBase coldTaigaHills = (new BiomeGenBase(31,cold));
    public static final BiomeGenBase megaTaiga = (new BiomeGenBase(32,medium));
    public static final BiomeGenBase megaTaigaHills = (new BiomeGenBase(33,medium));
    public static final BiomeGenBase extremeHillsPlus = (new BiomeGenBase(34,medium));
    public static final BiomeGenBase savanna = (new BiomeGenBase(35,warm));
    public static final BiomeGenBase savannaPlateau = (new BiomeGenBase(36,warm));
    public static final BiomeGenBase mesa = (new BiomeGenBase(37,warm));
    public static final BiomeGenBase mesaPlateau_F = (new BiomeGenBase(38,warm));
    public static final BiomeGenBase mesaPlateau = (new BiomeGenBase(39,warm));

    // mutated biomes

    public static final BiomeGenBase bryce = mesaPlateau.mutated();
    public static final BiomeGenBase bryce_F = mesaPlateau_F.mutated();
    public static final BiomeGenBase flower_Forest = forest.mutated();
    public static final BiomeGenBase forestHills_M = forestHills.mutated();
    public static final BiomeGenBase sunflowerFields = plains.mutated();
    public static final BiomeGenBase megaSpruceTaiga = megaTaiga.mutated();
    public static final BiomeGenBase mesa_M = mesa.mutated();
    public static final BiomeGenBase savanna_M = savanna.mutated();
    public static final BiomeGenBase savannaPlateau_M = savannaPlateau.mutated();
    public static final BiomeGenBase iceSpikes = icePlains.mutated();
    public static final BiomeGenBase iceMountainSpikes = iceMountains.mutated();

    public static final BiomeGenBase getBiome(int index) {
        return BiomeGenBase.biomeList[index];
    }

    public final TempCategory temperature;

    public TempCategory getTempCategory() {
        return temperature;
    }

    public final int biomeID;
    public BiomeGenBase(int id, TempCategory temperature) {
        //if (biomeList[id] != null) throw new RuntimeException();
        if (id > 127) biomeList[id] = this;
        this.biomeID = id;
        this.temperature = temperature;
    }

    public BiomeGenBase mutated() {
        if (this.biomeID < 128) {
            return new BiomeGenBase(this.biomeID+128,this.temperature);
        }
        throw new RuntimeException("cannot mutate biomes with ids over 127");
    }

    public static class Height
        {
            public float rootHeight;
            public float variation;
            private static final String __OBFID = "CL_00000159";

            public Height(float p_i45371_1_, float p_i45371_2_)
            {
                this.rootHeight = p_i45371_1_;
                this.variation = p_i45371_2_;
            }

            /**
             * Reduces the baseHeight by 20%, and the variation intensity by 40%, and returns the resulting Height
             * object
             */
            public BiomeGenBase.Height attenuate()
            {
                return new BiomeGenBase.Height(this.rootHeight * 0.8F, this.variation * 0.6F);
            }
        }

    public static enum TempCategory
    {
        OCEAN,
        COLD,
        MEDIUM,
        WARM;

        private static final String __OBFID = "CL_00000160";
    }
}