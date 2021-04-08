package top.b0x0.demo.designPattern.factory.inter.impl;

import top.b0x0.demo.designPattern.factory.inter.ComputerHighFactory;
import top.b0x0.demo.designPattern.factory.inter.Game;
/**
 * @author TANG
 * @since 2021-03-30
 */
public class PVEHighFactory implements ComputerHighFactory {

    @Override
    public Game playGame() {
        return new DNF();
    }

    @Override
    public Game playGame2() {
        return new WOW();
    }

}
