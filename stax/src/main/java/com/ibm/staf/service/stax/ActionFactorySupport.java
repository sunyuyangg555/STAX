package com.ibm.staf.service.stax;

import org.pmw.tinylog.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class ActionFactorySupport<T extends STAXActionDefaultImpl> extends ActionFactoryExtensionPointAdapter {

    private Class<T> clazz;

    public ActionFactorySupport(String name, Class<T> clazz) {
        super(name);
        this.clazz = clazz;
    }

    @Override
    public STAXAction parseAction(STAX staxService, STAXJob job, Node root) throws STAXException {
        T staxAction = createAction();

        staxAction.setLineNumber(root);
        staxAction.setXmlFile(job.getXmlFile());
        staxAction.setXmlMachine(job.getXmlMachine());

        handleRootAttributes(staxService, job, staxAction, root, root.getAttributes());
        NodeList children = root.getChildNodes();

        // TODO text元素也是node, 所以tag下面有一个text就会有child,
        //  这也是为什么<open>XXX</open>这类标记在handleChildNode中也会处理的原因, XXX将会在 Node.ELEMENT_NODE分支中处理

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);

            switch (child.getNodeType()) {
                case Node.COMMENT_NODE:
                    handleCommentNode(staxService, job, staxAction, root, child);
                    break;
                case Node.ELEMENT_NODE:
                    handleChildNode(staxService, job, staxAction, root, child);
                    break;
                case Node.TEXT_NODE:
                    handleTextNode(staxService, job, staxAction, root, child);
                    break;
                default:
                    staxAction.setElementInfo(
                            new STAXElementInfo(
                                    root.getNodeName(),
                                    STAXElementInfo.NO_ATTRIBUTE_NAME,
                                    STAXElementInfo.LAST_ELEMENT_INDEX,
                                    "Contains invalid node type: " + Integer.toString(child.getNodeType())));

                    throw new STAXInvalidXMLNodeTypeException(STAXUtil.formatErrorMessage(staxAction), staxAction);
            }
        }

        return staxAction.cloneAction();
    }

    /**
     * 所有xfs action都使用默认构造方法构造
     * @return
     * @throws STAXException
     */
    public T createAction() throws STAXException{
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            Logger.error(e);
            throw new STAXException("无法实例化" + clazz.getName());
        }
    }




    protected void handleRootAttributes(STAX staxService, STAXJob job, T action, Node root, NamedNodeMap attrs) throws STAXException {
    }

    protected void handleCommentNode(STAX staxService, STAXJob job, T action, Node root, Node child) {
    }

    protected void handleTextNode(STAX staxService, STAXJob job, T action, Node root, Node child) throws STAXException {
    }

    protected void handleChildNode(STAX staxService, STAXJob job, T action, Node root, Node child) throws STAXException {
    }
}
