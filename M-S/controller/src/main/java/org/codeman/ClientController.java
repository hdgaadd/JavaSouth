package org.codeman;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * Created on 2022/05/02
 */
@Slf4j
public class ClientController extends BaseController {
    public static void main(String[] args) {
        log.info(setSuccessful("request succeeded").toString());
    }
}
