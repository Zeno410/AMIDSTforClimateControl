
package climateControl;
import amidst.minecraft.IMinecraftInterface;
import amidst.version.VersionInfo;
import climateControl.utils.Zeno410Logger;
import genLayerPack.*;
import java.io.File;
import java.util.logging.Logger;
import minecraftForge.Configuration;
/**
 *
 * @author Zeno410
 */
public class ClimateControledGenerator implements IMinecraftInterface {
    VersionInfo version;
    File minecraftDirectory;
    long worldSeed;
    String worldType;
    public static Logger logger = new Zeno410Logger("ClimateControledGenerator").logger();
    File worldConfigFile;
    private String CONFIG_DIRECTORY = "config";
    private String CONFIG_FILE = "climatecontrol.cfg";

    public ClimateControledGenerator(VersionInfo version, File jarFile) {
        this.version = version;
        minecraftDirectory = jarFile.getParentFile().getParentFile().getParentFile();
        setWorldFile();
    }

    public VersionInfo getVersion() {return version;}

    public void createWorld(long seed, String type) {
        this.worldSeed = seed;
        worldType = type;
        //biomeLayers =  quarteredRiverMix();
        //biomeLayers = initializeAllBiomeGenerators(seed);
        ClimateControlSettings settings = new ClimateControlSettings();
        try {
            Configuration config = new Configuration(worldConfigFile);
            settings.readFrom(config);
            logger.info("large continents "+settings.largeContinentFrequency.value());
            logger.info("medium continents "+settings.mediumContinentFrequency.value());
            logger.info("small continents "+settings.smallContinentFrequency.value());
            logger.info("large islands "+settings.largeIslandFrequency.value());
        } catch (NullPointerException e){
            // no config file, use defaults
            throw e;
        }
        biomeLayers = new StackedContinentsGenerator().stackedContinents(
                settings, worldSeed);
    }


    public void setWorldFile() {
        // this is the world save directory
        String configFileName = minecraftDirectory.getAbsoluteFile()+File.separator+CONFIG_DIRECTORY+File.separator+CONFIG_FILE;
        this.worldConfigFile = new File(configFileName);
        logger.info(configFileName);
        this.worldConfigFile = new File(minecraftDirectory,CONFIG_DIRECTORY);
        this.worldConfigFile = new File(worldConfigFile,CONFIG_FILE);
        logger.info(worldConfigFile.getAbsolutePath());
    }

    public int[] getBiomeData(int top, int left, int bottom, int right) {
        return biomeLayers.getInts(top, left, bottom, right);
    }

    private GenLayer biomeLayers ;

    private GenLayer quarteredRiverMix() {
        GenLayerIsland genlayerisland = new GenLayerIsland(1L);
        GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
        GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayerfuzzyzoom);
        GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(2L, genlayerzoom);
        genlayeraddisland = new GenLayerAddIsland(50L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(70L, genlayeraddisland);
            GenLayer genlayerremovetoomuchocean = new GenLayerOceanicIslands(2L, genlayeraddisland,5);
            genlayerzoom = new GenLayerZoom(2002L, genlayerremovetoomuchocean);
        genlayerzoom = new GenLayerZoom(2003L, genlayerzoom);
        GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerzoom);
        genlayeraddisland = new GenLayerAddIsland(3L, genlayeraddsnow);
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        genlayeraddisland = new GenLayerAddIsland(4L, genlayeredge);
        GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland);
        GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
             byte b0 = 4;

        /*if (worldType.equals("LARGE_BIOMES"));
        {
            b0 = 6;
        }*/

        //b0 = getModdedBiomeSize(worldType, b0);

        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
        Object object = new GenLayerBiome(this.worldSeed, genlayer3);

        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer genlayerhills = new GenLayerDederpedHills(1000L, (GenLayer)object, genlayer1);
        genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
        GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        object = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

            if (j == 0)
            {
                //object = new GenLayerAddIsland(3L, (GenLayer)object);
            }

            if (j == 1)
            {
                object = new GenLayerShore(1000L, (GenLayer)object);
            }
        }

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(this.worldSeed);
        genlayervoronoizoom.initWorldGenSeed(this.worldSeed);
        return genlayerrivermix;
    }

    private GenLayer smallerContinents() {
        GenLayerIsland genlayerisland = new GenLayerIsland(1L);
        GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
        GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayerfuzzyzoom);
        GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(2L, genlayerfuzzyzoom);//<note change to fuzzy
        //genlayeraddisland = new GenLayerAddIsland(50L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(70L, genlayeraddisland);
            GenLayer genlayerremovetoomuchocean = new GenLayerOceanicIslands(2L, genlayeraddisland,5);
            genlayerzoom = new GenLayerZoom(2002L, genlayerremovetoomuchocean);
        genlayerzoom = new GenLayerZoom(2003L, genlayerzoom);
        GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerzoom);
        genlayeraddisland = new GenLayerAddIsland(3L, genlayeraddsnow);
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        genlayeraddisland = new GenLayerAddIsland(4L, genlayeredge);
        GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland);
        GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
             byte b0 = 4;

        /*if (worldType.equals("LARGE_BIOMES"));
        {
            b0 = 6;
        }*/

        //b0 = getModdedBiomeSize(worldType, b0);

        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
        Object object = new GenLayerBiome(this.worldSeed, genlayer3);

        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer genlayerhills = new GenLayerDederpedHills(1000L, (GenLayer)object, genlayer1);
        genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
        GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        object = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

            if (j == 0)
            {
                object = new GenLayerAddIsland(3L, (GenLayer)object);
            }

            if (j == 1)
            {
                object = new GenLayerShore(1000L, (GenLayer)object);
            }
        }

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(this.worldSeed);
        genlayervoronoizoom.initWorldGenSeed(this.worldSeed);
        return genlayerrivermix;
    }

    private GenLayer adjustedSmallerContinents() {
        GenLayerIsland genlayerisland = new GenLayerIsland(1L,15);
        GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
        GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayerfuzzyzoom);
        GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(2L, genlayerfuzzyzoom);//<note change to fuzzy
        genlayeraddisland = new GenLayerAddIsland(50L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(70L, new GenLayerSmooth(1001L, genlayeraddisland));
        //genlayeraddisland = new GenLayerAddIsland(90L, new GenLayerSmooth(1002L,genlayeraddisland));
            GenLayer genlayerremovetoomuchocean = new GenLayerOceanicIslands(2L, genlayeraddisland,5);
            genlayerzoom = new GenLayerZoom(2002L, genlayerremovetoomuchocean);
        genlayerzoom = new GenLayerZoom(2003L, genlayerzoom);
        GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerzoom);
        genlayeraddisland = new GenLayerAddIsland(3L, genlayeraddsnow);
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, new GenLayerClimateSmooth(3000L,genlayeraddisland), GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        genlayeraddisland = new GenLayerAddIsland(4L, genlayeredge);
        GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland);
        GenLayer genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, new GenLayerClimateSmooth(3000L,genlayerdeepocean), 0);
             byte b0 = 4;

        /*if (worldType.equals("LARGE_BIOMES"));
        {
            b0 = 6;
        }*/

        //b0 = getModdedBiomeSize(worldType, b0);
        genlayer3 = new GenLayerClimateSmooth(3002L,genlayerdeepocean);
        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
        Object object = new GenLayerBiome(this.worldSeed, genlayer3);

        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer genlayerhills = new GenLayerDederpedHills(1000L, (GenLayer)object, genlayer1);
        genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
        GenLayerClimateSmooth genlayersmooth = new GenLayerClimateSmooth(1000L, genlayerriver);
        object = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

            if (j == 0)
            {
                object = new GenLayerAddIsland(3L, (GenLayer)object);
            }

            if (j == 1)
            {
                object = new GenLayerShore(1000L, (GenLayer)object);
            }
        }

        GenLayerClimateSmooth genlayersmooth1 = new GenLayerClimateSmooth(1000L, (GenLayer)object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(this.worldSeed);
        genlayervoronoizoom.initWorldGenSeed(this.worldSeed);
        return genlayerrivermix;
    }

        private GenLayer stackedContinents() {
        GenLayerIsland genlayerisland = new GenLayerIsland(1L,35);
        GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
        GenLayer smallContinents = new GenLayerOceanicIslands(2L, genlayerfuzzyzoom,2);
        GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, smallContinents);
        GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        smallContinents = new GenLayerOceanicIslands(2L, genlayerzoom,2);
        genlayeraddisland = new GenLayerAddIsland(2L, smallContinents);
        genlayeraddisland = new GenLayerAddIsland(50L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(70L, new GenLayerSmooth(1001L, genlayeraddisland));
        //genlayeraddisland = new GenLayerAddIsland(90L, new GenLayerSmooth(1002L,genlayeraddisland));
            GenLayer genlayerremovetoomuchocean = new GenLayerOceanicIslands(2L, genlayeraddisland,4);
        genlayeraddisland = new GenLayerAddIsland(3L, genlayerremovetoomuchocean);
        GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayeraddisland);
            genlayerzoom = new GenLayerZoom(2002L, genlayeraddsnow);
        genlayeraddisland = new GenLayerAddIsland(4L, genlayerzoom);
        genlayerzoom = new GenLayerZoom(2003L, genlayeraddisland);
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayerzoom, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeredge);
        GenLayer genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, new GenLayerClimateSmooth(3000L,genlayerdeepocean), 0);
             byte b0 = 4;

        /*if (worldType.equals("LARGE_BIOMES"));
        {
            b0 = 6;
        }*/

        //b0 = getModdedBiomeSize(worldType, b0);
        genlayer3 = new GenLayerClimateSmooth(3002L,genlayerdeepocean);
        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);

        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer flatBiomes = new GenLayerFlatBiome(this.worldSeed, genlayer3);
        //flatBiomes = GenLayerZoom.magnify(1000L, flatBiomes, 2);
        GenLayer genlayerhills = new GenLayerTransbiomeHills(1000L, flatBiomes, genlayerriverinit,mountainsCore());
        genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
        //GenLayerClimateSmooth genlayersmooth = new GenLayerClimateSmooth(1000L, genlayerriver);
        GenLayer forRareBiomes = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            forRareBiomes = new GenLayerZoom((long)(1000 + j), forRareBiomes);

            if (j == 0)
            {
                forRareBiomes = new GenLayerAddIsland(3L, forRareBiomes);
            }

            if (j == 1)
            {
                forRareBiomes = new GenLayerShore(1000L, forRareBiomes);
            }
        }

        //GenLayerClimateSmooth genlayersmooth1 = new GenLayerClimateSmooth(1000L, (GenLayer)object);
        //GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, forRareBiomes, genlayerriver);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(this.worldSeed);
        genlayervoronoizoom.initWorldGenSeed(this.worldSeed);
        return genlayerrivermix;
    }

    private GenLayer onlyIslands(int percentage) {
        GenLayerIsland genlayerisland = new GenLayerIsland(1L,1000000);
        GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
        GenLayer smallContinents = new GenLayerOceanicIslands(2L, genlayerfuzzyzoom,0);
        GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, smallContinents);
        GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        smallContinents = new GenLayerOceanicIslands(2L, genlayerzoom,percentage);
        genlayeraddisland = new GenLayerAddIsland(2L, smallContinents);
        genlayeraddisland = new GenLayerAddIsland(50L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(70L, new GenLayerSmooth(1001L, genlayeraddisland));
        //genlayeraddisland = new GenLayerAddIsland(90L, new GenLayerSmooth(1002L,genlayeraddisland));
            GenLayer genlayerremovetoomuchocean = new GenLayerOceanicIslands(2L, genlayeraddisland,4);
        genlayeraddisland = new GenLayerAddIsland(3L, genlayerremovetoomuchocean);
        GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayeraddisland);
            genlayerzoom = new GenLayerZoom(2002L, genlayeraddsnow);
        genlayeraddisland = new GenLayerAddIsland(4L, genlayerzoom);
        genlayerzoom = new GenLayerZoom(2003L, genlayeraddisland);
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayerzoom, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeredge);
        GenLayer genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, new GenLayerClimateSmooth(3000L,genlayerdeepocean), 0);
             byte b0 = 4;

        /*if (worldType.equals("LARGE_BIOMES"));
        {
            b0 = 6;
        }*/

        //b0 = getModdedBiomeSize(worldType, b0);
        genlayer3 = new GenLayerClimateSmooth(3002L,genlayerdeepocean);
        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
        Object object = new GenLayerBiome(this.worldSeed, genlayer3);

        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer genlayerhills = new GenLayerDederpedHills(1000L, (GenLayer)object, genlayer1);
        genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
        GenLayerClimateSmooth genlayersmooth = new GenLayerClimateSmooth(1000L, genlayerriver);
        object = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

            if (j == 0)
            {
                object = new GenLayerAddIsland(3L, (GenLayer)object);
            }

            if (j == 1)
            {
                object = new GenLayerShore(1000L, (GenLayer)object);
            }
        }

        GenLayerClimateSmooth genlayersmooth1 = new GenLayerClimateSmooth(1000L, (GenLayer)object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(this.worldSeed);
        genlayervoronoizoom.initWorldGenSeed(this.worldSeed);
        return genlayerrivermix;
    }
    private GenLayer mountainsCore() {

        GenLayer genlayerremovetoomuchocean = new GenLayerOceanicIslands(2L, new GenLayerConstant(1L),1,2);
        GenLayer genlayeraddisland = new GenLayerStretchMountain(5L, genlayerremovetoomuchocean);
        //genlayeraddisland = new GenLayerStretchMountain(3L, genlayeraddisland);
        //genlayeraddisland = new GenLayerStretchMountain(3L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerRaiseTooLow(6L,genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(6L,genlayeraddisland);
        genlayeraddisland = new GenLayerFillLakes(3L,genlayeraddisland);
        //genlayeraddisland = new GenLayerOceanSmooth(4L,genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(6L,genlayeraddisland);
        genlayeraddisland = new GenLayerFillLakes(3L,genlayeraddisland);
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, genlayeraddisland, 2);
             byte b0 = 4;

        /*if (worldType.equals("LARGE_BIOMES"));
        {
            b0 = 6;
        }*/

        //b0 = getModdedBiomeSize(worldType, b0);
        //genlayer3 = new GenLayerClimateSmooth(3002L,genlayerdeepocean);
        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        Object object = new GenLayerBiome(this.worldSeed, genlayer3);

        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayer, 0);
        return genlayer1;
    }

    private GenLayer mountains() {

        GenLayer genlayerremovetoomuchocean = new GenLayerOceanicIslands(2L, new GenLayerConstant(1L),1,2);
        GenLayer genlayeraddisland = new GenLayerStretchMountain(5L, genlayerremovetoomuchocean);
        //genlayeraddisland = new GenLayerStretchMountain(3L, genlayeraddisland);
        //genlayeraddisland = new GenLayerStretchMountain(3L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerStretchMountain(5L, genlayeraddisland);
        genlayeraddisland = new GenLayerRaiseTooLow(6L,genlayeraddisland);
        genlayeraddisland = new GenLayerAddShore(6L,null);
        //genlayeraddisland = new GenLayerFillLakes(3L,genlayeraddisland);
        //genlayeraddisland = new GenLayerOceanSmooth(4L,genlayeraddisland);
        //genlayeraddisland = new GenLayerAddIsland(6L,genlayeraddisland);
        //genlayeraddisland = new GenLayerFillLakes(3L,genlayeraddisland);
        //genlayeraddisland = new GenLayerOceanSmooth(4L,genlayeraddisland);
        //GenLayer  genlayerzoom = new GenLayerZoom(2002L, genlayeraddisland);
        //genlayeraddisland = new GenLayerGrowMountain(4L, genlayerzoom);
        //genlayerzoom = new GenLayerZoom(2003L, genlayeraddisland);
        //genlayeraddisland = new GenLayerGrowMountain(4L, genlayerzoom);
        //GenLayer genlayerdeepocean = new GenLayerAbyssalPlain(4L, genlayeraddisland);
        //genlayeraddisland = new GenLayerAddIsland(5L,genlayeraddisland);
        /*GenLayer genlayerdeepocean = new GenLayerZoom(1000L, ridges(3));
        GenLayer genlayer3 = new GenLayerAddIsland(5L,genlayerdeepocean);
        genlayer3 = new GenLayerFillLakes(6L,genlayer3);
        genlayer3 = new GenLayerOceanSmooth(3L,genlayer3);*/
        GenLayer genlayer3 = new GenLayerRaiseTooLow(6L,ridges(1));
        genlayer3 = new GenLayerAddIsland(5L,genlayer3);
       // genlayer3 = new GenLayerFillLakes(6L,genlayer3);
        genlayer3 = new GenLayerOceanSmooth(3L,genlayer3);
        genlayer3 = new GenLayerZoom(1000L, genlayer3);
        genlayer3 = new GenLayerAddIsland(5L,genlayer3);
        //genlayer3 = new GenLayerFillLakes(6L,genlayer3);
        genlayer3 = new GenLayerOceanSmooth(3L,genlayer3);
             byte b0 = 4;

        /*if (worldType.equals("LARGE_BIOMES"));
        {
            b0 = 6;
        }*/

        //b0 = getModdedBiomeSize(worldType, b0);
        //genlayer3 = new GenLayerClimateSmooth(3002L,genlayerdeepocean);
        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        //GenLayer object = new GenLayerBiome(this.worldSeed, genlayer3);
        GenLayer object = climates();

        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, object);
        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);

        //genlayer = GenLayerZoom.magnify(1000L, genlayer, 2);
        GenLayer genlayerhills = new GenLayerTransbiomeHills(1000L, object, genlayer1,genlayer);
        //genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
        GenLayerClimateSmooth genlayersmooth = new GenLayerClimateSmooth(1000L, genlayerriver);
        object = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

            if (j == 0)
            {
                object = new GenLayerAddIsland(3L, (GenLayer)object);
            }

            if (j == 1)
            {
                object = new GenLayerShore(1000L, (GenLayer)object);
            }
        }

        GenLayerClimateSmooth genlayersmooth1 = new GenLayerClimateSmooth(1000L, (GenLayer)object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(this.worldSeed);
        genlayervoronoizoom.initWorldGenSeed(this.worldSeed);
        return genlayerrivermix;
    }

    private GenLayer climates() {
        GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, new GenLayerConstant(0L,1));
        GenLayer  genlayerzoom = new GenLayerZoom(2002L, genlayeraddsnow);
        GenLayer genlayeraddisland = new GenLayerAddIsland(4L, genlayerzoom);
        genlayerzoom = new GenLayerZoom(2003L, genlayeraddisland);
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayerzoom, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, new GenLayerClimateSmooth(3000L,genlayeredge), 0);
             byte b0 = 4;

        /*if (worldType.equals("LARGE_BIOMES"));
        {
            b0 = 6;
        }*/

        //b0 = getModdedBiomeSize(worldType, b0);
        genlayer3 = new GenLayerClimateSmooth(3002L,genlayer3);
        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayer genlayer1 = new GenLayerFlatBiome(this.worldSeed, genlayer3);

        return genlayer1;
    }

    private GenLayer ridges(int expansionRounds) {
        GenLayer genlayer = new GenLayerRidgeInit(101L,new GenLayerConstant(0L,1));
        for (int i = 0; i < expansionRounds; i++) {
            genlayer = new GenLayerZoom((long)i + 100L,genlayer);
        }
        genlayer = new GenLayerRidges(105L,genlayer,11,5,2);
        return genlayer;
    }

    public  GenLayer initializeAllBiomeGenerators(long par0)
    {
        boolean flag = false;
        GenLayerIsland genlayerisland = new GenLayerIsland(1L);
        GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
        GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayerfuzzyzoom);
        GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(2L, genlayerzoom);
        genlayeraddisland = new GenLayerAddIsland(50L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(70L, genlayeraddisland);
        GenLayerRemoveTooMuchOcean genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland);
        GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
        genlayeraddisland = new GenLayerAddIsland(3L, genlayeraddsnow);
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        genlayerzoom = new GenLayerZoom(2002L, genlayeredge);
        genlayerzoom = new GenLayerZoom(2003L, genlayerzoom);
        genlayeraddisland = new GenLayerAddIsland(4L, genlayerzoom);
        GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland);
        GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
        byte b0 = 4;

        if (false)
        {
            b0 = 6;
        }

        if (flag)
        {
            b0 = 4;
        }


        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
        Object object = new GenLayerBiome(par0, genlayer3);

        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayerHills genlayerhills = new GenLayerHills(1000L, (GenLayer)object, genlayer1);
        genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
        GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        object = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

            if (j == 0)
            {
                object = new GenLayerAddIsland(3L, (GenLayer)object);
            }

            if (j == 1)
            {
                object = new GenLayerShore(1000L, (GenLayer)object);
            }
        }

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(par0);
        genlayervoronoizoom.initWorldGenSeed(par0);
        return genlayerrivermix;
    }
}
