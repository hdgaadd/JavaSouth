package org.codeman.adapter;

/**
 * @author hdgaadd
 * Created on 2022/05/04
 *
 * 船长Captain只能操作游艇RowingBoat，要使船长Captain可操作渔船FishingBoat，可以创建一个与游艇RowingBoat相同类型的适配器FishingBoatAdapter
 * 该适配器可操作FishingBoat，故船长Captain可操作适配器FishingBoatAdapter来操作渔船FishingBoat
 *
 * 某些物件不能直接操作，但能通过创建一个适配器使其达到操作的目的
 */
public final class App {
    /**
     * 外界不可以通过创建该对象，来使用该对象的非static方法
     */
    private App() {

    }

    public static void main(String[] args) {
        // lombok的var可以修饰方法中的局部变量，可不必设置具体类型，lombok会根据运行时的推断出类型
        var captain = new Captain(new FishingBoatAdapter());
        captain.row();
    }
}
