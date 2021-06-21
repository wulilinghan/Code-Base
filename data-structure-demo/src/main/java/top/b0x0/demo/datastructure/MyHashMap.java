package top.b0x0.demo.datastructure;

import java.util.HashMap;

/**
 * @author TANG
 * @date 2021-06-21
 * @since 1.8
 */
public class MyHashMap {

    public Node[] headNode = new Node[11];
    public int size = 0;
    /**
     * 加载因子
     */
    public float loadFactor = 0.75f;

    public static class Node {
        Node next;// 指向下一个节点
        Object key;
        Object value;

        // 构造方法
        public Node(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }

    /**
     * 向哈希表中添加键值对
     */
    public void put(Object key, Object value) {
        // 判断是否超过加载因子（是都一定要先判断，一开始加入时就开始执行判断，也很耗时？或许可以到达一定程度后再开始判断以减少判断次数）
        if (size * 1.0 / headNode.length > loadFactor)
            rehash();

        //
        int index = hash(key, headNode.length);

        Node node = new Node(key, value);
        input(node, headNode, index);

    }

    /**
     * 添加函数
     */
    private void input(Node node, Node[] HeadNode, int index) {
        // 如果HeadNode为空，则把当前节点赋给头节点
        if (HeadNode[index] == null) {
            HeadNode[index] = node;
        } else {
            // 否则，遍历链表
            // 先用temp记下HeadNode[index]，后面就用temp代替HeadNode[index]，不如太长会看晕
            Node temp = HeadNode[index];
            if (node.key == temp.key) {
                System.out.println("键值" + temp.key + "已存在");
            } else {
                while (temp.next != null) {
                    temp = temp.next;
                }
            }
            // 把要加入的节点加到链表最后
            temp.next = node;
        }

    }

    /**
     * hash过程是获取hashcode，其实我的理解是：就是找它该在哪一个链表中
     */
    public int hash(Object key, int length) {
        int index = -1;
        if (key != null) {
            // key是Object类型的，要转成int类型的才能进行取余运算。
            // 先转成字符数组
            char[] c = key.toString().toCharArray();

            int tokey = 0;
            // 计算转换的key值
            for (int i = 0; i < c.length; i++) {
                tokey += c[i];
            }
            index = tokey % length;
        }
        return index;
    }

    public void rehash() {
        // 每次扩展都把当前的哈希表增大一倍
        Node[] newHeadNode = new Node[headNode.length * 2];

        // 把原来的哈希表依次重新hash进去
        for (int i = 0; i < headNode.length; i++) {
            if (headNode[i] != null) {
                int newindex = hash(headNode[i].key, headNode.length * 2);

                // 重新new新的节点来保存原理hash表中的键值对
                Node rehashheadnode = new Node(headNode[i].key, headNode[i].value);
                input(rehashheadnode, newHeadNode, newindex);
                Node temp = headNode[i];
                while (temp.next != null) {
                    temp = temp.next;
                    Node rehashnextnode = new Node(temp.key, temp.value);
                    int nextindex = hash(temp.key, newHeadNode.length);
                    input(rehashnextnode, newHeadNode, nextindex);
                }

            }
        }
        // 重新设置节点数组的引用，就是把newHeadNode数组的地址赋给HeadNode（可以理解为让HeadNode指向newHeadNode）
        headNode = newHeadNode;

    }

    /**
     * 获取键值对应的value
     */
    public Object getValue(Object key) {
        int index = hash(key, headNode.length);
        // 先找到在哪条链中
        Node temp = headNode[index];

        // 先判断头节点是否为空
        if (temp == null)
            return null;
        else {
            if (key == temp.key)
                return temp.value;
            else {// 遍历链表
                while (temp.next != null) {
                    temp = temp.next;
                    if (key == temp.key)
                        return temp.value;
                }
            }
        }
        return null;
    }

    // public Node getNode(){
    //
    // }

    public int size() {
        return size;

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public static void main(String[] args) {
        MyHashMap mhm = new MyHashMap();

        HashMap<Object, Object> hm = new HashMap<>();
        long s3 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            hm.put(i + " ", i);
        }
        for (int i = 0; i < 1000000; i++) {
            hm.get(i);
        }
        long s4 = System.currentTimeMillis();
        System.out.println("jdkHashMap usetime:" + (s4 - s3));
        // 测试
        long s1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            mhm.put(i + " ", i);
        }
        for (int i = 0; i < 1000000; i++) {
            mhm.getValue(i);
        }
        long s2 = System.currentTimeMillis();
        System.out.println("MyHashMap usetime:" + (s2 - s1));

    }

}
 