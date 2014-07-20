
package climateControl.customGenLayer;
import climateControl.BiomeRandomizer;
import climateControl.utils.IntRandomizer;

import genLayerPack.BiomeGenBase;
import genLayerPack.GenLayer;
import genLayerPack.IntCache;
/**
 *
 * @author MasterCaver modified by Zeno410
 */
public class GenLayerBiomeByClimate extends GenLayer {


    //private BiomeGenBase[] randomBiomeList;

    private BiomeRandomizer biomeRandomizer;

    private IntRandomizer randomCallback = new IntRandomizer() {
       public int nextInt(int maximum) {
            return GenLayerBiomeByClimate.this.nextInt(maximum);
       }
    };

    private BiomeRandomizer.PickByClimate pickByClimate;

    public GenLayerBiomeByClimate(long par1, GenLayer par3GenLayer){
        super(par1);
        this.parent = par3GenLayer;

        /*this.randomBiomeList = new BiomeGenBase[] {BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.savanna,
            BiomeGenBase.plains, BiomeGenBase.plains, BiomeGenBase.forest, BiomeGenBase.forest,
            BiomeGenBase.roofedForest, BiomeGenBase.extremeHills, BiomeGenBase.extremeHills,
            BiomeGenBase.birchForest, BiomeGenBase.swampland, BiomeGenBase.swampland, BiomeGenBase.taiga,
            BiomeGenBase.icePlains, BiomeGenBase.coldTaiga, BiomeGenBase.mesaPlateau, BiomeGenBase.mesaPlateau_F,
            BiomeGenBase.megaTaiga, BiomeGenBase.jungle, BiomeGenBase.jungle};*/


        biomeRandomizer = BiomeRandomizer.instance;
        pickByClimate = biomeRandomizer.pickByClimate();
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int[] aint = this.parent.getInts(par1, par2, par3, par4);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        for (int i1 = 0; i1 < par4; ++i1)
        {
            for (int j1 = 0; j1 < par3; ++j1)
            {
                this.initChunkSeed((long)(j1 + par1), (long)(i1 + par2));
                int k1 = aint[j1 + i1 * par3];
                int l1 = (k1 & 3840) >> 8;
                k1 &= -3841;
                //ClimateControl.logger.info(""+k1);

                if (isBiomeOceanic(k1)){
                    aint1[j1 + i1 * par3] = k1;
                }
                else if (k1 == BiomeGenBase.mushroomIsland.biomeID){
                    aint1[j1 + i1 * par3] = k1;
                }
                else {
                    aint1[j1 + i1 * par3] = pickByClimate.biome(k1, randomCallback);
                }
            }
        }

        return aint1;
    }
}