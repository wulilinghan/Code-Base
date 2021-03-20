package top.b0x0.demo.switchtooptimize.old;

import java.lang.*;


enum Mobile {
    /** // enum showing Mobile prices */
    Samsung(400), Nokia(250), Motorola(325);

    int price;

    Mobile(int p) {
        price = p;
    }

    int showPrice() {
        return price;
    }
}

/**
 * @author musui
 */
public class EnumDemo {

    public static void main(String[] args) {

        System.out.println("CellPhone List:");
        for (Mobile m : Mobile.values()) {
            System.out.println(m + " costs " + m.showPrice() + " dollars");
        }

        Mobile ret;
        ret = Mobile.valueOf("Samsung");
        System.out.println("Selected : " + ret);
    }
} 