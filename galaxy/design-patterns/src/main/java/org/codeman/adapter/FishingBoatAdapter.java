package org.codeman.adapter;

/**
 * @author hdgaadd
 * created on 2022/05/04
 *
 * description: 可操作渔船的适配器
 */
public class FishingBoatAdapter implements RowingBoat{
    private FishingBoat fishingBoat = new FishingBoat();

    public final void row() {
        fishingBoat.sail();
    }
}
