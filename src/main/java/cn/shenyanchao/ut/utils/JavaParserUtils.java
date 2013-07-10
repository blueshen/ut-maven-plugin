package cn.shenyanchao.ut.utils;

import cn.shenyanchao.ut.common.Consts;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * @author shenyanchao
 *         Date:  6/20/13
 *         Time:  8:15 PM
 */
public class JavaParserUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JavaParserUtils.class);

    /**
     * find Test Package Name
     *
     * @param sourceCU
     * @return
     */
    public static String findTestPackageName(CompilationUnit sourceCU) {
        PackageDeclaration packageDeclaration = sourceCU.getPackage();
        String testPackageName = PackageUtils.getTestPackageNameFrom(packageDeclaration);
        return testPackageName;
    }

    /**
     * find target Test Java File Name
     *
     * @param sourceCU
     * @param javaFile
     * @param testDir
     * @return
     */
    public static String findTestJavaFileName(CompilationUnit sourceCU, File javaFile, String testDir) {

        TypeDeclaration typeDeclaration = findTargetTypeDeclaration(sourceCU, javaFile);
        String className = typeDeclaration.getName();
        String testJavaFileName = PackageUtils.getTestJavaSourceName(testDir, findTestPackageName(sourceCU)
                , className + Consts.TEST_SUFFIX);
        return testJavaFileName;
    }

    /**
     * Find the Class which name is same as FileName
     *
     * @param sourceCU
     * @param javaFile
     * @return
     */
    public static TypeDeclaration findTargetTypeDeclaration(CompilationUnit sourceCU, File javaFile) {
        List typeList = sourceCU.getTypes();
        String sourceFileName = javaFile.getName();
        TypeDeclaration targetTypeDeclaration = null;
        for (Object type : typeList) {
            TypeDeclaration typeDeclaration = (TypeDeclaration) type;
            String className = typeDeclaration.getName();
            if (sourceFileName.equals(className + Consts.JAVA_SUFFIX)) {
                targetTypeDeclaration = typeDeclaration;
                break;
            }
        }
        if (null == targetTypeDeclaration) {
            LOG.error("your source code has error：" + javaFile.getAbsolutePath());
        }
        return targetTypeDeclaration;
    }


}
