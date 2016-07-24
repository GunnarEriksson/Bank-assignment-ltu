package se.gunnareriksson.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gunnareriksson.bank.logic.BankLogic;

public class BankLogicTest
{
    static BankLogic bank;
    
    @BeforeClass
    public static void initialize()
    {
        bank = new BankLogic();
        
        bank.addCustomer("Karl Karlsson", 8505221898L);
        bank.addCustomer("Pelle Persson", 6911258876L);
        bank.addCustomer("Lotta Larsson", 7505121231L);
        bank.addCustomer("Lennart Lennartsson", 7802099898L);
        bank.addCustomer("Anders Andersson", 6508172898L);
        bank.addCustomer("David Davidsson", 6109272898L);
        
        bank.addSavingsAccount(6911258876L); // Pelle Persson, konto 1001
        bank.addSavingsAccount(7505121231L); // Lotta Larsson, konto 1002
        
        bank.addSavingsAccount(7505121231L); // Lotta Larsson, konto 1003
        bank.addSavingsAccount(6911258876L); // Pelle Persson, konto 1004
        bank.addSavingsAccount(7505121231L); // Lotta Larsson, konto 1005
        bank.addSavingsAccount(7802099898L); // Lennart Lennartsson, konto 1006
        bank.addSavingsAccount(8505221898L); // Karl Karlsson, konto 1007
        bank.addSavingsAccount(6508172898L); // Anders Andersson, konto 1008
        bank.addSavingsAccount(6508172898L); // Anders Andersson, konto 1009
        bank.addSavingsAccount(6109272898L); // David Davidsson, konto 1010
        bank.addCreditAccount(6109272898L); // David Davidsson, konto 1011
    }

    @Test
    public void testSuccessfulAddCustomer()
    {
        assertTrue(bank.addCustomer("Erik Eriksson", 7510121898L));
    }
    
    @Test
    public void testAddingCustomerFailsWhenPersonalIndentityNumberAlreadyExists()
    {
        assertFalse(bank.addCustomer("Clark Olofsson", 8505221898L));
    }
    
    @Test
    public void testRemoveCustomerAndReturnInformationAboutCustomersAccounts()
    {
        assertTrue(bank.deposit(7505121231L, 1002, 790));
        assertTrue(bank.deposit(7505121231L, 1003, 520));
        assertTrue(bank.deposit(7505121231L, 1005, 2300));
        
        ArrayList<String> accountInformation = new ArrayList<String>();
        accountInformation = bank.removeCustomer(7505121231L);
        
        assertEquals(4, accountInformation.size());
        assertEquals("Namn: Lotta Larsson, Personnummer: 7505121231", accountInformation.get(0));
        assertEquals("Kontonummer: 1002, Saldo: 790.0 kr, Ränta: 7.9 kr", accountInformation.get(1));
        assertEquals("Kontonummer: 1003, Saldo: 520.0 kr, Ränta: 5.2 kr", accountInformation.get(2));
        assertEquals("Kontonummer: 1005, Saldo: 2300.0 kr, Ränta: 23.0 kr", accountInformation.get(3));
        
        assertTrue(bank.getCustomer(7505121231L).isEmpty());
    }
    
    @Test
    public void testRemoveCustomerThatDoesNotExistReturnEmptyList()
    {
        assertTrue(bank.removeCustomer(7505121232L).isEmpty());
    }
    
    @Test
    public void testSuccessfulChangeCustomersName()
    {
        assertTrue(bank.changeCustomerName("Per Persson", 6911258876L));
    }
    
    @Test
    public void testChangeCustomersNameBecausePersonalIdentityNumberDoesNotExist()
    {
        assertFalse(bank.changeCustomerName("Ola Persson", 7911258876L));
    }
    
    @Test
    public void testSuccessfulAddSavingsAccountToCustomerAndReturnAccountNumber()
    {
        assertEquals(1012, bank.addSavingsAccount(7802099898L));
    }
    
    @Test
    public void testAddSavingsAccountToCustomerFailsWhenCustomerDoesNotExistAndReturnMinusOne()
    {
        assertEquals(-1, bank.addSavingsAccount(7802089898L));
    }
    
    @Test
    public void testSuccessFulCloseCustomerAccountAndRecieveInformationAboutAccount()
    {
        assertTrue(bank.deposit(7802099898L, 1006, 1250));
        
        assertEquals("Kontonummer: 1006, Saldo: 1250.0 kr, Ränta: 12.5 kr", bank.closeAccount(7802099898L, 1006));
    }
    
    @Test
    public void testCloseCustomerAccountFailsBecauseCustomerDoesNotExistAndReturnEmptyString()
    {
        assertEquals("", bank.closeAccount(7802089898L, 1006));
    }
    
    @Test
    public void testCloseCustomerAccountFailsBecauseAccountDoesNotExistForThatCustomertAndReturnEmptyString()
    {
        assertEquals("", bank.closeAccount(7802089898L, 1002));
    }
    
    @Test
    public void testWithdrawMoneyFromAnCustomersAccount()
    {
        assertTrue(bank.deposit(8505221898L, 1007, 980));
        assertTrue(bank.withdraw(8505221898L, 1007, 400));
        assertEquals("Kontonummer: 1007, Saldo: 580.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", bank.getAccount(8505221898L, 1007));
    }
    
    @Test
    public void testWithdrawMoneyFailsBecauseCustomerDoesNotExist()
    {
        assertFalse(bank.withdraw(8505121898L, 1007, 400));
    }
    
    @Test
    public void testWithdrawMoneyFailsBecauseCustomersAccountDoesNotExist()
    {
        assertFalse(bank.withdraw(8505221898L, 1004, 400));
    }
    
    @Test
    public void testGetCustomerAndReceiveCustomerAndAccountInformation()
    {
        assertTrue(bank.deposit(6508172898L, 1008, 520));
        assertTrue(bank.deposit(6508172898L, 1009, 1785));
        
        ArrayList<String> customerAndAccountInformation = bank.getCustomer(6508172898L);
        assertEquals(3, customerAndAccountInformation.size());
        assertEquals("Namn: Anders Andersson, Personnummer: 6508172898", customerAndAccountInformation.get(0));
        assertEquals("Kontonummer: 1008, Saldo: 520.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", customerAndAccountInformation.get(1));
        assertEquals("Kontonummer: 1009, Saldo: 1785.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", customerAndAccountInformation.get(2));
    }
    
    @Test
    public void testGetCustomerFailsBecauseCustomerDoesNotExistAndRecieveEmptyArrayList()
    {
        assertTrue(bank.getCustomer(6508182898L).isEmpty());
    }
    
    @Test
    public void testGetAccountAndAccountTransactionsForSpecificAccount()
    {
        assertTrue(bank.deposit(6109272898L, 1010, 520));
        assertTrue(bank.withdraw(6109272898L, 1010, 400));
        assertTrue(bank.deposit(6109272898L, 1010, 1212));
        
        assertTrue(bank.deposit(6109272898L, 1011, 520));
        assertTrue(bank.withdraw(6109272898L, 1011, 400));
        assertTrue(bank.withdraw(6109272898L, 1011, 1212));
        
        ArrayList<String> accountAndTransactionInformation = bank.getTransactions(6109272898L, 1010);
        assertEquals("Kontonummer: 1010, Saldo: 1332.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", accountAndTransactionInformation.get(0));
        assertEquals("In: 520.0 kr   Saldo: 520.0 kr", accountAndTransactionInformation.get(1).substring(22));
        assertEquals("Ut: 400.0 kr   Saldo: 120.0 kr", accountAndTransactionInformation.get(2).substring(22));
        assertEquals("In: 1212.0 kr   Saldo: 1332.0 kr", accountAndTransactionInformation.get(3).substring(22));
        
        accountAndTransactionInformation = bank.getTransactions(6109272898L, 1011);
        assertEquals("Kontonummer: 1011, Saldo: -1092.0 kr, Typ av konto: Kreditkonto, Räntesats: 0.5 %, Räntesats skuldränta: 7 %", accountAndTransactionInformation.get(0));
        assertEquals("In: 520.0 kr   Saldo: 520.0 kr", accountAndTransactionInformation.get(1).substring(22));
        assertEquals("Ut: 400.0 kr   Saldo: 120.0 kr", accountAndTransactionInformation.get(2).substring(22));
        assertEquals("Ut: 1212.0 kr   Saldo: -1092.0 kr", accountAndTransactionInformation.get(3).substring(22));
        
    }
}
