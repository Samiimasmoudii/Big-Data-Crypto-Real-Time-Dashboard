package org.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Utility class to read property file
 */
public class PropertyFileReader {

    private static final Logger logger = Logger.getLogger(PropertyFileReader.class);

    private static Properties prop = new Properties();

    public static Properties readPropertyFile(String file) throws Exception {
        if (prop.isEmpty()) {
            InputStream input = null;
            try {
                input = PropertyFileReader.class.getClassLoader().getResourceAsStream(file);
                if (input == null) {
                    logger.error("Property file " + file + " not found in classpath");
                    throw new IOException("Property file not found");
                }
                prop.load(input);
            } catch (IOException ex) {
                logger.error("Error reading property file: " + file, ex);
                throw ex;
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                        logger.error("Error closing InputStream", ex);
                    }
                }
            }
        }
        return prop;
    }
}
