package kr.hs.emirim.app2015.muhanhyodo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Miryung Yeon on 2015-10-10.
 */

@EqualsAndHashCode

public class User {
    public User(){ }
    public User(String name, String tel){
        this.name = name;
        this.tel = tel;
    }
    @Getter @Setter
    String name;
    @Getter @Setter
    String tel;
}
