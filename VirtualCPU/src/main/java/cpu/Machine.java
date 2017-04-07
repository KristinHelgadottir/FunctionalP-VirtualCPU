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
    // Get the next instruction and make shure it is not unsigned
    int instr = memory.get(cpu.getIp()); 
   
    if (instr == 0b0000_0000) 
    { // 0000 0000  NOP no operation
       cpu.incIp();
    }
    else if (instr == 0b0000_0001) 
    { // 0000 0001 ADD A B
       cpu.setA(cpu.getA() + cpu.getB());
       cpu.setIp(cpu.getIp() + 1);
    }
    else if (instr == 0b0000_0010)
    { // MUL A B
        cpu.setA(cpu.getA() * cpu.getB());
        cpu.setIp(cpu.getIp() + 1);
    }
    else if(instr == 0b0000_0011)
    { // DIV A B
        cpu.setA(cpu.getA() / cpu.getB());
        cpu.setIp(cpu.getIp() + 1);
    }
    else if (instr == 0b0000_0100)
    { // ZERO |	F ← A = 0; IP++
        cpu.setFlag(false); // Not shure
        cpu.incIp();
    }
    else if (instr == 0b0000_0101)
    { // NEG |	F ← A < 0; IP++
        // TODO...
        cpu.incIp();
    }
    else if (instr == 0b0000_0110)
    { // POS |	F ← A > 0; IP++
        // TODO...
        cpu.incIp();
    }
    else if (instr == 0b0000_0110)
    { // NZERO |    F ← A ≠ 0; IP++
        // TODO...
        cpu.incIp();
    }
    else if (instr == 0b0000_1000)
    { // EQ |	F ← A = B; IP++
        // TODO...
        cpu.incIp();
    }
    else if (instr == 0b0000_1001)
    {// LT |	F ← A < B; IP++
    }
    else if (instr == 0b0000_1010)
    {// GT |	F ← A > B; IP++
    }
    else if (instr == 0b0000_1011)
    {//NEQ |	F ← A ≠ B; IP++
    }
    else if (instr == 0b0000_1100)
    {// ALWAYS |	F ← true; IP++
    }
    else if (instr == 0b0000_1101)
    {// Undefined
    }
    else if (instr == 0b0000_1110)
    {// Undefined
    }
    else if (instr == 0b0000_1111)
    {//HALT  |	Halts execution
    }
    else if ((instr & 0b1111_1110) == 0b0001_0000) 
    {  // PUSH r  |  [--SP] ← r; IP++
        int r = instr & 0b0000_0001;
        cpu.decSp();
        if (r == Cpu.A){
            memory.set(cpu.getSp(), cpu.getA());
        }
        else{
            memory.set(cpu.getSp(), cpu.getB());
        }
        cpu.incIp();
    }
    else if ((instr & 0b1111_1110) == 0b0001_0000) 
    {  // PUSH r  |  [--SP] ← r; IP++
        int r = instr & 0b0000_0001;
        cpu.decSp();
        if (r == Cpu.A){
            memory.set(cpu.getSp(), cpu.getA());
        }
        else{
            memory.set(cpu.getSp(), cpu.getB());
        }
        cpu.incIp();
    }
    else if (instr == 0b1111_1110)
    {// Mov A B
    }
    else if ((instr & 0b1111_1110) == 0b0001_0010) 
    {// POP r  |  r ← [SP++]; IP++
        
        cpu.incIp();
    }
    else if ((instr & 0b1111_0000) == 0b0011_0000) 
    {// MOV  o r | r ← [SP + o]; IP++
        int o = (instr & 0b0000_1110) >> 1;
        int r = instr & 0b0000_0001;
        //......
        if (r == cpu.A){
            memory.set(cpu.getSp() + o, cpu.getA());
        }
        else{
            memory.set(cpu.getSp() + o, cpu.getB());
        }
        cpu.incIp();
    }
    else if ((instr & 0b1111_0000) == 0b0010_0000) 
    { // MOV r o  |  [SP + o] ← r; IP++  
      int o = instr & 0b0000_0111;
      int r = (instr & 0b0000_1000) >> 3;
      if (r == cpu.A){
          memory.set(cpu.getSp() + o, cpu.getA());
      }
      else{ 
          memory.set(cpu.getSp() + o, cpu.getB());
      }
      cpu.incIp();
    }
    else if ((instr & 0b1100_0000) == 0b0100_0000)  
    { // MOVE value register |  r ← v; IP++
        int v =(instr & 0b0011_1110) >> 1 ;// the same as deviding by 2
        int r = instr & 0b0000_0001;
        if (r == Cpu.A){
            cpu.setA(v);
        }
        else cpu.setB(v);
        cpu.incIp();
    }
}
  
  public void print(PrintStream out) {
    memory.print(out);
    out.println("-------------");
    cpu.print(out);
  }
}
