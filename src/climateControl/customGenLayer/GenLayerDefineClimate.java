package climateControl.customGenLayer;
import climateControl.ClimateControlSettings;
import genLayerPack.GenLayerPack;
import genLayerPack.GenLayer;
import genLayerPack.IntCache;

/**
 *
 * @author Zeno410
 */
public class GenLayerDefineClimate extends GenLayerPack {
    
    private final int hotLevel;
    private final int warmLevel;
    private final int coldLevel;
    private final int totalLevel;

    public GenLayerDefineClimate(long par1, GenLayer par3GenLayer,int hot, int warm, int cold, int snow)
    {
        super(par1);
        this.parent = par3GenLayer;
        this.hotLevel = hot;
        this.warmLevel = hot + warm;
        this.coldLevel = hot + warm + cold;
        this.totalLevel = hot + warm + cold + snow;
    }

    public GenLayerDefineClimate(long seed, GenLayer genLayer,ClimateControlSettings settings){
        this(seed,
                genLayer,
                settings.hotIncidence(),
                settings.warmIncidence(),
                settings.chillyIncidence(),
                settings.snowyIncidence()) ;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int i1 = par1 - 1;
        int j1 = par2 - 1;
        int k1 = par3 + 2;
        int l1 = par4 + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        for (int i2 = 0; i2 < par4; ++i2)
        {
            for (int j2 = 0; j2 < par3; ++j2)
            {
                int k2 = aint[j2 + 1 + (i2 + 1) * k1];
                this.initChunkSeed((long)(j2 + par1), (long)(i2 + par2));

                if (k2 == 0)
                {
                    aint1[j2 + i2 * par3] = 0;
                }
                else
                {
                    int l2 = this.nextInt(totalLevel);
                    byte b0;

                    if (l2 < hotLevel)
                    {
                        b0 = 1;
                    }
                    else if (l2 < warmLevel)
                    {
                        b0 = 2;
                    }
                    else if (l2 < coldLevel) {
                        b0 = 3;
                    } else {
                        b0 = 4;
                    }

                    aint1[j2 + i2 * par3] = b0;
                }
            }
        }

        return aint1;
    }
}