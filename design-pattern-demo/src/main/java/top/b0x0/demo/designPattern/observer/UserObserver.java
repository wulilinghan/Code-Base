package top.b0x0.demo.designPattern.observer;

/**
 * 定义一个抽象观察者,在得到通知时进行更新
 * 这里就可以当做是用户
 *
 * @author TANG
 */
public interface UserObserver {
    /**
     * 更新通知
     *
     * @param anime 番剧名
     */
    void update(String anime);

    /**
     * 得到用户名称
     *
     * @return /
     */
    String getName();
}