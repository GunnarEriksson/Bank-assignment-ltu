/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-12-20
 * Last Updated: 2015-12-20, Gunnar Eriksson
 * Description: Responsible for the account number handling.
 *              Generates a unique account number. Makes it
 *              possible to reload the last assigned account
 *              value with an external value.
 * Version: First version.
 */
package se.gunnareriksson.bank.numbergenerator;

public class AccountNumberGenerator
{
    private static int lastAssignedAccountNumber = 1000;
    private static int accountNumber;
    
    /**
     * Static method to get a unique account number
     * 
     * @return - the unique account number
     */
    public static int getAccountNumber()
    {
        lastAssignedAccountNumber++;
        accountNumber = lastAssignedAccountNumber;
        
        return accountNumber;
    }
    
    /**
     * Static method to get the last assigned account number.
     * 
     * @return - the last assigned account number.
     */
    public static int getLastAssignedAccountNumber()
    {
        return lastAssignedAccountNumber;
    }
    
    /**
     * Static method to reload the last assigned account number
     * 
     * @param number - the number to reload (overwrite) the last
     *                 assigned account number with.
     */
    public static void setLastAssignedAccountNumber(int number)
    {
        lastAssignedAccountNumber = number;
    }
}
