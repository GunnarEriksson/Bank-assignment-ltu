/**
 * Labb 2 - Testar klasserna Account, SavingsAccount, CreditAccount samt Customer via BankLogic
 * Susanne Fahlman, susanne.fahlman@ltu.se
 * Notera att denna klass kan komma att förändras under kursens gång 
 * så du ska alltid kolla så att du har den senaste versionen innan du lämnar in
 * Senast ändrad: 2015-09-16
 */
package se.gunnareriksson.logic;

import se.gunnareriksson.bank.logic.BankLogic;
public class TestBank2
{
    private BankLogic bank = new BankLogic();

    //-----------------------------------------------------------------------------------
    // Kör igenom ett testscenario
    //-----------------------------------------------------------------------------------
    public void test()
    {
        System.out.println("# Här testas all funktionalitet. Notera att utskriften OK! betyder att testet\n" +
                "gick igenom medan FEL! betyder att testet misslyckades och din kod måste ses över.\n" +
                "Koden i denna testklass ska inte ändras!");

        // Skriver ut bankinfo
        testingGetCustomers();
        System.out.println("# Banken ska vara tom");
        printTheLine();

        // Skapar testkunder...
        testingAddCustomer("Karl Karlsson", 8505221898L, true );
        printTheLine();
        testingAddCustomer("Donald Duck",   8505221898L, false);
        printTheLine();
        testingAddCustomer("Pelle Persson", 6911258876L, true );    
        printTheLine();
        testingAddCustomer("Lotta Larsson", 7505121231L, true );    
        printTheLine();

        // Skriver ut en lista med kunder
        testingGetCustomers();
        System.out.println("# Banken ska innehålla:");
        System.out.println("#\t- Karl Karlsson");
        System.out.println("#\t- Pelle Persson");   
        System.out.println("#\t- Lotta Larsson");   
        printTheLine();


        // Byt namn på kund
        testingChangeName("Kalle Karlsson", 8505221898L, true);
        printTheLine();

        // Byt namn på kund som inte finns...
        testingChangeName("Olle Karlsson", 9905221898L, false);
        printTheLine();

        // Skriver ut kund med konton
        testingGetCustomer(8505221898L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Kalle Karlsson");
        printTheLine();


        // Skapa konton
        testingAddSavingsAccount(8505221898L);  // 1001
        printTheLine();
        testingAddSavingsAccount(11L);          // Ska inte skapa konto...
        printTheLine();
        testingAddCreditAccount(6911258876L);   // 1002
        printTheLine();
        testingAddCreditAccount(8505221898L);   // 1003 
        printTheLine();
        testingAddSavingsAccount(7505121231L);  // 1004

        // SKriver ut kunderna inklusive konton
        testingGetCustomer(8505221898L);
        printTheLine();
        System.out.println("# Kunden var:");
        System.out.println("# \t- Kalle Karlsson (1001, 1003)");
        printTheLine();

        testingGetCustomer(6911258876L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Pelle Persson (1002)");
        printTheLine();

        testingGetCustomer(7505121231L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Lotta Larsson (1004)");
        printTheLine();
        
        // Sätter in 4000 på sparkontot 1004
        System.out.println("# TESTAR SPARKONTO:");
        
        testingDeposit(7505121231L, 1004, 4000, true);          
        testingWithdraw(7505121231L, 1004, 1095, true);
        testingWithdraw(7505121231L, 1004, 2900, false);
        System.out.println("# Uttaget ovan gick inte genomföra eftersom uttagsränta + belopp överstiger saldot");
        testingGetTransactions(7505121231L, 1004);


        // Sätter in 700 kr på konto 1002 (ska ej gå pga fel kontoägare)
        testingDeposit(8505221898L, 1002, 700, false);      
        printTheLine();
        // Sätter in 500 kr på konto 1001
        testingDeposit(8505221898L, 1001, 500, true);                       
        printTheLine();
        // Ta ut 500 kr  på konto 1001
        testingWithdraw(8505221898L, 1001, 500, true);                      
        printTheLine();
        // Ta ut 1 kr  på konto 1001 (ska ej gå)
        testingWithdraw(8505221898L, 1001, 1, false);
        printTheLine();
        // Sätter in 1000 kr  på konto 1001
        testingDeposit(8505221898L, 1001, 1000, true);  
        printTheLine();
        // Skriver ut kunderna inklusive konton
        testingGetCustomer(8505221898L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Kalle Karlsson [1001,1000 kr], [1003,0 kr]");
        printTheLine();

        testingGetCustomer(6911258876L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Pelle Persson [1002,0 kr]");
        printTheLine();

        testingGetCustomer(7505121231L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Lotta Larsson [1004,2905 kr]");
        printTheLine();

        // Skriv ut kontoinformation
        testingGetAccount(8505221898L, 1001);
        System.out.println("# Kontot var:");
        System.out.println("# \t- 1001,1000 kr");
        printTheLine();
        testingGetAccount(8505221898L, 1002);   // Går ej pga fel kontoägare
        printTheLine();

        // Avslutar konto
        testingCloseAccount(8505221898L, 1001); 
        System.out.println("# Kontot var:");
        System.out.println("# \t- 1001,1000 kr => ränta 10 kr");
        printTheLine();

        testingGetCustomers();
        System.out.println("# Banken ska innehålla:");
        System.out.println("# \t- Kalle Karlsson");
        System.out.println("# \t- Pelle Persson");  
        System.out.println("# \t- Lotta Larsson");  
        printTheLine();


        // Sätter in 5000 kr på konto 1003
        testingDeposit(8505221898L, 1003, 5000, true);                          
        printTheLine();
        // Sätter in 5000 kr på konto 1003
        testingDeposit(8505221898L, 1003, 5000, true);  
        printTheLine();
        testingAddSavingsAccount(7505121231L); // Skapar konto 1005
        printTheLine();
        testingGetCustomer(8505221898L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Kalle Karlsson [1003,10000 kr]"); 
        printTheLine();

        testingGetCustomer(6911258876L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Pelle Persson [1002,0 kr]");  
        printTheLine();

        testingGetCustomer(7505121231L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Lotta Larsson [1004,2905 kr], [1005,0 kr]");  
        printTheLine();

        // Sätter in 1000 kr på konto 1005
        testingDeposit(7505121231L, 1005, 1000, true);      
        printTheLine();
        // Tar ut 100 kr tre gånger på konto 1005
        testingWithdraw(7505121231L, 1005, 100, true);
        printTheLine();
        testingWithdraw(7505121231L, 1005, 100, true);                      
        printTheLine();
        testingWithdraw(7505121231L, 1005, 100, true);  
        printTheLine();

        // Skriv ut kontoinformation
        testingGetCustomer(7505121231L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Lotta Larsson [1004,2905 kr], [1005,696 kr]");
        printTheLine();

        // Tar bort kund
        testingRemoveCustomer(7505121231L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Lotta Larsson [1004,2905 kr => ränta 29.05], [1005,696 kr => ränta 6.96 kr]");    
        printTheLine();


        testingGetCustomers();
        System.out.println("# Banken ska innehålla:");
        System.out.println("# \t- Kalle Karlsson");
        System.out.println("# \t- Pelle Persson");  
        printTheLine();

        testingGetAccount(6911258876L, 1003);
        System.out.println("# Kontot hör inte till kunden:");
        printTheLine();

        // Insättningen går inte pga fel kontoägare
        testingDeposit(6911258876L, 1003, 900, false);
        printTheLine();
        // Sätter in 900 kr på konto 1002

        testingDeposit(6911258876L, 1002, 900, true);
        printTheLine();
        testingGetCustomer(6911258876L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Pelle Persson [1002,900 kr]");
        printTheLine();



        testingWithdraw(6911258876L, 1002, 1900, true);
        printTheLine();
        testingGetCustomer(6911258876L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Pelle Persson [1002,-1000 kr]");
        printTheLine();

        // Tar bort kund
        testingRemoveCustomer(6911258876L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Pelle Persson [1002,-1000 kr => ränta -70 kr]");  
        printTheLine();

        // Tar bort kund
        testingRemoveCustomer(8505221898L);
        System.out.println("# Kunden var:");
        System.out.println("# \t- Kalle Karlsson [1003,10000kr => ränta 50 kr]");
        printTheLine();
        testingGetCustomers();
        System.out.println("# Banken ska vara tom");
        printTheLine();

    }

    /**
     * Hjälpmetod för att skriva ut kundlistan
     */
    private void testingGetCustomers()
    {
        System.out.println("\n## BANKEN INNEHÅLLER (namn och personnummer) ##");
        System.out.println(bank.getCustomers());
    }

    /**
     * Hjälpmetod för att skriva ut en kund
     * @param pNr - Personnummer
     */
    private void testingGetCustomer(long pNr)
    {
        System.out.println("\n# UTSKRIFT AV KUND (namn och personnummer samt för alla konton: kontonummer, saldo, kontotyp, räntesats)\n# Parameter:\n#\t- " + pNr);
        System.out.println(bank.getCustomer(pNr));
    }

    /**
     * Hjälpmetod för att skriva ut information om ett konto
     * @param pNr - Personnummer
     * @param accountId - Kontonummer
     */
    private void testingGetAccount(long pNr, int accountId)
    {
        System.out.println("\n# UTSKRIFT AV KONTO (Kontonummer, saldo, kontotyp, räntesats)\n# Parametrar:\n# \t- " + pNr + "\n# \t- " + accountId);
        System.out.println(bank.getAccount(pNr, accountId));
    }

    /**
     * Hjälpmetod som skapar upp en kund samt skriver ut om testet blev godkänt
     * @param name - Kundens namn (för och efternamn)
     * @param pNr  - Kundens personnummer
     * @param check - skicka in true om det borde fungera eller false om det inte borde gå skapa kund
     */
    private void testingAddCustomer(String name, long pNr, boolean check)
    {
        System.out.println("\n# SKAPA KUND\n# Parametrar:\n# \t- " + pNr + "\n# \t- " + name);
        if(bank.addCustomer(name, pNr) == check) // Om returnerat resultat är samma som check så är testet korrekt
            System.out.println("# Resultat:\n# \t- OK!");
        else
            System.out.println("# \t- FEL!");
    }

    /**
     * Hjälpmetod som byter namn på en kund samt skriver ut om testet blev godkänt
     * @param name - Kundens namn (för och efternamn)
     * @param pNr  - Kundens personnummer
     * @param check - skicka in true om det borde fungera eller false om det inte borde gå skapa kund
     */
    private void testingChangeName(String name, long pNr, boolean check)
    {
        System.out.println("\n# ÄNDRA NAMN\n# Parametrar:\n# \t- " + pNr + "\n# \t- " + name);
        if(bank.changeCustomerName(name, pNr) == check)     
            System.out.println("# Resultat:\n# \t- OK!");
        else
            System.out.println("# \t- FEL!");
    }


    /**
     * Hjälpmetod som skapar upp ett konto samt skriver ut OK! om kontot skapades eller FEL! om kontot inte skapades
     * Konto kan bara skapas om man kunden existerar
     * @param pNr - kontoägaren
     */
    private void testingAddSavingsAccount(long pNr)
    {
        int id = bank.addSavingsAccount(pNr);
        System.out.println("\n# SKAPA SPARKONTO\n# Parametrar:\n# \t- " + pNr + "\n# \t- " + id);
        System.out.println("# Kontonr:\n\t- " + id);
    }
    /**
     * Hjälpmetod som skapar upp ett konto samt skriver ut OK! om kontot skapades eller FEL! om kontot inte skapades
     * Konto kan bara skapas om man kunden existerar
     * @param pNr - kontoägaren
     */
    private void testingAddCreditAccount(long pNr)
    {
        int id = bank.addCreditAccount(pNr);
        System.out.println("\n# SKAPA KREDITKONTO\n# Parametrar:\n# \t- " + pNr + "\n# \t- " + id);
        System.out.println("# Kontonr:\n\t- " + id);
    }


    /**
     * Hjälpmetod som sätter in pengar på konto samt skriver ut om testet blev godkänt
     * Ska bara gå göra om man skickar in personnummer och kontonummer som hör ihop
     * @param pNr - kontoägaren
     * @param accountId - kontonummret
     * @param amount  - belopp
     * @param check - skicka in true om det borde fungera eller false om det inte borde gå sätta in pengar
     */
    private void testingDeposit(long pNr, int accountId, int amount, boolean check)
    {
        System.out.println("\n# INSÄTTNING\n# Parametrar:\n# \t- " + pNr + "\n# \t- " + accountId + "\n# \t- " + amount + " kr");
        if(bank.deposit(pNr, accountId, amount) == check)                           
            System.out.println("# Resultat:\n# \t- OK!");
        else
            System.out.println("# \t- FEL!");
    }

    /**
     * Hjälpmetod som tar ut pengar från konto samt skriver ut om testet blev godkänt
     * Ska bara gå göra om man skickar in personnummer och kontonummer som hör ihop samt om belopp finns
     * @param pNr - kontoägaren
     * @param accountId - kontonummret
     * @param amount  - belopp
     * @param check - skicka in true om det borde fungera eller false om det inte borde gå sätta in pengar
     */
    private void testingWithdraw(long pNr, int accountId, int amount, boolean check)
    {
        System.out.println("\n# UTTAG\n# Parametrar:\n# \t- " + pNr + "\n# \t- " + accountId + "\n# \t- -" + amount + " kr");
        if(bank.withdraw(pNr, accountId, amount) == check)                          
            System.out.println("# Resultat:\n# \t- OK!");
        else
            System.out.println("# \t- FEL!");
    }
    
    private void testingGetTransactions(long pNr, int accountId)
    {
        System.out.println("\n# HÄMTA TRANSAKTIONER \n# Parametrar:\n# \t- " + pNr + "\n# \t- " + accountId);
        System.out.println("# Resultat:\n" + bank.getTransactions(pNr, accountId));                         
        
    }

    /**
     * Hjälpmetod som stänger ett konto samt skriver ut information inklusive ränta man får
     * Ska bara gå göra om man skickar in personnummer och kontonummer som hör ihop
     * @param pNr - kundägaren
     * @param accountId - kontonummret
     */
    private void testingCloseAccount(long pNr, int accountId)
    {
        System.out.println("\n# AVSLUTA KONTO (Kontonummer, saldo samt vilken ränta (i kronor))\n# Parametrar:\n# \t- " + pNr + "\n# \t- " + accountId);
        System.out.println("# Resultat:\n" + bank.closeAccount(pNr, accountId));                            
    }

    /**
     * Hjälpmetod som tar bort en kund inklusive konton från banken
     * @param pNr - kund
     */
    private void testingRemoveCustomer(long pNr)
    {
        System.out.println("\n# TA BORT KUND (+ KONTON (Kontonummer, saldo samt vilken ränta (i kronor) för alla konton som togs bort))\n# Parameter:\n# \t- " + pNr);
        System.out.println("# Resultat:\n" + bank.removeCustomer(pNr));             
    }

    private void printTheLine()
    {
        System.out.println("#---------------------------------");
    }

    /**
     * Skapar en instans av BankMenu-klassen och kör igång menyn
     * @param   args    argument används inte
     */
    public static void main(String[] args)
    {

        TestBank2 bankMenu = new TestBank2();
        bankMenu.test();
    }
}