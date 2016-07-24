/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-12-23
 * Last Updated: 2015-12-23, Gunnar Eriksson
 * Description: Is the dialog window for text messages
 * Version: First version.
 */
package se.gunnareriksson.bank.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BankMessageDialog extends JDialog
{
    private static final long serialVersionUID = 3287645688855606558L;
    private static final int SCROLL_PANE_WIDTH = 370;
    private static final int SCROLL_PANE_HEIGHT = 180;
    private static final String NEWLINE = "\n";
    private static final String EMPTY_STRING = "";
    private static final String OK_BUTTON_NAME = "OK";
    
    private JButton oKButton;
    private JTextArea textArea;
    
    /**
     * Constructor
     * Creates the bank message dialog window
     * 
     * @param title - the title of the dialog window.
     * @param message - the message shown in the dialog window.
     */
    public BankMessageDialog(String title, String message)
    {
        new BankMessageDialog(null, title, message);
    }
    
    /**
     * Constructor
     * Creates the bank message dialog window
     * 
     * @param gui - the frame owner.
     * @param title - the title of the dialog window.
     * @param message - the message shown in the dialog window.
     */
    public BankMessageDialog(JFrame gui, String title, String message)
    {
        JDialog messageDialog = initializeMessageDialog(gui, title);
        showMessageInTextArea(message);
        addGUIListeners(messageDialog);
        
        messageDialog.pack();
        messageDialog.setVisible(true);
    }
    
    /**
     * Constructor
     * Creates the bank message dialog window
     * 
     * @param title - the title of the dialog window.
     * @param message - the message shown in the dialog window.
     */
    public BankMessageDialog(String title, ArrayList<String> message)
    {
        String arrayListAsString = convertArrayListToString(message);
        new BankMessageDialog(null, title, arrayListAsString);
    }
    
    /**
     * Helper method to convert array list to string
     * 
     * @param message - the array list that should be converted to a string.
     * @return - the converted message as a string.
     */
    private String convertArrayListToString(ArrayList<String> message)
    {
        String listString = EMPTY_STRING;

        for (String s : message)
        {
            listString += s + NEWLINE;
        }
        
        return listString;
    }
    
    /**
     * Helper method to initialize the message dialog window. Sets the
     * window to modal and adds the transaction list panel.
     * 
     * @param gui - the frame owner.
     * @param title - the title of the dialog window.
     * @return - the dialog.
     */
    private JDialog initializeMessageDialog(JFrame gui, String title)
    {
        JDialog textMessageDialog = new JDialog(gui, title);
        textMessageDialog.setModal(true);
        JPanel transactionListPanel = createTransactionListPanel();
        textMessageDialog.add(transactionListPanel);
        
        return textMessageDialog;
    }
    
    /**
     * Helper method to create the transaction list panel containing
     * a text area with scroll panel and a button panel.
     * 
     * @return - the transaction list panel.
     */
    private JPanel createTransactionListPanel()
    {
        JPanel transactionListPanel = new JPanel();
        transactionListPanel.setLayout(new BoxLayout(transactionListPanel, BoxLayout.Y_AXIS));
        
        JScrollPane textAreaWithScroll = createTextAreaWithScrollPane();
        transactionListPanel.add(textAreaWithScroll);
        
        JPanel okButtonPanel = createOkButtonPanel();
        okButtonPanel.setMaximumSize(okButtonPanel.getPreferredSize());
        transactionListPanel.add(okButtonPanel);
        
        return transactionListPanel;
    }
    
    /**
     * Helper method to create the text area with scroll panel.
     * 
     * @return - the text area with a scroll panel.
     */
    private JScrollPane createTextAreaWithScrollPane()
    {
        textArea = new JTextArea();
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
        
        return scrollPane;
    }
    
    /**
     * Helper method to create the button panel containing an OK button.
     * 
     * @return - the button panel with the OK button.
     */
    private JPanel createOkButtonPanel()
    {
        JPanel oKButtonPanel = new JPanel();
        oKButtonPanel.setLayout(new FlowLayout());
        
        oKButton = new JButton(OK_BUTTON_NAME);
        oKButtonPanel.add(oKButton);
        
        return oKButtonPanel;
    }
    
    /**
     * Helper method to show text in the text area in the
     * dialog window.
     * 
     * @param message - the message to be shown i the text area.
     */
    private void showMessageInTextArea(String message)
    {
        textArea.setText(message);
    }
    
    /**
     * Helper method to add listener to the button
     * @param messageDialog - the dialog window.
     */
    private void addGUIListeners(JDialog messageDialog)
    {
        // OK button
        // Shuts down the dialog window
        oKButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                messageDialog.dispose();
            }
        });
    }
}
