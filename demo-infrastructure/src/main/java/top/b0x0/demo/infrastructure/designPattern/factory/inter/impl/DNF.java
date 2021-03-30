package top.b0x0.demo.infrastructure.designPattern.factory.inter.impl;

import top.b0x0.demo.infrastructure.designPattern.factory.inter.Game;

public class DNF implements Game {
    @Override
    public void play() {
        System.out.println("正在玩DNF...");
    }
}