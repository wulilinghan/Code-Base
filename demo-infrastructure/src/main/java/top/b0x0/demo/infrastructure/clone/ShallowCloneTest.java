package top.b0x0.demo.infrastructure.clone;

/**
 * 浅拷贝与深拷贝
 * <p>
 * 重写clone()方法才能被调用
 * 而如果重写了clone()方法,但没实现 没有实现 Cloneable 接口,会抛出了 CloneNotSupportedException.
 * clone() 方法并不是 Cloneable 接口的方法，而是 Object 的一个 protected 方法。
 * Cloneable 接口只是规定，如果一个类没有实现 Cloneable 接口又调用了 clone() 方法，就会抛出 CloneNotSupportedException。
 *
 * @author musui
 */
public class ShallowCloneTest {
    public static void main(String[] args) {
        System.out.println(" *********************** 浅拷贝 *********************** ");
        ShallowCloneObj o1 = new ShallowCloneObj();
        o1.setName("张三");
        o1.setAge(18);
        o1.setHobby(new Integer[]{0, 1, 2});
        ShallowCloneObj o2 = null;
        try {
            o2 = o1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println("o1 = " + o1 + " hashCode: " + o1.hashCode());
        System.out.println("o2 = " + o2 + " hashCode: " + o2.hashCode());

        o1.setAge(20);
        o1.setHobby(1, 111);
        System.out.println(" \n*********************** 修改后的原对象 *********************** ");
        System.out.println("o1 = " + o1 + " hashCode: " + o1.hashCode());

        System.out.println(" \n*********************** 目标对象 *********************** ");
        System.out.println("o2 = " + o2 + " hashCode: " + o2.hashCode());

    }
}
