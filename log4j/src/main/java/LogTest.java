import org.apache.log4j.Logger;

/**
 * @author hdgaadd
 * Created on 2022/01/22
 */
public class LogTest {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(LogTest.class);

        logger.info("info");
        logger.debug("Here have debug");
        logger.warn("warning");
        logger.error("error");
    }
}
