package cn.shenyanchao.ut;

/**
 * Date:  13-7-10
 * Time:  下午1:06
 *
 * @author shenyanchao
 */

import cn.shenyanchao.ut.builder.CompilationUnitBuilder;
import cn.shenyanchao.ut.command.ExistTestCommand;
import cn.shenyanchao.ut.command.NewTestCommand;
import cn.shenyanchao.ut.command.invoker.CommandInvoker;
import cn.shenyanchao.ut.common.Consts;
import cn.shenyanchao.ut.filter.JavaFileFilter;
import cn.shenyanchao.ut.generator.TestWriter;
import cn.shenyanchao.ut.receiver.ExistTestReceiver;
import cn.shenyanchao.ut.receiver.NewTestReceiver;
import cn.shenyanchao.ut.utils.ClassTools;
import cn.shenyanchao.ut.utils.FileChecker;
import cn.shenyanchao.ut.utils.JavaParserFactory;
import cn.shenyanchao.ut.utils.JavaParserUtils;
import japa.parser.ast.CompilationUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;

/**
 * @author shenyanchao
 *         Date:  6/13/13
 *         Time:  5:13 PM
 */

public class DebugMain {

    private String fFlag;

    private String sourceEncode = Consts.DEFAULT_ENCODE;


    private String sourceDir = "/home/shenyanchao/IdeaProjects/ut-maven-plugin/src/it/spring-petclinic/src/main/java";

    private String testDir = "/home/shenyanchao/IdeaProjects/ut-maven-plugin/src/it/spring-petclinic/src/test/java";

    private Logger getLog() {
        return LoggerFactory.getLogger(DebugMain.class);
    }

    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info(sourceDir);
        getLog().info(testDir);

        File sourceDirectory = new File(sourceDir);
        makeDirIfNotExist(sourceDirectory);
        File testDirectory = new File(testDir);
        makeDirIfNotExist(testDirectory);

        Iterator<File> fileItr = FileUtils.iterateFiles(sourceDirectory, new JavaFileFilter(), TrueFileFilter.INSTANCE);
        while (fileItr.hasNext()) {
            File javaFile = fileItr.next();
            getLog().info("start process file:" + javaFile.getAbsolutePath());
            //process java file
            if (ClassTools.isNeedTest(javaFile, sourceEncode)) {
                convertJavaFile2Test(javaFile);
            }
        }
    }

    /**
     * if dir not exist,create it
     *
     * @param dir
     */
    private void makeDirIfNotExist(File dir) {
        if (!dir.isDirectory()) {
            getLog().error(dir.getAbsolutePath() + "is not a directory!");
        }
        if (!dir.exists()) {
            boolean success = false;
            while (!success) {
                success = dir.mkdirs();
            }
        }
    }


    /**
     * @param javaFile
     */
    private void convertJavaFile2Test(File javaFile) {

        CompilationUnit sourceCU = JavaParserFactory.getCompilationUnit(javaFile, sourceEncode);

        CompilationUnitBuilder compilationUnitBuilder = null;
        String testJavaFileName = JavaParserUtils.findTestJavaFileName(sourceCU, javaFile, testDir);
        boolean testExist = FileChecker.isTestJavaClassExist(new File(testJavaFileName));
        if (!testExist) {
            CommandInvoker invoker = new CommandInvoker(new NewTestCommand(new NewTestReceiver(sourceCU, javaFile)));
            compilationUnitBuilder = invoker.action();
        } else if (testExist) {
            CompilationUnit testCU = JavaParserFactory.getCompilationUnit(new File(testJavaFileName), sourceEncode);
            CommandInvoker invoker = new CommandInvoker(new ExistTestCommand(new ExistTestReceiver(sourceCU, javaFile,
                    testCU, new File(testJavaFileName))));
            compilationUnitBuilder = invoker.action();
        }

        if (null != compilationUnitBuilder) {
            CompilationUnit testCU = compilationUnitBuilder.build();
            //写入测试代码文件
            TestWriter.writeJavaTest(testJavaFileName, testCU.toString(), sourceEncode);
        }
    }

    public static void main(String[] args) {
        try {
            new DebugMain().execute();
        } catch (MojoExecutionException mojoException) {
            //ignore
        } catch (MojoFailureException mojoFailException) {
            //ignore
        }
    }
}

