/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-12-06
 * Last Updated: 2015-12-06, Gunnar Eriksson
 * Description: Transaction types that can be used
 * Version: First version
 */
package se.gunnareriksson.bank.gui;

public enum TransactionType
{
    DEPOSIT("Insättning"),
    WITHDRAWAL("Uttag");
    
    private String transactionType;
    
    /**
     * Constructor
     * 
     * @param transactionType - the name of the transaction type
     */
    private TransactionType(String transactionType)
    {
        this.transactionType = transactionType;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return transactionType;
    }
}
