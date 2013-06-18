package cn.shenyanchao.utils;

import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.expr.NameExpr;

/**
 * Created with IntelliJ IDEA.
 *
 * @author shenyanchao
 *         Date:  6/18/13
 *         Time:  1:48 PM
 */
public class PackageUtils {

    public static String getTestPackageNameFrom(PackageDeclaration packageDeclaration) {
        if (null == packageDeclaration) {
            return "test";
        } else {
            NameExpr packageNameExpr = packageDeclaration.getName();
            String packageName = packageNameExpr.toString();
            return packageName + ".test";
        }
    }
}
