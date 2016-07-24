/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-12-01
 * Last Updated: 2015-12-01, Gunnar Eriksson
 * Description: Is the bank application entry point
 * Version: First version.
 */
package se.gunnareriksson.bank;

import se.gunnareriksson.bank.gui.BankGUI;

public class Bank
{

    /**
     * Program entry point. Starts the Bank application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        new BankGUI().setVisible(true);
    }
}
