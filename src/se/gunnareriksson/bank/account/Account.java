/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-10-25
 * Last Updated: 2015-12-25, Gunnar Eriksson
 * Description: Abstract parent class for different account classes
 * 2015-12-25: Handling of account numbers is moved to an separate class
 *             to make it more easy to store and later reload the latest
 *             assigned account number.
 *             Implements the Serializable interface because an object
 *             of the class should be able to be stored on a binary file.
 * Version: Third version
 */
package se.gunnareriksson.bank.account;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.gunnareriksson.bank.exceptions.AccountNoCoverageException;
import se.gunnareriksson.bank.gui.TransactionType;
import se.gunnareriksson.bank.numbergenerator.AccountNumberGenerator;

public abstract class Account implements Serializable
{
    private static final long serialVersionUID = 2856236408253012247L;
    int accountNumber;
    double balance;
    List<String> accountTransactions;
    
    /**
     * Constructor
     * Sets an unique account number for the account
     * and creates a list for all transactions for
     * the account
     */
    Account()
    {
        accountNumber = AccountNumberGenerator.getAccountNumber();
        accountTransactions = new ArrayList<String>();
    }
    
    /**
     * Adds an amount to the account and save a notice
     * of the deposit such as date, time, transaction type
     * deposit, amount and balance after the deposit
     * @param amount - the amount to add to the account
     * @return - the account balance after deposit
     */
    public double depositAmount(double amount)
    {
        balance += amount;
        noteTransaction(amount, TransactionType.DEPOSIT);

        return balance;
    }
    
    /**
     * Saves information about the transaction such as date, time,
     * type of transaction, amount and balance after the transaction.
     * @param amount - the amount of the transaction
     * @param transactionType - Type of transaction, deposit or a withdrawal
     */
    void noteTransaction(double amount, TransactionType transactionType)
    {
        String transactionInformation = "";
        transactionInformation = getTransactionDateAndTime();
        transactionInformation += "   ";
        
        if (transactionType == TransactionType.DEPOSIT)
        {
            transactionInformation += "In: ";
        }
        else if (transactionType == TransactionType.WITHDRAWAL)
        {
            transactionInformation += "Ut: ";
        }
        
        transactionInformation += amount + " kr";
        transactionInformation += "   Saldo: " + balance + " kr";
        
        accountTransactions.add(transactionInformation);
    }
    
    /**
     * Helper method to return actual date and time in the format
     * year-month-day hours:minutes:seconds
     * @return - the date and time when the method is called
     */
    private String getTransactionDateAndTime()
    {
        DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        Date now = new Date();
        
        return dateTimeFormat.format(now);
    }
    
    /**
     * Withdraw an amount from the account if there is enough money in the account
     * If a withdrawal occurs, a notice of the withdrawal is saved with information
     * such as date, time, transaction type withdrawal, amount and balance after
     * the withdrawal.
     * @param amount - the amount to withdraw from the accounts balance
     * @return the account balance after the withdraw
     * @throws AccountNoCoverageException - if there is not enough money in the account
     */
    public abstract double withdrawAmount(double amount) throws AccountNoCoverageException;
    
    /**
     * Returns the unique account number
     * @return - the account number
     */
    public int getAccountNumber()
    {
        return accountNumber;
    }
    
    /**
     * Returns the balance of the account
     * @return - the account balance
     */
    public double getBalance()
    {
        return balance;
    }
    
    /**
     * Returns the account information.
     * @return - the account number, balance, type of account and rate in
     * percentage with each heading.
     */
    public abstract String getAccountInformation();
    
    /**
     * Calculates a percentage of the account balance
     * @return - the interest in amount
     */
    public abstract double calculateInterest();
    
    /**
     * Returns a copy of a list with all transactions for the account
     * @return - a copy of all transactions for the account
     */
    public List<String> getTransactionInformation()
    {
        return deepCopy(accountTransactions);
    }
    
    /**
     * Helper method returns a deep copy of a list
     * @param source - the list to copy
     * @return - the copy of the list
     */
    private List<String> deepCopy(List<String> source)
    {
        List<String> copy = new ArrayList<String>();
        for (String content : source)
        {
            copy.add(content);
        }
        
        return copy;
    }
}
