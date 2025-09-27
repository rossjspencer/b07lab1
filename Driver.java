import java.io.File;
import java.io.IOException;

public class Driver
{
    public static void main(String [] args) throws IOException
    {
        Polynomial p = new Polynomial();
        System.out.println(p.evaluate(3));

        double[] c1 = {6,5};
        int[] e1 = {0, 3};
        Polynomial p1 = new Polynomial(c1, e1);

        double[] c2 = {-9,-2};
        int[] e2 = {4, 1};
        Polynomial p2 = new Polynomial(c2, e2);

        Polynomial p3 = new Polynomial(new File("p3file.txt"));
        System.out.println(p3.evaluate(2));

        Polynomial p4 = p3.multiply(p2);
        Polynomial p5 = p3.add(p2);

        p4.saveToFile("multiplication.txt");
        p5.saveToFile("addition_out_of_order.txt");

        Polynomial s = p1.add(p2);
        System.out.println("s(0.1) = " + s.evaluate(0.1));
        if(s.hasRoot(1))
            System.out.println("1 is a root of s");
        else
            System.out.println("1 is not a root of s");

    }
}