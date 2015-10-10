package com.example.student.muhanhyodo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Miryung Yeon on 2015-10-10.
 */

@EqualsAndHashCode
public class Notice {

    @Getter @Setter
    int id;
    @Getter @Setter
    String title;
}
