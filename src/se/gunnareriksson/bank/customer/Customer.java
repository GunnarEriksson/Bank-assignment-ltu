/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-09-25
 * Last Updated: 2015-12-25, Gunnar Eriksson
 * Description: Handles the information about customers
 * 2015-10-25: Added the method getCustomerAccountTransactionInformation.
 * 2015-12-25: Implements the Serializable interface because an object
 *             of the class should be able to be stored on a binary file.
 * Version: Third version
 */
package se.gunnareriksson.bank.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import se.gunnareriksson.bank.account.Account;
import se.gunnareriksson.bank.account.CreditAccount;
import se.gunnareriksson.bank.account.SavingsAccount;
import se.gunnareriksson.bank.exceptions.AccountNoCoverageException;

public class Customer implements Serializable
{
    private static final long serialVersionUID = 7829420621439023966L;
    private String name;
    private long personalIdentityNumber;
    private Map<Integer, Account> accounts;

    /**
     * Constructor
     * Sets the customer's name and unique personal identity number.
     * Create a map, which should contain the customers' accounts.
     * @param name - the name of the customer
     * @param personalIdentityNumber - the personal identity number
     * (social security number)
     */
    public Customer(String name, long personalIdentityNumber)
    {
        this.name = name;
        this.personalIdentityNumber = personalIdentityNumber;
        accounts = new LinkedHashMap<Integer, Account>();
    }

    /**
     * Creates saving account
     * @return - the account number of the created account
     */
    public int createSavingsAccount()
    {
        SavingsAccount account = new SavingsAccount();
        int accountNumber = account.getAccountNumber();
        accounts.put(accountNumber, account);

        return accountNumber;
    }
    
    /**
     * Creates credit account
     * @return - the account number of the created account
     */
    public int createCreditAccount()
    {
        Account account = new CreditAccount();
        int accountNumber = account.getAccountNumber();
        accounts.put(accountNumber, account);

        return accountNumber;
    }

    /**
     * Adds an amount to the account and returns the result of
     * the transaction.
     * @param accountId - the account number for the account
     * @param amount - the amount to add to the account
     * @return - <code>true</code> if amount was added to the account;
     *           <code>false</code> if the account does not exist.
     */
    public boolean depositInSpecificAccountAnAmount(int accountId, double amount)
    {
        Account account = accounts.get(accountId);
        if (account != null)
        {
            account.depositAmount(amount);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Withdraws money from a the account and returns the result of the transaction.
     * @param accountId - the account number for the account
     * @param amount - the amount to withdraw from the account
     * @return - <code>true</code> if amount was withdrawn from the account
     *           <code>false</code> if the account does not exist or the amount
     *           is larger than the balance.
     */
    public boolean withdrawInAccountAnAmountIfCoverage(int accountId, double amount)
    {
        Account account = accounts.get(accountId);
        if (account != null)
        {
            return withdrawAmountInAccountIfEnoughWithMoney(amount, account);
        }
        else
        {
            return false;
        }
    }

    /**
     * Helper method to catch AccountNoCoverageException, if the amount to withdraw from
     * the account is larger than the accounts balance or below an account's credit limit.
     * @param amount - the amount to withdraw from the account
     * @param account - the account to withdraw from
     * @return <code>true</code> if amount was withdrawn from the account;
     *         <code>false</code> if the amount to withdraw from the account is larger than
     *         the balance.
     */
    private boolean withdrawAmountInAccountIfEnoughWithMoney(double amount, Account account)
    {
        try
        {
            account.withdrawAmount(amount);
            return true;
        }
        catch (AccountNoCoverageException e)
        {
            return false;
        }
    }

    /**
     * Closes the account and returns the account information.
     * Returns an empty string if the account is not found.
     * @param accountId - the account that should be closed.
     * @return the account number, balance and interest with each heading.
     *         Returns an empty string, if account is not found.
     */
    public String closeCustomerAccount(int accountId)
    {
        Account account = accounts.remove(accountId);
        if (account != null)
        {
            String accountNumberSaldoAndIntrest = "Kontonummer: " +  account.getAccountNumber() 
            + ", Saldo: " + account.getBalance() + " kr" + ", Ränta: " 
            + account.calculateInterest() + " kr";
            
            return accountNumberSaldoAndIntrest;
        }
        else
        {
            return "";
        }
    }

    /**
     * Closes all accounts and returns information about every
     * account that is closed.
     * @return the account number, balance and interest with each heading, for every
     *         account that is closed. Returns an empty string, if no account not found.
     */
    public List<String> closeAllCustomerAccounts()
    {
        List<String> accountsInformation = new ArrayList<String>();

        // Iterate through the map that contains the accounts if at least one account found.
        for (Iterator<Entry<Integer, Account>> iterator = accounts.entrySet().iterator(); iterator.hasNext();)
        {
            Entry<Integer, Account> accountEntry = iterator.next();
            Account account = accountEntry.getValue();

            accountsInformation.add("Kontonummer: " +  account.getAccountNumber() 
                + ", Saldo: " + account.getBalance() + " kr" + ", Ränta: " 
                + account.calculateInterest() + " kr");

            iterator.remove(); // Remove account
        }

        return accountsInformation;
    }

    /**
     * Changes customer's name.
     * @param name - the new name for the customer
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the customer unique personal identity number
     * @return - the unique personal identity number of the customer
     */
    public long getPersonalIdentityNumber()
    {
        return personalIdentityNumber;
    }

    /**
     * Returns information about the customer
     * @return the name and personal identity with each heading
     */
    public String getCustomerInformation()
    {
        return "Namn: " + name + ", Personnummer: " + personalIdentityNumber;
    }

    /**
     * Returns information about the account.
     * @param accountId - the account to get information about.
     * @return - the account information. Returns an empty string, if
     *           account not found.
     */
    public String getCustomerAccountInformation(int accountId)
    {
        Account account = accounts.get(accountId);
        if (account != null)
        {
            return account.getAccountInformation();
        }
        else
        {
            return "";
        }
    }

    /**
     * Returns information about all customer accounts.
     * @return - the account information for all accounts. Returns an
     *           empty string, if account not found.
     */
    public List<String> getCustomerAccountsInformation()
    {
        List<String> accountsInformation = new ArrayList<String>();

        // Iterate through the account map, if at least one account found.
        for (Map.Entry<Integer, Account> entry : accounts.entrySet())
        {
            Account account = entry.getValue();
            accountsInformation.add(account.getAccountInformation());
        }

        return accountsInformation;
    }
    
    /**
     * Returns transaction information about the account
     * @param accountId - the account to get transaction information about.
     * @return - the list of transactions, Returns an empty list if
     *           account not found.
     */
    public List<String> getCustomerAccountTransactionInformation(int accountId)
    {
        Account account = accounts.get(accountId);
        if (account != null)
        {
            return account.getTransactionInformation();
        }
        else
        {
            return new ArrayList<String>();
        }
    }
}
