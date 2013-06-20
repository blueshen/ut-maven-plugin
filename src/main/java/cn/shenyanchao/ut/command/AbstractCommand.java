package cn.shenyanchao.ut.command;

import cn.shenyanchao.ut.builder.CompilationUnitBuilder;

/**
 * @author shenyanchao
 *         <p/>
 *         Date:  6/20/13
 *         Time:  7:16 PM
 */
public abstract class AbstractCommand {

    public abstract CompilationUnitBuilder sendCommand();
}
