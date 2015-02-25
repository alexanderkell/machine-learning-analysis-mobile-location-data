package matrices;

public class Matrix {

    // return a random m-by-n matrix with values between 0 and 1
    public static double[][] random(int m, int n) {
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = Math.random();
        return C;
    }

    // return n-by-n identity matrix I
    public static double[][] identity(int n) {
        double[][] I = new double[n][n];
        for (int i = 0; i < n; i++)
            I[i][i] = 1;
        return I;
    }

    // return x^T y
    public static double dot(double[] x, double[] y) {
        if (x.length != y.length) throw new RuntimeException("Illegal vector dimensions.");
        double sum = 0.0;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];
        return sum;
    }

    // return C = A^T
    public static double[][] transpose(double[][] A) {
        int m = A.length;
        int n = A[0].length;
        double[][] C = new double[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[j][i] = A[i][j];
        return C;
    }

    // return C = A + B
    public static double[][] add(double[][] A, double[][] B) {
        int m = A.length;
        int n = A[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    // return C = A - B
    public static double[][] subtract(double[][] A, double[][] B) {
        int m = A.length;
        int n = A[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    // return C = A * B
    public static double[][] multiply(double[][] A, double[][] B) {
        int mA = A.length;
        int nA = A[0].length;
        int mB = B.length;
        int nB = B[0].length;
        if (nA != mB) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] C = new double[mA][nB];
        for (int i = 0; i < mA; i++)
            for (int j = 0; j < nB; j++)
                for (int k = 0; k < nA; k++)
                    C[i][j] += (A[i][k] * B[k][j]);
        return C;
    }
    
    /**Return A*b (matrix multiply by a scalar)
     * 
     * @param A	matrix
     * @param b	scalar constant
     * @return the resultant matrix Ab
     */
    public static double[][] multiply(double[][] A, double b) {
        int mA = A.length;
        int nA = A[0].length;
        double[][] C = new double[mA][nA];
        for (int i = 0; i < mA; i++)
                for (int j = 0; j < nA; j++)
                    C[i][j] = A[i][j] * b;
        return C;
    }
    /**Return aB (matrix multiply by a scalar)
     * 
     * @param a	scalar constant 
     * @param B	matrix
     * @return the resultant matrix aB
     */
    public static double[][] multiply(double a, double[][] B) {
        return multiply(B, a);
    }

    // matrix-vector multiplication (y = A * x)
    public static double[] multiply(double[][] A, double[] x) {
        int m = A.length;
        int n = A[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += (A[i][j] * x[j]);
        return y;
    }


    // vector-matrix multiplication (y = x^T A)
    public static double[] multiply(double[] x, double[][] A) {
        int m = A.length;
        int n = A[0].length;
        if (x.length != m) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                y[j] += (A[i][j] * x[i]);
        return y;
    }
    
    
    public static double[][] invert(double a[][]) {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i) b[i][i] = 1;

     // Transform the matrix into an upper triangle
        gaussian(a, index);

     // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i)
          for (int j=i+1; j<n; ++j)
            for (int k=0; k<n; ++k)
              b[index[j]][k]
                -= a[index[j]][i]*b[index[i]][k];

     // Perform backward substitutions
        for (int i=0; i<n; ++i) {
          x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
          for (int j=n-2; j>=0; --j) {
            x[j][i] = b[index[j]][i];
            for (int k=j+1; k<n; ++k) {
              x[j][i] -= a[index[j]][k]*x[k][i];
            }
            x[j][i] /= a[index[j]][j];
          }
        }
      return x;
      }

    // Method to carry out the partial-pivoting Gaussian
    // elimination.  Here index[] stores pivoting order.

      public static void gaussian(double a[][],
        int index[]) {
        int n = index.length;
        double c[] = new double[n];

     // Initialize the index
        for (int i=0; i<n; ++i) index[i] = i;

     // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i) {
          double c1 = 0;
          for (int j=0; j<n; ++j) {
            double c0 = Math.abs(a[i][j]);
            if (c0 > c1) c1 = c0;
          }
          c[i] = c1;
        }

     // Search the pivoting element from each column
        int k = 0;
        for (int j=0; j<n-1; ++j) {
          double pi1 = 0;
          for (int i=j; i<n; ++i) {
            double pi0 = Math.abs(a[index[i]][j]);
            pi0 /= c[index[i]];
            if (pi0 > pi1) {
              pi1 = pi0;
              k = i;
            }
          }

       // Interchange rows according to the pivoting order
          int itmp = index[j];
          index[j] = index[k];
          index[k] = itmp;
          for (int i=j+1; i<n; ++i) {
            double pj = a[index[i]][j]/a[index[j]][j];

         // Record pivoting ratios below the diagonal
            a[index[i]][j] = pj;

         // Modify other elements accordingly
            for (int l=j+1; l<n; ++l)
              a[index[i]][l] -= pj*a[index[j]][l];
          }
        }
      }
    

    
}