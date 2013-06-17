//package cn.shenyanchao.ast;
//
//import org.eclipse.jdt.core.dom.*;
//
///**
//* Created with IntelliJ IDEA.
//*
//* @author shenyanchao
//*         Date:  6/13/13
//*         Time:  11:27 AM
//*/
//public class AstHelloWorld {
//
//
//    public static void main(String[] args) {
//        AST ast = AST.newAST(AST.JLS3);
//        CompilationUnit compilationUnit = ast.newCompilationUnit();
//
//        // 创建类
//        TypeDeclaration programClass = ast.newTypeDeclaration();
//        programClass.setName(ast.newSimpleName("HelloWorld"));
//        programClass.modifiers().add(
//                ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
//        compilationUnit.types().add(programClass);
//
//        // 创建包
//        PackageDeclaration packageDeclaration = ast.newPackageDeclaration();
//        packageDeclaration.setName(ast.newName("cn.shenyanchao.hello"));
//        compilationUnit.setPackage(packageDeclaration);
//
//        MethodDeclaration main = ast.newMethodDeclaration();
//        main.setName(ast.newSimpleName("main"));
//        main.modifiers().add(
//                ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
//        main.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
//        main.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
//        programClass.bodyDeclarations().add(main);
//        Block mainBlock = ast.newBlock();
//        main.setBody(mainBlock);
//
//        // 给main方法定义String[]参数
//        SingleVariableDeclaration mainParameter = ast
//                .newSingleVariableDeclaration();
//        mainParameter.setName(ast.newSimpleName("arg"));
//        mainParameter.setType(ast.newArrayType(ast.newSimpleType(ast
//                .newName("String"))));
//        main.parameters().add(mainParameter);
//
//        MethodInvocation println = ast.newMethodInvocation();
//        println.setName(ast.newSimpleName("println"));
//
//        //生成String类型的常量
//        StringLiteral s = ast.newStringLiteral();
//        s.setLiteralValue("Hello World");
//        println.arguments().add(s);
//
//        println.setExpression(ast.newName("System.out"));
//
//        mainBlock.statements().add(ast.newExpressionStatement(println));
//
//        System.out.println(compilationUnit.toString());
//    }
//}
//
