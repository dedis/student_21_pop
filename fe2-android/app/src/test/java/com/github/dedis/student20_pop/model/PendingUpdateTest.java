package com.github.dedis.student20_pop.model;

import org.junit.Test;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PendingUpdateTest {

    private final long time = Instant.now().getEpochSecond();
    private final String id = "1234";
    private final PendingUpdate pendingUpdate1 = new PendingUpdate(time, id);

    @Test
    public void getModificationTimeTest() {
        assertThat(pendingUpdate1.getModificationTime(), is(time));
    }

    @Test
    public void compareTest() {
        assertThat(pendingUpdate1.compareTo(new PendingUpdate(0L, id)), is(1));
    }
}
