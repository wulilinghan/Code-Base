package top.b0x0.demo.designPattern.factory.inter.impl;


import top.b0x0.demo.designPattern.factory.inter.ComputerHighFactory;
import top.b0x0.demo.designPattern.factory.inter.Game;

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
