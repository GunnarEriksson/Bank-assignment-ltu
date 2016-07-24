/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-12-06
 * Last Updated: 2015-12-26, Gunnar Eriksson
 * Description: Creates the content in the account information tab
 * 2015-12-26: Added a button and logic to save listed transaction
 *             for a specific account to a text file.
 *             Information of a removed account or account information
 *             for a specific account is now listed in a separate dialog
 *             window.
 *             The account information window is updated after an account
 *             handling (transaction, account added or removed).
 * Version: Second version
 */
package se.gunnareriksson.bank.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import se.gunnareriksson.bank.timestamp.TimeStamp;

public class BankAccountPanel extends JPanel
{
    private static final long serialVersionUID = -2227590519738952542L;
    
    private static final int COMBO_BOX_WIDTH = 180;
    private static final int COMBO_BOX_HEIGHT = 25;
    private static final int BUTTON_WIDTH = 105;
    private static final int BUTTON_HEIGHT = 25;
    private static final int NUMBER_OF_COLUMNS = 10;
    private static final int SCROLL_PANE_WIDTH = 370;
    private static final int SCROLL_PANE_HEIGHT = 180;
    private static final int NUMBER_OF_MESSAGE_ROWS = 3;
    private static final int NUMBER_OF_MESSAGE_COLUMNS = 34;
    private static final int NO_CUSTOMERS = -1;
    private static final String ACCOUNT_BORDER_NAME = "Konto";
    private static final String TRANSACTION_BORDER_NAME = "Insättning / Uttag";
    private static final String TRANSACTIONS_BORDER_NAME = "Transaktioner";
    private static final String MESSAGE_BORDER_NAME = "Meddelanden";
    private static final String NEWLINE = "\n";
    private static final String EMPTY_STRING = "";
    private static final String TEXT_FILE_NAME_EXTENSION = ".txt";
    private static final String ACCOUNT_NUMBER_SEARCH_NUMBER = "Kontonummer: ";
    private static final String ACCOUNT_TYPE_SEARCH_WORD = "Typ av konto: ";
    private static final String CAN_NOT_SAVE_TO_FILE_EXCEPTION = "Kan ej spara transaktioner till fil";
    private static final String PNR_NUMBER_EXCEPTION_MESSAGE = "Personnummer saknas eller har fel format";
    private static final String ACCOUNT_ID_EXCEPTION_MESSAGE = "Account ID saknas eller har fel format";
    private static final String AMOUNT_EXCEPTION_MESSAGE = "Summa saknas eller har fel format";
    private static final String CAN_NOT_FIND_FILE_EXCEPTION_MESSAGE = "Hittar inte filen med sökvägen: ";
    private static final String TRANSACTION_TYPE_EXCEPTION_MESSAGE = "Felaktig transaktionstyp";
    private static final String WRITE_TO_FILE_EXCEPTION_MESSAGE = "Filen är skrivskyddad";
    private static final String CODING_FAULT_EXCEPTION_MESSAGE = "Fel i kodningen av fil har uppstått";
    private static final String IO_FAULT_EXCEPTION_MESSAGE = "Kan ej skapa eller skriva till fil";
    
    private JButton addButton, removeButton, accountInfoButton, transactionButton, saveTransactionListButton, listTransactionButton, listAccountButton;
    private JTextField pNrTextField, accountIdTextField, amountTextField;
    private JTextArea messageArea, transactionArea;
    private JList<String> accountList;
    private DefaultListModel<String> accountModelList;
    private JComboBox<AccountType> accountTypeBox;
    private JComboBox<TransactionType> transactionTypeBox;
    
    private BankLogic bankLogic;
    private JFileChooser fileChooser;
    
    /**
     * Constructor
     * Creates the bank account panel.
     * Initializes the listeners to the buttons and account list.
     * 
     * @param bankLogic - the logic to handle customers and accounts.
     */
    public BankAccountPanel(BankLogic bankLogic, JFileChooser fileChooser)
    {
        this.bankLogic = bankLogic;
        this.fileChooser = fileChooser;
        JPanel bankAccountPanel = createAccountGUIPanel();
        add(bankAccountPanel);
        addGUIListeners();
    }
    
    /**
     * Helper method to create the bank account information panel
     * 
     * @return - the content for the bank account information panel
     */
    private JPanel createAccountGUIPanel()
    {
        JPanel accountHandlingPanel = createAccountHandlingPanel();
        JPanel transactionHandlingPanel = createTransactionHandlingPanel();
        JPanel transactionInformationListPanel = createTransactionsPanel();
        JPanel accountInformationListPanel = createAccountInformationPanel();
        JPanel accountMessagePanel = createMessagePanel();
        JPanel bankAccountGUIPanel = new JPanel();
        
        bankAccountGUIPanel.setLayout(new BoxLayout(bankAccountGUIPanel, BoxLayout.Y_AXIS));
        bankAccountGUIPanel.add(accountHandlingPanel);
        bankAccountGUIPanel.add(transactionHandlingPanel);
        bankAccountGUIPanel.add(transactionInformationListPanel);
        bankAccountGUIPanel.add(accountInformationListPanel);
        bankAccountGUIPanel.add(accountMessagePanel);
        
        return bankAccountGUIPanel;
    }
    
    /**
     * Helper method to create the account handling panel, containing the account panel
     * and the account button panel.
     * 
     * @return - the account handling panel.
     */
    private JPanel createAccountHandlingPanel()
    {
        JPanel accountHandlingPanel = new JPanel();
        accountHandlingPanel.setLayout(new BoxLayout(accountHandlingPanel, BoxLayout.Y_AXIS));
        
        JPanel accountPanel = createAccountPanel();
        accountHandlingPanel.add(accountPanel);
        
        JPanel accountButtonPanel = createAccountButtonPanel();
        accountHandlingPanel.add(accountButtonPanel);
        accountHandlingPanel.setBorder(new TitledBorder(ACCOUNT_BORDER_NAME));
        
        return accountHandlingPanel;
    }

    /**
     * Helper method to create account panel, containing the account type
     * combo box, personal number field, account number field.
     * 
     * @return - the account panel.
     */
    private JPanel createAccountPanel()
    {
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new GridLayout(3, 2));
        
        JLabel accountTypeLabel = new JLabel("Kontotyp:");
        accountPanel.add(accountTypeLabel);
        accountTypeBox = new JComboBox<AccountType>(AccountType.values());
        accountTypeBox.setPreferredSize(new Dimension(COMBO_BOX_WIDTH,COMBO_BOX_HEIGHT));
        accountPanel.add(accountTypeBox);
        
        JLabel pNrLabel = new JLabel("Personnummer:");
        accountPanel.add(pNrLabel);
        pNrTextField = new JTextField(NUMBER_OF_COLUMNS);
        accountPanel.add(pNrTextField);
        
        JLabel nameLabel = new JLabel("Kontonummer:");
        accountPanel.add(nameLabel);
        accountIdTextField = new JTextField(NUMBER_OF_COLUMNS);
        accountPanel.add(accountIdTextField);
        
        return accountPanel;
    }
    
    /**
     * Helper method to create the button panel for the account handling panel.
     * Contains the button add, remove and information.
     * 
     * @return - the button panel for the account handling panel.
     */
    private JPanel createAccountButtonPanel()
    {
        JPanel accountButtonPanel = new JPanel();
        accountButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        addButton = new JButton("Lägg till");
        addButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        accountButtonPanel.add(addButton);
        
        removeButton = new JButton("Ta bort");
        removeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        accountButtonPanel.add(removeButton);
        
        accountInfoButton = new JButton("Info");
        accountInfoButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        accountButtonPanel.add(accountInfoButton);
        
        return accountButtonPanel;
    }
    
    /**
     * Helper method to create the transaction handling method, containing the transaction panel
     * transaction button panel.
     * 
     * @return the transaction handling panel.
     */
    private JPanel createTransactionHandlingPanel()
    {
        JPanel transactionHandlingPanel = new JPanel();
        transactionHandlingPanel.setLayout(new BoxLayout(transactionHandlingPanel, BoxLayout.Y_AXIS));
        
        JPanel transactionPanel = createTransactionPanel();
        transactionHandlingPanel.add(transactionPanel);
        
        JPanel transactionButtonPanel = createTransactionButtonPanel();
        transactionHandlingPanel.add(transactionButtonPanel);
        transactionHandlingPanel.setBorder(new TitledBorder(TRANSACTION_BORDER_NAME));
        
        return transactionHandlingPanel;
    }

    /**
     * Helper method to create the transaction panel, containing the account type combo box and
     * the amount label and text field.
     * 
     * @return - the transaction panel.
     */
    private JPanel createTransactionPanel()
    {
        JPanel transactionPanel = new JPanel();
        transactionPanel.setLayout(new GridLayout(2, 2));
        
        JLabel transactionTypeLabel = new JLabel("Transaktion:");
        transactionPanel.add(transactionTypeLabel);
        transactionTypeBox = new JComboBox<TransactionType>(TransactionType.values());
        transactionTypeBox.setPreferredSize(new Dimension(COMBO_BOX_WIDTH,COMBO_BOX_HEIGHT));
        transactionPanel.add(transactionTypeBox);
        
        JLabel amountLabel = new JLabel("Summa:");
        transactionPanel.add(amountLabel);
        amountTextField = new JTextField(NUMBER_OF_COLUMNS);
        transactionPanel.add(amountTextField);
        
        return transactionPanel;
    }
    
    /**
     * Helper method to create the button panel for transaction handling panel, containing
     * the transaction button.
     * 
     * @return - the button panel containing the transaction button.
     */
    private JPanel createTransactionButtonPanel()
    {
        JPanel transactionButtonPanel = new JPanel();
        transactionButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        transactionButton = new JButton("Transaktion");
        transactionButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        transactionButtonPanel.add(transactionButton);
        
        return transactionButtonPanel;
    }
    
    /**
     * Helper method to create the transactions panel. The panel that list all transactions
     * on an account. Contains the transaction area panel and transactions button panel.
     * 
     * @return - the transactions panel.
     */
    private JPanel createTransactionsPanel()
    {
        JPanel transactionsPanel = new JPanel();
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        
        JPanel transactionAreaPanel = createTransactionAreaPanel();
        transactionsPanel.add(transactionAreaPanel);
        
        JPanel transactionsButtonPanel = createTransactionsButtonPanel();
        transactionsPanel.add(transactionsButtonPanel);
        transactionsPanel.setBorder(new TitledBorder(TRANSACTIONS_BORDER_NAME));
        
        return transactionsPanel;
    }
    
    /**
     * Helper method to create transaction area panel, containing transaction area with a 
     * vertical and horizontal scroll pane.
     * 
     * @return - the transaction area panel.
     */
    private JPanel createTransactionAreaPanel()
    {
        JPanel transactionListPanel = new JPanel();
        transactionArea = new JTextArea();
        transactionArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transactionArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
        transactionListPanel.add(scrollPane);
        
        return transactionListPanel;
    }
    
    /**
     * Helper method to create the button panel for the transactions panel, containing the
     * button to list all transactions.
     * 
     * @return - the button panel containing the list all transactions button.
     */
    private JPanel createTransactionsButtonPanel()
    {
        JPanel transactionsButtonPanel = new JPanel();
        transactionsButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        saveTransactionListButton = new JButton("Spara");
        saveTransactionListButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        transactionsButtonPanel.add(saveTransactionListButton);
        
        listTransactionButton = new JButton("Lista");
        listTransactionButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        transactionsButtonPanel.add(listTransactionButton);
        
        return transactionsButtonPanel;
    }
    
    /**
     * Helper method to create the account information panel, containing the account list panel
     * and the account list button panel.
     * 
     * @return - the account information panel.
     */
    private JPanel createAccountInformationPanel()
    {
        JPanel accountInformationPanel = new JPanel();
        accountInformationPanel.setLayout(new BoxLayout(accountInformationPanel, BoxLayout.Y_AXIS));
        
        JPanel accountListPanel = createAccountListPanel();
        accountInformationPanel.add(accountListPanel);
        
        JPanel accountListButtonPanel = createAccountListButtonPanel();
        accountInformationPanel.add(accountListButtonPanel);
        accountInformationPanel.setBorder(new TitledBorder("Kontoinformation"));
        
        return accountInformationPanel;
    }

    /**
     * Helper method to create the account list panel, containing the account list with
     * a vertical and horizontal scroll pane.
     * 
     * @return - the account list panel.
     */
    private JPanel createAccountListPanel()
    {
        JPanel accountListPanel = new JPanel();
        accountModelList = new DefaultListModel<String>();
        accountList = new JList<String>(accountModelList);
        accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(accountList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
        accountListPanel.add(scrollPane);
        
        return accountListPanel;
    }
    
    /**
     * Helper method to create the button panel for the account information panel, containing
     * the button list all customers accounts.
     * 
     * @return - button panel containing the list alla customers account button.
     */
    private JPanel createAccountListButtonPanel()
    {
        JPanel accountListButtonPanel = new JPanel();
        accountListButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        listAccountButton = new JButton("Lista konto");
        listAccountButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        accountListButtonPanel.add(listAccountButton);
        
        return accountListButtonPanel;
    }

    /**
     * Helper method to create the panel containing the message area
     * 
     * @return - the panel with the message area.
     */
    private JPanel createMessagePanel()
    {
        JPanel messagetPanel = new JPanel();
        messagetPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        messageArea = new JTextArea(NUMBER_OF_MESSAGE_ROWS, NUMBER_OF_MESSAGE_COLUMNS);
        messageArea.setBackground(getBackground());
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messagetPanel.add(messageArea);
        messagetPanel.setBorder(new TitledBorder(MESSAGE_BORDER_NAME));
        
        return messagetPanel;
    }
    
    /**
     * Helper method to add listeners to all buttons and the customer list.
     */
    private void addGUIListeners()
    {
        // Add button
        // Clears the account id text field and the message area.
        // Creates and adds an account to a customer. When the account
        // is added, the account list is updated to show the new account
        // for the customer in the account information window.
        // Handles number format exception, if a new account could be added.
        addButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearAccountIdTextField();
                clearMessageArea();
                
                try
                {
                    createAccount();
                    listBankAccounts();
                }
                catch (NumberFormatException exception)
                {
                    showErrorMessage("Kan ej lägga till konto", exception.getMessage());
                }
            }
        });
        
        // Remove button
        // Clears the message area.
        // Removes an account from the customers account list and updates
        // the account list to show the existing accounts in the account
        // information window.
        // Clears the account id text field.
        // Creates a dialog window containing information about the removed
        // account.
        // Handles number format exception, if an account could not be removed.
        removeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearMessageArea();
                
                try
                {
                    String accountInformation = removeAccount();
                    listBankAccounts();
                    clearAccountIdTextField();
                    new BankMessageDialog("Konto borttaget", accountInformation);
                }
                catch (NumberFormatException exception)
                {
                    showErrorMessage("Kan ej avsluta konto", exception.getMessage());
                }
            }
        });
        
        // Account information button
        // Clears the message area.
        // Creates a dialog window with the account information for the specific
        // account.
        // Handles number format exception, if account information could not be shown.
        accountInfoButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearMessageArea();
                
                try
                {
                    listAccountInformation();
                }
                catch (NumberFormatException exception)
                {
                    showErrorMessage("Kan ej visa kontoinformation", exception.getMessage());
                }
            }
        });
        
        // Transaction button
        // Clears the message area.
        // Performs the wanted transaction (deposit or withdrawal).
        // After the transaction, the account list in the account information
        // window is updated with the latest transaction.
        // Clears the amount text field.
        // Handles number format exception and unsupported operation exception, if
        // it was not possible to perform the transaction.
        transactionButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearMessageArea();
                
                try
                {
                    performTransaction();
                    listBankAccounts();
                    clearAmountTextField();
                }
                catch (NumberFormatException | UnsupportedOperationException exception)
                {
                    showErrorMessage("Kan ej utföra transaktionen", exception.getMessage());
                }
            }
        });
        
        // Save transaction list button
        // Saves the transaction to an text file if OK-button is clicked. 
        // The location and the name of the text file is chosen with a file 
        // chooser dialog window.
        // Handles unsupported operation, unsupported encoding exception and IO 
        // exception, if transaction for an account could not be saved to file.
        saveTransactionListButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileFilter(FileFormatFilter.TEXT_FILE.getFilter());
                fileChooser.setSelectedFile(new File("ny_transaktion.txt"));
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
                    try
                    {
                        SaveTransactionToTextFile(absolutePath);
                        showInformationMessage("Filen sparad", "Sparande av data till filen " + absolutePath + " lyckades");
                        
                    }
                    catch (FileNotFoundException exception)
                    {
                        showErrorMessage(CAN_NOT_SAVE_TO_FILE_EXCEPTION, exception.getMessage() + absolutePath);
                    }
                    catch (UnsupportedOperationException exception)
                    {
                        showErrorMessage(CAN_NOT_SAVE_TO_FILE_EXCEPTION, exception.getMessage());
                    }
                    catch (UnsupportedEncodingException exception)
                    {
                        showErrorMessage(CAN_NOT_SAVE_TO_FILE_EXCEPTION, exception.getMessage());
                    }
                    catch (IOException exception)
                    {
                        showErrorMessage(CAN_NOT_SAVE_TO_FILE_EXCEPTION, exception.getMessage());
                    }
                }
                
            }
        });
        
        // List transaction button
        // Clears the message area.
        // Lists all transactions for a specific account in the
        // transaction window.
        // Handles number format exception, if it is not possible
        // to list transaction for an account.
        listTransactionButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearMessageArea();
                
                try
                {
                    listAccountTransactions();
                }
                catch (NumberFormatException exception)
                {
                    showErrorMessage("Kan ej lista transaktioner", exception.getMessage());
                }
            }
        });
        
        // List account button
        // Clears the account id text field and the message text area.
        // List all accounts for a specific customer in the account
        // information window.
        // Handles number format exception, if it is not possible to list
        // accounts.
        listAccountButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearAccountIdTextField();
                clearMessageArea();
                
                try
                {
                    listBankAccounts();
                }
                catch (NumberFormatException exception)
                {
                    showErrorMessage("Kan ej lista konton", exception.getMessage());
                }
            }
        });
        
        // Account list
        // Clears the transaction text area
        // Gets the selected account, clears the text message area, sets
        // the account number in the account number text field and changes
        // account type, if necessary, in the account type combo box.
        accountList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                clearTransactionTextArea();
                String accountInformation = accountList.getSelectedValue();
                if (accountInformation != null)
                {
                    clearMessageArea();
                    setAccountNumberFromAccountInformation(accountInformation);
                    setAccountTypeFromAccountInformation(accountInformation);
                }
            }
        });
    }
    
    /**
     * Helper method to clear the account id text field.
     */
    private void clearAccountIdTextField()
    {
        accountIdTextField.setText(EMPTY_STRING);
    }
    
    /**
     * Helper method to clear the message area
     */
    private void clearMessageArea()
    {
        messageArea.setText(EMPTY_STRING);
    }
    
    /**
     * Helper method to create and add an account to a customer
     * Calls a method in logic to create and add an account to
     * an customer using the personal number as input. Sends
     * the result to the message area
     * 
     * @throws NumberFormatException - if personal number contains a
     *                                 not parsable long.
     */
    private void createAccount() throws NumberFormatException
    {
        int accountNumber = NO_CUSTOMERS;
        long pNr = getPersonalNumberFromTextField();
        
        AccountType accountType = (AccountType) accountTypeBox.getSelectedItem();
        if (accountType == AccountType.CREDIT_ACCOUNT)
        {
            accountNumber = bankLogic.addCreditAccount(pNr);
            sendResultToMessageArea(accountType, accountNumber);
        }
        else if (accountType == AccountType.SAVINGS_ACCOUNT)
        {
            accountNumber = bankLogic.addSavingsAccount(pNr);
            sendResultToMessageArea(accountType, accountNumber);
        }
        else
        {
            showErrorMessage("Felaktig kontotyp" , "Programfel");
        }
    }
    
    /**
     * Helper method to get personal number from the personal number
     * text field.
     * 
     * @return - the personal number.
     * @throws NumberFormatException - if the personal number contains a not 
     *                                 parsable long
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
     * Helper method to send the result to the message area.
     * 
     * @param accountType - the type of the account.
     * @param accountNumber - the account number.
     */
    private void sendResultToMessageArea(AccountType accountType, int accountNumber)
    {
        if (accountNumber > NO_CUSTOMERS)
        {
            updateMessageArea(accountType + " med kontonummer " + accountNumber + " har skapats");
        }
        else
        {
            updateMessageArea(accountType + " kunde inte skapas då kund inte finns i registret");
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
     * Helper method to remove an account from a customers account list.
     * Calls methods to get the personal number and account id.
     * Calls a method in logic to remove the account.
     * Updates the message area with the result.
     * 
     * @throws NumberFormatException - if the personal number contains a not 
     *                                 parsable long
     */
    private String removeAccount() throws NumberFormatException
    {
        long pNr = getPersonalNumberFromTextField();
        int accountId = getAccountIdFromTextField();
        
        String accountInformation = bankLogic.closeAccount(pNr, accountId);
        
        if (accountInformation.isEmpty())
        {
            updateMessageArea("Konto med kontonummer " + accountId + " tillhör inte kunden");
        }
        else
        {
            clearAccountList();
            updateMessageArea("Konto med kontonummer " + accountId + " har avslutats");
        }
        
        return accountInformation;
    }
    
    /**
     * Helper method to get the account id from the account id text field.
     * 
     * @return - the account number.
     * @throws NumberFormatException - if the account number contains a not
     *                                 parsable int. An account ID exception
     *                                 message is added to the exception.
     */
    private int getAccountIdFromTextField() throws NumberFormatException
    {
        try
        {
            int accountId = Integer.parseInt(accountIdTextField.getText());
            return accountId;
        }
        catch (NumberFormatException exception)
        {
            throw new NumberFormatException(ACCOUNT_ID_EXCEPTION_MESSAGE);
        }
    }
    
    /**
     * Helper method to clear the account list.
     */
    private void clearAccountList()
    {
        accountList.clearSelection();
        accountModelList.removeAllElements();
    }
    
    /**
     * Helper method to list a account information from one account
     * in a separate dialog window.
     * Creates a message dialogue window with account information for
     * the specific account. If the account not belongs to the 
     * customer, information that the account does not belong to the 
     * customer is shown in the message area.
     */
    private void listAccountInformation()
    {
        long pNr = getPersonalNumberFromTextField();
        int accountId = getAccountIdFromTextField();
        
        String accountInformation = bankLogic.getAccount(pNr, accountId);
        
        if (accountInformation.isEmpty())
        {
            updateMessageArea("Konto med kontonummer " + accountId + " tillhör inte kunden");
        }
        else
        {
            new BankMessageDialog("Kontoinformation (kontonummer: " + accountId + ")", accountInformation);
        }
    }
    
    /**
     * Clears and update the message area with the new text.
     * 
     * @param message - the message to be set in the message area.
     */
    private void updateMessageArea(String message)
    {
        messageArea.setText(EMPTY_STRING);
        messageArea.setText(message);
    }
    
    /**
     * Helper method add or withdraw an amount from a specific account.
     * Updates the message area with the result.
     * 
     * @throws NumberFormatException - if the personal number contains a not 
     *                                 parsable long, account number contains
     *                                 a not parsable int or amount contains a
     *                                 not parsable double.
     *                                 
     * @throws UnsupportedOperationException - if transaction type is not supported.
     */
    private void performTransaction() throws NumberFormatException, UnsupportedOperationException
    {
        long pNr = getPersonalNumberFromTextField();
        int accountId = getAccountIdFromTextField();
        double amount = getAmountFromTextField();
        
        boolean isTransactionSuccessful = isTransactionSuccessful(pNr, accountId, amount);
        if (isTransactionSuccessful)
        {
            updateMessageArea("Transaktionen för konto " + accountId + " lyckades");
        }
        else
        {
            updateMessageArea("Transaktionen för konto " + accountId + " misslyckades");
        }
    }
    
    /**
     * Helper method to clear the amount text field.
     */
    private void clearAmountTextField()
    {
        amountTextField.setText(EMPTY_STRING);
    }
    
    /**
     * Helper method to get amount from the amount text field.
     * 
     * @return - the amount.
     * @throws NumberFormatException - if the amount contains a not
     *                                 parsable double.
     */
    private double getAmountFromTextField() throws NumberFormatException
    {
        try
        {
            double amount = Double.parseDouble(amountTextField.getText());
            return amount;
        }
        catch (NumberFormatException exception)
        {
            throw new NumberFormatException(AMOUNT_EXCEPTION_MESSAGE);
        }
    }
    
    /**
     * Helper method to call logic to perform a specific transaction depending on the
     * transaction type.
     * 
     * @param pNr - the personal number
     * @param accountId - the account id
     * @param amount - the amount to deposit or withdraw
     * @return - the result of the transaction.
     * @throws UnsupportedOperationException - if the transaction type is not supported.
     */
    private boolean isTransactionSuccessful(long pNr, int accountId, double amount) throws UnsupportedOperationException
    {
        TransactionType transactionType = (TransactionType) transactionTypeBox.getSelectedItem();
        if (transactionType == TransactionType.DEPOSIT)
        {
            return bankLogic.deposit(pNr, accountId, amount);
        }
        else if (transactionType == TransactionType.WITHDRAWAL)
        {
            return bankLogic.withdraw(pNr, accountId, amount);
        }
        else
        {
            throw new UnsupportedOperationException(TRANSACTION_TYPE_EXCEPTION_MESSAGE);
        }
    }

    /**
     * Helper method to save account transaction information to file.
     * 
     * @param absolutePath - the absolute path to file where the data should be saved.
     * @throws UnsupportedOperationException - if the file is not allowed to modify.
     * @throws FileNotFoundException - if the file could not be found or opened.
     * @throws UnsupportedEncodingException - if encoding could not be supported.
     * @throws IOException - I/O fault when saving data to binary file.
     */
    private void SaveTransactionToTextFile(String absolutePath) throws UnsupportedOperationException, FileNotFoundException, 
                                                                        UnsupportedEncodingException, IOException
    {
        String accountTransaction = transactionArea.getText();
        if (accountTransaction.isEmpty())
        {
            updateMessageArea("Inga transaktioner finns listade");
            throw new UnsupportedOperationException("Inga transaktioner finns listade");
        }
        else
        {
            saveToTextFile(absolutePath, accountTransaction);
        }
    }

    /**
     * Helper method to write and save a string to a text file.
     * 
     * @param absolutePath - the absolute path to file where the data should be saved.
     * @param accountTransaction - the account transaction information.
     * @throws UnsupportedOperationException - if the file is not allowed to modify.
     * @throws FileNotFoundException - if the file could not be found or opened.
     * @throws UnsupportedEncodingException - if encoding could not be supported.
     * @throws IOException - I/O fault when saving data to binary file.
     */
    private void saveToTextFile(String absolutePath, String accountTransaction) throws UnsupportedOperationException, FileNotFoundException, 
                                                                                        UnsupportedEncodingException, IOException
    {
        absolutePath = setFileNameExtensionToTxt(absolutePath);
        File file = createFile(absolutePath);
        
        if (!file.canWrite())
        {
            throw new UnsupportedOperationException(WRITE_TO_FILE_EXCEPTION_MESSAGE);
        }
        
        try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1")))
        {
            out.write("Filen sparades: " + TimeStamp.getCurrentYearDateAndTime() + NEWLINE);
            out.write(accountTransaction);
            out.close();
        }
        catch (FileNotFoundException exception)
        {
            throw new FileNotFoundException(CAN_NOT_FIND_FILE_EXCEPTION_MESSAGE);
        }
        catch (UnsupportedEncodingException exception)
        {
            throw new UnsupportedEncodingException(CODING_FAULT_EXCEPTION_MESSAGE);
        }
        catch (IOException exception)
        {
            throw new IOException(IO_FAULT_EXCEPTION_MESSAGE);
        }
    }
    
    /**
     * Helper method to set the file name extension to .txt, if needed, for
     * the text file.
     * 
     * @param fileName - the file name of the text file
     * @return - the file name with the file name extension .txt
     */
    private String setFileNameExtensionToTxt(String fileName)
    {
        if (!fileName.endsWith(TEXT_FILE_NAME_EXTENSION))
        {
            fileName += TEXT_FILE_NAME_EXTENSION;
        }
        
        return fileName;
    }
    
    /**
     * Helper method to create new file if the file does not exist.
     * 
     * @param absolutePath - the absolute path to the file
     * @return - the file.
     * @throws IOException - if a new file could not be created.
     */
    private File createFile(String absolutePath) throws IOException
    {
        File file = new File(absolutePath);
        if (!file.exists())
        {
            file.createNewFile();
        }
        
        return file;
    }
    
    /**
     * Helper method to a open a information message window
     * 
     * @param title - the title of the information.
     * @param message - the information message.
     */
    private void showInformationMessage(String title, String message)
    {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Helper method to list all transactions for one account.
     * Uses personal number and account id, read from the text fields.
     * @throws NumberFormatException - if the personal number contains a not parsable long or
     *                                 account number contains of a not parsable int.
     */
    private void listAccountTransactions() throws NumberFormatException
    {
        long pNr = getPersonalNumberFromTextField();
        int accountId = getAccountIdFromTextField();
        
        ArrayList<String> transactions = bankLogic.getTransactions(pNr, accountId);
        if (transactions.isEmpty())
        {
            updateMessageArea("Konto med kontonummer " + accountId + " tillhör inte kunden");
        }
        else
        {
            updateTransactionTextArea(transactions);
        }
    }
    
    /**
     * Helper method to update the transaction area with text from an list.
     * Clears the text area before new text is written.
     * 
     * @param transactions - the list to write in the transaction area.
     */
    private void updateTransactionTextArea(ArrayList<String> transactions)
    {
        clearTransactionTextArea();
        for(String transaction : transactions)
        {
            transactionArea.append(transaction + NEWLINE);
        }
    }
    
    /**
     * Helper method to clear the transaction area
     */
    private void clearTransactionTextArea()
    {
        transactionArea.setText(EMPTY_STRING);
    }
    
    /**
     * Helper method to list all accounts for a customer.
     * Uses the personal number, read from the personal number text field
     * to get all account information for a customer
     * 
     * @throws NumberFormatException - if the personal number contains a
     *                                 not parsable.
     */
    private void listBankAccounts() throws NumberFormatException
    {
        long pNr = getPersonalNumberFromTextField();
        
        ArrayList<String> accounts = bankLogic.getCustomer(pNr);
        if (accounts.isEmpty())
        {
            clearAccountList();
            updateMessageArea("Kunden finns ej i registret");
        }
        else
        {
            updateAccountListArea(accounts);
        }
    }
    
    /**
     * Helper method to update the account list with all accounts
     * for a customer.
     * @param accounts - the list with all accounts for a customer.
     */
    private void updateAccountListArea(ArrayList<String> accounts)
    {
        clearAccountList();
        for (String account : accounts)
        {
            accountModelList.addElement(account);
        }
    }
    
    /**
     * Helper method to set the account number text field with an account number
     * from the account information string.
     * 
     * @param accountInformation - the string containing information about the account.
     */
    private void setAccountNumberFromAccountInformation(String accountInformation)
    {
        String accountNumber = getAccountIdFromAccountInformation(accountInformation);
        accountIdTextField.setText(accountNumber);
    }
    
    /**
     * Helper method to get the account id from the account information.
     * 
     * @param accountInformation - the string containing information about the account.
     * @return - the account id. Returns an empty string if the string "Kontonummer: " can
     *           be found in the account information string.
     */
    private String getAccountIdFromAccountInformation(String accountInformation)
    {
        if (accountInformation.contains(ACCOUNT_NUMBER_SEARCH_NUMBER))
        {
            int accountIdStartPos = accountInformation.indexOf(ACCOUNT_NUMBER_SEARCH_NUMBER) + ACCOUNT_NUMBER_SEARCH_NUMBER.length();
            int accountIdEndPos = accountInformation.indexOf(',', accountIdStartPos);
            String accountId = accountInformation.substring(accountIdStartPos, accountIdEndPos);
            
            return accountId;
        }
        else
        {
            return EMPTY_STRING;
        }
    }
    
    /**
     * Helper method to set the account type combo box with help of the information
     * in the account information string.
     * 
     * @param accountInformation - the string containing information about the account.
     */
    private void setAccountTypeFromAccountInformation(String accountInformation)
    {
        String accountType = getAccountTypeFromAccountInformation(accountInformation);
        if (accountType.equals(AccountType.CREDIT_ACCOUNT.toString()))
        {
            accountTypeBox.setSelectedItem(AccountType.CREDIT_ACCOUNT);
        }
        else if (accountType.equals(AccountType.SAVINGS_ACCOUNT.toString()))
        {
            accountTypeBox.setSelectedItem(AccountType.SAVINGS_ACCOUNT);
        }
    }
    
    /**
     * Helper method to get the account type from the account information.
     * 
     * @param accountInformation - the string containing information about the account.
     * @return - the account type. Returns an empty string if the string "Typ av konto:  " can
     *           be found in the account information string.
     */
    private String getAccountTypeFromAccountInformation(String accountInformation)
    {
        if (accountInformation.contains(ACCOUNT_TYPE_SEARCH_WORD))
        {
            int accountTypeStartPos = accountInformation.indexOf(ACCOUNT_TYPE_SEARCH_WORD) + ACCOUNT_TYPE_SEARCH_WORD.length();
            int accountTypeEndPos = accountInformation.indexOf(',', accountTypeStartPos);
            String accountType = accountInformation.substring(accountTypeStartPos, accountTypeEndPos);
            
            return accountType;
        }
        else
        {
            return EMPTY_STRING;
        }
    }
    
    /**
     * Sets the personal number in the personal number text field.
     * Clears account number field, transaction field, account list
     * and message area.
     * 
     * @param pNr - the personal number.
     */
    public void setNewPersonalNumberInAccountPanel(String pNr)
    {
        pNrTextField.setText(pNr);
        clearAccountIdTextField();
        clearAccountList();
        clearTransactionTextArea();
        clearMessageArea();
        
    }
}
