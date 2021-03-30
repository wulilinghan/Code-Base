package top.b0x0.demo.infrastructure.designPattern.factory.inter.impl;

import top.b0x0.demo.infrastructure.designPattern.factory.inter.ComputerLowFactory;
import top.b0x0.demo.infrastructure.designPattern.factory.inter.Game;
import top.b0x0.demo.infrastructure.designPattern.factory.inter.impl.DNF;

/**
 * @author TANG
 * @since 2021-03-30
 */
public class DNFLowFactory implements ComputerLowFactory {
    @Override
    public Game playGame() {
        return new DNF();
    }
}
