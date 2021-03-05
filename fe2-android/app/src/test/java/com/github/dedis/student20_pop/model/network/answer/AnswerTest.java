package com.github.dedis.student20_pop.model.network.answer;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AnswerTest {

    private final Result result = new Result(0);
    private final ErrorCode errorCode = new ErrorCode(0, "Bad input");
    private final Error error = new Error(0, errorCode);

    @Test
    public void getIdTest() {
      assertThat(result.getId(), is(0));
      assertThat(error.getId(), is(0));
    }

    @Test
    public void getErrorTest() {
      assertThat(error.getError(), is(errorCode));
    }

    @Test
    public void getCodeTest() {
        assertThat(errorCode.getCode(), is(0));
    }

    @Test
    public void getDescriptionTest() {
        assertThat(errorCode.getDescription(), is("Bad input"));
    }

    @Test
    public void equalsTest() {
      assertEquals(errorCode, errorCode);
      assertNotEquals(result, new Result(2));

      assertEquals(error, error);
      assertNotEquals(error, new Error(2, errorCode));
    }

    @Test
    public void hashCodeTest() {
      assertEquals(result.hashCode(), result.hashCode());
      assertNotEquals(result.hashCode(), (new Result(2)).hashCode());

      assertEquals(error.hashCode(), error.hashCode());
      assertNotEquals(error.hashCode(), (new Error(2, errorCode)).hashCode());
    }
}
