package com.github.dedis.student20_pop;

import com.github.dedis.student20_pop.model.Person;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class PersonTest {

    private final String name1 = "Person name 1";
    private final String name2 = "Person name 2";
    private final ArrayList<String> laos = new ArrayList<>(Arrays.asList("0x3939", "0x4747"));
    private final ArrayList<String> laosWithNull = new ArrayList<>(Arrays.asList("0x3939", null, "0x4747"));
    private final Person person1 = new Person(name1);
    private final Person person2 = new Person(name2);

    @Test
    public void createPersonWithNullParameters() {
        assertThrows(IllegalArgumentException.class, () -> new Person(null));
    }

    @Test
    public void getNameTest() {
        assertThat(person1.getName(), is(name1));
    }

    @Test
    public void getIdTest() {
        assertThat(person1.getId(), is(""));
    }

    @Test
    public void getAuthenticationTest() {
        assertThat(person1.getAuthentication(), is(""));
    }

    @Test
    public void setAndGetLaosTest() {
        person1.setLaos(laos);
        assertThat(person1.getLaos(), is(laos));
    }

    @Test
    public void setNullLaosTest() {
        assertThrows(IllegalArgumentException.class, () -> person1.setLaos(null));
        assertThrows(IllegalArgumentException.class, () -> person1.setLaos(laosWithNull));
    }

    @Test
    public void equalsTest() {
        assertEquals(person1, person1);
        assertNotEquals(person2, person1);
    }

    @Test
    public void hashCodeTest() {
        assertEquals(person1.hashCode(), person1.hashCode());
        assertNotEquals(person1.hashCode(), person2.hashCode());
    }
}
