package cn.shenyanchao.ut;

import cn.shenyanchao.filter.DirAndJavaFilter;
import cn.shenyanchao.filter.JavaFileFilter;
import cn.shenyanchao.utils.JavaParserFactory;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.AnnotationExpr;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author shenyanchao
 *         Date:  6/14/13
 *         Time:  5:21 PM
 */
public class JavaParserFileProcess {
    private static final String ENCODE = "UTF-8";

    private Logger getLog() {
        return LoggerFactory.getLogger(JavaParserFileProcess.class.getName());
    }

    private String sourceDir = "/home/shenyanchao/IdeaProjects/ut-maven-plugin/src/it/simple-it/src/main/java";


    private String testDir = "/home/shenyanchao/IdeaProjects/ut-maven-plugin/src/it/simple-it/src/test/java";


    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info("############################################################");
        getLog().info(sourceDir);
        getLog().info(testDir);
        getLog().info("############################################################");

        File sourceDirectory = new File(sourceDir);
        File testDirectory = new File(testDir);
        makeDirIfNotExist(sourceDirectory);
        makeDirIfNotExist(testDirectory);

        Iterator<File> fileItr = FileUtils.iterateFiles(new File(sourceDir), new JavaFileFilter(), TrueFileFilter.INSTANCE);
        while (fileItr.hasNext()) {
            File javaFile = fileItr.next();
            getLog().info("start process filter:" + javaFile.getAbsolutePath());
            //todo
            //process java filter
            convertJavaFile2Test(javaFile);
        }


    }

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

    private void makeDirIfNotExist(File dir) {

        if (!dir.isDirectory()) {
            getLog().error(dir.getAbsolutePath() + "is not a directory!");
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }


    private void convertJavaFile2Test(File javaFile) {

        CompilationUnit compilationUnit = JavaParserFactory.getCompilationUnit(javaFile, ENCODE);

        //process package
        PackageDeclaration packageDeclaration = compilationUnit.getPackage();
        String packageName = packageDeclaration.getName().toString();
        getLog().info("packageName:" + packageName);
        //process import
        List importList = compilationUnit.getImports();
        if (null != importList) {
            for (Object impt : importList) {
                ImportDeclaration importDeclaration = (ImportDeclaration) impt;
                getLog().info("import:" + importDeclaration.getName().toString());
            }
        }
        //process type
        List typeList = compilationUnit.getTypes();
        for (Object type : typeList) {
            TypeDeclaration typeDeclaration = (TypeDeclaration) type;
            getLog().info("className:" + typeDeclaration.getName().toString());
            //process methods
            List<BodyDeclaration> members = typeDeclaration.getMembers();
            List<MethodDeclaration> methodDeclarations = new ArrayList<MethodDeclaration>();
            for (BodyDeclaration body : members) {
                if (body instanceof MethodDeclaration) {
                    methodDeclarations.add((MethodDeclaration) body);
                }
            }
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                int modifierMod = methodDeclaration.getModifiers();
                String modifierName = Modifier.toString(modifierMod);
                getLog().info("modifierName:" + modifierName);
                getLog().info("methodName:" + methodDeclaration.getName());
//                List<TypeParameter> typeParameters = methodDeclaration.getTypeParameters();
//                getLog().info("typeParameter"+typeParameters.toString());
//                BlockStmt bodyBlock = methodDeclaration.getBody();
//                getLog().info("bodyBLock:"+bodyBlock.getStmts().toString());
                String returnType = methodDeclaration.getType().toString();
                getLog().info("returnType:" + returnType);
                List<AnnotationExpr> annotationExprs = methodDeclaration.getAnnotations();
                if (null != annotationExprs)
                    getLog().info("annotations:" + annotationExprs.toString());
                List<Parameter> parameters = methodDeclaration.getParameters();
                if (null != parameters) {
//                    System.out.println(parameters.toString());
                    for (Parameter parameter : parameters) {
                        int iParamModifier = parameter.getModifiers();
                        getLog().info("paramModifier:" + Modifier.toString(iParamModifier));
                        String paramType = parameter.getType().toString();
                        getLog().info("paramType:" + paramType);
                        VariableDeclaratorId varId = parameter.getId();
                        String varName = varId.getName();
                        getLog().info("varName:" + varName);
                    }
                }
            }
        }


    }

    public static void main(String[] args) throws MojoFailureException, MojoExecutionException {
        new JavaParserFileProcess().execute();
    }
}

