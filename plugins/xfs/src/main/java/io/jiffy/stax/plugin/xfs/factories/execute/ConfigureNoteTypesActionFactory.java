package io.jiffy.stax.plugin.xfs.factories.execute;

import com.ibm.staf.service.stax.ActionFactoryExtensionPoint;
import io.jiffy.stax.plugin.xfs.actions.execute.ConfigureNoteTypesAction;
import io.jiffy.stax.plugin.xfs.factories.XfsExecuteCommandActionFactory;
import org.pf4j.Extension;

@Extension(points = {ActionFactoryExtensionPoint.class})
public class ConfigureNoteTypesActionFactory extends XfsExecuteCommandActionFactory<ConfigureNoteTypesAction> {

    public ConfigureNoteTypesActionFactory() {
        super("configure-note-types", ConfigureNoteTypesAction.class);
    }
}
