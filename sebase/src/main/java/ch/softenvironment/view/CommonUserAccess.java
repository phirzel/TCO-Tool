package ch.softenvironment.view;

import ch.softenvironment.client.ResourceManager;

import javax.swing.*;

/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

/**
 * NLS-Utility for common reusable User Interface expressions (CUA).
 *
 * @author Peter Hirzel
 * @since 1.8 (2005-06-30)
 */
public abstract class CommonUserAccess {

    public static String getMncViewStatusbarText() {
        return getResource("MncViewStatusbar_text");
    }

    public static String getMncViewSymbollistStandardText() {
        return getResource("MncViewSymbollistStandard_text");
    }

    public static String getMniEditAssignText() {
        return getResource("MniEditAssign");
    }

    public static String getMniEditAssignWindowText() {
        return getResource("MniEditAssignWindow");
    }

    public static String getMniEditChangeText() {
        return getResource("MniEditChange_text");
    }

    public static String getMniEditChangeWindowText() {
        return getResource("MniEditChangeWindow_text");
    }

    public static String getMniEditCopyText() {
        return getResource("MniEditCopy_text");
    }

    public static String getMniEditCutText() {
        return getResource("MniEditCut_text");
    }

    public static String getMniEditFindText() {
        return getResource("MniEditFindWindow_text");
    }

    public static String getMniEditFindReplaceText() {
        return getResource("MniEditFindReplaceWindow_text");
    }

    public static String getMniEditPasteText() {
        return getResource("MniEditPaste_text");
    }

    public static String getMniEditRedoText() {
        return getResource("MniEditRedo_text");
    }

    public static String getMniEditRemoveText() {
        return getResource("MniEditRemove_text");
    }

    public static String getMniEditRenameText() {
        return getResource("MniEditRename_text");
    }

    public static String getMniEditSelectAllText() {
        return getResource("MniEditSelectAll_text");
    }

    public static String getMniEditUndoText() {
        return getResource("MniEditUndo_text");
    }

    public static String getMniExtrasOptionsWindowsText() {
        return getResource("MniExtrasOptionsWindow_text");
    }

    public static String getMniFileExit() {
        return getResource("MniFileExit_text");
    }

    public static String getMniFileExportText() {
        return getResource("MniFileExport_text");
    }

    public static String getMniFileImportText() {
        return getResource("MniFileImport_text");
    }

    public static String getMniFileNewText() {
        return getResource("MniFileNew_text");
    }

    public static String getMniFileNewWindowText() {
        return getResource("MniFileNewWindow_text");
    }

    public static String getMniFileOpenWindowText() {
        return getResource("MniFileOpenWindow_text");
    }

    public static String getMniFilePrintWindowText() {
        return getResource("MniFilePrintWindow_text");
    }

    public static String getMniFileSaveAsText() {
        return getResource("MniFileSaveAs_text");
    }

    public static String getMniFileSaveText() {
        return getResource("MniFileSave_text");
    }

    public static String getMniFormatBgColorText() {
        return getResource("MniFormatBackgroundColor_text");
    }

    public static String getMniFormatFgColorText() {
        return getResource("MniFormatForegroundColor_text");
    }

    public static String getMniFormatFillColorText() {
        return getResource("MniFormatFillColor_text");
    }

    public static String getMniFormatFontText() {
        return getResource("MniFormatFont_text");
    }

    public static String getMniFormatLineColorText() {
        return getResource("MniFormatLineColor_text");
    }

    public static String getMniHelpAboutText() {
        return getResource("MniHelpAbout_text");
    }

    public static String getMniHelpText() {
        return getResource("MniHelp_text");
    }

    public static String getMniWindowCascadeText() {
        return getResource("MniWindowCascade_text");
    }

    public static String getMniWindowOrderAutomaticallyText() {
        return getResource("MniWindowOrderAutomatically");
    }

    public static String getMniWindowTileText() {
        return getResource("MniWindowTile_text");
    }

    public static String getMnuEditText() {
        return getResource("MnuEdit_text");
    }

    public static String getMnuExtrasText() {
        return getResource("MnuExtras_text");
    }

    public static String getMnuFileOpenRecentlyText() {
        return getResource("MnuFileOpenRecently_text");
    }

    public static String getMnuFileText() {
        return getResource("MnuFile_text");
    }

    public static String getMnuFormatText() {
        return getResource("MnuFormat_text");
    }

    public static String getMnuHelpText() {
        return getResource("MnuHelp_text");
    }

    public static String getMnuLanguageText() {
        return getResource("MnuLanguage_text");
    }

    public static String getMnuReportsText() {
        return getResource("MnuReports_text");
    }

    public static String getMnuToolsText() {
        return getResource("MnuTools_text");
    }

    public static String getMnuViewLookAndFeelText() {
        return getResource("MnuViewLookAndFeel_text");
    }

    public static String getMnuViewSymbollistsText() {
        return getResource("MnuViewSymbollists_text");
    }

    public static String getMnuViewText() {
        return getResource("MnuView_text");
    }

    public static String getMnuWindowText() {
        return getResource("MnuWindow_text");
    }

    public static String getQuestionSaveChanges() {
        return getResource("CQSaveChanges");
    }

    /**
     * Return an NLS-Resource.
     *
     * @param property
     * @return
     */
    private static String getResource(String property) { //TODO HIP
        return ch.softenvironment.client.ResourceManager.getResource(CommonUserAccess.class, property);
    }

    public static String getTitleFileOpen() {
        return getResource("CTFileOpen");
    }

    public static String getTitleSaveChanges() {
        return getResource("CTSaveChanges");
    }

    // return typical Icons use in any application
    public static Icon getIconNew() {
        return ResourceManager.getImageIcon(CommonUserAccess.class, "new.gif");
    }

    public static Icon getIconChange() {
        return ResourceManager.getImageIcon(CommonUserAccess.class, "open.gif");
    }

    public static Icon getIconCopy() {
        return ResourceManager.getImageIcon(CommonUserAccess.class, "copy.gif");
    }

    public static Icon getIconCut() {
        return ResourceManager.getImageIcon(CommonUserAccess.class, "cut.gif");
    }

    public static Icon getIconFind() {
        return ResourceManager.getImageIcon(CommonUserAccess.class, "find.gif");
    }

    public static Icon getIconPaste() {
        return ResourceManager.getImageIcon(CommonUserAccess.class, "paste.gif");
    }

    public static Icon getIconPrint() {
        return ResourceManager.getImageIcon(CommonUserAccess.class, "print.gif");
    }

    public static Icon getIconRemove() {
        return ResourceManager.getImageIcon(CommonUserAccess.class, "delete.gif");
    }

    public static Icon getIconRedo() {
        return ResourceManager.getImageIcon(CommonUserAccess.class, "redo.gif");
    }

    public static Icon getIconSave() {
        return ResourceManager.getImageIcon(CommonUserAccess.class, "save.gif");
    }

    public static Icon getIconUndo() {
        return ResourceManager.getImageIcon(CommonUserAccess.class, "undo.gif");
    }
}
