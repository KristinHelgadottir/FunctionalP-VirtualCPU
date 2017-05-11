package cpu;

import java.io.PrintStream;

public class Machine {

    private Cpu cpu = new Cpu();
    private Memory memory = new Memory();

    public void load(Program program) {
        int index = 0;
        for (int instr : program) {
            memory.set(index++, instr);
        }
    }

    public void tick() {
        // Get the next instruction and make shure it is not unsigned
        int instr = memory.get(cpu.getIp());

        if (instr == 0b0000_0000) { // 0000 0000  NOP no operation
            cpu.incIp();
        } else if (instr == 0b0000_0001) { // 0000 0001 ADD A B
            cpu.setA(cpu.getA() + cpu.getB());
            cpu.setIp(cpu.getIp() + 1);
        } else if (instr == 0b0000_0010) 
        { // MUL A B
            cpu.setA(cpu.getA() * cpu.getB());
            cpu.setIp(cpu.getIp() + 1);
        } else if (instr == 0b0000_0011) 
        { // DIV A B
            cpu.setA(cpu.getA() / cpu.getB());
            cpu.setIp(cpu.getIp() + 1);
        } else if (instr == 0b0000_0100) 
        { // ZERO |	F ← A = 0; IP++
            cpu.setFlag(false); // Not shure
            cpu.incIp();
        } else if (instr == 0b0000_0101) { // NEG |	F ← A < 0; IP++
            if (cpu.getA() < 0) {
                cpu.setFlag(true);
            }
            cpu.incIp();
        } else if (instr == 0b0000_0110) { // POS |	F ← A > 0; IP++
            if (cpu.getA() > 0) {
                cpu.setFlag(true);
            }
            cpu.incIp();
        } else if (instr == 0b0000_0111) { // NZERO |    F ← A ≠ 0; IP++
            if (cpu.getA() != 0 ) {
                cpu.setFlag(true);
            }else{cpu.setFlag(false);}
            cpu.incIp();
        } else if (instr == 0b0000_1000) { // EQ |	F ← A = B; IP++
            if (cpu.getA() == cpu.getB()) {
                cpu.setFlag(true);
            }
            cpu.incIp();
        } else if (instr == 0b0000_1001) {// LT |	F ← A < B; IP++
            if (cpu.A < cpu.getB()) {
                cpu.setFlag(true);
            }
            cpu.incIp();
        } else if (instr == 0b0000_1010) { // GT |	F ← A > B; IP++
            if (cpu.B < cpu.getA()) {
                cpu.setFlag(true);
            }
            cpu.incIp();
        }else if((instr & 0b1111_1000)== 0b0001_1000)
        { // RTN +o
            int o = (instr & 0b0000_0111);
            //IP<-[sp++]
            cpu.setIp(memory.get(cpu.getSp()));
            cpu.setSp(cpu.getSp()+1);
            //SP +=o
            cpu.setSp(cpu.getSp() + o);
            //IP++
            cpu.incIp();
        }
        else if (instr == 0b0000_1011) { //NEQ |	F ← A ≠ B; IP++     
        
        } else if (instr == 0b0000_1100) { // ALWAYS |   F ← true; IP++
            cpu.setFlag(true);
            cpu.incIp();
        } else if (instr == 0b0000_1101) { // Undefined
            System.out.println("Undefined");
            cpu.incIp();
        } else if (instr == 0b0000_1110) { // Undefined
            System.out.println("Undefined");
            cpu.incIp();
        } else if (instr == 0b0000_1111) 
        { //HALT  |	Halts execution
        } else if ((instr & 0b1111_1110) == 0b0001_0000) 
        {  // PUSH r  |  [--SP] ← r; IP++
            int r = instr & 0b0000_0001;
            cpu.decSp();
            if (r == Cpu.A) {
                memory.set(cpu.getSp(), cpu.getA());
            } else {
                memory.set(cpu.getSp(), cpu.getB());
            }
            cpu.incIp();
        } else if ((instr & 0b1111_1110) == 0b0001_0010) 
        { // POP r  |  r ← [SP++]; IP++
            int r = instr & 0b0000_0001;
            
            if (r == Cpu.A) {
                cpu.setA(memory.get(cpu.getSp()));
            } else {
                cpu.setB(memory.get(cpu.getSp()));
            }
            cpu.setSp(cpu.getSp() + 1);
            cpu.incIp();
        } else if (instr == 0b0001_0100)
        { // Mov A B |  B ← A; IP++
            cpu.setB(cpu.getA());
            cpu.incIp();
        } else if (instr == 0b0001_0101) 
        { // Mov B A  |  A ← B; IP++
            cpu.setA(cpu.getB());
            cpu.incIp();
        }else if(instr == 0b0001_0111)// DEC
        {
            cpu.setA(cpu.getA()-1);
            cpu.incIp();
            
        }else if(instr == 0b0001_0110) // INC
        {
            cpu.setA(cpu.getA() +1);
            cpu.incIp();
        }
        else if ((instr & 0b1111_0000) == 0b0010_0000) 
        { // MOV r o  |  [SP + o] ← r; IP++  
            int o = instr & 0b0000_0111;
            int r = (instr & 0b0000_1000) >> 3;
            if (r == cpu.A) {
                memory.set(cpu.getSp() + o, cpu.getA());
            } else {
                memory.set(cpu.getSp() + o, cpu.getB());
            }
            cpu.incIp();
        } else if ((instr & 0b1111_0000) == 0b0011_0000) 
        { // MOV  o r | r ← [SP + o]; IP++  0011 ooor
            int o = (instr & 0b0000_1110) >> 1;
            int r = instr & 0b0000_0001;
            if (r == cpu.A) {
                cpu.setA(memory.get(cpu.getSp() + o));
            } else {
                cpu.setB(memory.get(cpu.getSp() + o));
            }
            cpu.incIp();
        } else if ((instr & 0b1100_0000) == 0b0100_0000)
        { // MOV v r |  r ← v; IP++ 01vv vvvr
            int v = (instr & 0b0011_1110) >> 1; // the same as deviding by 2
            int r = instr & 0b0000_0001;
            if (r == Cpu.A) {
                cpu.setA(v);
            } else {
                cpu.setB(v);
            }
            cpu.incIp();
        } else if ((instr & 0b1100_0000) == 0b1100_0000) 
        {// CALL 11AAAAAA   11aa aaaa
            int a = (instr & 0b0011_1111);
            if (cpu.isFlag()) {
                cpu.decSp();
                memory.set(cpu.getSp(), cpu.getIp());
                cpu.setIp(a);
            } else {
                cpu.incIp();
            }
        } else if ((instr & 0b1000_0000) == 0b1000_0000) 
        { // JMP #a
            int a = (instr & 0b0011_1111);

            if (cpu.isFlag()) {
                cpu.setIp(a);
            } else {
                cpu.incIp();
            }
        }
    }

    public void print(PrintStream out) {
        memory.print(out);
        out.println("-------------");
        cpu.print(out);
    }
}
