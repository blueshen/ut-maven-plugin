package cn.shenyanchao.ut.builder;

import japa.parser.ASTHelper;
import japa.parser.ast.Comment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.expr.NameExpr;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:  6/15/13
 * Time:  3:29 PM
 *
 * @author shenyanchao
 */
public class CompilationUnitBuilder {

    private CompilationUnit cu;

    public CompilationUnitBuilder() {
        cu = new CompilationUnit();
    }

    public CompilationUnitBuilder(CompilationUnit cu) {
        this.cu = cu;
    }

    public CompilationUnitBuilder buildComment(String commentStr) {
        Comment comment = new JavadocComment(commentStr);
        if (null != cu.getComments()) {
            cu.getComments().add(comment);
        } else {
            cu.setComment(comment);
        }
        return this;
    }

    public CompilationUnitBuilder buildPackage(String packageName) {
        cu.setPackage(new PackageDeclaration(ASTHelper.createNameExpr(packageName)));
        return this;
    }


    public CompilationUnitBuilder buildTestNGImports() {
        List<ImportDeclaration> testngImports = new ArrayList<ImportDeclaration>();
        testngImports.add(new ImportDeclaration(new NameExpr("org.testng.annotations"),
                false, true));
        testngImports.add(new ImportDeclaration(new NameExpr("org.testng.Assert"),
                false, false));
        buildImports(testngImports);
        return this;
    }

    public CompilationUnitBuilder buildMockitoImports() {
        List<ImportDeclaration> mockitoImports = new ArrayList<ImportDeclaration>();
        mockitoImports.add(new ImportDeclaration(new NameExpr("org.mockito.Mockito"),
                true, true));
        mockitoImports.add(new ImportDeclaration(new NameExpr("org.mockito.InjectMocks"),
                false, false));
        mockitoImports.add(new ImportDeclaration(new NameExpr("org.mockito.Mock"),
                false, false));
        mockitoImports.add(new ImportDeclaration(new NameExpr("org.mockito.MockitoAnnotations"),
                false, false));
        buildImports(mockitoImports);
        return this;
    }

    public CompilationUnitBuilder buildImports(List<ImportDeclaration> importDeclarations) {
        if (null == cu.getImports()) {
            cu.setImports(importDeclarations);
        } else {
            List<ImportDeclaration> existImport = cu.getImports();
            existImport = new ArrayList<ImportDeclaration>(existImport);
            existImport.addAll(importDeclarations);
            cu.setImports(existImport);
        }
        return this;
    }

    public CompilationUnitBuilder buildClass(ClassOrInterfaceDeclaration type) {
        ASTHelper.addTypeDeclaration(cu, type);
        return this;
    }

    public CompilationUnit build() {
        return cu;
    }

}
