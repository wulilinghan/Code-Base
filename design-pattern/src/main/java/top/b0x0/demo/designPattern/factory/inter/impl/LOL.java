package top.b0x0.demo.designPattern.factory.inter.impl;


import top.b0x0.demo.designPattern.factory.inter.Game;

/**
 * 定义一个实现类
 *
 * @author TANG
 */
public class LOL implements Game {
    @Override
    public void play() {
        System.out.println("正在玩LOL...");
    }
}



