/*****************************************************************************/
/* Software Testing Automation Framework (STAF)                              */
/* (C) Copyright IBM Corp. 2002                                              */
/*                                                                           */
/* This software is licensed under the Eclipse Public License (EPL) V1.0.    */
/*****************************************************************************/

package com.ibm.staf.service.stax;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class STAXScriptActionFactory implements STAXActionFactory {
    private static String fDTDInfo =
            "\n" +
                    "<!--================= The Script Element =========================== -->\n" +
                    "<!--\n" +
                    "     Specifies Python code to be executed.\n" +
                    "-->\n" +
                    "<!ELEMENT script     (#PCDATA)>\n";

    public String getDTDInfo() {
        return fDTDInfo;
    }

    public String getDTDTaskName() {
        return "script";
    }

    public STAXAction parseAction(STAX staxService, STAXJob job,
                                  Node root) throws STAXException {
        STAXScriptAction action = new STAXScriptAction();

        action.setLineNumber(root);
        action.setXmlFile(job.getXmlFile());
        action.setXmlMachine(job.getXmlMachine());

        NodeList children = root.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node thisChild = children.item(i);

            switch (thisChild.getNodeType()) {
                case Node.COMMENT_NODE:
                    /* Do nothing */
                    break;
                case Node.TEXT_NODE:
                    action.setElementInfo(new STAXElementInfo(root.getNodeName()));

                    action.setValue(STAXUtil.parseAndCompileForPython(thisChild.getNodeValue(), action));
                    break;
                default:
                    action.setElementInfo(new STAXElementInfo(root.getNodeName(), STAXElementInfo.NO_ATTRIBUTE_NAME, "Contains invalid node type: " + Integer.toString(thisChild.getNodeType())));

                    throw new STAXInvalidXMLNodeTypeException(STAXUtil.formatErrorMessage(action), action);
            }
        }

        return action;
    }
}
