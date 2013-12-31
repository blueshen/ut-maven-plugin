package cn.shenyanchao.ut.command;

import cn.shenyanchao.ut.receiver.AbstractReceiver;
import japa.parser.ast.CompilationUnit;

/**
 * Date:  6/20/13
 * Time:  7:19 PM
 *
 * @author shenyanchao
 */
public class ExistTestCommand extends AbstractCommand {

    private AbstractReceiver receiver;

    public ExistTestCommand(AbstractReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public CompilationUnit sendCommand() {
        return receiver.createCU();
    }
}
