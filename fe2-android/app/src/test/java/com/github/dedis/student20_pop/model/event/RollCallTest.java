package com.github.dedis.student20_pop.model.event;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RollCallTest {

    private final String id = "1234";
    private final String name = "Roll Call";
    private final String location = "EPFL";
    private final String description = "Informative";
    private final long time = Instant.now().getEpochSecond();
    private final long scheduled = Instant.now().getEpochSecond();
    private final RollCall rollCall1 = new RollCall();
    private final RollCall rollCall2 = new RollCall();

    @Before
    public void setUp() {
        rollCall1.setId(id);
        rollCall1.setName(name);
        rollCall1.setCreation(time);
        rollCall1.setStart(time);
        rollCall1.setEnd(time);
        rollCall1.setLocation(location);
        rollCall1.setDescription(description);

        rollCall2.setScheduled(scheduled);
    }

    @Test
    public void getIdTest() {
        assertThat(rollCall1.getId(), is(id));
    }

    @Test
    public void getNameTest() {
        assertThat(rollCall1.getName(), is(name));
    }

    @Test
    public void getCreationTest() {
        assertThat(rollCall1.getCreation(), is(time));
    }

    @Test
    public void getStartTest() {
        assertThat(rollCall1.getStart(), is(time));
    }

    @Test
    public void getScheduledTest() {
        assertThat(rollCall2.getScheduled(), is(scheduled));
    }

    @Test
    public void getEndTest() {
        assertThat(rollCall1.getEnd(), is(time));
    }

    @Test
    public void getAttendeesTest() {
        Set<String> attendees = rollCall1.getAttendees();
        assertThat(attendees, is(new HashSet<>()));

        attendees.add("Attendee id");
        rollCall1.setAttendees(attendees);

        assertThat(rollCall1.getAttendees(), is(attendees));
    }

    @Test
    public void getLocationTest() {
        assertThat(rollCall1.getLocation(), is(location));
    }

    @Test
    public void getDescriptionTest() {
        assertThat(rollCall1.getDescription(), is(description));
    }

    @Test
    public void getStartTimestampTest() {
        assertThat(rollCall1.getStartTimestamp(), is(time));
        assertThat(rollCall2.getStartTimestamp(), is(scheduled));
    }

    @Test
    public void getEndTimestampTest() {
        assertThat(rollCall1.getEndTimestamp(), is(time));
        assertThat(rollCall2.getEndTimestamp(), is(Long.MAX_VALUE));
    }

    @Test
    public void compareTest() {
        assertThat(rollCall1.compareTo(rollCall2), is(-1));
    }
}
