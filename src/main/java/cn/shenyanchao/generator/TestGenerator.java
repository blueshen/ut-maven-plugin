package cn.shenyanchao.generator;

import cn.shenyanchao.common.Consts;
import cn.shenyanchao.utils.PathUtils;
import japa.parser.ast.CompilationUnit;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 *
 * @author shenyanchao
 *         Date:  6/18/13
 *         Time:  1:16 PM
 */
public class TestGenerator {

    public static void writeJavaTest(String testDir, CompilationUnit testCompilationUnit) {
        String packageName = testCompilationUnit.getPackage().getName().toString();
        String testJavaFile = testDir + File.separator + PathUtils.packageToPath(packageName) + File.separator + testCompilationUnit.getTypes().get(0).getName() + Consts.JAVA_SUFFIX;
//        try {
//            FileUtils.writeStringToFile(new File(testJavaFile), testCompilationUnit.toString(), Charset.forName(Consts.DEFAULT_ENCODE));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
