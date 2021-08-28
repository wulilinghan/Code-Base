package top.b0x0.demo.designPattern.observer;

/**
 * 定义一个抽象主题, 将观察者(订阅者)聚集起来,可以进行新增、删除和通知。
 * 这里就可以当做番剧
 *
 * @author TANG
 */
public interface AnimeSubject {
    /**
     * 追番
     *
     * @param user /
     */
    void followAnime(UserObserver user);

    /**
     * 取消追番
     *
     * @param user /
     */
    void unfollowAnime(UserObserver user);

    /**
     * 通知
     */
    void notifyUser();
}