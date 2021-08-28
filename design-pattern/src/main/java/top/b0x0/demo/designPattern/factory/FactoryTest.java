package top.b0x0.demo.designPattern.factory;


import top.b0x0.demo.designPattern.factory.inter.ComputerHighFactory;
import top.b0x0.demo.designPattern.factory.inter.ComputerSimpleFactory;
import top.b0x0.demo.designPattern.factory.inter.Game;
import top.b0x0.demo.designPattern.factory.inter.impl.*;

/**
 * 工厂模式
 *
 * @author TANG
 */
public class FactoryTest {
    private static final String LOL = "LOL";
    private static final String DNF = "DNF";

    public static void main(String[] args) {
        System.out.println(" ====================== 简单工厂模式 ====================== ");
        /*
         * 简单工厂模式
         * 根据条件决定一个接口由哪个具体产品类来实现
         * 优点:
         * 缺点:扩展性差
         */
        Game game = ComputerSimpleFactory.playGame(LOL);
        Game game2 = ComputerSimpleFactory.playGame(DNF);
        game.play();
        game2.play();

        System.out.println(" ====================== 工厂方法模式 ====================== ");
        /*
         * 工厂方法模式
         *
         * 优点:扩展性高
         * 缺点:增加了复杂度
         */
        Game game3 = new LOLLowFactory().playGame();
        Game game4 = new DNFLowFactory().playGame();
        Game game5 = new WOWLowFactory().playGame();
        game3.play();
        game4.play();
        game5.play();

        System.out.println(" ====================== 抽象工厂模式 ====================== ");
        /*
         * 抽象工厂模式
         *
         * 优点:
         *
         */
        ComputerHighFactory cf3 = new PVPHighFactory();
        cf3.playGame().play();
        cf3.playGame2().play();
        ComputerHighFactory cf4 = new PVEHighFactory();
        cf4.playGame().play();
        cf4.playGame2().play();

    }


}


