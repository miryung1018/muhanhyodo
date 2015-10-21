package kr.hs.emirim.app2015.muhanhyodo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Miryung Yeon on 2015-10-09.
 */
@EqualsAndHashCode
public class Address {

    public Address() {   }
    public Address(int id, String name, String tel, String address) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.address = address;
    }
    @Getter @Setter
    int id;

    @Getter @Setter
    String name;

    @Getter @Setter
    String tel;

    @Getter @Setter
    String address;
}
