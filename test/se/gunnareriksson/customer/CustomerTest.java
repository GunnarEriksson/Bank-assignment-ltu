package se.gunnareriksson.customer;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gunnareriksson.bank.customer.Customer;

public class CustomerTest
{
    static Customer customerLottaLarsson;
    static Customer customerErikEriksson;
    static Customer customerKarlKarlsson;
    static Customer customerPellePersson;
    static Customer customerNilsNilsson;
    static Customer customerAndersAndersson;
    static Customer customerHelgeHelgesson;
    static Customer customerLennartLennartsson;
    static Customer customerDavidDavidsson;
    
    @BeforeClass
    public static void initialize()
    {
        customerLottaLarsson = new Customer("Lotta Larsson", 7505121231L);
        customerLottaLarsson.createSavingsAccount(); // Account 1001
        
        customerErikEriksson = new Customer("Erik Eriksson", 7510121898L);
        customerErikEriksson.createSavingsAccount(); // Account 1002
        customerErikEriksson.createSavingsAccount(); // Account 1003
        
        customerKarlKarlsson = new Customer("Karl Karlsson", 8505221898L);
        customerKarlKarlsson.createSavingsAccount();  // Account 1004
        
        customerPellePersson = new Customer("Pelle Persson", 6911258876L);
        
        customerNilsNilsson = new Customer("Nils Nilsson", 8912156876L);
        customerNilsNilsson.createSavingsAccount();  // Account 1005
        
        customerAndersAndersson = new Customer("Anders Andersson", 6508172898L);
        customerAndersAndersson.createSavingsAccount();  // Account 1006
        
        customerHelgeHelgesson = new Customer("Helge Helgesson", 6203075898L);
        customerHelgeHelgesson.createSavingsAccount();  // Account 1007
        customerHelgeHelgesson.createSavingsAccount();  // Account 1008
        
        customerLennartLennartsson  = new Customer("Lennart Lennartsson", 7802099898L);
        customerLennartLennartsson.createSavingsAccount();  // Account 1009
        customerLennartLennartsson.createSavingsAccount();  // Account 1010
        customerLennartLennartsson.createSavingsAccount();  // Account 1011
        customerLennartLennartsson.createCreditAccount();  // Account 1012
        customerLennartLennartsson.createCreditAccount();  // Account 1013
        
        customerDavidDavidsson = new Customer("David Davidsson", 7205099398L);
        customerDavidDavidsson.createSavingsAccount();  // Account 1014
        
        
    }

    @Test
    public void testGetCustomerInformation()
    {
        assertEquals("Namn: Lotta Larsson, Personnummer: 7505121231", customerLottaLarsson.getCustomerInformation());
    }
    
    @Test
    public void testGetAccountInformationWhenAccountIsMissing()
    {
        assertTrue(customerPellePersson.getCustomerAccountsInformation().isEmpty());
    }
    
    @Test
    public void testGetAccountInformationOneAccount()
    {
        assertEquals("Kontonummer: 1001, Saldo: 0.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", customerLottaLarsson.getCustomerAccountsInformation().get(0));
    }
    
    @Test
    public void testGetAccountInformationTwoAccounts()
    {   
        List<String> accounts = customerErikEriksson.getCustomerAccountsInformation();
        assertEquals("Kontonummer: 1002, Saldo: 0.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", accounts.get(0));
        assertEquals("Kontonummer: 1003, Saldo: 0.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", accounts.get(1));
    }
    
    @Test
    public void testSuccessfulDepositOfMoneyInAccount()
    {
        assertEquals("Kontonummer: 1004, Saldo: 0.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", customerKarlKarlsson.getCustomerAccountsInformation().get(0));
        assertTrue(customerKarlKarlsson.depositInSpecificAccountAnAmount(1004, 900));
    }
    
    @Test
    public void testDepositMoneyInAccountFailsBecauseNoAccountWithThatId()
    {
        assertFalse(customerAndersAndersson.depositInSpecificAccountAnAmount(1001, 450));
    }
    
    @Test
    public void testSuccessfulWithdrawOfMoneyInAccount()
    {
        assertTrue(customerNilsNilsson.depositInSpecificAccountAnAmount(1005, 1700));
        assertTrue(customerNilsNilsson.withdrawInAccountAnAmountIfCoverage(1005, 1700));
    }
    
    @Test
    public void testWithdrawOfMoneyInAccountFailsBecauseOfCoverage()
    {
        assertTrue(customerAndersAndersson.depositInSpecificAccountAnAmount(1006, 1700));
        assertFalse(customerAndersAndersson.withdrawInAccountAnAmountIfCoverage(1006, 1701));
    }
    
    @Test
    public void testWithdrawOfMoneyInAccountFailsBecauseNoAccountWithThatId()
    {
        assertFalse(customerNilsNilsson.depositInSpecificAccountAnAmount(1001, 450));
    }
    
    @Test
    public void testSuccessfulRemovingOfOneAccount()
    {
        assertTrue(customerHelgeHelgesson.depositInSpecificAccountAnAmount(1007, 900));
        assertTrue(customerHelgeHelgesson.depositInSpecificAccountAnAmount(1008, 750));
        
        List<String> accounts = customerHelgeHelgesson.getCustomerAccountsInformation();
        assertEquals("Kontonummer: 1007, Saldo: 900.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", accounts.get(0));
        assertEquals("Kontonummer: 1008, Saldo: 750.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", accounts.get(1));
        
        assertEquals("Kontonummer: 1007, Saldo: 900.0 kr, Ränta: 9.0 kr", customerHelgeHelgesson.closeCustomerAccount(1007));
        
        assertEquals("Kontonummer: 1008, Saldo: 750.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", customerHelgeHelgesson.getCustomerAccountsInformation().get(0));
        assertEquals(1, customerHelgeHelgesson.getCustomerAccountsInformation().size());
    }
    
    @Test
    public void testSuccessfullRemoveAllAccountsForOneCustomer()
    {
        assertTrue(customerLennartLennartsson.depositInSpecificAccountAnAmount(1009, 420));
        assertTrue(customerLennartLennartsson.depositInSpecificAccountAnAmount(1010, 75));
        assertTrue(customerLennartLennartsson.depositInSpecificAccountAnAmount(1011, 825));
        assertTrue(customerLennartLennartsson.depositInSpecificAccountAnAmount(1012, 300));
        assertTrue(customerLennartLennartsson.depositInSpecificAccountAnAmount(1013, 622));
        assertTrue(customerLennartLennartsson.withdrawInAccountAnAmountIfCoverage(1012, 563));
        
        List<String> accounts = customerLennartLennartsson.getCustomerAccountsInformation();
        assertEquals("Kontonummer: 1009, Saldo: 420.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", accounts.get(0));
        assertEquals("Kontonummer: 1010, Saldo: 75.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", accounts.get(1));
        assertEquals("Kontonummer: 1011, Saldo: 825.0 kr, Typ av konto: Sparkonto, Räntesats: 1 %", accounts.get(2));
        assertEquals("Kontonummer: 1012, Saldo: -263.0 kr, Typ av konto: Kreditkonto, Räntesats: 0.5 %, Räntesats skuldränta: 7 %", accounts.get(3));
        assertEquals("Kontonummer: 1013, Saldo: 622.0 kr, Typ av konto: Kreditkonto, Räntesats: 0.5 %, Räntesats skuldränta: 7 %", accounts.get(4));
        
        List<String> accountInformation = customerLennartLennartsson.closeAllCustomerAccounts();
        
        assertEquals("Kontonummer: 1009, Saldo: 420.0 kr, Ränta: 4.2 kr", accountInformation.get(0));
        assertEquals("Kontonummer: 1010, Saldo: 75.0 kr, Ränta: 0.75 kr", accountInformation.get(1));
        assertEquals("Kontonummer: 1011, Saldo: 825.0 kr, Ränta: 8.25 kr", accountInformation.get(2));
        assertEquals("Kontonummer: 1012, Saldo: -263.0 kr, Ränta: -18.41 kr", accountInformation.get(3));
        assertEquals("Kontonummer: 1013, Saldo: 622.0 kr, Ränta: 3.11 kr", accountInformation.get(4));
        
        assertTrue(customerLennartLennartsson.getCustomerAccountsInformation().isEmpty());
    }
    
    @Test
    public void testSuccessfulGetTransactionInformationFromOneAccount()
    {
        assertTrue(customerDavidDavidsson.depositInSpecificAccountAnAmount(1014, 420));
        assertTrue(customerDavidDavidsson.withdrawInAccountAnAmountIfCoverage(1014, 210));
        assertTrue(customerDavidDavidsson.depositInSpecificAccountAnAmount(1014, 75));
        assertTrue(customerDavidDavidsson.depositInSpecificAccountAnAmount(1014, 825));
        
        List<String> accountTransactionInformation = customerDavidDavidsson.getCustomerAccountTransactionInformation(1014);
        
        assertEquals("In: 420.0 kr   Saldo: 420.0 kr", accountTransactionInformation.get(0).substring(22));
        assertEquals("Ut: 210.0 kr   Saldo: 210.0 kr", accountTransactionInformation.get(1).substring(22));
        assertEquals("In: 75.0 kr   Saldo: 285.0 kr", accountTransactionInformation.get(2).substring(22));
        assertEquals("In: 825.0 kr   Saldo: 1110.0 kr", accountTransactionInformation.get(3).substring(22));
    }
    
}

