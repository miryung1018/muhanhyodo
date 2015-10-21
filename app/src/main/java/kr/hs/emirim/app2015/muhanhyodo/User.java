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
    public User(int id, String name, String tel, String iid){
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.iid = iid;
    }
    @Getter @Setter
    int id;

    @Getter @Setter
    String name;

    @Getter @Setter
    String tel;

    @Getter @Setter
    String iid;
}
