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
import java.util.Arrays;
import java.util.List;

/**
 * @author shenyanchao
 *         Date:  6/15/13
 *         Time:  3:29 PM
 */
public class CompilationUnitBuilder {

    private CompilationUnit cu = new CompilationUnit();

    public CompilationUnitBuilder buildComment(String commentStr) {
        Comment comment = new JavadocComment(commentStr);
        if (null != cu.getComments()) {
            cu.getComments().add(comment);
        } else {
            cu.setComment(comment);
        }
        return this;
    }

    public CompilationUnitBuilder addComments(List<Comment> comments) {
        cu.setComments(comments);
        return this;
    }

    public CompilationUnitBuilder buildPackage(String packageName) {
        cu.setPackage(new PackageDeclaration(ASTHelper.createNameExpr(packageName)));
        return this;
    }

    public CompilationUnitBuilder addPackage(PackageDeclaration packageDeclaration) {
        cu.setPackage(packageDeclaration);
        return this;
    }

    public CompilationUnitBuilder buildTestNGImports() {

        ImportDeclaration importDeclaration = new ImportDeclaration(new NameExpr("org.testng.annotations.Test"),
                false, false);
        buildImports(Arrays.asList(importDeclaration));
        return this;
    }

    public CompilationUnitBuilder buildMockitoImports() {

        ImportDeclaration importDeclaration = new ImportDeclaration(new NameExpr("org.mockito"),
                true, true);
        buildImports(Arrays.asList(importDeclaration));
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
