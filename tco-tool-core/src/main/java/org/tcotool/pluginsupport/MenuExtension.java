package org.tcotool.pluginsupport;

public class MenuExtension {

    public String getId() {
        return id;
    }

    // menu id, relevant for actionPerformed
    public void setId(String id) {
        this.id = id;
    }

    String id;

    // getResourceString(ext, "textKey", loader)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // getResourceString(ext, "descriptionKey", loader)
    public String getToolTipText() {
        return toolTipText;
    }

    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }

    public boolean isGroupSpecific() {
        return groupSpecific;
    }

    //TODO remove or use
    public void setGroupSpecific(boolean groupSpecific) {
        this.groupSpecific = groupSpecific;
    }

    String text;

    String toolTipText;

    boolean groupSpecific;
}
