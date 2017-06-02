public class Main {
    static double a = -0.5, b = -0.5 + Math.sqrt(3) / 2;
    static MinPoint minPoint = new MinPoint(a, Function.func(a), b, a);
    static MinPoint minPoint1 = new MinPoint(a, Function.func(a), b, a);
    static MinPoint y = new MinPoint(a, Function.func(a));
    static MinPoint z = new MinPoint(b, Function.func(b));

    public static void main(String[] args) {
        double eps = 0.0001;
        int n = 4;
        Optimisation.equableSearch(minPoint, n, eps);
        System.out.println(minPoint.iter);
        System.out.format("Minimum of the function = %.8f in point = %.8f%n", minPoint.min, minPoint.minpoint);

        Optimisation.dihot(minPoint1, y, z, eps, eps / 10);
        System.out.println(minPoint1.iter);
        System.out.format("Minimum of the function = %.8f in point = %.8f%n", minPoint1.min, minPoint1.minpoint);
    }
}

class Optimisation {

    static MinPoint dihot(MinPoint minPoint, MinPoint y, MinPoint z, double eps, double a) {

        double length;

        if (minPoint.iter == 0)
            evaluation(minPoint, eps);

        y.minpoint = (minPoint.previous + minPoint.next - a) / 2;
        y.min = Function.func(y.minpoint);
        minPoint.iter++;

        z.minpoint = (minPoint.previous + minPoint.next + a) / 2;
        z.min = Function.func(z.minpoint);
        minPoint.iter++;

        if (y.min <= z.min)
            minPoint.next = z.minpoint;
        else
            minPoint.previous = y.minpoint;

        length = Math.abs(minPoint.next - minPoint.previous);
        if (length <= eps) {
            minPoint.minpoint = (minPoint.next + minPoint.previous) / 2;
            minPoint.min = Function.func(minPoint.minpoint);
            return minPoint;
        }

        dihot(minPoint, y, z, eps, a);
        return minPoint;
    }

    static MinPoint equableSearch(MinPoint minPoint, int n, double eps) {
        double length, step, point, min = Function.func(minPoint.previous), prevmin = Function.func(minPoint.previous), prev = 0, next = 0;
        Double[] points = new Double[n + 2];
        length = minPoint.next - minPoint.previous;
        step = step(length / (n + 1), eps);

        if (step < eps)
            return minPoint;

        for (int i = 1; i < n + 1; i++) {
            point = minPoint.previous + i * step;
            points[i] = step(point, eps);
        }
        points[0] = minPoint.previous;
        points[n + 1] = minPoint.next;

        for (int i = 0; i < points.length; i++) {
            minPoint.min = Math.min(prevmin, Function.func(points[i]));
            minPoint.iter++;
            if (minPoint.min != prevmin) {
                minPoint.minpoint = points[i];
                minPoint.previous = (i > 0) ? points[i - 1] : minPoint.previous;
                minPoint.next = (i < points.length - 1) ? points[i + 1] : minPoint.next;
            }
            prevmin = minPoint.min;
        }
        equableSearch(minPoint, n, eps);
        return minPoint;
    }

    private static int evaluation(MinPoint minPoint, double eps) {
        int n;
        n = (int) Math.ceil(Math.log((minPoint.next - minPoint.previous) / eps) / Math.log(2));
        System.out.println("n >= " + 2 * n);
        return n;
    }

    static void show(Double[] points) {
        for (Double i : points)
            System.out.print(i + " ");
        System.out.println();

    }

    private static double step(double step, double eps) {
        double res;
        res = Math.round(step / eps);
        return res * eps;
    }

}

class MinPoint {
    int iter;
    double previous;
    double next;
    double min;
    double minpoint;

    MinPoint(double minpoint, double min) {
        this.min = min;
        this.minpoint = minpoint;
        this.iter = 0;
    }

    MinPoint(double previous, double min, double next, double minpoint) {
        this.min = min;
        this.next = next;
        this.previous = previous;
        this.minpoint = minpoint;
        this.iter = 0;
    }
}

class Function {
    static double func(double x) {
        //return Math.exp(x) - Math.pow(x, 3) / 3 + 2 * x;
        return -Math.pow(x, 3) + 3 * (1 + x) * (Math.log(1 + x) - 1);
    }
}