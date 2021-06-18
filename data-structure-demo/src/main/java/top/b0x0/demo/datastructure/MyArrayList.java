package top.b0x0.demo.datastructure;

/**
 * 1.ArrayList的简单实现（手写）
 * <p>
 * 2.包括以下方法：
 * <p>             int size();
 * <p>             MyArrayList();
 * <p>             MyArrayList(int initialCapacity);
 * <p>             boolean isEmpty();
 * <p>             Object get(int index);
 * <p>             boolean add(Object obj);
 * <p>             void add(int index,Object obj)
 * <p>             Object remove(int index)
 * <p>             boolean remove(Object obj)
 * <p>             Object set(int index,Object obj)
 * <p>             void rangeCheck(int index)
 * <p>             void ensureCapacity()
 *
 * @author TANG
 * @date 2021-06-18
 * @since 1.8
 */
public class MyArrayList {

    /**
     * 底层数组
     */
    private Object[] elementData;
    /**
     * 数组大小
     */
    private int size;

    public int size() {
        /*
         * 返回数组大小
         */
        return size;
    }

    public MyArrayList() {
        /*
         * 无参构造器，通过显式调用含参构造器
         */
        this(10);
    }

    public MyArrayList(int initialCapacity) {
        /*
         * 1.含参构造器
         * 2.要对传入的初始量的合法性进行检测
         * 3.通过新建数组实现
         */
        if (initialCapacity < 0) {
            try {
                throw new Exception();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        elementData = new Object[initialCapacity];
    }

    public boolean isEmpty() {
        /*
         * 判断是否为空
         */
        return size == 0;
    }

    public Object get(int index) {
        /*
         * 1.获取指定下标的对象
         * 2.下标合法性检测
         */
        rangeCheck(index);
        return elementData[index];
    }

    public boolean add(Object obj) {
        /*
         * 添加对象（不指定位置）
         * 注意数组扩容
         */
        ensureCapacity();
        elementData[size] = obj;
        size++;
        return true;
    }

    public void add(int index, Object obj) {
        /*
         * 插入操作（指定位置）
         * 1.下标合法性检查
         * 2.数组容量检查、扩容
         * 3.数组复制（原数组，开始下标，目的数组，开始下标，长度）
         */
        rangeCheck(index);
        ensureCapacity();
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = obj;
        size++;
    }

    public Object remove(int index) {
        /*
         * 1.删除指定下标对象，并返回其值
         * 2.下标合法性检测
         * 3.通过数组复制实现
         * 4.因为前移，数组最后一位要置为空
         */
        rangeCheck(index);
        int arrNums = size - index - 1;
        Object oldValue = elementData[index];
        if (arrNums > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, arrNums);
        }
        elementData[--size] = null;
        return oldValue;
    }

    public boolean remove(Object obj) {
        /*
         * 1.删除指定对象
         * 2.通过遍历
         * 3.equals的底层运用，找到下标，调用remove(int i)
         */
        for (int i = 0; i < size; i++) {
            //注意底层用的是equals不是“==”
            if (get(i).equals(obj)) {
                remove(i);
            }
            break;
        }
        return true;
    }

    public Object set(int index, Object obj) {
        /*
         * 1.将指定下标的对象改变
         * 2.下标合法性检查
         * 3.直接通过数组的赋值来实现改变
         * 4.返回旧值
         */
        rangeCheck(index);
        Object oldValue = elementData[index];
        elementData[index] = obj;
        return oldValue;
    }

    private void rangeCheck(int index) {
        /*
         * 对下标的检查
         */
        if (index < 0 || index >= size) {
            try {
                throw new Exception();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void ensureCapacity() {
        /*
         * 1.对容器容量的检查
         * 2.数组扩容，通过数组复制来实现（量和值两者都要保障）
         */
        if (size == elementData.length) {
            Object[] newArray = new Object[size * 2 + 1];
            System.arraycopy(elementData, 0, newArray, 0, elementData.length);
            elementData = newArray;
        }
    }


}
