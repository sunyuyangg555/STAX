/*****************************************************************************/
/* Software Testing Automation Framework (STAF)                              */
/* (C) Copyright IBM Corp. 2002                                              */
/*                                                                           */
/* This software is licensed under the Eclipse Public License (EPL) V1.0.    */
/*****************************************************************************/

package com.ibm.staf.service.stax;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class STAXCallActionFactory implements STAXActionFactory {
    private static String fDTDInfo =
            "\n" +
                    "<!--================= The Call Element ============================= -->\n" +
                    "<!--\n" +
                    "     Perform a function with the referenced name.\n" +
                    "     The function attribute value is evaluated via Python.\n" +
                    "     Arguments can be specified as data to the call element.\n" +
                    "     Arguments are evaluated via Python.\n" +
                    "-->\n" +
                    "<!ELEMENT call       (#PCDATA)>\n" +
                    "<!ATTLIST call\n" +
                    "          function   CDATA    #REQUIRED\n" +
                    ">\n" +
                    "\n";

    public String getDTDInfo() {
        return fDTDInfo;
    }

    public String getDTDTaskName() {
        return "call";
    }

    public STAXAction parseAction(STAX staxService, STAXJob job,
                                  Node root) throws STAXException {
        String functionName = new String();
        STAXCallAction action = new STAXCallAction();

        action.setLineNumber(root);
        action.setXmlFile(job.getXmlFile());
        action.setXmlMachine(job.getXmlMachine());

        NamedNodeMap rootAttrs = root.getAttributes();

        for (int i = 0; i < rootAttrs.getLength(); ++i) {
            Node thisAttr = rootAttrs.item(i);

            action.setElementInfo(new STAXElementInfo(
                    root.getNodeName(), thisAttr.getNodeName()));

            if (thisAttr.getNodeName().equals("function")) {
                action.setFunction(STAXUtil.parseAndCompileForPython(
                        thisAttr.getNodeValue(), action));
            }
        }

        // Determine which call element is being processed

        action.setCallType(STAXCallAction.CALL_ONE_ARG);

        // Iterate its children nodes to get arguments passed in (if any)

        NodeList children = root.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node thisChild = children.item(i);

            switch (thisChild.getNodeType()) {
                case Node.COMMENT_NODE:
                    /* Do nothing */
                    break;
                case Node.TEXT_NODE:
                    action.setElementInfo(new STAXElementInfo(root.getNodeName()));

                    action.setArgs(STAXUtil.parseAndCompileForPython(thisChild.getNodeValue(), action));
                    break;
                default:
                    action.setElementInfo(new STAXElementInfo(
                            root.getNodeName(),
                            STAXElementInfo.NO_ATTRIBUTE_NAME,
                            STAXElementInfo.LAST_ELEMENT_INDEX,
                            "Contains invalid node type: " +
                                    Integer.toString(thisChild.getNodeType())));

                    throw new STAXInvalidXMLNodeTypeException(
                            STAXUtil.formatErrorMessage(action), action);
            }
        }

        return action;
    }
}
