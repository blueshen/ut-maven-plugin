package cn.shenyanchao.ut.builder;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:  13-7-11
 * Time:  下午5:17
 *
 * @author shenyanchao
 */
public class MethodBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(MethodBuilder.class);

    private MethodDeclaration method = new MethodDeclaration();

    public MethodBuilder buildMethodModifier(int modifiers) {
        method.setModifiers(modifiers);
        return this;
    }

    public MethodBuilder buildMethodName(String methodName) {
        method.setName(methodName);
        return this;
    }

    public MethodBuilder buildMethodReturnType(Type returnType) {
        method.setType(returnType);
        return this;
    }

    public MethodBuilder buildMethodParameters(List<Parameter> parameters) {
        method.setParameters(parameters);
        return this;
    }

    public MethodBuilder buildMethodAnnotations(String annotation) {
        List<AnnotationExpr> annotationExprList = new ArrayList<AnnotationExpr>();
        MarkerAnnotationExpr markerAnnotationExpr = new MarkerAnnotationExpr(new NameExpr(annotation));
        annotationExprList.add(markerAnnotationExpr);
        method.setAnnotations(annotationExprList);
        return this;
    }

    public MethodBuilder buildBody(BlockStmt stmt) {
        method.setBody(stmt);
        return this;
    }

    public MethodDeclaration build() {
        return method;
    }
}
