package cn.shenyanchao.ut.builder;

import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;

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

    public void buildAssert() {
        AssertStmt assertStmt = new AssertStmt();
        Expression expression = new StringLiteralExpr("Assert.assertTrue(false)");
        assertStmt.setCheck(expression);
        List<Statement> stmts = new ArrayList<Statement>();
        stmts.add(assertStmt);
        blockStmt.setStmts(stmts);
    }

    public BlockStmt build() {
        return blockStmt;
    }

}
