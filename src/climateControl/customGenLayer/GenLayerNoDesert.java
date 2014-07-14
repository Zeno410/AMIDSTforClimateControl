
package climateControl.customGenLayer;

/**
 *
 * @author Zeno410
 */
import genLayerPack.GenLayerPack;
import genLayerPack.BiomeGenBase;
import genLayerPack.GenLayer;
import genLayerPack.IntCache;



public class GenLayerNoDesert extends GenLayerPack {
    //private final int value;
    public GenLayerNoDesert(long seed, GenLayer parent) {
        super(seed);
        super.parent= parent;
    }

    public int[] getInts(int par1, int par2, int par3, int par4){

        int[] aint = parent.getInts(par1, par2, par3, par4);
        for (int i = 0; i < aint.length;i++) {
            if (aint[i] == BiomeGenBase.desert.biomeID) throw new RuntimeException();
        }
        return aint;
    }

}