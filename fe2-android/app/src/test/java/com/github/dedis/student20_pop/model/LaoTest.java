package com.github.dedis.student20_pop.model;

import com.github.dedis.student20_pop.model.event.RollCall;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LaoTest {

    private final String id = "1234";
    private final String name = "Lao";
    private final long time = Instant.now().getEpochSecond();
    private final Set<String> witnesses = new HashSet<>();
    private final Set<PendingUpdate> pendingUpdates = new HashSet<>();
    private final Map<String, RollCall> rollCalls = new HashMap<>();
    private final RollCall rollCall = new RollCall();
    private final Lao lao1 = new Lao(id, name);
    private final Lao lao2 = new Lao(id);

    @Before
    public void setUp() {
        witnesses.add(id);
        pendingUpdates.add(new PendingUpdate(time, id));
        rollCalls.put(id, rollCall);

        lao1.setLastModified(time);
        lao1.setCreation(time);
        lao1.setOrganizer(id);
        lao1.setModificationId(id);
        lao1.setWitnesses(witnesses);
        lao1.setPendingUpdates(pendingUpdates);
        lao1.setRollCalls(rollCalls);
    }

    @Test
    public void getChannel() {
        assertThat(lao1.getChannel(), is(id));
        lao2.setChannel(name);
        assertThat(lao2.getChannel(), is(name));
    }

    @Test
    public void getIdTest() {
        assertThat(lao1.getId(), is(id));
        lao2.setId(name);
        assertThat(lao2.getId(), is(name));
    }

    @Test
    public void getNameTest() {
        assertThat(lao1.getName(), is(name));
        lao2.setName(id);
        assertThat(lao2.getName(), is(id));
    }

    @Test
    public void getLastModifiedTest() {
        assertThat(lao1.getLastModified(), is(time));
    }

    @Test
    public void getCreationTest() {
        assertThat(lao1.getCreation(), is(time));
    }

    @Test
    public void getOrganizerTest() {
        assertThat(lao1.getOrganizer(), is(id));
    }

    @Test
    public void getModificationIdTest() {
        assertThat(lao1.getModificationId(), is(id));
    }

    @Test
    public void getWitnessesTest() {
        assertThat(lao1.getWitnesses(), is(witnesses));
        assertThat(lao2.getWitnesses(), is(new HashSet<>()));
    }

    @Test
    public void getPendingUpdatesTest() {
        assertThat(lao1.getPendingUpdates(), is(pendingUpdates));
        assertThat(lao2.getPendingUpdates(), is(new HashSet<>()));
    }

    @Test
    public void getRollCallsTest() {
        assertThat(lao1.getRollCalls(), is(rollCalls));
        assertThat(lao2.getRollCalls(), is(new HashMap<>()));
    }

    @Test
    public void getRollCallTest() {
        assertThat(lao1.getRollCall(id).get(), is(rollCall));
    }

    @Test
    public void updateRollCallTest() {
        rollCall.setId(id);
        rollCall.setName(name);
        lao1.updateRollCall(id, rollCall);
        assertThat(lao1.getRollCall(id).get().getName(), is(rollCall.getName()));
    }
}
