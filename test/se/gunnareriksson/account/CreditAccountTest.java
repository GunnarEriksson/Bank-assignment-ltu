package se.gunnareriksson.account;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gunnareriksson.bank.account.Account;
import se.gunnareriksson.bank.account.CreditAccount;
import se.gunnareriksson.bank.exceptions.AccountNoCoverageException;

public class CreditAccountTest
{
    static Account account1;
    static Account account2;
    static Account account3;
    static Account account4;
    static Account account5;
    static Account account6;
    static Account account7;
    static Account account8;
    
    @BeforeClass
    public static void initalize()
    {
        account1 = new CreditAccount();
        account2 = new CreditAccount();
        account3 = new CreditAccount();
        account4 = new CreditAccount();
        account5 = new CreditAccount();
        account6 = new CreditAccount();
        account7 = new CreditAccount();
        account8 = new CreditAccount();
    }

    @Test
    public void testThatAccountNumberAreUniqueForEveryAccountAndStartsWith1001()
    {
        assertEquals(1001, account1.getAccountNumber());
        assertEquals(1002, account2.getAccountNumber());
        assertEquals(1003, account3.getAccountNumber());
    }
    
    @Test
    public void testAdd1500And2100AndBalanceIs3600()
    {
        assertEquals(1500, account1.depositAmount(1500), 0);
        assertEquals(3600, account1.depositAmount(2100), 0);
    }
    
    @Test
    public void testSocketIntrestOnlyOneFreeWithDrawAdd1200AndWithDraw700AndWithDraw200BalanceIs300() throws AccountNoCoverageException
    {
        assertEquals(1200, account7.depositAmount(1200), 0);
        assertEquals(500, account7.withdrawAmount(700), 0);
        assertEquals(300, account7.withdrawAmount(200), 0);
    }
    
    @Test
    public void testWithDrawBalanceIsZero() throws AccountNoCoverageException
    {
        assertEquals(1200, account3.depositAmount(1200), 0);
        assertEquals(0, account3.withdrawAmount(1200), 0);
    }
    
    @Test
    public void testAdd1100And700AndIntrestIs18()
    {
        assertEquals(1100, account4.depositAmount(1100), 0);
        assertEquals(1800, account4.depositAmount(700), 0);
        assertEquals(9, account4.calculateInterest(), 0);
    }
    
    @Test
    public void testUsingCreditWhenAdd1100AndWithDraw1500AndIntrestIsMinus42() throws AccountNoCoverageException
    {
        assertEquals(1100, account5.depositAmount(1100), 0);
        assertEquals(-400, account5.withdrawAmount(1500), 0);
        assertEquals(-28, account5.calculateInterest(), 0);
    }
    
    @Test
    public void testToPresentTheAccount()
    {
        assertEquals(1400, account6.depositAmount(1400), 0);
        assertEquals("Kontonummer: 1006, Saldo: 1400.0 kr, Typ av konto: Kreditkonto, Räntesats: 0.5 %, Räntesats skuldränta: 7 %", account6.getAccountInformation());
    }
    
   
    @Test (expected = AccountNoCoverageException.class)
    public void testThrowAccountNoCoverageExceptionWhenBalanceAfterWithdrawIsBelowTheCreditLimit() throws AccountNoCoverageException
    {
        assertEquals(1400, account8.depositAmount(1400), 0);
        assertEquals(-5001, account8.withdrawAmount(6401), 0);
    }
}
