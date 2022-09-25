package org.codeman.stream.component;

/**
 * @author hdgaadd
 * Created on 2022/04/02
 * @description 分身
 */
public class Doppelganger {
    Integer id;

    public Doppelganger(Integer id) {
        this.id = id;
    }

    public Doppelganger() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }
}
