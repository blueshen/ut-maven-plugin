package cn.shenyanchao.utils;

import japa.parser.ASTHelper;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author shenyanchao
 *         Date:  6/15/13
 *         Time:  3:50 PM
 */
public class ClassTypeBuilder {

    private ClassOrInterfaceDeclaration type = null;

    public ClassTypeBuilder(String clazzName) {
        type = new ClassOrInterfaceDeclaration(ModifierSet.PUBLIC, false, clazzName);
    }


    public ClassTypeBuilder buildMethod(String methodName, String... params) {
        MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.VOID_TYPE, methodName);
        method.setModifiers(ModifierSet.addModifier(method.getModifiers(), ModifierSet.STATIC));
        ASTHelper.addMember(type, method);
        for (String paramName : params) {
            Parameter parameter = ASTHelper.createParameter(ASTHelper.createReferenceType("String", 0), paramName);
            ASTHelper.addParameter(method, parameter);
        }
        return this;
    }

    public ClassOrInterfaceDeclaration build() {
        return type;
    }
}
