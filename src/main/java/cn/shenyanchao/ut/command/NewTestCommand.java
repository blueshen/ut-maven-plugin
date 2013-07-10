package cn.shenyanchao.ut.command;

import cn.shenyanchao.ut.builder.CompilationUnitBuilder;
import cn.shenyanchao.ut.receiver.AbstractReceiver;

/**
 * @author shenyanchao
 *         Date:  6/20/13
 *         Time:  7:17 PM
 */
public class NewTestCommand extends AbstractCommand {

    private AbstractReceiver receiver;

    public NewTestCommand(AbstractReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public CompilationUnitBuilder sendCommand() {
        return receiver.createCU();
    }
}
