/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-12-06
 * Last Updated: 2015-12-26, Gunnar Eriksson
 * Description: Creates the content in the customer information tab
 * 2015-12-26: Added the two methods clearCustomerPanel and 
 *             listAllCustomersInCustomerInformationPanel which are
 *             used when reload the program with customer information
 *             from file.
 *             Customer information is now showed, in a separate dialog
 *             window, when a customer is removed.
 *             The customer information window is updated when customer
 *             is added, renamed, or removed.
 * Version: Second version
 */
package se.gunnareriksson.bank.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import se.gunnareriksson.bank.logic.BankLogic;

public class BankCustomerPanel extends JPanel
{
    private static final long serialVersionUID = -3547208781320373844L;
    private static final int BUTTON_WIDTH = 105;
    private static final int BUTTON_HEIGHT = 25;
    private static final int NUMBER_OF_COLUMNS = 10;
    private static final int SCROLL_PANE_WIDTH = 370;
    private static final int SCROLL_PANE_HEIGHT = 500;
    private static final int NUMBER_OF_MESSAGE_ROWS = 3;
    private static final int NUMBER_OF_MESSAGE_COLUMNS = 34;
    private static final String EMPTY_STRING = "";
    private static final String CUSTOMER_BORDER_NAME = "Kund";
    private static final String CUSTOMER_INFORMATION_BORDER_NAME = "Kundinformation";
    private static final String MESSAGE_BORDER_NAME = "Meddelanden";
    private static final String NAME_SEARCH_WORD = "Namn: ";
    private static final String PNR_SEARCH_WORD = "Personnummer: ";
    private static final String PNR_NUMBER_EXCEPTION_MESSAGE = "Personnummer saknas eller har fel format";
    
    private JButton addButton, updateButton, removeButton, clearButton, listCustomerButton;
    private JTextField pNrTextField, nameTextField;
    private JTextArea messageArea;
    private JList<String> customerList;
    private DefaultListModel<String> customerModelList;
    
    private BankLogic bankLogic;
    private BankAccountPanel bankAccountPanel;
    
    /**
     * Constructor
     * the bank account panel.
     * Initializes the listeners to the buttons and customer list.
     * 
     * @param bankLogic - the logic to handle customers and accounts
     * @param bankAccountPanel - the account information panel
     */
    public BankCustomerPanel(BankLogic bankLogic, BankAccountPanel bankAccountPanel)
    {
        this.bankLogic = bankLogic;
        this.bankAccountPanel = bankAccountPanel;
        JPanel bankCustomerPanel = createBankCustomerPanel();
        add(bankCustomerPanel);
        addGUIListeners();
    }
    
    /**
     * Helper method to create the bank customer panel.
     * 
     * @return - the content for the bank customer information panel
     */
    private JPanel createBankCustomerPanel() 
    {
        JPanel bankCustomerPanel = new JPanel();
        bankCustomerPanel.setLayout(new BoxLayout(bankCustomerPanel, BoxLayout.Y_AXIS));
        
        JPanel handleCustomerPanel = createHandleCustomerPanel();
        bankCustomerPanel.add(handleCustomerPanel);
        
        JPanel customerListPanel = createCustomerInformationListPanel();
        bankCustomerPanel.add(customerListPanel);
        
        JPanel messagePanel = createCustomerMessageArea();
        bankCustomerPanel.add(messagePanel);
        
        return bankCustomerPanel;
    }
    
    /**
     * Helper method to create the handle customer panel, containing the name and personal
     * numbers field and the customer handling buttons (add, update and remove).
     * 
     * @return - the handle customer panel.
     */
    private JPanel createHandleCustomerPanel()
    {
        JPanel handleCustomerPanel = new JPanel();
        handleCustomerPanel.setLayout(new BoxLayout(handleCustomerPanel, BoxLayout.Y_AXIS));
        
        JPanel customerInformationPanel = createCustomerInformationPanel();
        handleCustomerPanel.add(customerInformationPanel);
        JPanel addUpdateRemoveButtonPanel = createAddUpdateRemoveButtonPanel();
        handleCustomerPanel.add(addUpdateRemoveButtonPanel);
        JPanel clearButtonPanel = createClearButtonPanel();
        handleCustomerPanel.add(clearButtonPanel);
        
        handleCustomerPanel.setBorder(new TitledBorder(CUSTOMER_BORDER_NAME));
        
        return handleCustomerPanel;
    }
    
    /**
     * Helper method to create customer information panel, containing
     * two labels for personal number and name. Text field for
     * personal numbers and name.
     * 
     * @return - the customer information panel
     */
    private JPanel createCustomerInformationPanel()
    {
        JPanel customerInformationPanel = new JPanel();
        customerInformationPanel.setLayout(new GridLayout(2,2));
        
        JLabel pNrLabel = new JLabel("Personnummer:");
        customerInformationPanel.add(pNrLabel);
        pNrTextField = new JTextField(NUMBER_OF_COLUMNS);
        customerInformationPanel.add(pNrTextField);
        
        JLabel nameLabel = new JLabel("Namn:");
        customerInformationPanel.add(nameLabel);
        nameTextField = new JTextField(NUMBER_OF_COLUMNS);
        customerInformationPanel.add(nameTextField);
        
        return customerInformationPanel;
    }
    
    /**
     * Helper method to create the button panel to handle
     * adding, updating and removing of customers.
     * 
     * @return - the button panel for customer handling.
     */
    private JPanel createAddUpdateRemoveButtonPanel()
    {
        JPanel addUpdateRemovePanel = new JPanel();
        addUpdateRemovePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        addButton = new JButton("Lägg till");
        addButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        addUpdateRemovePanel.add(addButton);
        
        updateButton = new JButton("Uppdatera");
        updateButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        addUpdateRemovePanel.add(updateButton);
        
        removeButton = new JButton("Ta bort");
        removeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        addUpdateRemovePanel.add(removeButton);
        
        return addUpdateRemovePanel;
    }
    
    /**
     * Helper method to create the panel for the clear button
     * 
     * @return - the button panel to clear the personal number and name
     *           text field.
     */
    private JPanel createClearButtonPanel()
    {
        JPanel clearButtonPanel = new JPanel();
        clearButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        clearButton = new JButton("Rensa");
        clearButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        clearButtonPanel.add(clearButton);
        
        return clearButtonPanel;
    }
    
    /**
     * Helper method to customer information list panel, containing the customer list panel
     * and the customer list button panel.
     * 
     * @return - the customer information panel.
     */
    private JPanel createCustomerInformationListPanel()
    {
        JPanel customerInformationPanel = new JPanel();
        customerInformationPanel.setLayout(new BoxLayout(customerInformationPanel, BoxLayout.Y_AXIS));

        JPanel customerListPanel = createCustomerListPanel();
        customerInformationPanel.add(customerListPanel);
        
        JPanel listCustomerButtonPanel = createListCustomersButtonPanel();
        customerInformationPanel.add(listCustomerButtonPanel);
        
        customerInformationPanel.setBorder(new TitledBorder(CUSTOMER_INFORMATION_BORDER_NAME));
        
        return customerInformationPanel;
    }
    
    /**
     * Helper method to create the customer list panel, containing customer list
     * with scroll panels.
     * 
     * @return - the customer list panel.
     */
    private JPanel createCustomerListPanel()
    {
        JPanel customerListPanel = new JPanel();
        customerModelList = new DefaultListModel<String>();
        customerList = new JList<String>(customerModelList);
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(customerList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
        customerListPanel.add(scrollPane);
        
        return customerListPanel;
    }
    
    /**
     * Helper method to create the button panel to list customer, containing
     * the list customer button.
     * 
     * @return - the button panel to list customers.
     */
    private JPanel createListCustomersButtonPanel()
    {
        JPanel customersButtonPanel = new JPanel();
        customersButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        listCustomerButton = new JButton("Lista");
        listCustomerButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        customersButtonPanel.add(listCustomerButton);
        
        return customersButtonPanel;
    }
    
    /**
     * Helper method to create the panel containing the message area
     * 
     * @return - the panel with the message area.
     */
    private JPanel createCustomerMessageArea()
    {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        messageArea = new JTextArea(NUMBER_OF_MESSAGE_ROWS, NUMBER_OF_MESSAGE_COLUMNS);
        messageArea.setBackground(getBackground());
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messagePanel.add(messageArea);
        messagePanel.setBorder(new TitledBorder(MESSAGE_BORDER_NAME));
        
        return messagePanel;
    }
    
    /**
     * Helper method to add listeners to all buttons and the customer list.
     */
    private void addGUIListeners()
    {
        // Add button
        // Clears the message area.
        // Adds a customer to the register.
        // Clears the name text field and makes the personal number
        // text field editable.
        // Updates the customer information window with the added
        // customer.
        // Handles number format exception if customer could not be
        // added.
        addButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearMessageArea();
                
                try
                {
                    createAndAddCustomerToRegister();
                    clearNameTextField();
                    clearPNrTextFieldAndMakeEditable();
                    listAllCustomersInRegister();
                }
                catch (NumberFormatException exception)
                {
                    showErrorMessage("Kan ej lägga till kund", exception.getMessage());
                }
            }
        });
        
        // Update button
        // Clears the message area.
        // Clears the name text field and makes the personal number
        // text field editable.
        // Updates the customer information window with the changed
        // name of the customer.
        // Handles number format exception if customers name could
        // not be changed.
        updateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearMessageArea();
                try
                {
                    changeCustomersName();
                    clearNameTextField();
                    clearPNrTextFieldAndMakeEditable();
                    listAllCustomersInRegister();
                }
                catch (NumberFormatException exception)
                {
                    showErrorMessage("Kan ej uppdatera namn" , exception.getMessage());
                }
            }
        });
        
        // Remove button
        // Clears the message area.
        // Removes customer from register.
        // Clears the name text field and makes the personal number
        // text field editable.
        // Updates the customer information window when the customer
        // is removed.
        // Creates a separate dialog window with contains information
        // about the removed customer (name, personal number, accounts
        // with balance and interest).
        // Handles number format exception if customer could not be 
        // removed.
        removeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearMessageArea();
                try
                {
                    ArrayList<String> customerList = removeCustomer();
                    clearNameTextField();
                    clearPNrTextFieldAndMakeEditable();
                    listAllCustomersInRegister();
                    new BankMessageDialog("Borttagen kund", customerList);
                }
                catch (NumberFormatException exception)
                {
                    showErrorMessage("Kan ej ta bort kund" , exception.getMessage());
                }
            }
        });
        
        // Clear button
        // Clears the name text field, personal number, message area and
        // the customer list window. Makes the personal number field
        // editable.
        clearButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearNameTextField();
                clearPNrTextFieldAndMakeEditable();
                clearMessageArea();
                clearCustomerList();
            }
        });
        
        // List customers button
        // Clears the message area and list all customers in
        // customer information window.
        listCustomerButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearMessageArea();
                listAllCustomersInRegister();
            }
        });
        
        // Customer list (reacts when clicking in the customer list).
        // If a customer is selected in customer information window, the
        // message area is cleared, the name and personal number is fetched
        // from the selected line and are set to the name and personal number
        // text field.
        customerList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                String customer = customerList.getSelectedValue();
                if (customer != null)
                {
                    clearMessageArea();
                    
                    String name = getNameFromCustomerInformation(customer);
                    nameTextField.setText(name);

                    String pNr = getPNrAsStringFromCustomerInformation(customer);
                    setPNrTextFieldInCustomerAndAccountPanel(pNr);
                }
            }
        });
    }
    
    /**
     * Helper method to clear the message area.
     */
    private void clearMessageArea()
    {
        messageArea.setText(EMPTY_STRING);
    }
    
    /**
     * Helper method to create and add customer to the register.
     * Reads the name and personal number from the text fields.
     * 
     * @throws NumberFormatException - if personal number contains a
     *                                 not parsable long.
     */
    private void createAndAddCustomerToRegister() throws NumberFormatException
    {
        String name = nameTextField.getText();
        long pNr = getPersonalNumberFromTextField();
        
        boolean isCustomerAdded = bankLogic.addCustomer(name, pNr);
        if (isCustomerAdded)
        {
            updateMessagaArea("Kunden " + name + " är nu registrerad");
        }
        else
        {
            updateMessagaArea("Kund är redan inlagd i kundregistret");
        }
    }
    
    /**
     * Helper method to get the personal number from the personal number
     * text field.
     * 
     * @return - the personal number
     * @throws NumberFormatException - if the personal number contains a not 
     *                                 parsable long. An personal number error
     *                                 message is added to the exception.
     */
    private long getPersonalNumberFromTextField() throws NumberFormatException
    {
        try
        {
            long pNr = Long.parseLong(pNrTextField.getText());
            return pNr;
            
        }
        catch (NumberFormatException exception)
        {
            throw new NumberFormatException(PNR_NUMBER_EXCEPTION_MESSAGE);
        }
    }
    
    /**
     * Helper method to update the message area.
     * Clear old text and sets the new text.
     * 
     * @param message - the text to be set in the message area.
     */
    private void updateMessagaArea(String message)
    {
        messageArea.setText(EMPTY_STRING);
        messageArea.setText(message);
    }
    
    /**
     * Helper method to clear the name text field.
     */
    private void clearNameTextField()
    {
        nameTextField.setText(EMPTY_STRING);
    }
    
    /**
     * Helper method to clear the personal number text field
     * and make the text field editable.
     */
    private void clearPNrTextFieldAndMakeEditable()
    {
        pNrTextField.setEditable(true);
        pNrTextField.setText(EMPTY_STRING);
    }
    
    /**
     * Helper method to change the customers name in the register.
     * Reads the customer name from the name text field and the personal number
     * from the personal number text field. Updates the message area with the
     * result.
     * 
     * @throws NumberFormatException - if the personal number contains a not 
     *                                 parsable long.
     */
    private void changeCustomersName() throws NumberFormatException
    {
        String name = nameTextField.getText();
        long pNr = getPersonalNumberFromTextField();
        boolean isCustomerChanged = bankLogic.changeCustomerName(name, pNr);
        
        if (isCustomerChanged)
        {
            updateMessagaArea("Kunden med personnummer " + pNr + " heter nu " + name);
        }
        else
        {
            updateMessagaArea("Kunde inte hitta kund med personnummer: " + pNr);
        }
    }
    
    /**
     * Helper method to remove customer from the customer register.
     * Reads the personal number from the personal number text field and
     * updates the message area with the result.
     * 
     * @throws NumberFormatException - if the personal number contains a not 
     *                                 parsable long.
     */
    private ArrayList<String> removeCustomer() throws NumberFormatException
    {
        long pNr = getPersonalNumberFromTextField();
        ArrayList<String> customerInformation = bankLogic.removeCustomer(pNr);
        
        if (customerInformation.isEmpty())
        {
            updateMessagaArea("Kunde inte hitta kund med personnummer: " + pNr);
        }
        else
        {
            updateMessagaArea("Kund med personnummer " + pNr + " är bortagen");
        }
        
        return customerInformation;
    }
    
    /**
     * Helper method to update customer list with all customers.
     * 
     * @param customers - the list of all customers.
     */
    private void updateCustomerListArea(ArrayList<String> customers)
    {
        customerModelList.removeAllElements();
        for(String customer : customers)
        {
            customerModelList.addElement(customer);
        } 
    }
    
    /**
     * Helper method to clear the customer list.
     */
    private void clearCustomerList()
    {
        customerList.clearSelection();
        customerModelList.removeAllElements();
    }
    
    /**
     * Helper method to list all customers in the customer list.
     * If no customer exists, the message area is updated with the
     * result.
     */
    private void listAllCustomersInRegister()
    {
        clearCustomerList();
        ArrayList<String> customers = bankLogic.getCustomers();
        if (customers.isEmpty())
        {
            updateMessagaArea("Inga kunder finns i kundregistret");
        }
        else
        {
            updateCustomerListArea(customers);
        }
    }
    
    /**
     * Helper method to get customer from the customer information string.
     * 
     * @param customerInformation - string containing information about a
     *                              customer.
     * @return - the customer name of an empty string if the string contain
     *           the word "Namn: ".
     */
    private String getNameFromCustomerInformation(String customerInformation)
    {
        if (customerInformation.contains(NAME_SEARCH_WORD))
        {
            int nameStartPos = customerInformation.indexOf(NAME_SEARCH_WORD) + NAME_SEARCH_WORD.length();
            int nameEndPos = customerInformation.indexOf(',', nameStartPos);
            String name = customerInformation.substring(nameStartPos, nameEndPos);
            
            return name;
        }
        else
        {
            return EMPTY_STRING;
        }
    }
    
    /**
     * Helper method to set the personal number in the personal number text field.
     * Sets the personal number text field to not editable.
     * Sends the personal number to the personal number text field in the
     * account information tab (bank account panel).
     * @param pNr
     */
    private void setPNrTextFieldInCustomerAndAccountPanel(String pNr)
    {
        pNrTextField.setEditable(false);
        pNrTextField.setText(pNr);
        bankAccountPanel.setNewPersonalNumberInAccountPanel(pNr);
    }
    
    /**
     * Helper method to get the personal number from the customer information string.
     * 
     * @param customerInformation - string containing information about a customer.
     * @return - the personal number as a string. Returns an empty string if the
     *           string does not contain the word "Personnummer: ".
     */
    private String getPNrAsStringFromCustomerInformation(String customerInformation)
    {
        if (customerInformation.contains(PNR_SEARCH_WORD))
        {
            int pNrStartPos = customerInformation.lastIndexOf(PNR_SEARCH_WORD) + PNR_SEARCH_WORD.length();
            String pNr = customerInformation.substring(pNrStartPos);
            
            return pNr;
        }
        else
        {
            return EMPTY_STRING;
        }
    }
    
    /**
     * Helper method to a open a error message window
     * 
     * @param title - the title of the error.
     * @param message - the error message.
     */
    private void showErrorMessage(String title, String message)
    {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Clears all fields in the customer panel.
     */
    public void clearCustomerPanel()
    {
        clearPNrTextFieldAndMakeEditable();
        clearNameTextField();
        clearCustomerList();
        clearMessageArea();
    }
    
    /**
     * List all customers in the register in the customer
     * information window.
     */
    public void listAllCustomersInCustomerInformationPanel()
    {
        listAllCustomersInRegister();
    }
}
