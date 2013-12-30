package cn.shenyanchao.ut.builder;

import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:  13-12-30
 * Time:  下午5:46
 *
 * @author shenyanchao
 */
public class FieldBuilder {

    private FieldDeclaration fieldDeclaration = new FieldDeclaration();

    public FieldBuilder buildModifer(int modifiers) {
        fieldDeclaration.setModifiers(modifiers);
        return this;
    }

    public FieldBuilder buildFieldType(String name) {
        fieldDeclaration.setType(new ClassOrInterfaceType(name));
        return this;
    }

    public FieldBuilder buildFieldAnnotation(String annotation) {
        List<AnnotationExpr> injectMockAnnos = new ArrayList<AnnotationExpr>();
        if (null != fieldDeclaration.getAnnotations()) {
            injectMockAnnos.addAll(fieldDeclaration.getAnnotations());
        }
        MarkerAnnotationExpr annotationExpr
                = new MarkerAnnotationExpr(new NameExpr(annotation));
        injectMockAnnos.add(annotationExpr);
        fieldDeclaration.setAnnotations(injectMockAnnos);
        return this;
    }

    public FieldBuilder buildFieldVarName(String varName) {
        List<VariableDeclarator> variables = new ArrayList<VariableDeclarator>();
        if (null != fieldDeclaration.getVariables()) {
            variables.addAll(fieldDeclaration.getVariables());
        }
        VariableDeclaratorId id = new VariableDeclaratorId(varName);
        VariableDeclarator variable = new VariableDeclarator(id);
        variables.add(variable);
        fieldDeclaration.setVariables(variables);
        return this;
    }

    public FieldBuilder buildFieldComment(String comment) {
        JavadocComment javadocComment = new JavadocComment(comment);
        fieldDeclaration.setJavaDoc(javadocComment);
        return this;
    }

    public FieldDeclaration build() {
        return fieldDeclaration;
    }

}
