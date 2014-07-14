
package climateControl;
/**
 *
 * @author Zeno410
 */
public class ClimateControlSettings {
    static final String halfSizeName = "Half Zone Size";
    static final String quarterZoneName = "Quarter Zone Size";
    static final String allowDerpyIslandsName = "Allow Derpy Island";
    static final String moreOceanName = "More Ocean";
    static final String randomBiomesName = "Random Biomes";

    static final String climateControlCategory = "Climate Control Parameters";

    static final String customParameter = "Custom Climate Control Parameters";

        boolean halfSize = true;
        boolean quarterSize = false;
        boolean allowDerpyIslands = false;
        boolean moreOcean = true;
        boolean randomBiomes = false;

        int largeContinentFrequency = 8;
        int mediumContinentFrequency = 11;
        int smallContinentFrequency = 16;
        int largeIslandFrequency = 30;

        int hotIncidence = 1;
        int warmIncidence = 1;
        int chillyIncidence = 1;
        int snowyIncidence = 1;

        BiomeIncidences biomeIncidences = new BiomeIncidences();

        public int hotIncidence() {return hotIncidence;}
        public int warmIncidence() {return warmIncidence;}
        public int chillyIncidence() {return chillyIncidence;}
        public int snowyIncidence() {return snowyIncidence;}

        ClimateControlSettings() {}

        public ClimateControlSettings clone() {
            ClimateControlSettings clone = new ClimateControlSettings();
            clone.halfSize = this.halfSize;
            clone.quarterSize = this.quarterSize;
            clone.allowDerpyIslands = this.allowDerpyIslands;
            clone.moreOcean = this.moreOcean;
            return clone;
        }

        public boolean doFull() {return !halfSize && !quarterSize;}
        public boolean doHalf() {return halfSize && !quarterSize;}
    }
