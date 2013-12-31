package cn.shenyanchao.ut.builder;

import cn.shenyanchao.ut.factory.BlockStmtFactory;
import japa.parser.ASTHelper;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenyanchao
 *         Date:  6/15/13
 *         Time:  3:50 PM
 */
public class ClassTypeBuilder {

    private ClassOrInterfaceDeclaration type = null;

    public ClassTypeBuilder() {
        type = new ClassOrInterfaceDeclaration();
    }

    public ClassTypeBuilder(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.type = classOrInterfaceDeclaration;
    }

    public ClassTypeBuilder(String clazzName) {
        type = new ClassOrInterfaceDeclaration(ModifierSet.PUBLIC, false, clazzName);
    }

    public ClassTypeBuilder buildComment(String commentStr) {
        JavadocComment comment = new JavadocComment(commentStr);
        type.setComment(comment);
        return this;
    }

    public ClassTypeBuilder buildMethod(String methodName, MethodDeclaration method, String... params) {

        method.setName(methodName);
        MarkerAnnotationExpr markerAnnotationExpr = new MarkerAnnotationExpr(new NameExpr("Test"));
        List<AnnotationExpr> annotationExprList = new ArrayList<AnnotationExpr>();
        annotationExprList.add(markerAnnotationExpr);
        if (null != method.getParameters()) {
//            method.getParameters().clear();
        }
        method.setComment(null);
        method.setAnnotations(annotationExprList);
        method.setType(ASTHelper.VOID_TYPE);
        method.setBody(BlockStmtFactory.createAssertStmt());
        return addMethod(method);
    }

    public ClassTypeBuilder buildMockitoSetUpMethod() {
        MethodBuilder methodBuilder = new MethodBuilder();
        methodBuilder.buildMethodModifier(ModifierSet.PUBLIC);
        methodBuilder.buildMethodReturnType(ASTHelper.VOID_TYPE);
        methodBuilder.buildMethodName("initMocks");
        methodBuilder.buildMethodAnnotations("BeforeClass");

        List<Statement> statements = new ArrayList<Statement>();
//        MockitoAnnotations.initMocks(this);
//        statements.add(new LabeledStmt());
        methodBuilder.buildBody(new BlockStmt(statements));

        MethodDeclaration mockitoSetUpMethod = methodBuilder.build();
        addMethod(mockitoSetUpMethod);
        return this;
    }

    public ClassTypeBuilder addMethod(MethodDeclaration method) {
        ASTHelper.addMember(type, method);
        return this;
    }

    public ClassOrInterfaceDeclaration build() {
        return type;
    }
}