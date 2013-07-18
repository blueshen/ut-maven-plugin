package cn.shenyanchao.ut.utils.test;

import cn.shenyanchao.ut.utils.FileChecker;
import org.testng.annotations.Test;
import java.io.File;
import static org.testng.Assert.*;



/**
 * Date:  13-7-12
 * Time:  上午11:42
 *
 * @author shenyanchao
 */
public class FileCheckerTest {


    @Test
    public void isTestJavaClassExistTest() {
        boolean falseExist = FileChecker.isTestJavaClassExist(new File("  "));
        assertFalse(falseExist);
        String existFileName = System.getProperty("user.dir") +
                ("/src/test/java/cn/shenyanchao/ut/utils/test/FileCheckerTest.java".replaceAll("/", File.separator));
        boolean trueExist = FileChecker.isTestJavaClassExist(new File(existFileName));
        assertTrue(trueExist);
    }

}
