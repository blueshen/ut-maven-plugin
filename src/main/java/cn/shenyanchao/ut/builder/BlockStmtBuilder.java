package cn.shenyanchao.ut.builder;

import japa.parser.ASTHelper;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
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

    public void buildAssertStmt() {
        NameExpr clazz = new NameExpr(Assert.class.getSimpleName());
        MethodCallExpr call = new MethodCallExpr(clazz, "assertTrue");
        ASTHelper.addArgument(call, (new BooleanLiteralExpr(false)));
        BlockStmt r = new BlockStmt();
        ASTHelper.addStmt(r, call);
        List<Statement> stmts = new ArrayList<Statement>();
        stmts.add(r);
        blockStmt.setStmts(stmts);
    }

    public BlockStmt build() {
        return blockStmt;
    }

}
