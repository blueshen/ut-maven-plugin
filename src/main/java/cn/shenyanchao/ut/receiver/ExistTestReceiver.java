package cn.shenyanchao.ut.receiver;

import cn.shenyanchao.ut.builder.ClassTypeBuilder;
import cn.shenyanchao.ut.builder.CompilationUnitBuilder;
import cn.shenyanchao.ut.common.Consts;
import cn.shenyanchao.ut.common.FileComments;
import cn.shenyanchao.ut.utils.FileChecker;
import cn.shenyanchao.ut.utils.JavaParserUtils;
import cn.shenyanchao.ut.utils.MembersFilter;
import japa.parser.ast.Comment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;

import java.io.File;
import java.util.List;

/**
 * @author shenyanchao
 *         <p/>
 *         Date:  6/20/13
 *         Time:  7:15 PM
 */
public class ExistTestReceiver extends AbstractReceiver {

    private CompilationUnit sourceCU;

    private CompilationUnit testCU;

    private File javaFile;

    public ExistTestReceiver(CompilationUnit sourceCU, File javaFile, CompilationUnit testCU) {
        this.sourceCU = sourceCU;
        this.javaFile = javaFile;
        this.testCU = testCU;
    }

    @Override
    public CompilationUnitBuilder createCU() {
        CompilationUnitBuilder compilationUnitBuilder = new CompilationUnitBuilder();
        compilationUnitBuilder.buildComment(FileComments.GENERATOR_COMMENT);
        TypeDeclaration typeDeclaration = JavaParserUtils.findTargetTypeDeclaration(sourceCU, javaFile);
        List<MethodDeclaration> methodDeclarations = MembersFilter.findMethodsFrom(typeDeclaration);
        String className = typeDeclaration.getName();
        ClassTypeBuilder classTypeBuilder = new ClassTypeBuilder(className + Consts.TEST_SUFFIX);

        List<Comment> existComments = testCU.getComments();
        compilationUnitBuilder.addComments(existComments);
        PackageDeclaration existPackageDeclaration = testCU.getPackage();
        compilationUnitBuilder.addPackage(existPackageDeclaration);
        //process import
        List<ImportDeclaration> existImports = testCU.getImports();
        compilationUnitBuilder.addImports(existImports);
        //process methods
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            String methodName = methodDeclaration.getName();
            MethodDeclaration testMethodDeclaration = FileChecker.isTestCaseExist(testCU,
                    methodDeclaration);
            boolean methodExist = (testMethodDeclaration == null ? false : true);
            if (!methodExist) {
                classTypeBuilder.buildMethod(methodName + Consts.TEST_SUFFIX, methodDeclaration);
            } else if (methodExist) {
                classTypeBuilder.addMethod(testMethodDeclaration);
            }
        }
        compilationUnitBuilder.buildClass(classTypeBuilder.build());
        return compilationUnitBuilder;
    }
}
