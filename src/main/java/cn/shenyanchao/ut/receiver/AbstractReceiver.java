package cn.shenyanchao.ut.receiver;

import cn.shenyanchao.ut.builder.CompilationUnitBuilder;
import japa.parser.ast.CompilationUnit;

/**
 * Date:  6/20/13
 * Time:  7:12 PM
 *
 * @author shenyanchao
 */
public abstract class AbstractReceiver {

    public abstract CompilationUnitBuilder createCUBuilder();
    public abstract CompilationUnit createCU();
}
