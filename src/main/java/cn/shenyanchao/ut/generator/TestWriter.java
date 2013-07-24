package cn.shenyanchao.ut.generator;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Date:  6/18/13
 * Time:  1:16 PM
 *
 * @author shenyanchao
 */
public class TestWriter {

    private static final Logger LOG = LoggerFactory.getLogger(TestWriter.class);

    /**
     * write Test Java File
     *
     * @param testJavaFileName
     * @param testSource
     * @param encode
     */
    public static void writeJavaTest(String testJavaFileName, String testSource, String encode) {

        try {
            FileUtils.writeStringToFile(new File(testJavaFileName), testSource,
                    Charset.forName(encode));
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        LOG.debug(testJavaFileName);
        LOG.debug(testSource);
    }

}
