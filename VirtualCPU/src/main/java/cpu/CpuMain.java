package cpu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CpuMain 
{
    public static void main(String[] args) throws IOException 
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to the awesome CPU program");

        Program program = new Program("01000011", "00001111", "10101010", "00010000", "00010001");
        //41        //15       // 170

        Machine machine = new Machine();
        machine.load(program);
//    machine.print(System.out);
//    machine.tick();
//    machine.print(System.out);

        String key = in.readLine();

        while (!key.equals("exit")) //&&  machine.isRunning(), but to use the is running, I need to implement in memmory
        {
            machine.tick();
            machine.print(System.out);
            key = in.readLine();
        }
        for (int line : program) {
            System.out.println(">>> " + line);
        }
    }
}
