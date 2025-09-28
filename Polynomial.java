import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Math.pow;

public class Polynomial
{
    double[] coefficients;
    int[] exponents;

    public Polynomial()
    {
        coefficients = new double[]{};
        exponents = new int[]{};
    }

    public Polynomial(double[] coefficients, int[] exponents)
    {
        this.coefficients = coefficients;
        this.exponents = exponents;
    }

    public Polynomial(File file) throws FileNotFoundException
    {
        Scanner reader = new Scanner(file);
        String polynomialString = reader.nextLine();

        //this regex made me pull my hair out
        //splits polynomial string into individual terms
        String[] polynomialStrings = polynomialString.split("(?=[+\\-])");

        double[] coefficients = new double[polynomialStrings.length];
        int[] exponents = new int[polynomialStrings.length];

        for(int i = 0; i < polynomialStrings.length; i++)
        {
            if(!polynomialStrings[i].contains("x"))
            {
                coefficients[i] = parseDouble(polynomialStrings[i]);
                exponents[i] = 0;
            }
            else
            {
                String[] splitOnX = polynomialStrings[i].split("x");
                coefficients[i] = parseDouble(splitOnX[0]);
                exponents[i] = Integer.parseInt(splitOnX[1]);
            }
        }

        this.coefficients = coefficients;
        this.exponents = exponents;
    }

    public void saveToFile(String filename) throws IOException
    {
        String polynomialString = "";

        //evil dirty disgusting if chain
        for(int i = 0; i < this.coefficients.length; i++)
        {
            //polynomial string does not display x0
            if(this.exponents[i] == 0 && polynomialString.isEmpty())
            {
                polynomialString += this.coefficients[i];
            }
            else if(this.exponents[i] == 0)
            {
                if(this.coefficients[i] > 0)
                {
                    polynomialString += "+" + this.coefficients[i];
                }
                else
                {
                    polynomialString += "-" + this.coefficients[i];
                }
            }
            else
            {
                //positive coefficient and empty string
                if(this.coefficients[i] > 0 && polynomialString.isEmpty())
                {
                    polynomialString += this.coefficients[i] + "x" + this.exponents[i];
                }
                //positive coefficient with nonempty string
                else if (this.coefficients[i] > 0)
                {
                    polynomialString += "+" + this.coefficients[i] + "x" + this.exponents[i];
                }
                //the - is included in the coefficient double
                else
                {
                    polynomialString += this.coefficients[i] + "x" + this.exponents[i];
                }
            }
        }

        //set up file writer
        FileWriter writer = new FileWriter(filename);

        writer.write(polynomialString);
        writer.close();
    }

    public Polynomial add(Polynomial p)
    {
        //keeps coefficients and exponents parallel
        //key system automatically adds coefficients when adding to map - handy!
        Map<Integer, Double> polynomialMap = new HashMap<>();

        //add this to map
        for (int i = 0; i < this.coefficients.length; i++)
        {
            polynomialMap.put(this.exponents[i],
                    polynomialMap.getOrDefault(this.exponents[i], 0.0) + this.coefficients[i]);
        }

        //add p to map
        for (int i = 0; i < p.coefficients.length; i++)
        {
            polynomialMap.put(p.exponents[i],
                    polynomialMap.getOrDefault(p.exponents[i], 0.0) + p.coefficients[i]);
        }

        //clear zero coefficients
        polynomialMap.entrySet().removeIf(e -> e.getValue() == 0.0);

        //bonus step: output polynomials will be sorted and pretty
        List<Map.Entry<Integer, Double>> prettyPolynomial = new ArrayList<>(polynomialMap.entrySet());
        prettyPolynomial.sort(Comparator.comparingInt(Map.Entry::getKey));
        Collections.reverse(prettyPolynomial);

        //return to arrays
        int size = prettyPolynomial.size();
        double[] coefficients = new double[size];
        int[] exponents = new int[size];

        for (int i = 0; i < size; i++)
        {
            exponents[i] = prettyPolynomial.get(i).getKey();
            coefficients[i] = prettyPolynomial.get(i).getValue();
        }

        return new Polynomial(coefficients, exponents);
    }

    //the new implementation of the add method makes this wonderfully simple
    public Polynomial multiply(Polynomial p)
    {
        Polynomial result = new Polynomial();

        for(int i = 0; i < this.coefficients.length; i++)
        {
            for(int j = 0; j < p.coefficients.length; j++)
            {
                int[] exponent = {this.exponents[i] + p.exponents[j]};
                double[] coefficient = {this.coefficients[i] * p.coefficients[j]};

                result = result.add(new Polynomial(coefficient, exponent));
            }
        }

        return result;
    }

    public double evaluate(double x)
    {
        double result = 0;

        for(int i = 0; i < this.coefficients.length; i++)
        {
            result += this.coefficients[i] * pow(x, this.exponents[i]);
        }

        return result;
    }

    public boolean hasRoot(double x)
    {
        return Math.abs(this.evaluate(x)) <= 0.0000000000000001;
    }
}
