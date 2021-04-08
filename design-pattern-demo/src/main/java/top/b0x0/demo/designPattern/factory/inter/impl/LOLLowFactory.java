package top.b0x0.demo.designPattern.factory.inter.impl;


import top.b0x0.demo.designPattern.factory.inter.ComputerLowFactory;
import top.b0x0.demo.designPattern.factory.inter.Game;

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
