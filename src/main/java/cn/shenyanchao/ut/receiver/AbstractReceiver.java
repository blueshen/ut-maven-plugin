package cn.shenyanchao.ut.receiver;

import cn.shenyanchao.ut.builder.CompilationUnitBuilder;

/**
 * @author shenyanchao
 *         <p/>
 *         Date:  6/20/13
 *         Time:  7:12 PM
 */
public abstract class AbstractReceiver {

    public abstract CompilationUnitBuilder createCU();
}
