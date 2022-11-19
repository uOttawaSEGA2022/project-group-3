package com.example.mealer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.mealer", appContext.getPackageName());
    }

    @Test
    //@RunWith(MockitoJUnitRunner.class)
    public void AddingItemToCookMenuInDatabaseWorks() {
        //Using cook | username: jester@gmail.com | password: jUnit123 | for jUnit testing
        String title, description, ingredients;
        boolean isOffered;

        //Set values as you'd like the new menu to have
        title = "Mushroom Mint";
        description = "A snack popularized by Mario from the Mushroom Kingdom.";
        ingredients = "Mushroom power-ups probably. No Toads though...";
        isOffered = false;

        //Creates new cook menu item
        //mockedDatabaseReference = Mockito.mock(DatabaseReference.class);
        //FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);

        DatabaseReference cook = FirebaseDatabase.getInstance().getReference("accounts").child("PLW5uihXclbof9DAL6ERhKcVhVv2");
        cook.child("Cook Menu").setValue(new MenuItem(title,description,ingredients,isOffered));

        cook.child("Cook Menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    assertEquals(title, postSnapshot.child(title).getValue(MenuItem.class).getTitle());
                    assertEquals(description, postSnapshot.child(title).getValue(MenuItem.class).getDescription());
                    assertEquals(ingredients, postSnapshot.child(title).getValue(MenuItem.class).getIngredients());
                    assertEquals(isOffered, postSnapshot.child(title).getValue(MenuItem.class).getIsOffered());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //assertEquals(title, cook.child("Cook Menu").child(title).getValue().getTitle()); //.most recently added menu item
    }

}
