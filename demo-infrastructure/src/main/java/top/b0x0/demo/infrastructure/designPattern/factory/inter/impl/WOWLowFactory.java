package top.b0x0.demo.infrastructure.designPattern.factory.inter.impl;

import top.b0x0.demo.infrastructure.designPattern.factory.inter.ComputerLowFactory;
import top.b0x0.demo.infrastructure.designPattern.factory.inter.Game;
import top.b0x0.demo.infrastructure.designPattern.factory.inter.impl.WOW;

/**
 * @author TANG
 * @since 2021-03-30
 */
public class WOWLowFactory implements ComputerLowFactory {
    @Override
    public Game playGame() {
        return new WOW();
    }
}
