package cn.shenyanchao.ut.utils;

import cn.shenyanchao.ut.common.Consts;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.expr.NameExpr;

import java.io.File;

/**
 * Date:  6/18/13
 * Time:  1:48 PM
 *
 * @author shenyanchao
 */
public class PackageUtils {

    /**
     * find what is test package
     *
     * @param packageDeclaration
     * @return
     */
    public static String getTestPackageNameFrom(PackageDeclaration packageDeclaration) {
        if (null == packageDeclaration) {
            return Consts.TEST_PACKAGE;
        } else {
            NameExpr packageNameExpr = packageDeclaration.getName();
            String packageName = packageNameExpr.toString();
            return packageName + "." + Consts.TEST_PACKAGE;
        }
    }

    /**
     * find what is the test java file name
     *
     * @param testDir
     * @param testPackageName
     * @param className
     * @return
     */
    public static String getTestJavaSourceName(String testDir, String testPackageName, String className) {
        StringBuilder sb = new StringBuilder();
        sb.append(testDir).append(File.separator);
        sb.append(PathUtils.packageToPath(testPackageName)).append(File.separator);
        sb.append(className).append(Consts.JAVA_SUFFIX);
        return sb.toString();
    }
}
