package cpu;


import java.io.PrintStream;

public class Machine 
{
  private Cpu cpu = new Cpu();
  private Memory memory = new Memory();
  
  public void load(Program program) 
  {
    int index = 0;
    for (int instr : program) 
    {
      memory.set(index++, instr);
    }
  }
  
  public void tick() 
  {
    int instr = memory.get(cpu.getIp());
    
    if (instr == 0b0000_0000) {
      // 0000 0000  NOP no operation
      cpu.incIp();
    }
    else if (instr == 0b0000_0001) 
    { // 0000 0001 ADD A B
      cpu.setA(cpu.getA() + cpu.getB());
      cpu.setIp(cpu.getIp() + 1);
    }
    else if ((instr & 0b1100_0000) == 0b0100_0000) //0b = binary number 
    {// MOVE v r  --  r ← v; IP++
        int v =(instr & 0b0011_1110) >> 1 ;// the same as deviding by 2
        int r = instr & 0b0000_0001;
        if (r == Cpu.A) cpu.setA(v);
        else cpu.setB(v);
        cpu.incIp();
    }
    else if ((instr & 0b1111_1110) == 0b0001_0000) // instr & 0b1111_1110
    {
        // PUSH r  --  [--SP] ← r; IP++
        int r = instr & 0b0000_0001;
        cpu.decSp();
        if (r == Cpu.A) memory.set(cpu.getSp(), cpu.getA());
        else memory.set(cpu.getSp(), cpu.getB());
        cpu.incIp();
    }
    else if ((instr & 0b1111_1110) == 0b0001_0010) 
    {// POP r  --  r ← [SP++]; IP++
    
    }
    else if ((instr & 0b1111_0000) == 0b0011_0000) 
    {// MOV  o r
        int o = (instr & 0b0000_1110) >> 1;
        int r = instr & 0b0000_0001;
        //......
        cpu.incIp();
    }
    else if ((instr & 0b1111_0000) == 0b0010_0000) 
    {
      // 0010 r ooo	MOV r o	   [SP + o] ← r; IP++
      // 0010 1 011 MOV B (=1) +3  [SP +3] // Move register B to memory position of SP with offset 3
      
      // 00101011 finding instruction
      //    and
      // 11110000
      // --------
     //  00100000 = 32
      
      // 00101011 finding offset
      //    and
      // 00000111
      // --------
      // 00000011 = 3
      
      // 00101011 finding register
      //    and
      // 00001000
      // --------
      // 00001000 = 8
      //    >> 3
      // 00000001 = 1
      
      int o = instr & 0b0000_0111;
      int r = (instr & 0b0000_1000) >> 3;
      if (r == cpu.A) memory.set(cpu.getSp() + o, cpu.getA());
      else memory.set(cpu.getSp() + o, cpu.getB());
      cpu.incIp();
    }
}
  
  public void print(PrintStream out) {
    memory.print(out);
    out.println("-------------");
    cpu.print(out);
    }
  
  }
