package cn.shenyanchao.ut;

import japa.parser.ASTHelper;
import japa.parser.ast.CompilationUnit;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author shenyanchao
 *         Date:  6/15/13
 *         Time:  12:16 PM
 */
public class JavaParserCreateCU {

    private static final Logger LOG = LoggerFactory.getLogger(JavaParserCreateCU.class);

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

        LOG.info(cu.toString());
    }
}
