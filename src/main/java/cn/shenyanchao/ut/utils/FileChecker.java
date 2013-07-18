package cn.shenyanchao.ut.utils;

import cn.shenyanchao.ut.common.Consts;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;

import java.io.File;
import java.util.List;

/**
 *
 * @author shenyanchao
 * Date:  6/18/13
 * Time:  3:25 PM
 */
public class FileChecker {

    /**
     * check whether the test java file is exists
     * @param file
     * @return true or false;
     */
    public static boolean isTestJavaClassExist(File file) {
        if (!file.isDirectory()) {
            return file.exists();
        }
        return false;
    }

    /**
     * check whether test method has exists
     * @param method
     * @return existMethod or null
     */
    public static MethodDeclaration isTestCaseExist(CompilationUnit testCU, MethodDeclaration method) {
        MethodDeclaration rtnMethodDeclaration = null;
        if (null == testCU){
            return  null;
        }
        List<TypeDeclaration> types = testCU.getTypes();
        for (TypeDeclaration type : types) {
            List<MethodDeclaration> methodDeclarations = MembersFilter.findMethodsFrom(type);
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                if ((method.getName() + Consts.TEST_SUFFIX).equals(methodDeclaration.getName())) {
                    rtnMethodDeclaration = methodDeclaration;
                }
            }
        }
        return rtnMethodDeclaration;
    }

}
