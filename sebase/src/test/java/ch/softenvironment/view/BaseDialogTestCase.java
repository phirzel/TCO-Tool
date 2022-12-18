package ch.softenvironment.view;

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.util.UserException;
import ch.softenvironment.view.swingext.SwingWorker;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.eventdata.KeyEventData;
import junit.extensions.jfcunit.finder.NamedComponentFinder;

/**
 * Test the BaseDialog class by JFCUnit.
 * <p>
 * Semi-automated test - Dialog opened must be clicked by Tester actively!
 *
 * @author Peter Hirzel
 */
public class BaseDialogTestCase extends JFCTestCase {

    // private Container mainFrame = null;
    String title = null;
    String message = null;

    /**
     * Test manually.
     */
    public static void main(String[] args) {
        String title = "Test BaseDialog";
        String msgShort = "This is my message!";
        String msgLong = "This is my message!\nFFirst line.............................................................................\n\nBack from free breakline!"
            + "and some\nother\nlines\n............................";

        // Warning
        BaseDialog.showWarning(null, title, msgShort);
        BaseDialog.showWarning(null, title, msgLong);

        title = ResourceManager.getResource(BaseDialog.class, "CTQuestion");
        // Confirm
        if (!BaseDialog.showConfirm(null, title, "Please press <Yes>!")) {
            BaseDialog.showWarning(null, "ERROR", "Wrong button pressed");
        }
        if (BaseDialog.showConfirm(null, title, "Please press <No>!")) {
            BaseDialog.showWarning(null, "ERROR", "Wrong button pressed");
        }

        // ConfirmCancel
        Boolean result = BaseDialog.showConfirmCancel(null, title, "Please press <Yes>!");
        if ((result == null) || (!result.equals(Boolean.TRUE))) {
            BaseDialog.showWarning(null, "Press-Info", "Error <Yes> NOT PRESSED");
        }
        result = BaseDialog.showConfirmCancel(null, title, "Please press <No>!");
        if ((result == null) || (!result.equals(Boolean.FALSE))) {
            BaseDialog.showWarning(null, "ERROR", "Wrong button pressed");
        }
        result = BaseDialog.showConfirmCancel(null, title, "Please press <Cancel>!");
        if (result != null) {
            BaseDialog.showWarning(null, "ERROR", "Wrong button pressed");
        }

        // ConfirmDelete
        if (!BaseDialog.showConfirmDeletion(null)) {
            BaseDialog.showWarning(null, "ERROR", "<Yes> assumed");
        }
        if (BaseDialog.showConfirmDeletion(null)) {
            BaseDialog.showWarning(null, "Feedback", "<No> assumed");
        }
        if (BaseDialog.showConfirmDeletion(null, "MyDeleteTitle", "please press <No>")) {
            BaseDialog.showWarning(null, "ERROR", "Wrong button pressed");
        }

        // default title
        BaseDialog.showWarning(null, null, "should show default 'Warning' title");
        BaseDialog.showConfirm(null, null, "should show default 'Question' title");
        BaseDialog.showConfirmDeletion(null, null, "should show default 'Deletion' title");
        BaseDialog.showError(null, null, "should show default 'Error' title", null);
        BaseDialog.showError(null, "show default error-msg", null, null);

        BaseDialog.showError(null, "My Error", "error with 'null'-exception", null);
        BaseDialog.showError(null, "My Error", "error with 'UserException'-exception", new UserException("not a stacktrace", "TestCase"));
        BaseDialog.showConfirmExit(null);
    }

    public BaseDialogTestCase(String test) {
        super(test);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        title = "Test BaseDialog";
        message = "This is my message!";
        setHelper(new JFCTestHelper()); // Uses the AWT Event Queue.

        SwingWorker modalDialogFork = new SwingWorker() {
            @Override
            public Object construct() {
                BaseDialog.showWarning(null, title, message);
                return null;
            }
        };
        modalDialogFork.start();
    }

    @Override
    protected void tearDown() throws Exception {
        TestHelper.cleanUp(this);
        super.tearDown();
    }

    public void testShowWarning() throws Exception {
        java.util.List<JDialog> dialogs = JFCTestHelper.getShowingDialogs();
        assertEquals("Dialog opened", 1, dialogs.size());
        JDialog dialog = dialogs.iterator().next();
        assertEquals("Dialog Title", title, dialog.getTitle());
        /*
         * DialogFinder dFinder = new DialogFinder( loginScreen );
         * showingDialogs = dFinder.findAll(); assertEquals(
         * "Number of dialogs showing is wrong", 1, showingDialogs.size( ) );
         * dialog = ( JDialog )showingDialogs.get( 0 ); assertEquals(
         * "Wrong dialog showing up", "Login Error", dialog.getTitle( ) );
         * getHelper().disposeWindow( dialog, this );
         */
        // JButton button =
        // (JButton)JFCTestHelper.findNamedComponent(ResourceManager.getInstance().getResource(BaseDialog.class,
        // "BtnCancel_text"), 0);
        NamedComponentFinder finder = new NamedComponentFinder(JButton.class, ResourceManager.getResource(BaseDialog.class, "BtnCancel_text"));
        JButton button = (JButton) finder.find();
        assertNotNull("Cancel-Button exists?", button);
        /*
         * JTextField inputTextField = (JTextField)
         * JFCTestHelper.findNamedComponent("input", 0); assertNotNull(
         * "Es konnte keine Textfeld mit dem Namen 'input' gefunden werden.",
         * inputTextField); assertEquals(inputTextField.getText(),
         * "<Bitte geben Sie das geheime Wort ein>"); JTextArea outputArea =
         * (JTextArea) JFCTestHelper.findNamedComponent("output", 0);
         * assertNotNull
         * ("Es konnte keine Textfeld mit dem Namen 'output' gefunden werden.",
         * inputTextField);
         */

        /*
         * inputTextField.requestFocus(); helper.sendString(new
         * StringEventData(this, inputTextField, "Falsches Wort"));
         * helper.enterClickAndLeave(new MouseEventData(this, okButton));
         * assertEquals("Die Eingabe " + inputTextField.getText() +
         * " führte zu einem falschen Ergebnis.",
         * "Falsch geraten, probieren Sie es mit 'Testing'.",
         * outputArea.getText()); // Um die Richtigkeit der Funktionalität im
         * positiven Fall (richtige Eingabe) zu testen, // wird ein Tastendruck
         * simuliert. inputTextField.requestFocus();
         * inputTextField.setText("Testing");
         */
        // press Cancel
        button.requestFocus();
        // Enter Taste simulierten
        getHelper().sendKeyAction(new KeyEventData(this, button, KeyEvent.VK_ENTER));
        dialogs = JFCTestHelper.getShowingDialogs();
        assertEquals("Dialog closed", 0, dialogs.size());

        // assertEquals("Die Eingabe " + inputTextField.getText() +
        // " führte zu einem falschen Ergebnis.", "Testing is cool!",
        // outputArea.getText());
    }
}