package top.b0x0.demo.infrastructure.designPattern.factory.inter.impl;

import top.b0x0.demo.infrastructure.designPattern.factory.inter.ComputerLowFactory;
import top.b0x0.demo.infrastructure.designPattern.factory.inter.Game;
import top.b0x0.demo.infrastructure.designPattern.factory.inter.impl.LOL;

/**
 * @author TANG
 * @since 2021-03-30
 */
public class LOLLowFactory implements ComputerLowFactory {
    @Override
    public Game playGame() {
        return new LOL();
    }
}
