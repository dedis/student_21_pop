package com.github.dedis.student20_pop.model.network.level.low.answer;

import java.util.Objects;

/**
 * A failed query's answer
 */
public final class Error extends Answer {

    private final ErrorCode error;

    public Error(int id, ErrorCode error) {
        super(id);
        this.error = error;
    }

    public ErrorCode getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Error error = (Error) o;
        return Objects.equals(getError(), error.getError());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getError());
    }
}