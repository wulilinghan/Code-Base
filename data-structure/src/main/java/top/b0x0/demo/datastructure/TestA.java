package top.b0x0.demo.datastructure;

/**
 * @author TANG
 * @date 2021-06-18
 * @since 1.8
 */
public class TestA {
    public static void main(String[] args) {
        MyArrayList mylist = new MyArrayList();
        mylist.add("123");
        mylist.add("呵呵呵");
        mylist.add("哦哦");
        mylist.add("哈哈哈");
        mylist.add(1, "你好");
        String old = (String) mylist.remove(3);    //返回的是旧值
        System.out.println(old);
        System.out.println(mylist.remove("哦哦"));
        System.out.println(mylist.isEmpty());
        System.out.println(mylist.get(1));
        System.out.println(mylist.size());
        System.out.println(mylist.set(2, "你好啊"));   //返回旧值
        System.out.println(mylist.get(2));
    }
}
