package Tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RunPythonScriptFromJava {

    public static void main(String[] args) throws IOException, InterruptedException {

//        Process process = Runtime.getRuntime().exec("C:\\Users\\גיא\\AppData\\Local\\Programs\\Python\\Python39\\python.exe" +
//                " C:\\Users\\גיא\\Desktop\\CryptoProject-Mars+OFB mode+EC El-Gamel+Rabin Signature\\main.py");

        String[] cmd = {"C:\\Users\\גיא\\AppData\\Local\\Programs\\Python\\Python39\\python.exe", "C:\\Users\\גיא\\Desktop\\CryptoProject-Mars+OFB mode+EC El-Gamel+Rabin Signature\\main.py" };
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();

        String line = "", output = "";
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = br.readLine())!= null) {sb = sb.append(line).append("\n"); }

        output = sb.toString();
        System.out.println(output);
  }
}
