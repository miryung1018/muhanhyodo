package kr.hs.emirim.app2015.muhanhyodo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Miryung Yeon on 2015-10-10.
 */

@EqualsAndHashCode
public class Notice {

    public Notice() {
    }
    public Notice( int id, String title) {
        this.id = id;
        this.title = title;
    }
    @Getter @Setter
    int id;
    @Getter @Setter
    String title;
}
