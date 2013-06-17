//package cn.shenyanchao.ut;
//
//import cn.shenyanchao.filter.DirAndJavaFilter;
//import org.apache.commons.io.FileUtils;
//import org.apache.maven.plugin.MojoExecutionException;
//import org.apache.maven.plugin.MojoFailureException;
//import org.eclipse.jdt.core.dom.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @author shenyanchao
// *         Date:  6/14/13
// *         Time:  5:21 PM
// */
//public class FileProcess {
//    private static final String ENCODE = "UTF-8";
//
//    private Logger getLog() {
//        return LoggerFactory.getLogger(FileProcess.class.getName());
//    }
//
//    private String sourceDir = "/home/shenyanchao/IdeaProjects/ut-maven-plugin/src/it/simple-it/src/main/java";
//
//
//    private String testDir = "/home/shenyanchao/IdeaProjects/ut-maven-plugin/src/it/simple-it/src/test/java";
//
//
//    public void execute() throws MojoExecutionException, MojoFailureException {
//
//        Logger log = getLog();
//        log.info("############################################################");
//        log.info(sourceDir);
//        log.info(testDir);
//        log.info("############################################################");
//
//        File sourceDirectory = new File(sourceDir);
//        File testDirectory = new File(testDir);
//        makeDirIfNotExist(sourceDirectory);
//        makeDirIfNotExist(testDirectory);
//
//        recruiseDir(sourceDirectory);
//
//
//    }
//
//    private void recruiseDir(File dir) {
//        if (dir.isDirectory()) {
//            File[] subFiles = dir.listFiles(new DirAndJavaFilter());
//            for (File subFile : subFiles) {
//                if (subFile.isDirectory()) {
//                    recruiseDir(subFile);
//                } else if (subFile.isFile()) {
//                    getLog().info("start process filter:" + subFile.getAbsolutePath());
//                    //todo
//                    //process java filter
//                    convertJavaFile2Test(subFile);
//                }
//            }
//
//        }
//    }
//
//    private void makeDirIfNotExist(File dir) {
//
//        if (!dir.isDirectory()) {
//            getLog().error(dir.getAbsolutePath() + "is not a directory!");
//        }
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//    }
//
//
//    private void convertJavaFile2Test(File javaFile) {
//        ASTParser astParser = ASTParser.newParser(AST.JLS3);
//        try {
//            astParser.setSource(FileUtils.readFileToString(javaFile, Charset.forName(ENCODE)).toCharArray());
//        } catch (IOException e) {
//            getLog().error("read filter: " + javaFile.getAbsolutePath() + " wrong!");
//        }
//        CompilationUnit compilationUnit = (CompilationUnit) astParser.createAST(null);
//        //process package
//        PackageDeclaration packageDeclaration = compilationUnit.getPackage();
//        String packageName = packageDeclaration.getName().toString();
//        getLog().info("packageName:" + packageName);
//        //process import
//        List importList = compilationUnit.imports();
//        for (Object impt : importList) {
//            ImportDeclaration importDeclaration = (ImportDeclaration) impt;
//            getLog().info("import:" + importDeclaration.getName().toString());
//        }
//        //process type
//        List typeList = compilationUnit.types();
//        for (Object type : typeList) {
//            TypeDeclaration typeDeclaration = (TypeDeclaration) type;
//            getLog().info("className:" + typeDeclaration.getName().toString());
//            //process methods
//            MethodDeclaration[] methodDeclarations = typeDeclaration.getMethods();
//            for (MethodDeclaration methodDeclaration : methodDeclarations) {
//                if (methodDeclaration.isConstructor()) {
//                    continue; //跳过constructor
//                }
//                String modifierName = "";
//                List modifiers = methodDeclaration.modifiers();
//                for (Object mdf : modifiers) {
//                    IExtendedModifier imodifier = (IExtendedModifier)mdf;
//
//                    if (imodifier.isModifier()){
//                        modifierName += imodifier.toString()+" ";
//                    }else {
//                        getLog().info("*****"+imodifier.toString());
//                    }
//                }
//                String returnType = methodDeclaration.getReturnType2().toString();
////                getLog().info("returnType:" + returnType);
//                String methodName = methodDeclaration.getName().toString();
////                getLog().info("methodName:" + methodName);
//                List parameters = methodDeclaration.parameters();
//                for (Object para : parameters) {
//                    SingleVariableDeclaration variableDeclaration = (SingleVariableDeclaration) para;
//                    String paramModifier = variableDeclaration.modifiers().toString();
//                    String paramType = variableDeclaration.getType().toString();
//                    String varParm = variableDeclaration.getName().toString();
//                    getLog().info("paramModifier:"+paramModifier+" paramType:"+paramType+" paramVar:" + varParm);
//                }
//                getLog().info(modifierName + " " + returnType + " " + methodName + "()");
//
//            }
//        }
//
//    }
//
//    public static void main(String[] args) throws MojoFailureException, MojoExecutionException {
//        new FileProcess().execute();
//    }
//}
