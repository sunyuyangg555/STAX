/*****************************************************************************/
/* Software Testing Automation Framework (STAF)                              */
/* (C) Copyright IBM Corp. 2002                                              */
/*                                                                           */
/* This software is licensed under the Eclipse Public License (EPL) V1.0.    */
/*****************************************************************************/

package com.ibm.staf.service.stax;

import com.ibm.staf.STAFMapClassDefinition;
import com.ibm.staf.STAFMarshallingContext;
import com.ibm.staf.STAFResult;
import com.ibm.staf.STAFUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 * A factory class for creating STAXSTAFCommandAction objects based on
 * information obtained by parsing an XML node from a DOM tree for a
 * &lt;stafcmd&gt; element.
 *
 * @see STAXSTAFCommandAction
 */
public class STAXSTAFCommandActionFactory implements STAXActionFactory, STAXListRequestHandler, STAXQueryRequestHandler, STAXJobManagementHandler {
    static final String STAX_STAFCOMMAND_EVENT = "STAFCommand";

    static final String sStafcmdInfoMapClassName = "STAF/Service/STAX/StafcmdInfo";
    static final String sQueryStafcmdMapClassName = "STAF/Service/STAX/QueryStafcmd";

    /**
     * Creates a new STAXSTAFCommandActionFactory instance and registers
     * with the STAX service to handle requests to LIST the active STAF
     * commands in a job, to handle requests to QUERY a particular STAF
     * command that running, and to manage a map of currently active
     * STAF commands for a job.
     */
    public STAXSTAFCommandActionFactory(STAX staxService) {
        staxService.registerListHandler("STAFCMDS", this);
        staxService.registerQueryHandler("STAFCMD", "Request#", this);
        staxService.registerJobManagementHandler(this);

        // Construct map-class for list stafcmd information

        fStafcmdInfoMapClass = new STAFMapClassDefinition(sStafcmdInfoMapClassName);

        fStafcmdInfoMapClass.addKey("stafcmdName", "Stafcmd Name");
        fStafcmdInfoMapClass.addKey("location", "Location");
        fStafcmdInfoMapClass.addKey("requestNum", "Request#");
        fStafcmdInfoMapClass.addKey("service", "Service");
        fStafcmdInfoMapClass.addKey("request", "Request");

        // Construct map-class for query stafcmd information

        fQueryStafcmdMapClass = new STAFMapClassDefinition(sQueryStafcmdMapClassName);

        fQueryStafcmdMapClass.addKey("stafcmdName", "Stafcmd Name");
        fQueryStafcmdMapClass.addKey("location", "Location");
        fQueryStafcmdMapClass.addKey("requestNum", "Request#");
        fQueryStafcmdMapClass.addKey("service", "Service");
        fQueryStafcmdMapClass.addKey("request", "Request");
        fQueryStafcmdMapClass.addKey("blockName", "Block Name");
        fQueryStafcmdMapClass.addKey("threadID", "Thread ID");
        fQueryStafcmdMapClass.addKey("startTimestamp", "Start Date-Time");
    }

    public String getDTDInfo() {
        return fDTDInfo;
    }

    public String getDTDTaskName() {
        return "stafcmd";
    }

    /**
     *
     * @param  staxService an object representing the STAX Service
     * @param  job         an object representing the STAX job
     * @param  root        the XML node from a DOM tree representing an action
     *                     element
     * @return
     * @throws STAXException
     */
    public STAXAction parseAction(STAX staxService, STAXJob job, Node root) throws STAXException {
        STAXSTAFCommandAction action = new STAXSTAFCommandAction();

        action.setActionFactory(this);

        action.setLineNumber(root);
        action.setXmlFile(job.getXmlFile());
        action.setXmlMachine(job.getXmlMachine());

        NamedNodeMap attrs = root.getAttributes();

        for (int i = 0; i < attrs.getLength(); ++i) {
            Node thisAttr = attrs.item(i);

            String attrName = thisAttr.getNodeName();
            action.setElementInfo(new STAXElementInfo(root.getNodeName(), attrName));

            if (thisAttr.getNodeName().equals("name")) {
                action.setName(STAXUtil.parseAndCompileForPython(thisAttr.getNodeValue(), action));
            }
        }

        NodeList children = root.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node thisChild = children.item(i);

            switch (thisChild.getNodeType()) {
                case Node.COMMENT_NODE:
                    /* Do nothing */
                    break;
                case Node.ELEMENT_NODE:
                    action.setLineNumber(thisChild);

                    switch (thisChild.getNodeName()) {
                        case "location":
                            action.setLocation(handleChild(thisChild, action));
                            break;
                        case "service":
                            action.setService(handleChild(thisChild, action));
                            break;
                        case "request":
                            action.setRequest(handleChild(thisChild, action));
                            break;
                    }
                    break;
            }
        }

        return action;
    }

    /**
     * @param root   location | service | request
     * @param action
     * @return
     * @throws STAXException
     */
    private String handleChild(Node root, STAXSTAFCommandAction action) throws STAXException {
        NodeList children = root.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node thisChild = children.item(i);

            // XXX: Should I be able to have a COMMENT_NODE here?

            switch (thisChild.getNodeType()) {
                case Node.COMMENT_NODE:
                    /* Do nothing */
                    break;
                case Node.TEXT_NODE:
                    action.setElementInfo(new STAXElementInfo(root.getNodeName()));
                    return STAXUtil.parseAndCompileForPython(thisChild.getNodeValue(), action);
                case Node.CDATA_SECTION_NODE:
                    /* Do nothing */
                    break;
                default:
                    action.setElementInfo(new STAXElementInfo(root.getNodeName(), STAXElementInfo.NO_ATTRIBUTE_NAME, STAXElementInfo.LAST_ELEMENT_INDEX, "Contains invalid node type: " + Integer.toString(thisChild.getNodeType())));

                    throw new STAXInvalidXMLNodeTypeException(STAXUtil.formatErrorMessage(action), action);
            }
        }

        return "";
    }


    // STAXJobManagement methods

    public void initJob(STAXJob job) {
        boolean result = job.setData("stafcmdRequestMap", new TreeMap<String, STAXSTAFCommandActionFactory>());

        if (!result) {
            String msg = "STAXSTAFCommandActionFactory.initJob: setData for " + "stafcmdRequestMap failed.";
            job.log(STAXJob.JOB_LOG, "error", msg);
        }
    }

    public void terminateJob(STAXJob job) { /* Do Nothing */ }


    // STAXListRequestHandler method

    public STAFResult handleListRequest(String type, STAXJob job, STAXRequestSettings settings) {
        if (type.equalsIgnoreCase("stafcmds")) {
            // LIST JOB <Job ID> STAFCMDS

            // Create the marshalling context and set its map class definitions
            // and create an empty list to contain the block map entries

            STAFMarshallingContext mc = new STAFMarshallingContext();
            mc.setMapClassDefinition(fStafcmdInfoMapClass);
            List<Map<String, Object>> stafcmdOutputList = new ArrayList<Map<String, Object>>();

            // Iterate through the stafcmd map, generating the output list

            TreeMap stafcmds = (TreeMap) job.getData("stafcmdRequestMap");

            if (stafcmds != null) {
                synchronized (stafcmds) {
                    Iterator iter = stafcmds.values().iterator();

                    while (iter.hasNext()) {
                        STAXSTAFCommandAction command = (STAXSTAFCommandAction) iter.next();

                        Map<String, Object> stafcmdMap = new TreeMap<String, Object>();
                        stafcmdMap.put("staf-map-class-name", fStafcmdInfoMapClass.name());
                        stafcmdMap.put("stafcmdName", command.getName());
                        stafcmdMap.put("location", command.getLocation());
                        stafcmdMap.put("requestNum", "" + command.getRequestNumber());
                        stafcmdMap.put("service", command.getService());
                        stafcmdMap.put("request", STAFUtil.maskPrivateData(command.getRequest()));

                        stafcmdOutputList.add(stafcmdMap);
                    }
                }
            }

            mc.setRootObject(stafcmdOutputList);

            return new STAFResult(STAFResult.Ok, mc.marshall());
        } else
            return new STAFResult(STAFResult.DoesNotExist, type);
    }

    // STAXQueryRequestHandler methods

    public STAFResult handleQueryRequest(String type, String key, STAXJob job, STAXRequestSettings settings) {
        if (type.equalsIgnoreCase("stafcmd")) {
            // QUERY JOB <Job ID> STAFCMD <Request#>

            // Create the marshalling context and set its map class definition

            STAFMarshallingContext mc = new STAFMarshallingContext();
            mc.setMapClassDefinition(fQueryStafcmdMapClass);

            TreeMap stafcmds = (TreeMap) job.getData("stafcmdRequestMap");

            if (stafcmds == null)
                return new STAFResult(STAFResult.DoesNotExist, key);

            STAXSTAFCommandAction command = null;

            synchronized (stafcmds) {
                command = (STAXSTAFCommandAction) stafcmds.get(key);
            }

            if (command == null)
                return new STAFResult(STAFResult.DoesNotExist, key);

            Map<String, Object> stafcmdMap = new TreeMap<String, Object>();
            stafcmdMap.put("staf-map-class-name", fQueryStafcmdMapClass.name());
            stafcmdMap.put("stafcmdName", command.getName());
            stafcmdMap.put("location", command.getLocation());
            stafcmdMap.put("requestNum", "" + command.getRequestNumber());
            stafcmdMap.put("service", command.getService());
            stafcmdMap.put("request", STAFUtil.maskPrivateData(command.getRequest()));
            stafcmdMap.put("blockName", command.getCurrentBlockName());
            stafcmdMap.put("threadID", "" + command.getThread().getThreadNumber());
            stafcmdMap.put("startTimestamp", command.getStartTimestamp().getTimestampString());

            mc.setRootObject(stafcmdMap);

            return new STAFResult(STAFResult.Ok, mc.marshall());
        } else
            return new STAFResult(STAFResult.DoesNotExist, type);
    }

    public STAFResult handleQueryJobRequest(STAXJob job, STAXRequestSettings settings) {
        return new STAFResult(STAFResult.Ok, "");
    }

    private STAFMapClassDefinition fStafcmdInfoMapClass;
    private STAFMapClassDefinition fQueryStafcmdMapClass;

    private static String fDTDInfo =
            "\n" +
                    "<!--================= The STAF Command Element ===================== -->\n" +
                    "<!--\n" +
                    "     Specifies a STAF command to be executed.\n" +
                    "     Its name and all of its element values are evaluated via Python.\n" +
                    "-->\n" +
                    "<!ELEMENT stafcmd    (location, service, request)>\n" +
                    "<!ATTLIST stafcmd\n" +
                    "          name       CDATA   #IMPLIED\n" +
                    ">\n" +
                    "<!ELEMENT service    (#PCDATA)>\n" +
                    "<!ELEMENT request    (#PCDATA)>\n";
}
