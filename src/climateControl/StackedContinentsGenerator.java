package climateControl;
import climateControl.utils.RandomIntUser;
import genLayerPack.GenLayer;
import genLayerPack.GenLayerAddIsland;
import genLayerPack.GenLayerEdge;
import genLayerPack.GenLayerFuzzyZoom;
import genLayerPack.GenLayerHills;
import genLayerPack.GenLayerRareBiome;
import genLayerPack.GenLayerRiver;
import genLayerPack.GenLayerRiverInit;
import genLayerPack.GenLayerRiverMix;
import genLayerPack.GenLayerShore;
import genLayerPack.GenLayerSmooth;
import genLayerPack.GenLayerVoronoiZoom;
import genLayerPack.GenLayerZoom;
import climateControl.customGenLayer.GenLayerAddLand;
import climateControl.customGenLayer.GenLayerAdjustIsland;
import climateControl.customGenLayer.GenLayerBiomeByClimate;
import climateControl.customGenLayer.GenLayerCache;
import climateControl.customGenLayer.GenLayerClimateSmooth;
import climateControl.customGenLayer.GenLayerConstant;
import climateControl.customGenLayer.GenLayerContinentalShelf;
import climateControl.customGenLayer.GenLayerDefineClimate;
import climateControl.customGenLayer.GenLayerOceanicMushroomIsland;
import climateControl.customGenLayer.GenLayerRandomBiomes;
import climateControl.customGenLayer.GenLayerRecentBiome;
import climateControl.customGenLayer.GenLayerTemperClimate;


import genLayerPack.GenLayerAddSnow;
import genLayerPack.GenLayerIsland;
import genLayerPack.GenLayerRemoveTooMuchOcean;
//import net.minecraft.world.WorldType;
/**
 * This class creates a world generator from a ClimateControlSettings and a world seed
 * @author Zeno410
 */
public class StackedContinentsGenerator {

    public GenLayerRiverMix stackedContinents(ClimateControlSettings settings, long worldSeed) {

        GenLayer emptyOcean = new GenLayerConstant(0);
        GenLayer genlayerisland = new GenLayerOceanicIslands(1L,emptyOcean,settings.largeContinentFrequency.value());
        GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
        GenLayer genlayeraddisland = new GenLayerAddLand(1L, genlayerfuzzyzoom);
        GenLayer smallContinents = new GenLayerOceanicIslands(2L, genlayeraddisland,settings.mediumContinentFrequency.value());
        genlayeraddisland = new GenLayerAddLand(1L, smallContinents);
        GenLayerZoom genlayerzoom = new GenLayerFuzzyZoom(2001L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddLand(2L, smallContinents);
        smallContinents = new GenLayerOceanicIslands(2L, genlayerzoom,settings.smallContinentFrequency.value());
        genlayeraddisland = new GenLayerAddLand(2L, smallContinents);
        if (settings.doFull()) {
            genlayeraddisland = new GenLayerDefineClimate(2L, genlayeraddisland,2,1,1,2);
        }
        genlayeraddisland = new GenLayerAddLand(50L, genlayeraddisland);
        genlayeraddisland = new GenLayerAdjustIsland(70L, new GenLayerSmooth(1001L, genlayeraddisland),1,5,6);
        genlayeraddisland = new GenLayerZoom(2002L, genlayeraddisland);
        //genlayeraddisland = new GenLayerAddIsland(90L, new GenLayerSmooth(1002L,genlayeraddisland));
        if (settings.doFull()) {
            // climates are already defined so the island creator has to use a climate definer;
            genlayeraddisland = new GenLayerOceanicIslands(
                    2L, genlayeraddisland,settings.largeIslandFrequency.value(),this.islandClimates());
        } else {
            genlayeraddisland = new GenLayerOceanicIslands(2L, genlayeraddisland,settings.largeIslandFrequency.value());
        }
        genlayeraddisland = new GenLayerAddLand(3L, genlayeraddisland);
        if (settings.doHalf()) {
            genlayeraddisland = new GenLayerDefineClimate(2L, genlayeraddisland,settings);
        }
        genlayeraddisland = new GenLayerAdjustIsland(4L, genlayeraddisland,1,5,6);
        genlayeraddisland = new GenLayerClimateSmooth(4L, genlayeraddisland);
        genlayeraddisland = new GenLayerZoom(2003L, genlayeraddisland);
        if (settings.quarterSize.value()) {
            genlayeraddisland = new GenLayerDefineClimate(2L, genlayeraddisland,settings);
        }
        genlayeraddisland = new GenLayerAddLand(3L, genlayeraddisland);
        genlayeraddisland = new GenLayerAdjustIsland(4L, genlayeraddisland,1,5,6);
        genlayeraddisland = new GenLayerClimateSmooth(4L, genlayeraddisland);
        genlayeraddisland = new GenLayerCache(new GenLayerTemperClimate(0L,genlayeraddisland));
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        GenLayer genlayerdeepocean = new GenLayerContinentalShelf(4L, genlayeredge);
        GenLayer genlayeraddmushroomisland = new GenLayerOceanicMushroomIsland(5L, genlayerdeepocean, settings.);
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, new GenLayerClimateSmooth(3000L,genlayeraddmushroomisland), 0);
             byte b0 = 4;

        /*if (worldType.equals("LARGE_BIOMES"));
        {
            b0 = 6;
        }*/

        //b0 = getModdedBiomeSize(worldType, b0);
        //genlayer3 = new GenLayerClimateSmooth(3002L,genlayerdeepocean);
        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayer flatBiomes = new GenLayerRecentBiome(worldSeed, genlayer3);
        flatBiomes = new GenLayerZoom(1000L, flatBiomes);
        flatBiomes = new GenLayerZoom(1000L, flatBiomes);
        flatBiomes = new GenLayerZoom(1000L, flatBiomes);
        flatBiomes = new GenLayerZoom(1000L, flatBiomes);
        flatBiomes = new GenLayerZoom(1000L, flatBiomes);
        flatBiomes = new GenLayerZoom(1000L, flatBiomes);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);

        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer genlayerhills = new GenLayerHills(1000L, flatBiomes, genlayer1);
        genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
        GenLayerClimateSmooth genlayersmooth = new GenLayerClimateSmooth(1000L, genlayerriver);
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

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, forRareBiomes);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        //GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, forRareBiomes, genlayerriver);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(worldSeed);
        genlayervoronoizoom.initWorldGenSeed(worldSeed);
        return traditionalExpansion(worldSeed,genlayer3, settings);
    }

    private RandomIntUser islandClimates() {
        // these numbers are different from "standard" as island climates don't get normalized so
        // a bias to extreme climates isn't necessary
        return new ClimateChooser(1,1,1,1);
    }

    public static GenLayerRiverMix traditionalExpansion(long par0, GenLayer genlayer3,ClimateControlSettings settings){

        BiomeRandomizer.instance.set(settings.biomeIncidences);
        boolean flag = false;
        byte b0 = 4;
        //b0 = getModdedBiomeSize(par2WorldType, b0);

        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
        GenLayer biomes = null;
        if (settings.randomBiomes.value()) {
            biomes = new GenLayerRandomBiomes(par0,genlayer3);
        } else {
            biomes = new GenLayerBiomeByClimate(par0,genlayer3);
        }//par2WorldType.getBiomeLayer(par0, genlayer3);

        GenLayer object = new GenLayerZoom(1000L, biomes);
        object = new GenLayerAddLand(1000L, object);
        object  = new GenLayerClimateSmooth(100L,object);
        object = new GenLayerZoom(1001L, object);
        object = new GenLayerAddLand(1000L, object);
        object  = new GenLayerClimateSmooth(101L,object);

        //object = biomes; this is to shrink maps for viewing in AMIDST
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
                object  = new GenLayerClimateSmooth(100L,object);
            }

            if (j == 1)
            {
                object = new GenLayerShore(1000L, (GenLayer)object);
            }
        }

        GenLayer genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(par0);
        genlayervoronoizoom.initWorldGenSeed(par0);
        return genlayerrivermix;
    }
}
