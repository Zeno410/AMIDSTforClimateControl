
package climateControl.customGenLayer;
import climateControl.utils.Zeno410Logger;
import genLayerPack.GenLayerPack;
import genLayerPack.GenLayer;
import genLayerPack.IntCache;
import java.util.logging.Logger;

/**
 *
 * @author Zeno410
 */
public class GenLayerCache extends GenLayerPack{

    public static Logger logger = new Zeno410Logger("Cache").logger();
    private int storedPar1 = Integer.MIN_VALUE;
    private int storedPar2 = Integer.MIN_VALUE;
    private int storedPar3 = Integer.MIN_VALUE;
    private int storedPar4 = Integer.MIN_VALUE;
    private int[] cached = null;

    public GenLayerCache(GenLayer parent) {
        super(0L);
        this.parent = parent;
    }

    public int [] getInts(int par1, int par2, int par3, int par4) {
        if (par1 != storedPar1) return store(par1, par2, par3, par4);
        if (par2 != storedPar2) return store(par1, par2, par3, par4);
        if (par3 != storedPar3) return store(par1, par2, par3, par4);
        if (par4 != storedPar4) return store(par1, par2, par3, par4);
        //logger.info("succeed " + par1 + " " + par2 + " " + par3 + " " + par4);
        return cached;
    }

    private int [] store(int par1, int par2, int par3, int par4) {
        //logger.info("fail " + par1 + " " + par2 + " " + par3 + " " + par4);
        cached = parent.getInts(par1, par2, par3, par4);
        storedPar1 = par1;
        storedPar2 = par2;
        storedPar3 = par3;
        storedPar4 = par4;
        return cached;
    }

}
