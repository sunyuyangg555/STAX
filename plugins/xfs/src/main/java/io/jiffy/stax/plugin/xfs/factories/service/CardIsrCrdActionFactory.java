package io.jiffy.stax.plugin.xfs.factories.service;

import com.ibm.staf.service.stax.ActionFactoryExtensionPoint;
import io.jiffy.stax.plugin.xfs.actions.service.CardIsrCrdAction;
import io.jiffy.stax.plugin.xfs.factories.XfsServiceActionFactory;
import org.pf4j.Extension;

@Extension(points = {ActionFactoryExtensionPoint.class})
public class CardIsrCrdActionFactory extends XfsServiceActionFactory<CardIsrCrdAction> {

    public static final String XFS_PTR_EVENT = "CardIsrCrd";

    public CardIsrCrdActionFactory() {
        super("card-isr-crd", CardIsrCrdAction.class);
    }

}
