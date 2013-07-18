package cn.shenyanchao.ut.receiver;

import cn.shenyanchao.ut.builder.ClassTypeBuilder;
import cn.shenyanchao.ut.builder.CompilationUnitBuilder;
import cn.shenyanchao.ut.common.Consts;
import cn.shenyanchao.ut.common.FileComments;
import cn.shenyanchao.ut.utils.JavaParserUtils;
import cn.shenyanchao.ut.utils.MembersFilter;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.NameExpr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Date:  6/20/13
 * Time:  7:14 PM
 *
 * @author shenyanchao
 */
public class NewTestReceiver extends AbstractReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(NewTestReceiver.class);

    private CompilationUnit sourceCU;

    private File javaFile;

    public NewTestReceiver(CompilationUnit sourceCU, File javaFile) {
        this.sourceCU = sourceCU;
        this.javaFile = javaFile;
    }

    @Override
    public CompilationUnitBuilder createCU() {

        String testPackageName = JavaParserUtils.findTestPackageName(sourceCU);
        TypeDeclaration typeDeclaration = JavaParserUtils.findTargetTypeDeclaration(sourceCU, javaFile);

        CompilationUnitBuilder compilationUnitBuilder = new CompilationUnitBuilder();
        compilationUnitBuilder.buildComment(FileComments.GENERATOR_COMMENT);

        List<MethodDeclaration> methodDeclarations = MembersFilter.findMethodsFrom(typeDeclaration);
        String className = typeDeclaration.getName();
        ClassTypeBuilder classTypeBuilder = new ClassTypeBuilder(className + Consts.TEST_SUFFIX);
        compilationUnitBuilder.buildComment(FileComments.GENERATOR_COMMENT);
        compilationUnitBuilder.buildPackage(testPackageName);
        //process test import
        compilationUnitBuilder.buildTestNGImports();
        compilationUnitBuilder.buildMockitoImports();

        compilationUnitBuilder.buildImports(sourceCU.getImports());
        ImportDeclaration pkgImport = new ImportDeclaration(new NameExpr(sourceCU.getPackage().getName().toString()),
                false, true);
        compilationUnitBuilder.buildImports(Arrays.asList(pkgImport));
        classTypeBuilder.buildMockitoSetUpMethod();
        //process methods
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            String methodName = methodDeclaration.getName();
            classTypeBuilder.buildMethod(methodName + Consts.TEST_SUFFIX, methodDeclaration);
        }
        compilationUnitBuilder.buildClass(classTypeBuilder.build());
        return compilationUnitBuilder;
    }
}
