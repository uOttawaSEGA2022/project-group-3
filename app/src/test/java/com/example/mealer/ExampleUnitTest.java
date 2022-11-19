package com.example.mealer;

import org.junit.Test;

import static org.junit.Assert.*;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private DatabaseReference mockedDatabaseReference;

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

    @Test
    public void MakingItemInCookMenuWorks() {
        String title, description, ingredients, allergens, mealType, cuisineType, price;
        boolean isOffered;

        //Set values as you'd like the new menu to have
        title = "Mushroom Mint";
        mealType = "Snack";
        cuisineType = "Italian I guess?";
        description = "A snack popularized by Mario from the Mushroom Kingdom.";
        ingredients = "Mushroom power-ups probably. No Toads though...";
        allergens = "Mushrooms (duh)";
        price = "5.00";
        isOffered = false;

        //Creates new cook menu item
        MenuItem menu = new MenuItem(title,description,ingredients,mealType,cuisineType,allergens,price,"PLW5uihXclbof9DAL6ERhKcVhVv2",isOffered);

        assertEquals(title, menu.getTitle());
        assertEquals(description, menu.getDescription());
        assertEquals(ingredients, menu.getIngredients());
        assertEquals(isOffered, menu.getIsOffered());
        assertEquals(mealType, menu.getMealType());
        assertEquals(cuisineType, menu.getCuisineType());
        assertEquals(price, menu.getPrice());
        assertEquals(allergens, menu.getAllergens());
    }

    @Test
    public void mealsAreAssignedToSpecificCooksIsTrue() {
        //Using cook | username: jester@gmail.com | password: jUnit123 | for jUnit testing
        String title, description, ingredients, allergens, mealType, cuisineType, price, realCook, fakeCook;
        boolean isOffered;

        //Set values as you'd like the new menu to have
        title = "Do Rego's Pizzeria";
        mealType = "Gourmet";
        cuisineType = "Italian";
        price = "100.00";
        allergens = "Nothing at all....";
        description = "Noah Do Rego's pizza freshly made using a wooden oven.";
        ingredients = "It's all good, trust.";
        isOffered = false;
        realCook = "jester@gmail.com";
        fakeCook = "banMan@gmail.com";

        //Creates new cook menu item
        MenuItem menu = new MenuItem(title, description, ingredients, mealType, cuisineType, allergens, price, realCook, isOffered);

        assertEquals(realCook, menu.getCookID());
        assertNotEquals(fakeCook, menu.getCookID());

    }

    @Test
    public void menuItemSettersWorkAsIntended() {
        String title, description, ingredients, allergens, mealType, cuisineType, price;
        String newTitle, newDescription, newIngredients;
        boolean isOffered;

        //Set values as you'd like the new menu to have
        title = "Subway Special";
        description = "It's not even bad. Trust me please!";
        ingredients = "Tuna & Barbeque Sauce";
        allergens = "fish";
        mealType = "Seafood? idk man";
        cuisineType = "what do i even say";
        price = "16.00";
        isOffered = false;
        newTitle = "Food Fernandez!";
        newDescription = "Not as good as before";
        newIngredients = "Not so healthy either...";


        //Creates new cook menu item
        MenuItem menu = new MenuItem(title, description, ingredients, mealType, cuisineType, allergens, price, "PLW5uihXclbof9DAL6ERhKcVhVv2", isOffered);

        //Set new data for menu params
        menu.setTitle(newTitle);
        menu.setDescription(newDescription);
        menu.setIngredients(newIngredients);

        assertNotEquals(title, menu.getTitle());
        assertNotEquals(description, menu.getDescription());
        assertNotEquals(ingredients, menu.getIngredients());
        assertEquals(newTitle, menu.getTitle());
        assertEquals(newDescription, menu.getDescription());
        assertEquals(newIngredients, menu.getIngredients());
    }

    @Test
    public void cookCanToggleIfMenuItemIsOfferedOrNot() {
        String title, description, ingredients, allergens, mealType, cuisineType, price;
        boolean isOffered;

        //Set values as you'd like the new menu to have
        title = "Scarlet & Violet";
        description = "Pokemon woo!";
        ingredients = "This is not food. Don't ask why it's on my menu.";
        mealType = "not edible";
        cuisineType = "still not edible";
        allergens = "none?";
        price = "0.00";
        isOffered = true;

        //Creates new cook menu item
        MenuItem menu = new MenuItem(title, description, ingredients, mealType, cuisineType, allergens, price, "PLW5uihXclbof9DAL6ERhKcVhVv2",isOffered);

        assertEquals(isOffered, menu.getIsOffered());

        if (isOffered == true) {
            menu.setIsOffered(false);
        }
        else {
            menu.setIsOffered(true);
        }

        assertNotEquals(isOffered, menu.getIsOffered());
    }
}
