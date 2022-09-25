package org.codeman.adapter;

/**
 * @author hdgaadd
 * Created on 2022/05/04
 *
 * @Description： 可操作渔船的适配器
 */
public class FishingBoatAdapter implements RowingBoat{
    private FishingBoat fishingBoat = new FishingBoat();

    public final void row() {
        fishingBoat.sail();
    }
}
