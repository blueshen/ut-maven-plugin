package cn.shenyanchao.ut.utils;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author shenyanchao
 *         Date:  6/15/13
 *         Time:  3:22 PM
 */
public class JavaParserFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JavaParserFactory.class);

    public static CompilationUnit getCompilationUnit(File javaFile, String encoding) {

        CompilationUnit compilationUnit = null;
        try {
            compilationUnit = JavaParser.parse(javaFile, encoding);
        } catch (ParseException e) {
            LOG.error(e.getMessage());
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return compilationUnit;
    }
}
