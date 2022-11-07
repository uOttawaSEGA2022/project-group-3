package com.example.mealer;

import org.junit.Test;

import static org.junit.Assert.*;

import android.widget.EditText;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    //@Test
    //public void addition_isCorrect() {
    //    assertEquals(4, 2 + 2);
    //}

    // jUnit tests for mealer project start here

    @Test
    public void gettersAndSettersForCreatingAClientIsCorrect() {
        String username = "viv@gmail.com";
        String password = "pokemon";
        String[] fullName = {"Vivethen", "Balachandiran"};
        String[] outputName = {"", ""};

        Client test = new Client(username, password);

        test.setFirstName(fullName[0]);
        test.setLastName(fullName[1]);

        outputName[0] = test.getFirstName();
        outputName[1] = test.getLastName();

        assertEquals(outputName, fullName);
    }

    @Test
    public void cookPermanentBanStateWhenDefaultAndWhenChangedIsCorrect() {
        String username = "Magnus42@gmail.com";
        String password = "bEEmoviE";
        String[] expectedBanState = {"false", "true"};
        String[] actualBanState = {"", ""};

        Cook test = new Cook(username, password);

        actualBanState[0] = test.getPermanentBan();

        test.setPermanentBan("true");

        actualBanState[1] = test.getPermanentBan();

        assertEquals(expectedBanState, actualBanState);
    }

    @Test
    public void firstNameSetterWorksNoMatterWhatTheInputIs() {
        String username = "StarProdigy@gmail.com";
        String password = "DSIsTheBest";

        Client test = new Client(username, password);

        test.setFirstName("Hi");
        test.setFirstName("'wkd'pfz.@kvkr'fw efimgtcgw'okmw'");

        assertEquals("'wkd'pfz.@kvkr'fw efimgtcgw'okmw'", test.getFirstName());
    }

    @Test
    public void clientChangingCreditCardNumberWorks() {
        String username = "JohnWickNeedsYourHelp";
        String password = "GiveUsAnA+Plz";
        String[] expectedNumber = {"911", "678432456"};
        String[] actualNumber = {"", ""};

        Client test = new Client(username, password);

        test.setCreditCardNumber(expectedNumber[0]);
        actualNumber[0] = test.getCreditCardNumber();

        test.setCreditCardNumber(expectedNumber[1]);
        actualNumber[1] = test.getCreditCardNumber();

        assertEquals(expectedNumber, actualNumber);
    }

}
