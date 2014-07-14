
package climateControl;
/**
 *
 * @author Zeno410
 */
public class BiomeIncidences implements Cloneable {

    // biome incidences. Numbers are defaults.
    public int birchForest =10;
    public int coldTaiga=10;
    public int desert=30;
    public int extremeHills=20;
    public int forest=20;
    public int icePlains=30;
    public int jungle=5;
    public int megaTaiga=5;
    public int mesaPlateau=1;
    public int mesaPlateau_F=4;
    public int plains=30;
    public int roofedForest=10;
    public int savanna=20;
    public int swampland=10;
    public int taiga=10;

    public static final String birchForestName ="Birch Forest";
    public static final String coldTaigaName="Cold Taiga";
    public static final String desertName="Desert";
    public static final String extremeHillsName="Extreme Hills";
    public static final String forestName="Forest";
    public static final String icePlainsName="Ice Plains";
    public static final String jungleName="Jungle";
    public static final String megaTaigaName="Mega Taiga";
    public static final String mesaPlateauName="Mesa Plateau";
    public static final String mesaPlateau_FName="Mesa Plateau F";
    public static final String plainsName="Plains";
    public static final String roofedForestName="Roofed Forest";
    public static final String savannaName="Savanna";
    public static final String swamplandName="Swampland";
    public static final String taigaName="Taiga (snowless)";

    public static final String biomeFrequencyCategory = "BiomeFrequencies";

    public BiomeIncidences() {
        //super("RandomBiomeIncidences");
    }


    public int totalIncidences() {
        return birchForest +coldTaiga+desert+extremeHills+forest+icePlains+jungle+megaTaiga+
                mesaPlateau+mesaPlateau_F+plains+roofedForest+savanna+swampland+taiga;
    }

    public BiomeIncidences clone() {
        try {
            return (BiomeIncidences)(super.clone());
        } catch (CloneNotSupportedException ex) {
            return this;
        }
    }

/* this.randomBiomeList = new BiomeGenBase[] {BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.savanna,
BiomeGenBase.plains, BiomeGenBase.plains, BiomeGenBase.forest, BiomeGenBase.forest,
BiomeGenBase.roofedForest, BiomeGenBase.extremeHills, BiomeGenBase.extremeHills,
BiomeGenBase.birchForest, BiomeGenBase.swampland, BiomeGenBase.swampland, BiomeGenBase.taiga,
BiomeGenBase.icePlains, BiomeGenBase.coldTaiga, BiomeGenBase.mesaPlateau, BiomeGenBase.mesaPlateau_F,
BiomeGenBase.megaTaiga, BiomeGenBase.jungle, BiomeGenBase.jungle};*/

}