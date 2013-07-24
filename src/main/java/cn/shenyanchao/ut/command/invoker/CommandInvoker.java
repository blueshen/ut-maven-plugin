package cn.shenyanchao.ut.command.invoker;

import cn.shenyanchao.ut.builder.CompilationUnitBuilder;
import cn.shenyanchao.ut.command.AbstractCommand;

/**
 * Date:  6/20/13
 * Time:  7:25 PM
 *
 * @author shenyanchao
 */
public class CommandInvoker {

    private AbstractCommand command;

    public CommandInvoker(AbstractCommand command) {
        this.command = command;
    }

    public CompilationUnitBuilder action() {
        return command.sendCommand();
    }
}
