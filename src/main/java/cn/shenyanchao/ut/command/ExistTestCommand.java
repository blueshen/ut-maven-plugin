package cn.shenyanchao.ut.command;

import cn.shenyanchao.ut.builder.CompilationUnitBuilder;
import cn.shenyanchao.ut.receiver.AbstractReceiver;

/**
 * @author shenyanchao
 *         <p/>
 *         Date:  6/20/13
 *         Time:  7:19 PM
 */
public class ExistTestCommand extends AbstractCommand {

    private AbstractReceiver receiver;

    public ExistTestCommand(AbstractReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public CompilationUnitBuilder sendCommand() {
        return receiver.createCU();
    }
}
