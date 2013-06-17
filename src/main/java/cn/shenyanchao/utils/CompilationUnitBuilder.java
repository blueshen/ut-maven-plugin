package cn.shenyanchao.utils;

import japa.parser.ASTHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
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

    public CompilationUnitBuilder buildPackage(String packageName) {
        cu.setPackage(new PackageDeclaration(ASTHelper.createNameExpr(packageName)));
        return this;
    }
    public CompilationUnitBuilder buildImports(String[] imports) {
        List<ImportDeclaration> importList = new ArrayList<ImportDeclaration>();
        importList.add(new ImportDeclaration(new NameExpr("cn.shenyanchao.import"),false,false));
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

    public static void main(String[] args) {
        CompilationUnit cu = new CompilationUnit();
        //set the package
        cu.setPackage(new PackageDeclaration(ASTHelper.createNameExpr("cn.shenyanchao.javaparser.test")));
        //create the type declaration
        ClassOrInterfaceDeclaration type = new ClassOrInterfaceDeclaration(ModifierSet.PUBLIC, false, "HelloWorld");
        ASTHelper.addTypeDeclaration(cu, type);
        //create a method
        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.VOID_TYPE, "main");
        method.setModifiers(ModifierSet.addModifier(method.getModifiers(), ModifierSet.STATIC));
        ASTHelper.addMember(type, method);

        //add a parameter to the method
        Parameter param = ASTHelper.createParameter(ASTHelper.createReferenceType("String", 1), "args");
//        param.setVarArgs(true);
        ASTHelper.addParameter(method, param);

        //add a body to the method
        BlockStmt block = new BlockStmt();
        method.setBody(block);

        //add a statement do the method body
        NameExpr clazz = new NameExpr("System");
        FieldAccessExpr field = new FieldAccessExpr(clazz, "out");
        MethodCallExpr call = new MethodCallExpr(field, "println");
        ASTHelper.addArgument(call, new StringLiteralExpr("Hello World"));
        ASTHelper.addStmt(block, call);

        System.out.println(cu.toString());

    }
}
