package com.epam.learn.multithreading.filescanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathStatistic {
    private long filesCount;
    private long directoriesCount;
    private long size;
}
