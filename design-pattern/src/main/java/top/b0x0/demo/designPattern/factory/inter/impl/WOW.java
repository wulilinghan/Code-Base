package top.b0x0.demo.designPattern.factory.inter.impl;


import top.b0x0.demo.designPattern.factory.inter.Game;

public class WOW implements Game {
    @Override
    public void play() {
        System.out.println("正在玩WOW...");
    }
}