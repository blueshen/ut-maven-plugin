package cn.shenyanchao.ut.factory;

import japa.parser.ASTHelper;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.stmt.BlockStmt;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;

/**
 * Date:  13-12-31
 * Time:  上午11:56
 *
 * @author shenyanchao
 */
public class BlockStmtFactory {
    public static BlockStmt createInitMockStmt() {
        BlockStmt blockStmt = new BlockStmt();
        NameExpr nameExpr = new NameExpr(MockitoAnnotations.class.getSimpleName());
        MethodCallExpr methodCallExpr = new MethodCallExpr(nameExpr, "initMocks");
        ASTHelper.addArgument(methodCallExpr, new ThisExpr());
        ASTHelper.addStmt(blockStmt, methodCallExpr);
        return blockStmt;
    }

    public static BlockStmt createAssertStmt() {
        BlockStmt blockStmt = new BlockStmt();
        NameExpr clazz = new NameExpr(Assert.class.getSimpleName());
        MethodCallExpr call = new MethodCallExpr(clazz, "assertTrue");
        ASTHelper.addArgument(call, (new BooleanLiteralExpr(false)));
        ASTHelper.addStmt(blockStmt, call);

        return blockStmt;
    }
}
