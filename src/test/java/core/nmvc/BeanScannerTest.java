package core.nmvc;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.di.BeanScanner;

public class BeanScannerTest {
    private static final Logger logger = LoggerFactory.getLogger(BeanScannerTest.class);

    private BeanScanner cf;

    @Before
    public void setup() {
        cf = new BeanScanner("core.nmvc");
    }
}
