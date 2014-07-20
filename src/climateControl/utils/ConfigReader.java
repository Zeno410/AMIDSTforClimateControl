
package climateControl.utils;

import minecraftForge.Configuration;

/**
 *
 * @author Zeno410
 */
public interface ConfigReader {
    public void readFrom(Configuration source);
    public void copyTo(Configuration target);
}
