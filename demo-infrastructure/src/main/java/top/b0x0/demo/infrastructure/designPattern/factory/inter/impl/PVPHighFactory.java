package top.b0x0.demo.infrastructure.designPattern.factory.inter.impl;

import top.b0x0.demo.infrastructure.designPattern.factory.inter.ComputerHighFactory;
import top.b0x0.demo.infrastructure.designPattern.factory.inter.Game;
import top.b0x0.demo.infrastructure.designPattern.factory.inter.impl.LOL;
import top.b0x0.demo.infrastructure.designPattern.factory.inter.impl.WOW;

/**
 * @author TANG
 * @since 2021-03-30
 */
public class PVPHighFactory implements ComputerHighFactory {

    @Override
    public Game playGame() {
        return new LOL();
    }

    @Override
    public Game playGame2() {
        return new WOW();
    }

}
