/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-10-25
 * Last Updated: 2015-10-25, Gunnar Eriksson
 * Description: An exception class for exceptions when an
 *              account has no coverage
 * Version: First version
 */
package se.gunnareriksson.bank.exceptions;

public class AccountNoCoverageException extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * Calls the parent constructor Exception
     */
    public AccountNoCoverageException()
    {
        super();
    }
}
