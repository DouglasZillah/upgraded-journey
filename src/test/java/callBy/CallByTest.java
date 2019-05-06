package callBy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CallByTest {

    @Test
    public void testCallByBaseType() {
        int i = 0;
        callByInt(i);
        System.out.println("基本数据类型传递: i = 0 ->  i++ -> i = " + i);
    }

    private void callByInt(int i) {
        i++;
    }

    @Test
    public void testCallByInteger() {
        Integer integer = 1;
        callByInteger(integer);
        System.out.println("封装数据类型传递: integer = 0 ->  i++ -> i = " + integer);
    }

    private void callByInteger(Integer integer) {
        integer++;
    }

    @Test
    public void testCallByAddAndGet() {
        int i = 0;
        callByInt(i);
        System.out.println("i = 0 -> call by : i++ -> i++ -> i = " + i);
        callByAddAndGetInt(i);
        System.out.println("i = 0 -> call by : ++i -> ++i -> i = " + i);
        i = i++;
        System.out.println("i = 0 -> i = i++ : " + i);
        i = ++i;
        System.out.println("i = 0 -> i = ++i : " + i);
        i++;
        System.out.println("run i++ then :  " + i);
        ++i;
        System.out.println("run ++i then :  " + i);
        System.out.println("in line i++ :  " + i++);
        System.out.println("then i = :  " + i);
        System.out.println("in line ++i :  " + ++i);
    }

    private void callByAddAndGetInt(int i) {
        ++i;
    }

    @Test
    public void callByString() {
        String string = "Hello";
        callByConcatString(string);
        System.out.println("字符串 concat(字符串): string = Hello ->  string.concat(\" World\") -> string = " + string);
        callByAddString(string);
        System.out.println("字符串 + 字符串: string = Hello ->  string + \" World\" -> string = " + string);
    }

    private void callByConcatString(String string) {
        string.concat(" World");
    }

    private void callByAddString(String string) {
        string = string + " World";
    }

    @Test
    public void callByObject() {

    }

    @Test
    public void callByFinalObject() {
        InnerClass innerClass = new InnerClass();
        System.out.println("before: " + innerClass);
        callByFinalObject(innerClass);
        System.out.println("after: " + innerClass);
        callByReplaceFinalObject(innerClass);
        System.out.println("after replace: " + innerClass);
    }

    private void callByReplaceFinalObject(InnerClass innerClass) {
        innerClass = new InnerClass();
    }

    private void callByFinalObject(InnerClass innerClass) {
        int i = innerClass.getPrivateI();
        i++;
        innerClass.getPrivateString().replace("Hello", "World");
        innerClass.publicI++;
        innerClass.publicString = "public World";
    }

    final class InnerClass {
        private int privateI;

        private String privateString = "private Hello";

        public int publicI = 0;

        public String publicString = "public Hello";

        public int getPrivateI() {
            return privateI;
        }

        public void setPrivateI(int privateI) {
            this.privateI = privateI;
        }

        public String getPrivateString() {
            return privateString;
        }

        public void setPrivateString(String privateString) {
            this.privateString = privateString;
        }

        @Override
        public String toString() {
            return "InnerClass{" +
                    "privateI=" + privateI +
                    ", privateString=\"" + privateString + '\"' +
                    ", publicI=" + publicI +
                    ", publicString=\"" + publicString + '\"' +
                    '}';
        }
    }

    @Test
    public void callByList() {
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        callFunctionInList(list);
        System.out.println("list size: " + list.size());
        System.out.println("list content:");
        list.forEach(System.out::println);
        callByList(list);
        System.out.println("list size: " + list.size());
        System.out.println("list content:");
        list.forEach(System.out::println);
    }

    private void callFunctionInList(List<String> list) {
        list.add("Zillah");
    }

    private void callByList(List<String> list) {
        list = new ArrayList<>();
        list.add("Upgraded");
        list.add("Journey");
    }
}
