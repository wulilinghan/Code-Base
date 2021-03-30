package top.b0x0.demo.infrastructure.designPattern.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义一个具体主题,实现了抽象主题(AnimeSubject)接口的方法
 * 同时通过一个List集合保存观察者的信息，当需要通知观察者的时候，遍历通知即可。
 *
 * @author TANG
 */
public class Anime implements AnimeSubject {

    private static final List<UserObserver> OBSERVER_LIST = new ArrayList<>();
    private String anime;

    static {
        /*
         * 初始化订阅用户
         */
        OBSERVER_LIST.add(new User("张三"));
        OBSERVER_LIST.add(new User("李四"));
    }

    public Anime(String anime) {
        this.anime = anime;
    }

    /**
     * 追番
     *
     * @param user /
     */
    @Override
    public void followAnime(UserObserver user) {
        System.out.println("用户" + user.getName() + "订阅了" + anime + "!");
        OBSERVER_LIST.add(user);
    }

    /**
     * 取消订阅
     *
     * @param user /
     */
    @Override
    public void unfollowAnime(UserObserver user) {
        if (!OBSERVER_LIST.isEmpty()) {
            System.out.println("用户" + user.getName() + "取消订阅" + anime + "!");
            OBSERVER_LIST.remove(user);
        }
    }

    /**
     * 通知用户
     */
    @Override
    public void notifyUser() {
        System.out.println(anime + "更新了！开始通知订阅该番剧的用户！");
        OBSERVER_LIST.forEach(user ->
                user.update(anime)
        );
    }

}