package cn.shenyanchao.ut.receiver;

import cn.shenyanchao.ut.builder.ClassTypeBuilder;
import cn.shenyanchao.ut.builder.CompilationUnitBuilder;
import cn.shenyanchao.ut.common.Consts;
import cn.shenyanchao.ut.utils.FileChecker;
import cn.shenyanchao.ut.utils.JavaParserUtils;
import cn.shenyanchao.ut.utils.MembersFilter;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Date:  6/20/13
 * Time:  7:15 PM
 *
 * @author shenyanchao
 */
public class ExistTestReceiver extends AbstractReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(ExistTestReceiver.class);

    private CompilationUnit sourceCU;

    private File javaFile;

    private CompilationUnit testCU;

    private File testJavaFile;

    public ExistTestReceiver(CompilationUnit sourceCU, File javaFile, CompilationUnit testCU, File testJavaFile) {
        this.sourceCU = sourceCU;
        this.javaFile = javaFile;
        this.testCU = testCU;
        this.testJavaFile = testJavaFile;
    }

    @Override
    public CompilationUnitBuilder createCUBuilder() {
        CompilationUnitBuilder compilationUnitBuilder = new CompilationUnitBuilder(testCU);
        TypeDeclaration typeDeclaration = JavaParserUtils.findTargetTypeDeclaration(sourceCU, javaFile);
        TypeDeclaration testTypeDeclaration = JavaParserUtils.findTargetTypeDeclaration(testCU, testJavaFile);
        List<MethodDeclaration> methodDeclarations = MembersFilter.findMethodsFrom(typeDeclaration);

        ClassTypeBuilder classTypeBuilder = new ClassTypeBuilder((ClassOrInterfaceDeclaration) testTypeDeclaration);

        //process methods
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            String methodName = methodDeclaration.getName();
            MethodDeclaration testMethodDeclaration = FileChecker.isTestCaseExist(testCU,
                    methodDeclaration);
            boolean methodExist = (testMethodDeclaration == null ? false : true);
            if (!methodExist) {
                classTypeBuilder.buildMethod(methodName + Consts.TEST_SUFFIX, methodDeclaration);

            }
        }
        return compilationUnitBuilder;

    }

    @Override
    public CompilationUnit createCU() {
        return testCU;
    }
}
