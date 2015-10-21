package kr.hs.emirim.app2015.muhanhyodo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Miryung Yeon on 2015-10-10.
 */

@EqualsAndHashCode
public class Normal {
    public Normal() {
    }
    public Normal( int id, String title, int chk) {
        this.id = id;
        this.title = title;
        this.chk = chk;
    }
    @Getter @Setter
    int id;
    @Getter @Setter
    String title;
    @Getter @Setter
    int chk;


}
