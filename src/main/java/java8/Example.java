package java8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public class Example {

    public static String processFile() throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new FileReader("data.txt"))) {
            return br.readLine();
        }
    }

    public static void main(String[] args) throws IOException {
//        processFile();

        IntPredicate evenNumbers = (int i) -> i % 2 == 0;
        System.out.println(evenNumbers.test(1000));     //无装箱操作

        Predicate<Integer> oddNumbers = (Integer i) -> i % 2 == 0;
        System.out.println(oddNumbers.test(1000));      //有装箱操作:把1000装到Integer对象中

    }
}
