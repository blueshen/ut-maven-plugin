package cn.shenyanchao.utils;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author shenyanchao
 *         Date:  6/15/13
 *         Time:  3:22 PM
 */
public class JavaParserFactory {

    public static CompilationUnit getCompilationUnit(File javaFile, String encoding) {

        CompilationUnit compilationUnit = null;
        try {
            compilationUnit = JavaParser.parse(javaFile, encoding);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compilationUnit;
    }
}
