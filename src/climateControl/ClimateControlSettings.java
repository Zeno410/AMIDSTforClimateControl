
package climateControl;

import climateControl.utils.Acceptor;
import climateControl.utils.Mutable;
import climateControl.utils.Settings;
//import highlands.api.HighlandsBiomes;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import minecraftForge.Configuration;

/**
 *
 * @author Zeno410
 */
public class ClimateControlSettings extends Settings {
    static final String halfSizeName = "Half Zone Size";
    static final String quarterSizeName = "Quarter Zone Size";
    static final String randomBiomesName = "Random Biomes";
    static final String allowDerpyIslandsName = "Allow Derpy Islands";
    static final String moreOceanName = "More Ocean";
    static final String largeContinentFrequencyName = "Large Continent Frequency";
    static final String mediumContinentFrequencyName = "Medium Continent Frequency";
    static final String smallContinentFrequencyName = "Small Continent Frequency";
    static final String largeIslandFrequencyName ="Large Island Frequency";
    static final String mediumIslandFrequencyName ="Medium Island Frequency";
    static final String hotIncidenceName = "Hot Zone Incidence";
    static final String warmIncidenceName = "Warm Zone Incidence";
    static final String coolIncidenceName = "Cool Zone Incidence";
    static final String snowyIncidenceName = "Snowy Zone Incidence";
    static final String vanillaBiomesOnName = "VanillaBiomesOn";
    static final String highlandsBiomesOnName = "HighlandsBiomesOn";
    static final String bopBiomesOnName = "BoPBiomesOn";
    static final String biomeSizeName = "Biome Size";
    static final String vanillaStructure = "VanillaLandAndClimate";


    private final Category climateZoneCategory = category("Climate Zone Parameters","" +
                    "Full-size is similar to 1.7 defaults. " +
                    "Half-size creates zones similar to pre-1.7 snowy zones. " +
                    "Quarter-size creates fairly small zones but the hot and snowy incidences are limited");

    public final Mutable<Boolean> halfSize = climateZoneCategory.booleanSetting(halfSizeName, true,
                    "half the climate zone size from vanilla, unless quartering");

    public final Mutable<Boolean> quarterSize = climateZoneCategory.booleanSetting(quarterSizeName, false,
                    "quarter the climate zone size from vanilla");

    public final Mutable<Boolean> randomBiomes = climateZoneCategory.booleanSetting(
            randomBiomesName, false,"ignore climate zones altogether");


    private final Category climateControlCategory = category("Assorted Parameters");

    public final Mutable<Boolean> allowDerpyIslands = climateControlCategory.booleanSetting(
            allowDerpyIslandsName, false, "place little islands near shore rather than in deep ocean");

    public final Mutable<Integer> biomeSize = climateControlCategory.intSetting(
            biomeSizeName, 4, "Biome size, exponential: 4 is regular and 6 is large biomes");
    
    public final Mutable<Boolean> noGenerationChanges = climateControlCategory.booleanSetting(
            "No Generation Changes",false,"generate as if CC weren't on; for loading pre-existing worlds or just preventing chunk boundaries");

    public final Mutable<Boolean> smootherCoasts = climateControlCategory.booleanSetting(
            "Smoother Coastlines",true,"increase smoothing steps; also shrinks unusual biomes some; changing produces occaisional chunk walls");

    public final Mutable<Boolean> vanillaLandAndClimate = climateControlCategory.booleanSetting(
            vanillaStructure, false, "Generate land masses and climate zones similar to vanilla 1.7");

    public final Mutable<Integer> climateRingsNotSaved = climateControlCategory.intSetting(
            "climateRingsNotSaved", 3, "climate not saved on edges; more than 3 has no effect; -1 deactivates saving climates");

    public final Mutable<Integer> biomeRingsNotSaved = climateControlCategory.intSetting(
            "biomeRingsNotSaved", 3, "biomes not saved on edges; more than 3 has no effect; -1 deactivates saving biomes");

    public final Mutable<Integer> subBiomeRingsNotSaved = climateControlCategory.intSetting(
            "subBiomeRingsNotSaved", 0, "subbiomes not saved on edges, default -1, -1 deactivates saving sub-biomes");
    
    private final Category climateIncidenceCategory = category("Climate Incidences", "" +
                    "Blocks of land are randomly assigned to each zone with a frequency proportional to the incidence. " +
                    "Processing eliminates some extreme climates later, especially for quarter size zones. " +
                    "Consider doubling hot and snowy incidences for quarter size zones.");

    public final Mutable<Integer> hotIncidence = climateIncidenceCategory.intSetting(hotIncidenceName, 1,
                    "relative incidence of hot zones like savanna/desert/plains/mesa");
    public final Mutable<Integer> warmIncidence = climateIncidenceCategory.intSetting(warmIncidenceName, 1,
                    "relative incidence of warm zones like forest/plains/hills/jungle/swamp");
    public final Mutable<Integer> coolIncidence = climateIncidenceCategory.intSetting(coolIncidenceName, 1,
                    "relative incidence of cool zones like forest/plains/hills/taiga/roofed forest");
    public final Mutable<Integer> snowyIncidence =climateIncidenceCategory.intSetting(snowyIncidenceName, 1,
                    "relative incidence of snowy zones");

    private final Category oceanControlCategory = category("Ocean Control Parameters", "" +
                    "Frequencies are x per thousand but a little goes a very long way because seeds grow a lot. " +
                    "About half the total continent frequencies is the percent land. " +
                    "For worlds with 1.7-like generation set large island seeds to about 300. " +
                    "That will largely fill the oceans after seed growth.");

    public final Mutable<Integer> largeContinentFrequency = oceanControlCategory.intSetting(
            largeContinentFrequencyName, 8,"frequency of large continent seeds, about 8000x16000");
    public final Mutable<Integer> mediumContinentFrequency = oceanControlCategory.intSetting(
            mediumContinentFrequencyName, 11, "frequency of medium continent seeds, about 4000x8000");
    public final Mutable<Integer> smallContinentFrequency = oceanControlCategory.intSetting(
            smallContinentFrequencyName, 16,"frequency of small continent seeds, about 2000x4000");
    public final Mutable<Integer> largeIslandFrequency = oceanControlCategory.intSetting(
            largeIslandFrequencyName, 30,"frequency of large island seeds, about 1000x2000");
    public final Mutable<Integer> mediumIslandFrequency = oceanControlCategory.intSetting(
            mediumIslandFrequencyName, 30,"frequency of medium island seeds, about 500x1000");

    public final BiomeIncidences biomeIncidences = new BiomeIncidences();

    public final Mutable<Boolean> vanillaBiomesOn = climateControlCategory.booleanSetting(
            vanillaBiomesOnName, "Include vanilla biomes in world", true);

    private final Mutable<Boolean> highlandsBiomesOn = new Mutable.Concrete<Boolean>(false);
    Acceptor<Boolean> highlandsOnTracker = new Acceptor<Boolean>() {
        public void accept(Boolean accepted) {
            highlandsBiomesOn.set(accepted);
        }
    };
    private Mutable<Boolean> highlandsBiomesFromConfig;

    private final Mutable<Boolean> BoPBiomesOn = new Mutable.Concrete<Boolean>(false);
    Acceptor<Boolean> BoPOnTracker = new Acceptor<Boolean>() {
        public void accept(Boolean accepted) {
            BoPBiomesOn.set(accepted);
        }
    };
    private Mutable<Boolean> BoPFromConfig;
    public ClimateControlSettings() {
        try {
            // see if highlands is there
            //int highlandsBiomes = HighlandsBiomes.biomesForHighlands.size();
            // attach a setting
            highlandsBiomesFromConfig = climateControlCategory.booleanSetting(
                    highlandsBiomesOnName, "", false);
            // pass changes to the flag guaranteed to exists;
            highlandsBiomesFromConfig.informOnChange(highlandsOnTracker);

        } catch (java.lang.NoClassDefFoundError e){
            // Highlands isn't installed
        }
        try {
            // see if BoP is there
            // attach a setting
            BoPFromConfig = climateControlCategory.booleanSetting(
                    bopBiomesOnName, "", false);
            // pass changes to the flag guaranteed to exists;
            BoPFromConfig.informOnChange(BoPOnTracker);

        } catch (java.lang.NoClassDefFoundError e){
            // BoP isn't installed
        }
    }
        public boolean doFull() {return !halfSize.value() && !quarterSize.value();}
        public boolean doHalf() {return halfSize.value() && !quarterSize.value();}



    @Override
    public void readFrom(Configuration source) {
        super.readFrom(source);
        biomeIncidences.readFrom(source);
    }

    @Override
    public void copyTo(Configuration target) {
        super.copyTo(target);
    }

    @Override
    public void readFrom(DataInput input) throws IOException {
        super.readFrom(input);
    }

    @Override
    public void writeTo(DataOutput output) throws IOException {
        super.writeTo(output);
    }

}

