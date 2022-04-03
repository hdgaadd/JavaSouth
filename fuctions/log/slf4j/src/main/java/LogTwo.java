import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * Created on 2022/01/27
 */
@Slf4j
public class LogTwo { // 只需引入lombok即可

    public static void main(String[] args) {
        log.info("info");
        log.debug("debug");
        log.warn("warn");
        log.error("error");
    }
}
