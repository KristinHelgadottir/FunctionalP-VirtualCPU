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

        Program program = new Program(
        "01001010", "00010000", "00001100", "11000110", "00010010", "00001111", 
        "00110010", "00000111", "10001100", "01000010", "00100001", "00011000",
        "00010000", "00010111", "00010000", "00001100", "11000110", "00010011", "00010010", "00000010", "00100001", "00011000");
//                                      // MOV 5 A  
                                        // PUSH A 
//                                      // ALWAYS
//                                      // CALL FACT
//                                      // POP A 
//                                      // HALT

//                                      // MOV +1 A
//                                      // NZERO
//                                      // JMP RECUR
//                                      // MOV 1 A
//                                      // MOV A 1+
//                                      // RTN

//                                      // PUSH A
//                                      // DEC
//                                      // PUSH A
//                                      // ALWAYS
//                                      // CALL FACT
//                                      // POP B
//                                      // POP A
//                                      // MUL
//                                      // MOV A +1
//                                      // RTN +0
                
        Machine machine = new Machine();
        machine.load(program);
        machine.print(System.out);

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
