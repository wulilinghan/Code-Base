package top.b0x0.demo.designPattern.factory.inter;


import top.b0x0.demo.designPattern.factory.inter.impl.DNF;
import top.b0x0.demo.designPattern.factory.inter.impl.LOL;

/**
 * 定义一个电脑
 *
 * @author TANG
 */
public class ComputerSimpleFactory {
    private static final String GAME_LOL = "LOL";
    private static final String GAME_DNF = "DNF";

    /**
     * 玩游戏
     *
     * @param game /
     * @return /
     */
    public static Game playGame(String game) {
        if (GAME_LOL.equalsIgnoreCase(game)) {
            return new LOL();
        } else if (GAME_DNF.equalsIgnoreCase(game)) {
            return new DNF();
        }
        return null;
    }
}