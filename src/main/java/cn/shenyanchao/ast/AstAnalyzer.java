//package cn.shenyanchao.ast;
//
//import org.apache.commons.io.FileUtils;
//import org.eclipse.jdt.core.dom.*;
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
///**
//* Created with IntelliJ IDEA.
//*
//* @author shenyanchao
//*         Date:  6/13/13
//*         Time:  11:52 AM
//*/
//public class AstAnalyzer {
//    public static void main(String[] args) throws IOException {
//
//        String javaSource = FileUtils.readFileToString(new File("/home/shenyanchao/IdeaProjects/ast/src/main/java/cn/shenyanchao/from/ShenYanChaoAST.java"));
//
//        ASTParser parser = ASTParser.newParser(AST.JLS3);
//        parser.setSource(javaSource.toCharArray());
//
//        // 使用解析器进行解析并返回AST上下文结果(CompilationUnit为根节点)
//        CompilationUnit result = (CompilationUnit) parser.createAST(null);
//
//        result.imports();
//        result.getPackage();
//        result.getCommentList();
//        System.out.println(result.getCommentList().toString());
//
//        TypeDeclaration type = (TypeDeclaration) result.types().get(0);
//        System.out.println("---------Type---------");
//        System.out.println(type.toString());
//
//        MethodDeclaration method = type.getMethods()[0];
//        method.parameters();
//        method.isConstructor();
//
//        System.out.println("---------Method---------");
//        System.out.println(method.toString());
//        method.getName();
//        method.getModifiers();
//        Type returnType = method.getReturnType2();
//        System.out.println("returnType = " + returnType.toString());
//
//
//        Block methodBody = method.getBody();
//        List<Statement> statementList = methodBody.statements();
//
//        System.out.println(statementList.toString());
//
//        statementList.get(0);
//
////        ExpressionStatement ifs = (ExpressionStatement) method.getBody().statements().get(1);
////        Assignment expression=(Assignment)ifs.getExpression();
////        Expression exp=expression.getRightHandSide();
////
////        System.out.println(result.toString());
//    }
//}
