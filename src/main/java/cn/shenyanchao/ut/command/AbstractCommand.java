package cn.shenyanchao.ut.command;

import cn.shenyanchao.ut.builder.CompilationUnitBuilder;

/**
 * Date:  6/20/13
 * Time:  7:16 PM
 *
 * @author shenyanchao
 */
public abstract class AbstractCommand {

    public abstract CompilationUnitBuilder sendCommand();
}
