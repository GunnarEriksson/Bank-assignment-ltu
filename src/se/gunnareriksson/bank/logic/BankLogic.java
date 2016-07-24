/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-09-25
 * Last Updated: 2015-12-25, Gunnar Eriksson
 * Description: Handles customers and their accounts
 * 2015-10-25: Added the methods addCreditAccount
 *             and getTransactions. Created a new helper method gets
 *             the customer from the customer Id. The helper method
 *             is used in several methods.
 * 2015-12-25: Added get and set method for the customer register.
 *             The method makes it possible to get the customer
 *             register and save it to file. The set method makes
 *             it possible to reload the program with an external
 *             customer register.
 * Version: Third version
 */
package se.gunnareriksson.bank.logic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.gunnareriksson.bank.customer.Customer;
import se.gunnareriksson.bank.exceptions.CustomerNotFoundException;

public class BankLogic
{
    private Map<Long, Customer> customers;

    /**
     * Constructor
     * Creates map to store the customers
     */
    public BankLogic()
    {
        customers = new LinkedHashMap<Long, Customer>();
    }

    /**
     * Creates and add a customer, if the customer does
     * not already exists.
     * @param name - the customer's name
     * @param pNr - the customer's unique personal identity number
     * @return <code>true</code> if customer is added.
     *         <code>false</code> if customer already exists.
     */
    public boolean addCustomer(String name, long pNr)
    {
        Customer customer = customers.get(pNr);
        if (customer == null)
        {
            customers.put(pNr, new Customer(name, pNr));
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Removes customer, if found. Returns information about the
     * customer and all customer accounts and with the calculated interest.
     * @param pNr - the unique personal identity number for the customer to be removed
     * @return - list, which contains information about the customer and all
     *           customer accounts with each heading. Returns information about
     *           customer only, if no accounts found. Returns and empty list, if
     *           no customer found.
     */
    public ArrayList<String> removeCustomer(long pNr)
    {
        ArrayList<String> customerAndAccountsInformation = new ArrayList<String>();

        Customer customer = customers.remove(pNr);
        if (customer != null)
        {
            customerAndAccountsInformation.add(customer.getCustomerInformation());
            List<String> accountsInformation = customer.closeAllCustomerAccounts();
            addAccountsInformationToCustomerList(accountsInformation, customerAndAccountsInformation);
        }
        return customerAndAccountsInformation;
    }

    /**
     * Helper method to add account information to the customer and account information list.
     * @param accountsInformation - the list containing information about all customer accounts.
     * @param customerAndAccountsInformation - the list, which information about the accounts should be
     *                                         customer information. If no accounts found, no information
     *                                         is added.
     */
    private void addAccountsInformationToCustomerList(List<String> accountsInformation, List<String> customerAndAccountsInformation)
    {
        for (String accountInformation : accountsInformation)
        {
            customerAndAccountsInformation.add(accountInformation);
        }
    }

    /**
     * Changes customer name, if customer is found.
     * @param name - the customer's new name.
     * @param pNr - the customer's unique personal identity number.
     * @return <code>true</code> if name is changed.
     *         <code>false</code> customer can not be found.
     */
    public boolean changeCustomerName(String name, long pNr)
    {
        try
        {
            Customer customer = getCustomerFromId(pNr);
            customer.setName(name);
            return true;
        }
        catch (CustomerNotFoundException e)
        {
            return false;
        }
    }
    
    /**
     * Helper method to get customer from personal identity number
     * @param pNr - Unique personal identity number
     * @return - the customer
     * @throws CustomerNotFoundException - if customer not found
     */
    private Customer getCustomerFromId(long pNr) throws CustomerNotFoundException
    {
        Customer customer = customers.get(pNr);
        if (customer != null)
        {
            return customer;
        }
        else
        {
            throw new CustomerNotFoundException();
        }
    }

    /**
     * Creates savings account for the customer and returns the result.
     * @param pNr - customer's unique personal identity number
     * @return - the account number of the account. Returns -1 if customer
     *           not found.
     */
    public int addSavingsAccount(long pNr)
    {
        try
        {
            Customer customer = getCustomerFromId(pNr);
            return customer.createSavingsAccount();
        }
        catch (CustomerNotFoundException e)
        {
            return  -1;
        }
    }
    
    /**
     * Creates credit account for the customer and returns the result.
     * @param pNr - customer's unique personal identity number
     * @return - the account number of the account. Returns -1 if customer
     *           not found.
     */
    public int addCreditAccount(long pNr)
    {
        try
        {
            Customer customer = getCustomerFromId(pNr);
            return customer.createCreditAccount();
        }
        catch (CustomerNotFoundException e)
        {
            return -1;
        }
    }

    /**
     * Closes the account for the customer and returns the result.
     * @param pNr - customer's unique personal identity number
     * @param accountId - the account number for the account to close
     * @return - account information and calculated interest for the closed account
     *           with each heading. An empty string if no account is found.
     */
    public String closeAccount(long pNr, int accountId)
    {
        try
        {
            Customer customer = getCustomerFromId(pNr);
            return customer.closeCustomerAccount(accountId);
        }
        catch (CustomerNotFoundException e)
        {
            return "";
        }
    }

    /**
     *
     * @param pNr - customer's unique personal identity number
     * @param accountId -the account number for the account to add an amount
     * @param amount - the amount to add to the customer account.
     * @return <code>true</code> if the amount is added to the account
     *         <code>false</code> if the account is not found
     */
    public boolean deposit(long pNr, int accountId, double amount)
    {
        try
        {
            Customer customer = getCustomerFromId(pNr);
            return customer.depositInSpecificAccountAnAmount(accountId, amount);
        }
        catch (CustomerNotFoundException e)
        {
            return false;
        }
    }

    /**
     *  Withdraws money from the customer account and returns the result of the transaction.
     * @param pNr - customer's unique personal identity number
     * @param accountId - the account number for the account to withdraw an amount from.
     * @param amount - the amount to withdraw from the customer account
     * @return <code>true</code> if amount was withdrawn from the customer account.
     *         <code>false</code> if the account does not exist or the amount
     *         is larger than the balance.
     */
    public boolean withdraw(long pNr, int accountId, double amount)
    {
        try
        {
            Customer customer = getCustomerFromId(pNr);
            return customer.withdrawInAccountAnAmountIfCoverage(accountId, amount);
        }
        catch (CustomerNotFoundException e)
        {
            return false;
        }
    }

    /**
     * Returns information about the customer and all customer accounts.
     * @param pNr - customer's unique personal identity number
     * @return - the information list about the customer and all customer accounts.
     *           If the customer is not found, an empty list is returned.
     */
    public ArrayList<String> getCustomer(long pNr)
    {
        ArrayList<String> customerAndAccountsInformation = new ArrayList<String>();
        
        try
        {
            Customer customer = getCustomerFromId(pNr);
            customerAndAccountsInformation.add(customer.getCustomerInformation());
            List<String> accountsInformation = customer.getCustomerAccountsInformation();
            addAccountsInformationToCustomerList(accountsInformation, customerAndAccountsInformation);
            
        }
        catch (CustomerNotFoundException e) {}

        return customerAndAccountsInformation;
    }

    /**
     * Returns a list of information about all customers.
     * @return - the list of information about all customers. If no customers
     *           is found, an empty list is returned.
     */
    public ArrayList<String> getCustomers()
    {
        ArrayList<String> informationAboutCustomers = new ArrayList<String>();

        // Iterate through map with all customers, if at least one found.
        for (Map.Entry<Long, Customer> entry : customers.entrySet())
        {
            Customer customer = entry.getValue();
            informationAboutCustomers.add(customer.getCustomerInformation());
        }
        return informationAboutCustomers;
    }

    /**
     * Returns information about the customer account, if found.
     * @param pNr - customer's unique personal identity number
     * @param accountId - the account number for the account.
     * @return the information about the customer account.
     *         Returns an empty string, if the customer account
     *         is not found.
     */
    public String getAccount(long pNr, int accountId)
    {
        try
        {
            Customer customer = getCustomerFromId(pNr);
            return customer.getCustomerAccountInformation(accountId);
        }
        catch (CustomerNotFoundException e)
        {
            return "";
        }
    }
    
    /**
     * Returns information about specific account and all transactions on the account.
     * Information such as account number, balance, account type, rate, date and time
     * for the transaction, type of transaction, amount and balance after transaction
     * @param pNr - customer's unique personal identity number
     * @param accountId - the account number for the account.
     * @return - the list of account and transaction information.
     */
    public ArrayList<String> getTransactions(long pNr, int accountId)
    {
        ArrayList<String> informationAboutTransactionsOnSpecficAccount = new ArrayList<String>();
        
        try
        {
            Customer customer = getCustomerFromId(pNr);
            String accountInformation = customer.getCustomerAccountInformation(accountId);
            if (!accountInformation.isEmpty())
            {
                informationAboutTransactionsOnSpecficAccount.add(accountInformation);
                List<String> accountTransactionInformation = customer.getCustomerAccountTransactionInformation(accountId);
                for (String transactionInformation : accountTransactionInformation)
                {
                    informationAboutTransactionsOnSpecficAccount.add(transactionInformation);
                }
            }
        }
        catch (CustomerNotFoundException e) {}
        
        return informationAboutTransactionsOnSpecficAccount;
    }
    
    /**
     * Returns the customer register
     * 
     * @return - the customer register
     */
    public LinkedHashMap<Long, Customer> getCustomerMap()
    {
        return (LinkedHashMap<Long, Customer>) customers;
    }
    
    /**
     * Reloads the customer register.
     * 
     * @param customerMap - the customer register to reload the program
     *                      with.
     */
    public void setCustomerMap(LinkedHashMap<Long, Customer> customerMap)
    {
        customers.clear();
        
        customers = customerMap;
    }
}
