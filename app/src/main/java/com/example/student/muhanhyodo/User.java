package com.example.student.muhanhyodo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Miryung Yeon on 2015-10-10.
 */

@EqualsAndHashCode
public class User {

    @Getter @Setter
    String name;
    @Getter @Setter
    String tel;
}
