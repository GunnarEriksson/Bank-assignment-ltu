/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-09-25
 * Last Updated: 2015-10-25, Gunnar Eriksson
 * Description: Handles the account information for a credit account.
 * 2015-10-25: Changes according the class is a subclass to Account.
 *             Changed IllegalArgumentException to AccountNoCoverageException
 *             Added a time stamp when a transaction occurs.
 * Version: Second version
 */
package se.gunnareriksson.bank.account;

import se.gunnareriksson.bank.exceptions.AccountNoCoverageException;
import se.gunnareriksson.bank.gui.AccountType;
import se.gunnareriksson.bank.gui.TransactionType;

public class SavingsAccount extends Account
{
    private static final long serialVersionUID = -5289560552914224439L;
    private static final String SAVINGS_ACCOUNT = AccountType.SAVINGS_ACCOUNT.toString();
    private static final int RATE_IN_PERCENT = 1;
    private static final int SOCKET_RATE = 2;
    private static final int NUMBER_OF_FREE_BANK_WITHDRAWAL = 1;
    private int numberOfBankWithdrawal;

    /**
     * Constructor
     * Calls the constructor from super class and
     * sets the number of bank withdrawal to zero.
     */
    public SavingsAccount()
    {
        super();
        numberOfBankWithdrawal = 0;
    }
    
    /**
     * Withdraw an amount from the account if there is enough money in the account
     * If a withdrawal occurs, a notice of the withdrawal is saved with information
     * such as date, time, transaction type withdrawal, amount and balance after
     * the withdrawal.
     * @param amount - the amount to withdraw from the accounts balance
     * @return - the account balance after the withdraw
     * @throws AccountNoCoverageException - if there is not enough money in the account
     */
    @Override
    public double withdrawAmount(double amount) throws AccountNoCoverageException
    {
        numberOfBankWithdrawal++;
        if (!isBankWithdrawalFreeOfCharge(numberOfBankWithdrawal))
        {
            amount += amount * SOCKET_RATE / 100;
        }
        
        if (hasAccountEnoughMoneyForWithdraw(amount))
        {
            balance -= amount;
            noteTransaction(amount, TransactionType.WITHDRAWAL);

            return balance;
        }
        else
        {
            throw new AccountNoCoverageException();
        }
    }
    
    /**
     * Helper method to check if the bank withdrawal is free or a socket interest
     * should be added to the amount.
     * @param numberOfBankWithdrawal - number of bank withdrawal for the account
     * @return - <code>true</code> if the bank withdrawal is free of charge
     *           <code>false</code> otherwise.
     */
    private boolean isBankWithdrawalFreeOfCharge(int numberOfBankWithdrawal)
    {
        if (numberOfBankWithdrawal <= NUMBER_OF_FREE_BANK_WITHDRAWAL)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Helper method to check if there is enough money in the account
     * @param amount - the amount to withdraw from the accounts balance
     * @return - <code>true</code> if there is enough money in the account;
     *           <code>false</code> otherwise.
     */
    private boolean hasAccountEnoughMoneyForWithdraw(double amount)
    {
        return balance >= amount;
    }

    /**
     * Returns the account information.
     * @return - the account number, balance, type of account and rate in
     *           percentage with each heading.
     */
    public String getAccountInformation()
    {
        return "Kontonummer: " + accountNumber + ", Saldo: " + balance
                + " kr, Typ av konto: "+ SAVINGS_ACCOUNT + ", Räntesats: "
                +  RATE_IN_PERCENT + " %";
    }
    
    /**
     * Calculates a percentage of the account balance
     * @return - the interest in amount
     */
    @Override
    public double calculateInterest()
    {
        return balance * RATE_IN_PERCENT / 100;
    }
}
