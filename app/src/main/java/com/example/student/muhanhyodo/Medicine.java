package com.example.student.muhanhyodo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Miryung Yeon on 2015-10-10.
 */

@EqualsAndHashCode
public class Medicine {

    @Getter @Setter
    int id;
    @Getter @Setter
    String title;

    @Getter @Setter
    int morning;

    @Getter @Setter
    int afternoon;

    @Getter @Setter
    int evening;

    @Getter @Setter
    String sound;

}
