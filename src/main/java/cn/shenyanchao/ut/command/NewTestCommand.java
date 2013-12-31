package cn.shenyanchao.ut.command;

import cn.shenyanchao.ut.receiver.AbstractReceiver;
import japa.parser.ast.CompilationUnit;

/**
 * Date:  6/20/13
 * Time:  7:17 PM
 *
 * @author shenyanchao
 */
public class NewTestCommand extends AbstractCommand {

    private AbstractReceiver receiver;

    public NewTestCommand(AbstractReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public CompilationUnit sendCommand() {
        return receiver.createCU();
    }
}
