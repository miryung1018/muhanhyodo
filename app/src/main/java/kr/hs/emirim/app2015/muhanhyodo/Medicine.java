package kr.hs.emirim.app2015.muhanhyodo;

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

    @Getter @Setter
    String message;

    @Getter @Setter
    int user_id;

    public Medicine(int id, String title, int morning, int afternoon, int evening, String sound, String message, int user_id ) {
        this.id = id;
        this.title = title;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.sound = sound;
        this.message = message;
        this.user_id = user_id;
    }


}
