/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-10-25
 * Last Updated: 2015-10-25, Gunnar Eriksson
 * Description: Handles the account information for a credit account
 * Version: First version
 */
package se.gunnareriksson.bank.account;

import se.gunnareriksson.bank.exceptions.AccountNoCoverageException;
import se.gunnareriksson.bank.gui.AccountType;
import se.gunnareriksson.bank.gui.TransactionType;

public class CreditAccount extends Account
{
    private static final long serialVersionUID = 8195531175036432417L;
    private static final String CREDIT_ACCOUNT = AccountType.CREDIT_ACCOUNT.toString();
    private static final double RATE_IN_PERCENT = 0.5;
    private static final int INTEREST_ON_DEBT = 7;
    private double creditLimit;
    
    /**
     * Constructor
     * Calls the constructor from super class and
     * sets the balance to zero and the credit limit
     * to -5000.
     */
    public CreditAccount()
    {
        super();
        balance = 0;
        creditLimit = -5000;
    }

    /**
     * Withdraw an amount from the account if there is enough money in the account
     * (credit limit included).
     * If a withdrawal occurs, a notice of the withdrawal is saved with information
     * such as date, time, transaction type withdrawal, amount and balance after
     * the withdrawal.
     * @param amount - the amount to withdraw from the accounts balance
     * @return - the account balance after the withdraw
     * @throws AccountNoCoverageException - if the new balance is below the credit limit
     */
    @Override
    public double withdrawAmount(double amount) throws AccountNoCoverageException
    {
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
     * Helper method to check if the new balance is not below the credit limit
     * @param amount - the amount to withdraw from the accounts balance
     * @return - <code>true</code> if the new balance is above or equal to credit limit;
     *           <code>false</code> otherwise.
     */
    private boolean hasAccountEnoughMoneyForWithdraw(double amount)
    {
        return (balance - amount) >= creditLimit;
    }
    
    /**
     * Returns the account information.
     * @return - the account number, balance, type of account and rate in
     * percentage with each heading.
     */
    @Override
    public String getAccountInformation()
    {
        return "Kontonummer: " + accountNumber + ", Saldo: " + balance
                + " kr, Typ av konto: "+ CREDIT_ACCOUNT + ", Räntesats: "
                +  RATE_IN_PERCENT + " %" + ", Räntesats skuldränta: "
                + INTEREST_ON_DEBT  + " %";
    }

    /**
     * Calculates a percentage of the account balance
     * @return - the interest in amount. If the balance is
     *           negative (using the credit), a negative
     *           amount is returned.
     */
    @Override
    public double calculateInterest()
    {
        if (isAccountBalanceNegative())
        {
            return balance * INTEREST_ON_DEBT / 100;
        }
        else
        {
            return balance * RATE_IN_PERCENT / 100;
        }
    }
    
    /**
     * Helper method to check if the balance is negative
     * (credit used)
     * @return - <code>true</code> if credit is used
     *           <code>false</code> otherwise.
     */
    private boolean isAccountBalanceNegative()
    {
        return balance < 0;
    }
}
