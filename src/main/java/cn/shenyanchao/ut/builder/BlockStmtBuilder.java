package cn.shenyanchao.ut.builder;

import japa.parser.ASTHelper;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:  13-12-9
 * Time:  下午2:36
 *
 * @author shenyanchao
 */
public class BlockStmtBuilder {

    private BlockStmt blockStmt = new BlockStmt();

    public BlockStmtBuilder buildInitMockStmt() {
        List<Statement> stmts = new ArrayList<Statement>();
        if (null != blockStmt.getStmts()){
        stmts.addAll(blockStmt.getStmts());
        }
        NameExpr nameExpr = new NameExpr(MockitoAnnotations.class.getSimpleName());
        MethodCallExpr methodCallExpr = new MethodCallExpr(nameExpr, "initMocks");
        ASTHelper.addArgument(methodCallExpr, new ThisExpr());
        BlockStmt mockStmt = new BlockStmt();
        stmts.add(mockStmt);
        blockStmt.setStmts(stmts);
        return this;
    }

    public BlockStmtBuilder buildAssertStmt() {
        List<Statement> stmts = new ArrayList<Statement>();
        if (null != blockStmt.getStmts()){
            stmts.addAll(blockStmt.getStmts());
        }
        BlockStmt statement = new BlockStmt();
        NameExpr clazz = new NameExpr(Assert.class.getSimpleName());
        MethodCallExpr call = new MethodCallExpr(clazz, "assertTrue");
        ASTHelper.addArgument(call, (new BooleanLiteralExpr(false)));
        ASTHelper.addStmt(statement, call);
        stmts.add(statement);
        blockStmt.setStmts(stmts);
        return this;
    }

    public BlockStmt build() {
        return blockStmt;
    }

}
