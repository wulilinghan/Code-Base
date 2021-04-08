package top.b0x0.demo.designPattern.observer;

/**
 * 定义了一个具体观察者,实现抽象观察者(UserObserver)接口的方法
 *
 * @author TANG
 */
public class User implements UserObserver {
    private String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void update(String anime) {
        System.out.println(name + "订阅的番剧: " + anime + "更新啦！");
    }

    @Override
    public String getName() {
        return name;
    }
}