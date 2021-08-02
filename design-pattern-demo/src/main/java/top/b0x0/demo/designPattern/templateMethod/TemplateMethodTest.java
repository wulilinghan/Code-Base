package top.b0x0.demo.designPattern.templateMethod;

/**
 * 模式的应用场景
 * 1. 算法的整体步骤很固定，但其中个别部分易变时，这时候可以使用模板方法模式，将容易变的部分抽象出来，供子类实现。
 * 2. 当多个子类存在公共的行为时，可以将其提取出来并集中到一个公共父类中以避免代码重复。首先，要识别现有代码中的不同之处，并且将不同之处分离为新的操作。最后，用一个调用这些新的操作的模板方法来替换这些不同的代码。
 * 3. 当需要控制子类的扩展时，模板方法只在特定点调用钩子操作，这样就只允许在这些点进行扩展。
 *
 * @author ManJiis
 * @since 2021-08-02
 */
public class TemplateMethodTest {
    public static void main(String[] args) {
        AbstractStudyAbroad tm = new StudyInAmerica();
        tm.studyAbroadFormalities();
    }
}