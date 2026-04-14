package com.taskflow.dto;

import lombok.Getter;

@Getter
public class TaskStatsDto {

    private final long total;
    private final long done;
    private final long open;

    public TaskStatsDto(long total, long done) {
        this.total = total;
        this.done  = done;
        this.open  = total - done;
    }
}
