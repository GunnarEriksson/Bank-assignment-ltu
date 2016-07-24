/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-12-06
 * Last Updated: 2015-12-06, Gunnar Eriksson
 * Description: Account types that can be used
 * Version: First version
 */
package se.gunnareriksson.bank.gui;

public enum AccountType
{
    CREDIT_ACCOUNT("Kreditkonto"),
    SAVINGS_ACCOUNT("Sparkonto");
    
    private String accountType;
    
    /**
     * Constructor
     * 
     * @param accountType - the name of the account type
     */
    private AccountType(String accountType)
    {
        this.accountType = accountType;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return accountType;
    }
}
