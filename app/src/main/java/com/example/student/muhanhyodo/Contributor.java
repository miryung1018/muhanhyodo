package com.example.student.muhanhyodo;

/**
 * Created by Student on 2015-10-09.
 */

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Contributor {

    @Getter @Setter
    String login;

    @Getter @Setter
    int contributions;

    @Getter @Setter
    String url;

}