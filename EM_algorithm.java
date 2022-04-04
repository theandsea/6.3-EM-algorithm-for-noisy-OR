import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EM_algorithm {
    public static void main(String[] args) throws Exception {
        EM_algorithm obj = new EM_algorithm("noisyOrX.txt", "noisyOrY.txt");
        obj.training();
    }

    public int[][] x = null;
    public int[] y = null;

    public EM_algorithm(String x_path, String y_path) throws IOException {
        String[] x_data = path_str(x_path).split("\r\n");
        String[] y_data = path_str(y_path).split("\r\n");
        int l = x_data.length;
        x = new int[l][];
        y = new int[l];
        String[] x_line = null;
        for (int i = 0; i < l; i++) {
            x_line = x_data[i].split(" ");
            x[i] = new int[x_line.length];
            //System.out.println("==============x============");
            for (int j = 0, jl = x_line.length; j < jl; j++) {
                x[i][j] = Integer.parseInt(x_line[j]);
                // check the format correctness
                //System.out.print(x[i][j]+"  ");
            }
            y[i] = Integer.parseInt(y_data[i]);
            //System.out.println("\ty__"+y[i]);
        }
    }

    public void training() throws Exception {
        // pi initialization
        int p_l = x[0].length;
        pi = new double[p_l];
        for (int i = 0; i < p_l; i++)
            pi[i] = 0.05;
        PZXXY_t_i = new double[x.length][p_l];

        // iteration 0:
        System.out.println("iteration 0___" + mistake() + "___" + p_Log());

        int[] report_num = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256};
        int th = 0;
        for (int i = 1; i <= 256; i++) {
            // E
            E_p_PZXXY();
            // M
            M_PZXXY_p();
            // report
            if (i == report_num[th]) {
                th++;
                System.out.println("iteration " + i + "___" + mistake() + "___" + p_Log());
            }
        }
    }


    public double[][] PZXXY_t_i = null;

    public void E_p_PZXXY() {
        int[] x_t = null;
        for (int t = 0; t < PZXXY_t_i.length; t++) {
            x_t = x[t];
            // denominator
            double denominator = 1 - PYX_product_0(t);

            // numerator & result
            for (int i = 0; i < x_t.length; i++) {
                PZXXY_t_i[t][i] = y[t] * x_t[i] * pi[i] / denominator;
            }
        }
    }

    public double PYX_product_0(int t) {
        int[] x_t = x[t];
        double product = 1;
        for (int i = 0; i < x_t.length; i++) {
            if (x_t[i] == 1) {
                product *= (1 - pi[i]);
            }
        }
        return product;
    }

    public double[] pi = null;

    public void M_PZXXY_p() {
        double sum = 0;
        int Ti = 0;
        for (int i = 0; i < pi.length; i++) {
            sum = 0;
            Ti = 0;
            for (int t = 0; t < x.length; t++) {
                sum += PZXXY_t_i[t][i];
                if (x[t][i] == 1)
                    Ti++;
            }
            pi[i] = sum / Ti;
        }
    }


    public double p_Log() throws Exception {
        int T = x.length;
        double log_sum = 0;
        for (int t = 0; t < x.length; t++) {
            if (y[t] == 1) {
                log_sum += Math.log(1 - PYX_product_0(t));
            } else if (y[t] == 0) {
                log_sum += Math.log(PYX_product_0(t));
            } else
                throw new Exception("error");
        }

        return log_sum / T;
    }

    public int mistake() {
        double proba_1 = 0;
        int count = 0;
        for (int t = 0; t < x.length; t++) {
            proba_1 = 1 - PYX_product_0(t);
            if ((y[t] == 0 && proba_1 >= 0.5) || (y[t] == 1 && proba_1 <= 0.5)) {
                count++;
            }
        }
        return count;
    }


    public static String path_str(String path) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader bf = new BufferedReader(new FileReader(path));
        String s = null;
        while ((s = bf.readLine()) != null) {//使用readLine方法，一次读一行
            buffer.append(s.trim() + "\r\n");
        }

        return buffer.toString();
    }
}
