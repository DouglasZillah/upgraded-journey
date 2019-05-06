# Java中的值传递


## 目录：


* [引用传递还是值传递](#引用传递还是值传递)
  * [StackOverflow](#StackOverflow)
  * [Wikipedia](#Wikipedia)
  * [知乎](#知乎)
* [代码解释](#代码解释)
  * [基础数据类型](#基础数据类型)
  * [基础数据类型的封装类型](#基础数据类型的封装类型)
  * [复杂一点的例子](#复杂一点的例子)
  * [结论一](#结论一)
  * [结论二](#结论二)
  * [字符串类型](#字符串类型)
  * [结论三](#结论三)
  * [集合类型及复合类型](#集合类型及复合类型)
  * [结论四](#结论四)


---


我第一次见到这个问题其实应该追溯到当年在培训机构的时候,讲师的一句话。


在这之后很长一段时间我都没有怎么去深究这方面的问题。


后来有一天很偶然的想起来有这么一件事情, 就顺手查了一下 StackOverflow, Wikipedia 和知乎。


## 引用传递还是值传递


### StackOverflow: 


[StackOverflow - Is Java "pass-by-reference" or "pass-by-value"?](https://stackoverflow.com/questions/40480/is-java-pass-by-reference-or-pass-by-value)


### Wikipedia: 


[Wikipedia 上求值策略词条关于传值调用的描述: ](https://zh.wikipedia.org/wiki/%E6%B1%82%E5%80%BC%E7%AD%96%E7%95%A5#%E4%BC%A0%E5%80%BC%E8%B0%83%E7%94%A8%EF%BC%88-{Call_by_value}-%EF%BC%89)


> 传值调用(Call by value)
>
> "传值调用"求值是最常见的求值策略, C和Scheme这样差异巨大的语言都在使用。在传值调用中实际参数被求值,其值被绑定到函数中对应的变量上(通常是把值复制到新内存区域)。如果函数或过程能把值赋给它的形式参数, 则被赋值的只是局部拷贝——就是说, 在函数返回后调用者作用域里的曾传给函数的任何东西都不会变。
>
> 传值调用不是一个单一的求值策略, 而是指一类函数的实参在被传给函数之前就被求值的求值策略。尽管很多使用传值调用的编程语言(如Common Lisp、Eiffel、Java)从左至右的求值函数的实际参数,某些语言(比如OCaml)从右至左的求值函数和它们的实际参数, 而另一些语言(比如Scheme和C)未指定这种次序(尽管它们保证顺序一致性)。 


[Wikipedia 上求值策略词条关于传引用调用的描述: ](https://zh.wikipedia.org/wiki/%E6%B1%82%E5%80%BC%E7%AD%96%E7%95%A5#%E4%BC%A0%E5%BC%95%E7%94%A8%E8%B0%83%E7%94%A8%EF%BC%88-{Call_by_reference}-%EF%BC%89)


>传引用调用(Call by reference)
>
>在"传引用调用"求值中, 传递给函数的是它的实际参数的隐式引用而不是实参的拷贝。通常函数能够修改这些参数(比如赋值), 而且改变对于调用者是可见的。因此传引用调用提供了一种调用者和函数交换数据的方法。传引用调用的语言中追踪函数调用的副作用比较难,易产生不易察觉的bug。
>
>很多语言支持某种形式的传引用调用, 但是很少有语言默认使用它。FORTRAN II 是一种早期的传引用调用语言。一些语言如C++、PHP、Visual Basic .NET、C#和REALbasic默认使用传值调用, 但是提供一种传引用的特别语法。
>
>在那些使用传值调用又不支持传引用调用的语言里, 可以用引用(引用其他对象的对象),比如指针(表示其他对象的内存地址的对象)来模拟。C和ML就用了这种方法。这不是一种不同的求值策略(语言本身还是传值调用)。它有时被叫做"传地址调用"(call by address)。这可能让人不易理解。在C之类不安全的语言里会引发解引用空指针之类的错误。但ML的引用是类型安全和内存安全的。
>
>类似的效果可由传共享对象调用(传递一个可变对象)实现。比如Python、Ruby。


[Wikipedia 上求值策略词条关于传共享对象调用的描述: ](https://zh.wikipedia.org/wiki/%E6%B1%82%E5%80%BC%E7%AD%96%E7%95%A5#%E4%BC%A0%E5%85%B1%E4%BA%AB%E5%AF%B9%E8%B1%A1%E8%B0%83%E7%94%A8%EF%BC%88-{Call_by_sharing}-%EF%BC%89)


> 传共享对象调用(Call by sharing)
> 
> 此方式由Barbara Liskov命名, 并被Python、Java(对象类型)、JavaScript、Scheme、OCaml等语言使用。
> 
> 与传引用调用不同, 对于调用者而言在被调用函数里修改参数是没有影响的。如果要达成传引用调用的效果就需要传一个共享对象, 一旦被调用者修改了对象, 调用者就可以看到变化(因为对象是共享的, 没有拷贝)。


### 知乎: 


[知乎 - Java 到底是值传递还是引用传递?](https://www.zhihu.com/question/31203609)


知乎上有几个高票答案讲得比较干脆: `Java 是值传递, 虽然看上去是传对象的时候是传地址引用但实际上是传地址引用的副本`。


StackOverflow上则比较含蓄: `如何定义"引用", 会影响判断Java是值传递还是引用传递, 而大多数时候我们认为Java是值传递`。


## 代码解释


接下来我们从代码的层面来看看 `Java` 传参时的状态。


### 基础数据类型


我们以 `int` 来举例子:


```java
public class PassBy {
    @Test
    public void testCallByInt() {
        int i = 0;
        callByInt(i);
        System.out.println("基本数据类型传递: i = 0 ->  i++ -> i = " + i);
    }
        
    private void callByInt(int i) {
        i++;
    }
}
```


输出:


> 基本数据类型传递: i = 0 ->  i++ -> i = 0


### 基础数据类型的封装类型


接下来是基础数据类型的封装类型, 这次以 `Integer` 来举例:


```java
public class PassBy {
    @Test
    public void testCallByInteger() {
        Integer integer = 1;
        callByInteger(integer);
        System.out.println("封装数据类型传递: integer = 0 ->  i++ -> i = " + integer);
    }
    
    private void callByInteger(Integer integer) {
        integer++;
    }
}
```


输出:


> 封装数据类型传递: integer = 0 ->  i++ -> i = 1


### 复杂一点的例子


接下来我们来一个复杂一点的例子:


```java
public class PassBy {
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
    
    private void callByInt(int i) {
        i++;
    }
}
```


输出:


> i = 0 -> call by : i++ -> i++ -> i = 0 <br/>
> i = 0 -> call by : ++i -> ++i -> i = 0 <br/>
> i = 0 -> i = i++ : 0 <br/>
> i = 0 -> i = ++i : 1 <br/>
> run i++ then :  2 <br/>
> run ++i then :  3 <br/>
> in line i++ :  3 <br/>
> then i = :  4 <br/>
> in line ++i :  5 <br/>


从上面两个例子我们可以先推导出一个结论:


### 结论一


**`Java` 在传递基础数据类型变量时, 是传递了这个变量的拷贝**


这部分内容其实涉及了`Java`内存模型的内容。


基础数据类型变量在实例化时是直接在栈上分配, 栈是线程私有的, 所以需要直接拷贝这个变量给被调用的方法栈。


封装的数据类型变量实例化时是在堆中分配, 而堆是所有线程共享, 所以可以直接传递这个变量的内存地址拷贝, 这样减少了内存消耗, 但由此而来的问题就是内存可见性问题和并发问题。这部分文章可以参考[volatile和transient关键字](volatile和transient关键字.md)。


### 结论二


**`Java`在传递封装数据类型时, 传递的是这个变量的内存地址的拷贝**


### 字符串类型


```java
public class PassBy {
    @Test
    public void callByString() {
        String string = "Hello";
        callByConcatString(string);
        System.out.println("字符串 concat(字符串): string = Hello ->  string.concat(\" World\") -> string = " + string);
        callByAddString(string);
        System.out.println("字符串 + 字符串: string = Hello ->  string + \" World\" -> string = " + string);
    }
    
    private void callByConcatString(String string) {
        string.replace("Hello", "World");
    }
    
    private void callByAddString(String string) {
        string = string + " World";
    }
}
```


输出:


> 字符串 concat(字符串): string = Hello ->  string.concat(" World") -> string = Hello <br/>
> 字符串 + 字符串: string = Hello ->  string + " World" -> string = Hello <br/>


### 结论三


直接用`=`将字符串重新指向这部分好理解, 因为传入的是地址拷贝, 所以外部方法的字符串没有任何变化。但为什么`concat`方法修改了字符串内容,字符串还是没变呢?


**这里其实有一个需要注意的地方, 因为 `Java` 自身对字符串的优化, 字符串类的 `final`修饰符, 以及字符串本身的不可变性质, 这里调用`concat`方法实质上是把`string`变量指向了一个新的字符串, 而外层方法实际上传入的是一份地址拷贝, 方法中的`string`变量依然没变。**


### 集合类型及复合类型

```java
public class PassBy {
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
```


输出:


> list size: 3 <br/>
> list content: <br/>
> Hello <br/>
> World <br/>
> Zillah <br/>
> list size: 3 <br/>
> list content: <br/>
> Hello <br/>
> World <br/>
> Zillah <br/>


```java
public class PassBy {
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
}
```


输出:
> before: InnerClass{privateI=0, privateString="private Hello", publicI=0, publicString="public Hello"} <br/>
> after: InnerClass{privateI=0, privateString="private Hello", publicI=1, publicString="public World"} <br/>
> after replace: InnerClass{privateI=0, privateString="private Hello", publicI=1, publicString="public World"} <br/>


### 结论四


**这部分的代码测试其实也证实了结论二, `Java`传递类变量的真相其实是传递了一份内存地址拷贝, 如果我们改变了内存地址中的对象状态, 则外部方法中的对象状态也会改变,如果只是改变传递进来的拷贝的内存地址指向, 那么外部变量的值是不会被改变的。**


由值传递问题其实还可以推导出`浅拷贝`和`深拷贝`的问题， 至于这部分问题， 欢迎阅读另一篇文章： [Java的浅拷贝与深拷贝](Java的浅拷贝与深拷贝.md)。