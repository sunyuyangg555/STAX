package io.jiffy.stax.plugin.xfs.actions.execute;

import com.ibm.staf.service.stax.STAXSTAFCommandAction;
import io.jiffy.stax.plugin.xfs.actions.XfsExecuteCommandMapParamAction;

public class DispenseCardAction extends XfsExecuteCommandMapParamAction {


    @Override
    public STAXSTAFCommandAction createClone() {
        DispenseCardAction clone = new DispenseCardAction();
        clone.setParameter(getParameter());
        clone.setEvents(getEvents());
        return clone;
    }


    @Override
    public String createCommand() {
        return "dispenseCard dispense";
    }

}
