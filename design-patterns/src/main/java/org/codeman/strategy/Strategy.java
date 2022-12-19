package org.codeman.strategy;

// 策略模式：使用抽象类属性对某个物体进行分类
// 由于是抽象类的属性，当需要扩展其他功能的机器人时，也可快速添加该属性的机器人
class Robot {
    Ability ability;

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public String toString() {
        return "该机器人是" + ability.abilityName + "机器人";
    }
}

abstract class Ability {
    String abilityName;
}

class Sweep extends Ability {
    public Sweep() {
        super.abilityName = "打扫";
    }
}

class Cook extends Ability {
    public Cook() {
        super.abilityName = "煮饭";
    }
}

public class Strategy {
    public static void main(String[] args) {
        Robot robot1 = new Robot();
        Robot robot2 = new Robot();
        robot1.setAbility(new Sweep());//注入属性即可区分不同机器人
        robot2.setAbility(new Cook());
        System.out.println(robot1);
        System.out.println(robot2);
    }
}
