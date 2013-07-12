package cn.shenyanchao.ut.builder;

import japa.parser.ASTHelper;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.BlockStmt;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenyanchao
 *         Date:  6/15/13
 *         Time:  3:50 PM
 */
public class ClassTypeBuilder {

    private ClassOrInterfaceDeclaration type = null;

    public ClassTypeBuilder(){
        type = new ClassOrInterfaceDeclaration();
    }

    public ClassTypeBuilder(ClassOrInterfaceDeclaration classOrInterfaceDeclaration){
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
//        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
//        memberValuePairs.add(new MemberValuePair("description", new NameExpr("\"申艳超的测试用例\"")));
//        NormalAnnotationExpr normalAnnotationExpr = new NormalAnnotationExpr(new NameExpr("Test"),null);
//        annotationExprList.add(normalAnnotationExpr);
        List<AnnotationExpr> annotationExprList = new ArrayList<AnnotationExpr>();
        annotationExprList.add(markerAnnotationExpr);
        if (null != method.getParameters()) {
//            method.getParameters().clear();
        }
        method.setAnnotations(annotationExprList);
        method.setType(ASTHelper.VOID_TYPE);
        method.setBody(new BlockStmt());
        return addMethod(method);
    }

    public ClassTypeBuilder addMethod(MethodDeclaration method) {
        ASTHelper.addMember(type, method);
        return this;
    }

    public ClassOrInterfaceDeclaration build() {
        return type;
    }
}