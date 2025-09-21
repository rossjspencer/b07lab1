import static java.lang.Math.max;
import static java.lang.Math.pow;

public class Polynomial
{
    double[] coefficients;

    public Polynomial ()
    {
        coefficients = new double[]{0};
    }

    public Polynomial (double[] input)
    {
        coefficients = input;
    }

    public Polynomial add(Polynomial p)
    {
        if(max(p.coefficients.length, this.coefficients.length) == p.coefficients.length)
        {
            //add polynomials when p is the longer list
            for(int i = 0; i < this.coefficients.length; i++)
            {
                p.coefficients[i] = this.coefficients[i] + p.coefficients[i];
            }

            return p;
        }
        else
        {
            //add polynomials when this is the longer list
            for(int i = 0; i < p.coefficients.length; i++)
            {
                this.coefficients[i] = this.coefficients[i] + p.coefficients[i];
            }

            return this;
        }
    }

    public double evaluate(double x)
    {
        double result = 0;

        for(int i = 0; i < this.coefficients.length; i++)
        {
            result += this.coefficients[i] * pow(x, i);
        }

        return result;
    }

    public boolean hasRoot(double x)
    {
        return Math.abs(this.evaluate(x)) <= 0.0000000000000001;
    }
}
