package cn.shenyanchao.ut.utils;

import japa.parser.ast.body.*;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Date:  13-7-10
 * Time:  下午5:18
 *
 * @author shenyanchao
 */
public class TypeUtils {

    public static boolean isJavaBean(TypeDeclaration typeDeclaration) {
        boolean isJavaBean = true;
        List<FieldDeclaration> fields = MembersFilter.findFieldsFrom(typeDeclaration);
        List<MethodDeclaration> methods = MembersFilter.findMethodsFrom(typeDeclaration);
        if (null == fields || null == methods) {
            isJavaBean = false;
        } else {
            for (FieldDeclaration fieldDeclaration : fields) {
                List<VariableDeclarator> variables = fieldDeclaration.getVariables();
                for (VariableDeclarator variableDeclarator : variables) {
                    String fieldName = variableDeclarator.getId().getName();
                    if (!isFieldHaveGetAndSetMethod(typeDeclaration, fieldName)) {
                        isJavaBean = false;
                    }
                }
            }
        }
        return isJavaBean;
    }

    private static String analyzeFieldName(String fieldName) {
        if (fieldName.length() >= 2) {
            char firstChar = fieldName.charAt(0);
            char secondChar = fieldName.charAt(1);
            if (Character.isLowerCase(firstChar) && Character.isUpperCase(secondChar)) {
                return fieldName;
            } else if (Character.isUpperCase(firstChar) && Character.isUpperCase(secondChar)) {
                return fieldName;
            }
        }
        return StringUtils.capitalize(fieldName);
    }

    private static boolean isFieldHaveGetAndSetMethod(TypeDeclaration typeDeclaration, String fieldName) {

        boolean hasSetMethod = false;
        boolean hasGetMethod = false;
        List<MethodDeclaration> methods = MembersFilter.findMethodsFrom(typeDeclaration);
        for (MethodDeclaration methodDeclaration : methods) {
            String methodName = methodDeclaration.getName();
            fieldName = analyzeFieldName(fieldName);
            if (("set" + fieldName).equals(methodName)) {
                hasSetMethod = true;
            }
            if (("get" + fieldName).equals(methodName) || ("is" + fieldName).equals(methodName)) {
                hasGetMethod = true;
            }
        }
        return hasGetMethod && hasSetMethod;
    }

    public static boolean isPrivate(TypeDeclaration typeDeclaration) {
        int modifiers = typeDeclaration.getModifiers();
        return ModifierSet.isPrivate(modifiers);
    }

    public static boolean isPublic(TypeDeclaration typeDeclaration) {
        int modifiers = typeDeclaration.getModifiers();
        return ModifierSet.isPublic(modifiers);
    }

    public static boolean isAbstract(TypeDeclaration typeDeclaration) {
        int modifiers = typeDeclaration.getModifiers();
        return ModifierSet.isAbstract(modifiers);
    }


}
