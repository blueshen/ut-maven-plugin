package cn.shenyanchao.ut.utils;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Date:  13-7-10
 * Time:  下午2:51
 *
 * @author shenyanchao
 */
public class MethodUtils {

    private static final Logger LOG = LoggerFactory.getLogger(MethodUtils.class);

    public static ImportDeclaration findReferenceReturnTypeFrom(MethodDeclaration methodDeclaration,
                                                                List<ImportDeclaration> sourceImports) {
        Type returnType = methodDeclaration.getType();
        if (returnType instanceof ReferenceType) {
            String typeName = ((ReferenceType) returnType).getType().toString();
            LOG.info("###############typeName:{}###############", typeName);
            if (null != sourceImports) {
                for (ImportDeclaration impt : sourceImports) {
                    if (impt.getName().getName().endsWith(typeName)) {
                        return impt;
                    }
                }
            }
        }
        return null;
    }
}
