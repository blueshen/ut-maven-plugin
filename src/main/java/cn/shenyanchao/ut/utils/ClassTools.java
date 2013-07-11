package cn.shenyanchao.ut.utils;

import cn.shenyanchao.ut.common.Consts;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Date:  13-7-11
 * Time:  下午3:47
 *
 * @author shenyanchao
 */
public class ClassTools {

    private static final Logger LOG = LoggerFactory.getLogger(ClassTools.class);


    private static TypeDeclaration javaFileToTargetType(File javaFile, String encode) {
        CompilationUnit sourceCU = JavaParserFactory.getCompilationUnit(javaFile, encode);
        TypeDeclaration targetType = JavaParserUtils.findTargetTypeDeclaration(sourceCU, javaFile);
        return targetType;
    }

    public static boolean isClassByTypeOf(TypeDeclaration typeDeclaration, String encode) {
        boolean isClass = true;
        if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration classOrInterfaceDeclaration = ((ClassOrInterfaceDeclaration) typeDeclaration);
            if (classOrInterfaceDeclaration.isInterface()) {//is interface
                isClass = false;
            }
        } else { //neither class nor interface
            isClass = false;
        }
        return isClass;
    }

    public static boolean isNeedTest(File javaFile, String encode) {
        boolean isNeed = false;
        TypeDeclaration targetType = javaFileToTargetType(javaFile, encode);
        if (isClassByTypeOf(targetType, encode)) {
            if (TypeUtils.isPublic(targetType) && !TypeUtils.isAbstract(targetType)
                    && !TypeUtils.isJavaBean(targetType)) {
                isNeed = true;
            }
        }
        return isNeed;
    }

    public static boolean isNeedTest(File javaFile) {
        return isNeedTest(javaFile, Consts.DEFAULT_ENCODE);
    }
}
