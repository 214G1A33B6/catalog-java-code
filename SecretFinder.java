import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SecretFinder {

    // Lagrange interpolation to calculate f(0)
    public static int lagrangeInterpolation(int[][] points, int k) {
        double constantTerm = 0.0;

        for (int i = 0; i < k; i++) {
            double li = 1.0; // Initialize li for Lagrange polynomial
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    li *= (0 - points[j][0]) / (double)(points[i][0] - points[j][0]);
                }
            }
            // Calculate contribution to the constant term
            constantTerm += points[i][1] * li;
        }
        return (int)Math.round(constantTerm);
    }

    public static void main(String[] args) {
        try {
            // Load and parse JSON
            FileInputStream fileStream = new FileInputStream("data.json");
            JSONObject jsonObject = new JSONObject(new JSONTokener(fileStream));

            // Extract n and k values
            JSONObject keys = jsonObject.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            // Parse and decode points
            int[][] points = new int[k][2]; // Only need k points
            int count = 0;

            for (int i = 1; i <= n && count < k; i++) {
                String key = String.valueOf(i);
                if (jsonObject.has(key)) {
                    JSONObject point = jsonObject.getJSONObject(key);
                    int base = point.getInt("base");
                    int y = Integer.parseInt(point.getString("value"), base);
                    points[count][0] = i; // The x-coordinate is the index
                    points[count][1] = y;  // The y-coordinate is the decoded value
                    count++;
                }
            }

            // Calculate the secret (constant term)
            int secret = lagrangeInterpolation(points, k);
            System.out.println("The secret (constant term) is: " + secret);

        } catch (FileNotFoundException e) {
            System.err.println("JSON file not found.");
        }
    }
}
