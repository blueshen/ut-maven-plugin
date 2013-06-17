package cn.shenyanchao.ut;

import cn.shenyanchao.filter.DirAndJavaFilter;
import cn.shenyanchao.filter.JavaFileFilter;
import cn.shenyanchao.utils.JavaParserFactory;
import japa.parser.ast.*;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.AnnotationExpr;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author shenyanchao
 *         Date:  6/13/13
 *         Time:  5:13 PM
 */
@Mojo(name = "source2test", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class AstGenerator extends AbstractMojo {

    private static final String ENCODE = "UTF-8";

    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDir", required = false)
    private String sourceDir;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}", property = "testDir", required = false)
    private String testDir;

    @Override
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

    private void recruiseDir(File dir) {
        if (dir.isDirectory()) {
            File[] subFiles = dir.listFiles(new DirAndJavaFilter());
            for (File subFile : subFiles) {
                if (subFile.isDirectory()) {
                    recruiseDir(subFile);
                } else if (subFile.isFile()) {
                    getLog().info("start process filter:" + subFile.getAbsolutePath());
                    //todo
                    //process java filter
                    convertJavaFile2Test(subFile);
                }
            }

        }
    }

    private void makeDirIfNotExist(File dir) {

        if (!dir.isDirectory()) {
            getLog().error(dir.getAbsolutePath() + "is not a directory!");
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }


    private void convertJavaFile2Test(File javaFile) {

        japa.parser.ast.CompilationUnit compilationUnit = JavaParserFactory.getCompilationUnit(javaFile, ENCODE);

        //process package
        japa.parser.ast.PackageDeclaration packageDeclaration = compilationUnit.getPackage();
        String packageName = packageDeclaration.getName().toString();
        getLog().info("packageName:" + packageName);
        //process import
        List importList = compilationUnit.getImports();
        if (null != importList) {
            for (Object impt : importList) {
                japa.parser.ast.ImportDeclaration importDeclaration = (japa.parser.ast.ImportDeclaration) impt;
                getLog().info("import:" + importDeclaration.getName().toString());
            }
        }
        //process type
        List typeList = compilationUnit.getTypes();
        for (Object type : typeList) {
            japa.parser.ast.body.TypeDeclaration typeDeclaration = (japa.parser.ast.body.TypeDeclaration) type;
            getLog().info("className:" + typeDeclaration.getName().toString());
            //process methods
            List<japa.parser.ast.body.BodyDeclaration> members = typeDeclaration.getMembers();
            List<japa.parser.ast.body.MethodDeclaration> methodDeclarations = new ArrayList<japa.parser.ast.body.MethodDeclaration>();
            for (japa.parser.ast.body.BodyDeclaration body : members) {
                if (body instanceof japa.parser.ast.body.MethodDeclaration) {
                    methodDeclarations.add((japa.parser.ast.body.MethodDeclaration) body);
                }
            }
            for (japa.parser.ast.body.MethodDeclaration methodDeclaration : methodDeclarations) {
                int modifierMod = methodDeclaration.getModifiers();
                String modifierName = java.lang.reflect.Modifier.toString(modifierMod);
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
                List<japa.parser.ast.body.Parameter> parameters = methodDeclaration.getParameters();
                if (null != parameters) {
//                    System.out.println(parameters.toString());
                    for (japa.parser.ast.body.Parameter parameter : parameters) {
                        int iParamModifier = parameter.getModifiers();
                        getLog().info("paramModifier:" + java.lang.reflect.Modifier.toString(iParamModifier));
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

}
