package cn.shenyanchao.ut.command.invoker;

import cn.shenyanchao.ut.command.AbstractCommand;
import japa.parser.ast.CompilationUnit;

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

    public CompilationUnit action() {
        return command.sendCommand();
    }
}
