package cn.shenyanchao.builder;

import japa.parser.ASTHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.stmt.BlockStmt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author shenyanchao
 *         Date:  6/15/13
 *         Time:  3:29 PM
 */
public class CompilationUnitBuilder {

    private CompilationUnit cu = new CompilationUnit();

    public CompilationUnitBuilder buildComment(String commentStr) {
        JavadocComment comment = new JavadocComment(commentStr);
        cu.setComment(comment);
        return this;
    }

    public CompilationUnitBuilder buildPackage(String packageName) {
        cu.setPackage(new PackageDeclaration(ASTHelper.createNameExpr(packageName)));
        return this;
    }
    public CompilationUnitBuilder buildImports(String[] imports) {
        List<ImportDeclaration> importList = new ArrayList<ImportDeclaration>();
        importList.add(new ImportDeclaration(new NameExpr("org.testng.annotations.Test"),false,false));
        cu.setImports(importList);
        return this;
    }

    public CompilationUnitBuilder buildClass(ClassOrInterfaceDeclaration type){
        ASTHelper.addTypeDeclaration(cu, type);
        return  this;
    }

    public CompilationUnit build(){
        return cu;
    }

}
