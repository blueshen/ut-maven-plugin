package cn.shenyanchao.ut.utils;

import japa.parser.ast.body.*;
import japa.parser.ast.expr.AnnotationExpr;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Date:  13-7-10
 * Time:  下午5:18
 *
 * @author shenyanchao
 */
public class TypeUtils {

    /**
     * if class is POJO
     *
     * @param typeDeclaration
     * @return true or false
     */
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

    /**
     * getter or setter process
     *
     * @param fieldName
     * @return methodName without get or set prefix
     */
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

    /**
     * is method have get and set method
     *
     * @param typeDeclaration
     * @param fieldName
     * @return true or false
     */
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

    /**
     * is private class
     *
     * @param typeDeclaration
     * @return true or false
     */
    public static boolean isPrivate(TypeDeclaration typeDeclaration) {
        int modifiers = typeDeclaration.getModifiers();
        return ModifierSet.isPrivate(modifiers);
    }

    /**
     * is public class
     *
     * @param typeDeclaration
     * @return true or false
     */
    public static boolean isPublic(TypeDeclaration typeDeclaration) {
        int modifiers = typeDeclaration.getModifiers();
        return ModifierSet.isPublic(modifiers);
    }

    /**
     * is  abstract class
     *
     * @param typeDeclaration
     * @return true or false
     */
    public static boolean isAbstract(TypeDeclaration typeDeclaration) {
        int modifiers = typeDeclaration.getModifiers();
        return ModifierSet.isAbstract(modifiers);
    }

    public static boolean isNeedTest(TypeDeclaration typeDeclaration) {
        List<String> springAnnotations = new ArrayList<String>();
        springAnnotations.add(Service.class.getSimpleName());
        springAnnotations.add(Repository.class.getSimpleName());
        springAnnotations.add(Controller.class.getSimpleName());
        springAnnotations.add(Component.class.getSimpleName());
        springAnnotations.add(Resource.class.getSimpleName());
        List<AnnotationExpr> annotationExprs = typeDeclaration.getAnnotations();
        if (null != annotationExprs) {
            for (AnnotationExpr annotationExpr : annotationExprs) {
                String annotation = annotationExpr.getName().getName();
                if (springAnnotations.contains(annotation)) {
                    return true;
                }
            }
        }
        return false;
    }

}
