/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-12-01
 * Last Updated: 2015-12-26, Gunnar Eriksson
 * Description: Is the GUI for the bank application
 * 2015-12-26: Added menu and logic to open a transaction 
 *             text file.
 *             Added logic to open and save binary file
 *             containing customer register and the latest
 *             assigned account number.
 * Version: Second version.
 */
package se.gunnareriksson.bank.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import se.gunnareriksson.bank.customer.Customer;
import se.gunnareriksson.bank.logic.BankLogic;
import se.gunnareriksson.bank.numbergenerator.AccountNumberGenerator;

public class BankGUI extends JFrame
{
    private static final long serialVersionUID = -5007747993530667408L;
    private static final String SUPPORTED_CHAR_SET = "ISO-8859-1";
    private static final String NEWLINE = "\n";
    private static final String EMPTY_STRING = "";
    private static final String BANK_GUI_TITLE = "Bank";
    private static final String MENU_BAR_ARCHIVES = "Arkiv";
    private static final String MENU_BAR_TRANSACTION = "Transaktioner";
    private static final String MENU_ITEM_OPEN_FILE = "Öppna";
    private static final String MENU_ITEM_SAVE = "Spara som";
    private static final String PATH_SEPARATOR = "\\";
    private static final String CAN_NOT_SAVE_FILE_EXCEPTION = "Kan ej spara till fil";
    private static final String CAN_NOT_OPEN_FILE_EXCEPTION = "Kan ej öppna filen";
    private static final String CAN_NOT_CONVERT_DATA_EXCEPTION = "Kan inte konvertera data";
    private static final String CAN_NOT_FIND_FILE_EXCEPTION_MESSAGE = "Hittar inte filen med sökvägen: ";
    private static final String WRITE_TO_FILE_EXCEPTION_MESSAGE = "Filen är skrivskyddad";
    private static final String CORRUPT_DATA_EXCEPTION_MESSAGE = "Filen innehåller korrupt data";
    private static final String DECODING_EXCEPTION_MESSAGE = "Filen kan ej avkodas";
    private static final String TRANSLATION_EXCEPTION_MESSAGE = "Fel i översättningen av data";
    
    private static enum FileContentType {LAST_ASSIGNED_ACCOUNT_NUMBER, BANK_CUSTOMERS};
    
    private BankLogic bankLogic;
    private BankCustomerPanel bankCustomerPanel;
    private BankAccountPanel bankAccountPanel;
    private JFileChooser fileChooser;
    
    private JMenuItem openArchiveFileMenuItem, saveArchiveFileMenuItem, openTransactionFileMenuItem;
    
    /**
     * Constructor
     * Creates the bank logic, sets the title, initialize the GUI
     * and sets the default close operation.
     */
    public BankGUI()
    {
        bankLogic = new BankLogic();
        createFileChooserSwedishLanguage();
        setTitle(BANK_GUI_TITLE);
        initializeGUI();
        addMenuItemListeners();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Helper method to initialize the GUI with menu bar, customer information
     * panel and account information panel. Sets the width, height and makes
     * the GUI not resizable.
     */
    private void initializeGUI()
    {
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        bankAccountPanel = new BankAccountPanel(bankLogic, fileChooser);
        bankCustomerPanel = new BankCustomerPanel(bankLogic, bankAccountPanel);
        JTabbedPane bankGUI = new JTabbedPane();
        bankGUI.add("Kundinformation", bankCustomerPanel);
        bankGUI.add("Kontoinformation", bankAccountPanel);
        add(bankGUI);
        this.pack();
        setResizable(false);
    }

    /**
     * Helper method to create the menu bar
     * 
     * @return - the menu bar
     */
    private JMenuBar createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu archiveMenu =  new JMenu(MENU_BAR_ARCHIVES);
        createArchiveMenuItems(archiveMenu);
        menuBar.add(archiveMenu);
        
        JMenu transactionMenu = new JMenu(MENU_BAR_TRANSACTION);
        createTransactionMenuItem(transactionMenu);
        menuBar.add(transactionMenu);
        
        return menuBar;
    }
    
    /**
     * Helper method to create archive menu items 
     * open file and save file
     * 
     * @param menu - the menu bar to add menu items.
     */
    private void createArchiveMenuItems(JMenu menu)
    {
        saveArchiveFileMenuItem = new JMenuItem(MENU_ITEM_SAVE);
        menu.add(saveArchiveFileMenuItem);
        
        openArchiveFileMenuItem = new JMenuItem(MENU_ITEM_OPEN_FILE);
        menu.add(openArchiveFileMenuItem);
    }
    
    /**
     * Helper method to create transaction menu item 
     * to open a transaction file.
     * 
     * @param menu - the menu bar to add menu items.
     */
    private void createTransactionMenuItem(JMenu menu)
    {
        openTransactionFileMenuItem = new JMenuItem(MENU_ITEM_OPEN_FILE);
        menu.add(openTransactionFileMenuItem);
    }
    
    /**
     * Helper method to set the file chooser to Swedish language.
     */
    private void createFileChooserSwedishLanguage()
    {
        fileChooser = new JFileChooser();
        UIManager.put("FileChooser.acceptAllFileFilterText", "Alla filer");
        UIManager.put("FileChooser.cancelButtonText", "Ångra");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Stäng filhanteraren");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Detaljerad lista");
        UIManager.put("FileChooser.fileNameLabelText", "Filnamn:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Filformat");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
        UIManager.put("FileChooser.newFolderToolTipText", "Skapa ny katalog");
        UIManager.put("FileChooser.openButtonText", "Öppna");
        UIManager.put("FileChooser.openButtonToolTipText", "Öppna vald fil");
        UIManager.put("FileChooser.openDialogTitleText", "Öppna");
        UIManager.put("FileChooser.lookInLabelText", "Sök i:");
        UIManager.put("FileChooser.saveButtonText", "Spara");
        UIManager.put("FileChooser.saveButtonToolTipText", "Spara vald fil");
        UIManager.put("FileChooser.saveDialogTitleText", "Spara");
        UIManager.put("FileChooser.saveInLabelText", "Spara i:");
        UIManager.put("FileChooser.upFolderToolTipText", "Upp en nivå");
        
        SwingUtilities.updateComponentTreeUI(fileChooser);
        fileChooser.addChoosableFileFilter(FileFormatFilter.TEXT_FILE.getFilter());
    }
    
    private void addMenuItemListeners()
    {
        // Save archive file menu item
        // Opens a file chooser dialog and if the save button is clicked
        // the bank customer register and latest assigned account number
        // is saved to a binary file. The result is shown in the text
        // message area.
        // Handles file not found exceptions, if file could not be found.
        // Handles I/O exception if an I/O fault occurs when saving the file.
        // Handles unsupported operation exception if the file could not be
        // modified.
        // At exception, the fault is shown in a separate window.
        saveArchiveFileMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fileChooser.setAcceptAllFileFilterUsed(true);
                fileChooser.removeChoosableFileFilter(FileFormatFilter.TEXT_FILE.getFilter());
                fileChooser.setSelectedFile(new File("nytt_kundregister.bin"));
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
                    try
                    {
                        saveToArchive(absolutePath);
                        showInformationMessage("Filen sparad", "Sparande av data till filen " + absolutePath + " lyckades");
                    }
                    catch (FileNotFoundException exception)
                    {
                        showErrorMessage(CAN_NOT_SAVE_FILE_EXCEPTION, exception.getMessage() + absolutePath);
                    }
                    catch (UnsupportedOperationException exception)
                    {
                        showErrorMessage(CAN_NOT_SAVE_FILE_EXCEPTION, exception.getMessage());
                    }
                    catch (IOException exception)
                    {
                        showErrorMessage(CAN_NOT_SAVE_FILE_EXCEPTION, exception.getMessage());
                    }
                }
            }
        });
        
        // Open archive file menu item
        // Opens a file chooser and if the open button is clicked, it reads the
        // latest assigned account number and the bank customer register from a
        // binary file. Existing last assigned account number and bank customer
        // register are overwritten with the values from the file. The result
        // is shown in the text message area.
        // Handles file not found exceptions, if file could not be found or opened.
        // Handles I/O exception if an I/O fault occurs when opening the file.
        // Handles class not found exception, if there is problem with translation
        // data.
        // Handles class cast exception, if data from file could not be casted.
        // At exception, the fault is shown in a separate window.
        openArchiveFileMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fileChooser.setAcceptAllFileFilterUsed(true);
                fileChooser.removeChoosableFileFilter(FileFormatFilter.TEXT_FILE.getFilter());
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
                    try
                    {
                        reloadBankProgramFromFile(absolutePath);
                        showInformationMessage("Filen läst", "Läsning av filen " + absolutePath + " lyckades");
                    }
                    catch (FileNotFoundException exception)
                    {
                        showErrorMessage(CAN_NOT_OPEN_FILE_EXCEPTION, exception.getMessage() + absolutePath);
                    }
                    catch (ClassNotFoundException exception)
                    {
                        showErrorMessage(CAN_NOT_OPEN_FILE_EXCEPTION, exception.getMessage());
                    }
                    catch (ClassCastException exception)
                    {
                        showErrorMessage(CAN_NOT_OPEN_FILE_EXCEPTION, exception.getMessage());
                    }
                    catch (IOException exception)
                    {
                        showErrorMessage(CAN_NOT_OPEN_FILE_EXCEPTION, exception.getMessage());
                    }
                }
            }
        });
        
        // Open transaction file menu item.
        // Open a file chooser and if the open button is clicked, it reads
        // account transaction data from a text file according to 
        // standard ISO-8859-1.
        // The account transaction data is shown in a separate dialog window.
        // Handles unsupported encoding exceptions, if the file could not
        // be encoded.
        // Handles file not found exception, if the file could not be found or
        // opened.
        // Handles I/O exception, if an I/O exception occurs when reading the
        // file.
        // At exception, the fault is shown in a separate window.
        openTransactionFileMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileFilter(FileFormatFilter.TEXT_FILE.getFilter());
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
                    try
                    {
                        readTransactionTextFile(absolutePath);
                    }
                    catch (FileNotFoundException exception)
                    {
                        showErrorMessage(CAN_NOT_OPEN_FILE_EXCEPTION, exception.getMessage() + absolutePath);
                    }
                    catch (UnsupportedEncodingException exception)
                    {
                        showErrorMessage(CAN_NOT_OPEN_FILE_EXCEPTION, exception.getMessage());
                    }
                    catch (IOException exception)
                    {
                        showErrorMessage(CAN_NOT_OPEN_FILE_EXCEPTION, exception.getMessage());
                    }
                }
            }
        });
    }
    
    /**
     * Helper method to save data to archive.
     * 
     * @param absolutePath - the absolute path to file where the data should be saved.
     * 
     * @throws FileNotFoundException - if file could not be found or opened.
     * @throws UnsupportedOperationException - if file could not be modified.
     * @throws IOException - I/O fault when saving data to binary file.
     */
    private void saveToArchive(String absolutePath) throws FileNotFoundException, UnsupportedOperationException, IOException
    {
        int lastAssignedAccountNumber = AccountNumberGenerator.getLastAssignedAccountNumber();
        LinkedHashMap<Long, Customer> customers = bankLogic.getCustomerMap();
        saveToBinaryFile(lastAssignedAccountNumber, customers, absolutePath);
    }
    
    /**
     * Helper method to save last assigned account number and all customers in archive to a binary file.
     * 
     * @param lastAssignedAccountNumber - the last assigned account number.
     * @param customers - all customers in the register
     * @param absolutePath - the absolute path to file where the data should be saved.
     * @throws FileNotFoundException - if file could not be found or opened.
     * @throws IOException - I/O fault when saving data to binary file.
     * @throws UnsupportedOperationException - if file could not be modified.
     */
    private void saveToBinaryFile(Object lastAssignedAccountNumber, Object customers, String absolutePath) throws FileNotFoundException, UnsupportedOperationException, IOException
    {
        File file = createFile(absolutePath);
        if (!file.canWrite())
        {
            throw new UnsupportedOperationException(WRITE_TO_FILE_EXCEPTION_MESSAGE);
        }
        
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file))))
        {
            out.writeObject(lastAssignedAccountNumber);
            out.writeObject(customers);
            out.close();
        }
        catch (FileNotFoundException exception)
        {
            throw new FileNotFoundException(CAN_NOT_FIND_FILE_EXCEPTION_MESSAGE);
        }
        catch (IOException exception)
        {
            throw new IOException(CORRUPT_DATA_EXCEPTION_MESSAGE);
        }
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
     * Helper method to reload the bank program from file. Reads the latest assigned account number from
     * file and reloads the account number generator with the value. Reads customer register and overwrite
     * the register in the program.
     * 
     * @param absolutePath - the absolute path to the archive file.
     * @throws FileNotFoundException - if the file could not be found or opened.
     * @throws ClassNotFoundException - if class of a serialized object cannot be found.
     * @throws IOException - I/O fault when reading the file.
     */
    private void reloadBankProgramFromFile(String absolutePath) throws FileNotFoundException, ClassNotFoundException, IOException
    {
        Map<FileContentType, Object> fileContentMap = readBinaryFile(absolutePath);
        int lastAssignedAccountNumber = getLastAssignedAccountNumber(fileContentMap);
        LinkedHashMap<Long, Customer> customers = getAllCustomersInRegister(fileContentMap);
        reloadBankProgram(lastAssignedAccountNumber, customers);
        
        bankCustomerPanel.clearCustomerPanel();
        bankCustomerPanel.listAllCustomersInCustomerInformationPanel();
    }
    
    /**
     * Helper method to read information from a binary file.
     * 
     * @param absolutePath - the absolute path to the archive file.
     * @return - A map containing last assigned account number object and bank customer register object.
     * @throws FileNotFoundException - if the file could not be found or opened.
     * @throws IOException - I/O fault when reading the file.
     * @throws ClassNotFoundException - if class of a serialized object cannot be found.
     */
    private Map<FileContentType, Object> readBinaryFile(String absolutePath) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        Map<FileContentType, Object> objectMap = new HashMap<FileContentType, Object>();
        
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(absolutePath))))
        {
            objectMap.put(FileContentType.LAST_ASSIGNED_ACCOUNT_NUMBER, in.readObject());
            objectMap.put(FileContentType.BANK_CUSTOMERS, in.readObject());
            in.close();
        }
        catch (FileNotFoundException exception)
        {
            throw new FileNotFoundException(CAN_NOT_FIND_FILE_EXCEPTION_MESSAGE);
        }
        catch (ClassNotFoundException exception)
        {
            throw new ClassNotFoundException(TRANSLATION_EXCEPTION_MESSAGE);
        }
        catch (IOException exception)
        {
            throw new IOException(CORRUPT_DATA_EXCEPTION_MESSAGE);
        }
        
        return objectMap;
    }
    
    /**
     * Helper method to get last assigned account number from an last assigned account number object.
     * 
     * @param fileContentMap - the map containing the last assigned account number object.
     * @return - the last assigned account number.
     */
    private int getLastAssignedAccountNumber(Map<FileContentType, Object> fileContentMap)
    {
        Object lastAssignedAccountNumberObject = fileContentMap.get(FileContentType.LAST_ASSIGNED_ACCOUNT_NUMBER);
        int lastAssignedAccountNumber = convertObjectToInteger(lastAssignedAccountNumberObject);
        
        return lastAssignedAccountNumber;
    }
    
    /**
     * Helper method to convert an object in to an integer.
     * 
     * @param obj - the object containing the integer.
     * @return - the integer in the integer object.
     * @throws ClassCastException - if the object could not be cast to an integer.
     */
    private int convertObjectToInteger(Object obj) throws ClassCastException
    {
        try
        {
            return (int) obj;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(CAN_NOT_CONVERT_DATA_EXCEPTION);
        }
    }
    
    /**
     * Helper method to get all customers in register from customer register object.
     * 
     * @param fileContentMap - the map containing the register with all bank customers.
     * @return - the map containing the bank customer register.
     */
    private LinkedHashMap<Long, Customer> getAllCustomersInRegister(Map<FileContentType, Object> fileContentMap)
    {
        Object customersObject = fileContentMap.get(FileContentType.BANK_CUSTOMERS);
        LinkedHashMap<Long, Customer> customers = convertObjectToLinkedHashMap(customersObject);
        
        return customers;
    }
    
    /**
     * Helper method reload the bank program. Overwrites the bank customer register and the last
     * assigned account number.
     * 
     * @param lastAssignedAccountNumber - the last assigned account number.
     * @param customers - the bank customer register.
     */
    private void reloadBankProgram(int lastAssignedAccountNumber, LinkedHashMap<Long, Customer> customers)
    {
        bankLogic.setCustomerMap(customers);
        AccountNumberGenerator.setLastAssignedAccountNumber(lastAssignedAccountNumber);
    }
    
    /**
     * Helper method to convert an object to a linked hash map containing an integer
     * a customer.
     * 
     * @param obj - the object containing the linked hash map with an integer and
     *              a customer.
     * @return - the hash map of the type integer and customer
     * @throws ClassCastException - if the object could not be cast to a linked
     *                              hash map of type integer and customer.
     */
    @SuppressWarnings ("unchecked")
    private LinkedHashMap<Long, Customer> convertObjectToLinkedHashMap(Object obj) throws ClassCastException
    {
        try
        {
            return (LinkedHashMap<Long, Customer>) obj;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(CAN_NOT_CONVERT_DATA_EXCEPTION);
        }
    }
    
    /**
     * Helper method to read transaction information from text file according to the
     * standard ISO-8859-1.
     * The transaction information is presented in a separate dialog window.
     * 
     * @throws UnsupportedEncodingException - If the named charset is not supported
     * @throws FileNotFoundException - If the file could not be found or read.
     * @throws IOException - If an I/O exception occurs when reading the buffer.
     */
    private void readTransactionTextFile(String absolutePath) throws UnsupportedEncodingException, FileNotFoundException, IOException
    {
        String message = getMessageFromFile(absolutePath);
        String fileName = getFileNameFromAbsolutePath(absolutePath);
        new BankMessageDialog(this, fileName, message);
    }
    
    /**
     * Helper method that reads message from file according to the standard ISO-8859-1.
     * 
     * @param absolutePath - the absolute path to the file that should be read.
     * @return - the message.
     *           An empty string if the file does not contain any information.
     * @throws UnsupportedEncodingException - If the named charset is not supported
     * @throws FileNotFoundException - If the file could not be found or read.
     * @throws IOException - If an I/O exception occurs when reading the buffer.
     */
    private String getMessageFromFile(String absolutePath) throws UnsupportedEncodingException, FileNotFoundException, IOException
    {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(absolutePath), SUPPORTED_CHAR_SET)))
        {
            String message = getMessageFromBuffer(in);
            in.close();
            
            return message;
        }
        catch (UnsupportedEncodingException exception)
        {
            throw new UnsupportedEncodingException(DECODING_EXCEPTION_MESSAGE);
        }
        catch (FileNotFoundException exception)
        {
            throw new FileNotFoundException(CAN_NOT_FIND_FILE_EXCEPTION_MESSAGE);
        }
        catch (IOException exception)
        {
            throw new IOException(CORRUPT_DATA_EXCEPTION_MESSAGE);
        }
    }
    
    /**
     * Helper method to get message from buffer.
     * 
     * @param buffer - the buffer
     * @return - the message in the buffer.
     *           An empty string if the buffer does not contains any information.
     * @throws IOException - If an I/O exception occurs when reading the buffer.
     */
    private String getMessageFromBuffer(BufferedReader buffer) throws IOException
    {
        String message = EMPTY_STRING;
        String sCurrentLine;
        while ((sCurrentLine = buffer.readLine()) != null) 
        {
            message += sCurrentLine;
            message += NEWLINE;
        }
        
        return message;
    }
    
    /**
     * Helper method to get the file name from the absolute path
     * @param absolutePath - the absolute path to the file
     * @return - the name of the file if the path is valid.
     *           An empty string if path is invalid.
     */
    private String getFileNameFromAbsolutePath(String absolutePath)
    {
        if (absolutePath.contains(PATH_SEPARATOR))
        {
            int pNrStartPos = absolutePath.lastIndexOf(PATH_SEPARATOR) + PATH_SEPARATOR.length();
            String fileName = absolutePath.substring(pNrStartPos);
            
            return fileName;
        }
        else
        {
            return EMPTY_STRING;
        }
    }
}
