package top.b0x0.demo.clone;

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
public class DeepCloneTest {
    public static void main(String[] args) {
        System.out.println(" *********************** 深拷贝 *********************** ");
        DeepCloneObj deepObj1 = new DeepCloneObj();
        DeepCloneObj deepObj2 = null;
        try {
            deepObj2 = deepObj1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println("deepObj1 = " + deepObj1);
        deepObj1.setAge(20);
        deepObj1.setHobby(1, 111);
        System.out.println("deepObj2 = " + deepObj2);
    }
}
