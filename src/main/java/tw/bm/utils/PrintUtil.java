package tw.bm.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Neil
 * @date 2017/9/10.
 */
public class PrintUtil {
    private static Logger logger = LogManager.getLogger(PrintUtil.class);


    public static void info(String string) {
        logger.info(string);
    }

    public static void error(String string) {
        logger.error(string);
    }

    public static void out(String string) {
        logger.info(string);
    }
}
