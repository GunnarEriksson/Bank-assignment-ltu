package se.gunnareriksson.account;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gunnareriksson.bank.account.Account;
import se.gunnareriksson.bank.account.SavingsAccount;
import se.gunnareriksson.bank.exceptions.AccountNoCoverageException;

public class SavingsAccountTest
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
        account1 = new SavingsAccount();
        account2 = new SavingsAccount();
        account3 = new SavingsAccount();
        account4 = new SavingsAccount();
        account5 = new SavingsAccount();
        account6 = new SavingsAccount();
        account7 = new SavingsAccount();
        account8 = new SavingsAccount();
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
    public void testOneFreeWithDrawAdd1200AndWithDraw700AndBalanceIs500() throws AccountNoCoverageException
    {
        assertEquals(1200, account2.depositAmount(1200), 0);
        assertEquals(500, account2.withdrawAmount(700), 0);
    }
    
    @Test
    public void testSocketIntrestOnlyOneFreeWithDrawAdd1200AndWithDraw700AndWithDraw200BalanceIs296() throws AccountNoCoverageException
    {
        assertEquals(1200, account7.depositAmount(1200), 0);
        assertEquals(500, account7.withdrawAmount(700), 0);
        assertEquals(296, account7.withdrawAmount(200), 0);
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
        assertEquals(18, account4.calculateInterest(), 0);
    }
    
    @Test
    public void testToPresentTheAccount()
    {
        assertEquals(1400, account5.depositAmount(1400), 0);
        assertEquals("Kontonummer: 1005, Saldo: 1400.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", account5.getAccountInformation());
    }
    
    @Test (expected = AccountNoCoverageException.class)
    public void testThrowAccountNoCoverageExceptionWhenWithDrawAmountIsGreaterThanBalance() throws AccountNoCoverageException
    {
        assertEquals(700, account6.depositAmount(700), 0);
        assertEquals(-1, account6.withdrawAmount(701), 0);
    }
    
    @Test (expected = AccountNoCoverageException.class)
    public void testThrowAccountNoCoverageExceptionWhenWithDrawAmountIsGreaterThanBalanceBecauseOfSocketRate() throws AccountNoCoverageException
    {
        assertEquals(900, account8.depositAmount(900), 0);
        assertEquals(300, account8.withdrawAmount(600), 0);
        assertEquals(-6, account8.withdrawAmount(300), 0);
    }
}
