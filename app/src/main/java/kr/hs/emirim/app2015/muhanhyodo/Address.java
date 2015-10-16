package kr.hs.emirim.app2015.muhanhyodo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Miryung Yeon on 2015-10-09.
 */
@EqualsAndHashCode
public class Address {
    @Getter @Setter
    int id;
    @Getter @Setter
    String name;
    @Getter @Setter
    String tel;
    @Getter @Setter
    String address;
}
