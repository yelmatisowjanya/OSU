
/*
Description:
The cpu subsystem is called by the system.The cpu is divided into two modules
cpu and instruction set in order to maintain modularity.The below code simply
assigns the values to appropriate registers and checks for the values out of
range,and writes to trace file.The Operations are performed in the functions.
It loops indefinitely until a error occours.The below variables are used as
static because they are accessed by the instruction set class which is below.
Each instruction is written in switch case to bring down the number of if loops and maintain
modularity.Each opcode is being written from function everytime using memory function call and
based on opcode the instruction is performed.This functionality takes care of the exceptions and
throw them in error handler class.  THE cpu generates the trace file when it is set to 1 in tracefile
The cpu class has lot of global variables which are used throughout the class to retain their vales in different classes*/

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CPU extends SYSTEM {
 String OP_CODE;
 String current_instruction;
 String[] CPU_registers = new String[10];
 String R0 = "0";
 String R1 = "1";
 String contentsOfEABefore;
 static String R2, R3, R7 = "000", R6 = "000";
 String[] instruction_1_array = new String[13];
 String[] instruction_2_array = new String[13];
 String[] instruction_3_array = new String[13];
 String[] instruction_4_array = new String[13];
 String tempaccess;
 static int traceCount = 0;
 static String finalOutput;
 static int headingcount = 0;
 static int intitalcount = 0;
 static ArrayList<String> traceFile = new ArrayList<String>(Arrays.asList());
 static String file = "trace_file";
 
 public static String TRACEFILENAME = "";

 public CPU(int X, int Y, PROCESSMANAGER PCB) {
   String name="tb";
  PCB.JobClockIn = CLOCK;
  int EA_decimal = 0;
  int counttemp = 0;
  while (true) {
   if ((CLOCK - PCB.JobClockIn) > SYSTEM.timer)
   {
    new ERROR_HANDLER(140, PCB);
   }

   R2 = Integer.toString(PCB.JobPC);
   String tracename = Integer.toString(PCB.jobid);
   TRACEFILENAME = tracename;
   CPU_registers[0] = R0;
   CPU_registers[1] = R1;
   CPU_registers[2] = R2;
   CPU_registers[3] = R3;
   CPU_registers[4] = PCB.R4;
   CPU_registers[5] = PCB.R5;
   String fetchOpcodefromMemory = MEMORY.memory("READ", PCB.JobPC, tempaccess,
     PCB);
   OP_CODE = fetchOpcodefromMemory.substring(1, 4);
   String addressmodeoutput;
   String Effectiveaddress;
   String R_bit_Instuction1;
   String R_bit_Instuction2;
   String readbit;
   String writebit;
   String haltbit;
   PCB.traceSwitch = Y;
   String contentsOfEA;
   StringBuilder sbf = new StringBuilder();
   traceFile.clear();
 
   switch (OP_CODE) {
   case "000": /*
                * Logical AND Makes a function call which redirects to a
                * function for calculating the effective mode using addressing
                * mode Instruction 1 type
                */
    if (PCB.TimeSlice > 35) {
     SCHEDULER.TimeSliceCheck();
    }
    if (PCB.traceSwitch == 1) {
     contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
     String PC_temp = Integer.toString(PCB.JobPC);
     traceFile.addAll(Arrays.asList(PC_temp));
    }
    PCB.JobPC = PCB.JobPC + 1;
    CLOCK = CLOCK + 1;
    PCB.TimeSlice = PCB.TimeSlice + 1;
    addressmodeoutput = calculateInstruction1Addressingmode(PCB.JobPC, PCB);
    Effectiveaddress = calculate_effective_address(addressmodeoutput, PCB);
    EA_decimal = Integer.parseInt(Effectiveaddress, 2);
    R_bit_Instuction1 = instruction_1_array[4];

    if (R_bit_Instuction1.equals("0")) {
     String tempR = PCB.R5;
     int temp_m = Integer.parseInt(PCB.R5, 2);
     String temp_EA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
     int temp_n = Integer.parseInt(temp_EA, 2);
     temp_n = temp_m & temp_n;
     String R5AddOperation = Decimal_to_binary(temp_n);
     PCB.R5 = R5AddOperation;
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList(current_instruction, "R5", EA_String,
        tempR, contentsOfEABefore, PCB.R5, contentsOfEA));
      traceFileGeneration(traceFile, PCB);
     }

    } else if (R_bit_Instuction1.equals("1")) {
     String tempR = PCB.R4;
     int temp_m = Integer.parseInt(PCB.R4, 2);
     String temp_EA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
     int temp_n = Integer.parseInt(temp_EA, 2);
     temp_n = temp_m & temp_n;
     String R4AddOperation = Decimal_to_binary(temp_n);
     PCB.R4 = R4AddOperation;

     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList(current_instruction, "R4", EA_String,
        tempR, contentsOfEABefore, PCB.R4, contentsOfEA));
      traceFileGeneration(traceFile, PCB);
     }
    }
    break;

   case "001":
    /*
     * Addition Makes a function call which redirects to a function for
     * calculating the effective mode using addressing mode Instruction 1 type
     */

    if (PCB.TimeSlice > 35) {

     SCHEDULER.TimeSliceCheck();
    }
    if (PCB.traceSwitch == 1) {
     contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
     String PC_temp = Integer.toString(PCB.JobPC);
     traceFile.addAll(Arrays.asList(PC_temp));

    }
    PCB.JobPC = PCB.JobPC + 1;
    CLOCK = CLOCK + 1;
    PCB.TimeSlice = PCB.TimeSlice + 1;
    addressmodeoutput = calculateInstruction1Addressingmode(PCB.JobPC, PCB);
    Effectiveaddress = calculate_effective_address(addressmodeoutput, PCB);
    EA_decimal = Integer.parseInt(Effectiveaddress, 2);
    R_bit_Instuction1 = instruction_1_array[4];
    if (instruction_1_array[4].equals("0")) {
     String tempR = PCB.R5;
     String temp1 = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
    
     PCB.R5 = checkoverflowbit(PCB.R5, temp1, "", PCB);
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList(current_instruction, "R5", EA_String,
        tempR, contentsOfEABefore, PCB.R5, contentsOfEA));
      traceFileGeneration(traceFile, PCB);
     }

    } else if (instruction_1_array[4].equals("1")) {
     String tempR = PCB.R4;
     String temp1 = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
  
     PCB.R4 = checkoverflowbit(PCB.R4, temp1, "", PCB);
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList(current_instruction, "R4", EA_String,
        tempR, contentsOfEABefore, PCB.R4, contentsOfEA));
      traceFileGeneration(traceFile, PCB);
     }
    }

    break;
   case "010": /*
                * Store the contents of register R in memory location EA(Store)
                * Makes a function call which redirects to a function for
                * calculating the effective mode using addressing mode
                * Instruction 1 type
                * 
                */

    if (PCB.TimeSlice > 35) {

     SCHEDULER.TimeSliceCheck();

    }
    if (PCB.traceSwitch == 1) {
     contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
     String PC_temp = Integer.toString(PCB.JobPC);
     traceFile.addAll(Arrays.asList(PC_temp));
    }

    PCB.JobPC = PCB.JobPC + 1;
    CLOCK = CLOCK + 1;
    PCB.TimeSlice = PCB.TimeSlice + 1;
    addressmodeoutput = calculateInstruction1Addressingmode(PCB.JobPC, PCB);

    Effectiveaddress = calculate_effective_address(addressmodeoutput, PCB);
    EA_decimal = Integer.parseInt(Effectiveaddress, 2);
    if (instruction_1_array[4].equals("0")) {
     String tempR = PCB.R5;
     String R5temp = String.format("%12s", tempR).replace(' ', '0');

     MEMORY.memory("WRIT", EA_decimal, PCB.R5, PCB);
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      if (counttemp == 0) {
       traceFile.addAll(Arrays.asList(current_instruction, "R5", EA_String,
         tempR, "NA", PCB.R5, contentsOfEA));
       counttemp++;
      } else {
       traceFile.addAll(Arrays.asList(current_instruction, "R5", EA_String,
         tempR, "NA", PCB.R5, contentsOfEA));
      }
      traceFileGeneration(traceFile, PCB);
     }

    } else if (instruction_1_array[4].equals("1")) {
     String tempR = PCB.R4;
     String R4temp = String.format("%12s", tempR).replace(' ', '0');
     MEMORY.memory("WRIT", EA_decimal, PCB.R4, PCB);
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList(current_instruction, "R4", EA_String,
        tempR, contentsOfEABefore, PCB.R4, contentsOfEA));
      traceFileGeneration(traceFile, PCB);
     }
    }

    break;

   case "011":
    /*
     * Load the contents of memory location EA in memory location R(LOAD) Makes
     * a function call which redirects to a function for calculating the
     * effective mode using addressing mode Instruction 1 type
     */
    if (PCB.TimeSlice > 35) {

     SCHEDULER.TimeSliceCheck();

    }
    if (PCB.traceSwitch == 1) {
     contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
     String PC_temp = Integer.toString(PCB.JobPC);
     traceFile.addAll(Arrays.asList(PC_temp));
    }
    CLOCK = CLOCK + 1;
    PCB.TimeSlice = PCB.TimeSlice + 1;
    PCB.JobPC = PCB.JobPC + 1;
    addressmodeoutput = calculateInstruction1Addressingmode(PCB.JobPC, PCB);
    Effectiveaddress = calculate_effective_address(addressmodeoutput, PCB);
    EA_decimal = Integer.parseInt(Effectiveaddress, 2);
    if (instruction_1_array[4].equals("0")) {
     String tempR = PCB.R5;
     PCB.R5 = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList(current_instruction, "R5", EA_String,
        tempR, contentsOfEABefore, PCB.R5, contentsOfEA));
      traceFileGeneration(traceFile, PCB);
     }
    } else if (instruction_1_array[4].equals("1")) {

     String tempR = PCB.R4;
     PCB.R4 = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList(current_instruction, "R4", EA_String,
        tempR, contentsOfEABefore, PCB.R4, contentsOfEA));
      traceFileGeneration(traceFile, PCB);
     }
    }
    break;
   case "100":
    /*
     * Transfer control to the instruction at memory location EA.(JMP) Makes a
     * function call which redirects to a function for calculating the effective
     * mode using addressing mode Instruction 1 type
     */
    if (PCB.TimeSlice > 35) {

     SCHEDULER.TimeSliceCheck();
    }

    if (PCB.traceSwitch == 1) {
     contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
     String PC_temp = Integer.toString(PCB.JobPC);
     traceFile.addAll(Arrays.asList(PC_temp));
    }

    CLOCK = CLOCK + 1;
    PCB.TimeSlice = PCB.TimeSlice + 1;
    PCB.JobPC = PCB.JobPC + 1;

    addressmodeoutput = calculateInstruction1Addressingmode(PCB.JobPC, PCB);
    Effectiveaddress = calculate_effective_address(addressmodeoutput, PCB);
    EA_decimal = Integer.parseInt(Effectiveaddress, 2);
    if (instruction_1_array[4].equals("0")) {
     String tempR = PCB.R5;
     PCB.JobPC = EA_decimal;
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList(current_instruction, "R5", EA_String,
        tempR, contentsOfEABefore, PCB.R5, contentsOfEA));
      traceFileGeneration(traceFile, PCB);
     }
    } else if (instruction_1_array[4].equals("1")) {
     String tempR = PCB.R4;
     PCB.JobPC = EA_decimal;
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList(current_instruction, "R4", EA_String,
        tempR, contentsOfEABefore, PCB.R4, contentsOfEA));
      traceFileGeneration(traceFile, PCB);
     }

    }
    break;
   case "101":
    /*
     * Save the Makes a function call which redirects to a function for
     * calculating the effective mode using addressing mode Instruction 1 type
     */
    if (PCB.TimeSlice > 35) {

     SCHEDULER.TimeSliceCheck();
    }
    if (PCB.traceSwitch == 1) {
     contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
     String PC_temp = Integer.toString(PCB.JobPC);
     traceFile.addAll(Arrays.asList(PC_temp));
    }
    CLOCK = CLOCK + 1;
    PCB.TimeSlice = PCB.TimeSlice + 1;
    PCB.JobPC = PCB.JobPC + 1;
    addressmodeoutput = calculateInstruction1Addressingmode(PCB.JobPC, PCB);
    Effectiveaddress = calculate_effective_address(addressmodeoutput, PCB);
    EA_decimal = Integer.parseInt(Effectiveaddress, 2);

    if (instruction_1_array[4].equals("0")) {
     String tempR = PCB.R5;
     String R5_binary = Decimal_to_binary(PCB.JobPC);
     PCB.R5 = R5_binary;
     PCB.JobPC = EA_decimal;
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList(current_instruction, "R5", EA_String,
        tempR, contentsOfEABefore, PCB.R5, contentsOfEA));
      traceFileGeneration(traceFile, PCB);
     }

    } else if (instruction_1_array[5].equals("1")) {
     String tempR = PCB.R4;
     String R4_binary = Decimal_to_binary(PCB.JobPC);
     PCB.R4 = R4_binary;
     PCB.JobPC = EA_decimal;
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList(current_instruction, "R4", EA_String,
        tempR, contentsOfEABefore, PCB.R4, contentsOfEA));
      traceFileGeneration(traceFile, PCB);
     }
    }
    break;
   case "110":
    /*
     * Opcode 110 specifies the 2nd instruction The below instruction is used to
     * perform the input and output instructions
     */

    current_instruction = MEMORY.memory("READ", PCB.JobPC, tempaccess, PCB);
    instruction_2_array = current_instruction.split("");
    R_bit_Instuction2 = instruction_2_array[4];
    readbit = instruction_2_array[5];
    writebit = instruction_2_array[6];
    haltbit = instruction_2_array[7];
    int count = 0;
    /*
     * Get a 3 digit Hex number from the keyboard i.e with read bit set and R5
     * set
     */
    if (R_bit_Instuction2.equals("0") && readbit.equals("1")) {

     if (PCB.TimeSlice > 35) {
      SCHEDULER.TimeSliceCheck();
     }
     if (PCB.traceSwitch == 1) {

      String PC_temp = Integer.toString(PCB.JobPC);
      traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
     }
     PCB.JobPC = PCB.JobPC + 1;
     CLOCK = CLOCK + 1;
     PCB.TimeSlice = PCB.TimeSlice + 1;
     IOClock = IOClock + 10;
     PCB.EachJobIO = PCB.EachJobIO + 1;
     PCB.JobClockOut = CLOCK;
     PCB.dataLinesBaseAddressPointer = PCB.JobLength;
     PCB.dataLinesBoundAddressPointer = PCB.dataLinesBaseAddressPointer
       + PCB.DataLinesLength;
     MEMORY.IO_read_operation1(PCB, PCB.dataLinesBaseAddressPointer,
       PCB.dataLinesBoundAddressPointer);
     if (PCB.InputLines.get(PCB.index).length() > 12) {
      new ERROR_HANDLER(109, PCB);
     }
     for (int m = 1; m < PCB.InputLines.get(PCB.index).length(); m++) {
      if (Character.digit(PCB.InputLines.get(PCB.index).charAt(m), 16) == -1) {
       new ERROR_HANDLER(110, PCB);
      }
     }
     if (PCB.index < PCB.InputLines.size()) {
      String input_temp = PCB.InputLines.get(PCB.index);
      // String input = Integer.toBinaryString(input_temp);
      // input=String.format("%12s", input).replace(' ', '0');
      PCB.R5 = PCB.InputLines.get(PCB.index);
      count++;
     }

     if (PCB.traceSwitch == 1) {
      String tempR = PCB.R5;
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList("R5", "NA", "NA", "NA", PCB.R5, "NA"));
      traceFileGeneration(traceFile, PCB);
     }
     PCB.JobClockOut = CLOCK;
     PCB.JobStatus = 0;
     PCB.index++;
     SCHEDULER.ScheduleJob();
    }
    /*
     * Get a 3 digit Hex number from the keyboard i.e with read bit set and R4
     * set
     */
    if (R_bit_Instuction2.equals("1") && readbit.equals("1")) {

     if (PCB.TimeSlice > 35) {
      SCHEDULER.TimeSliceCheck();
     }
     String tempR = "";
     if (PCB.traceSwitch == 1) {

      tempR = PCB.R4;
      String PC_temp = Integer.toString(PCB.JobPC);
      traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
     }
     PCB.JobPC = PCB.JobPC + 1;
     CLOCK = CLOCK + 1;
     IOClock = IOClock + 10;
     PCB.EachJobIO = PCB.EachJobIO + 1;
     PCB.JobClockOut = CLOCK;
     PCB.TimeSlice = PCB.TimeSlice + 1;
     PCB.dataLinesBaseAddressPointer = PCB.JobLength;
     PCB.dataLinesBoundAddressPointer = PCB.dataLinesBaseAddressPointer
       + PCB.DataLinesLength;
     MEMORY.IO_read_operation1(PCB, PCB.dataLinesBaseAddressPointer,
       PCB.dataLinesBoundAddressPointer);
     if (PCB.InputLines.get(PCB.index).length() > 12) {
      new ERROR_HANDLER(109, PCB);
     }
     for (int m = 1; m < PCB.InputLines.get(PCB.index).length(); m++) {
      if (Character.digit(PCB.InputLines.get(PCB.index).charAt(m), 16) == -1) {
       new ERROR_HANDLER(110, PCB);
      }
     }
     if (PCB.index < PCB.InputLines.size()) {
      // int input_temp = Integer.parseInt(PCB.InputLines.get(PCB.index), 16);
      // String input = Integer.toBinaryString(input_temp);
      // input=String.format("%12s", input).replace(' ', '0');
      PCB.R4 = PCB.InputLines.get(PCB.index);
      count++;
     }
     if (PCB.traceSwitch == 1) {

      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList("R4", "NA", tempR, "NA", PCB.R4, "NA"));
      traceFileGeneration(traceFile, PCB);
     }
     PCB.JobStatus = 0;
     PCB.index++;
     SCHEDULER.ScheduleJob();

    }
    /* Contents of R5 will be displayed on screen via system */
    if (R_bit_Instuction2.equals("0") && writebit.equals("1")) {

     if (PCB.TimeSlice > 35) {
      SCHEDULER.TimeSliceCheck();
     }
     String tempR = "";
     if (PCB.traceSwitch == 1) {

      tempR = PCB.R5;
      String PC_temp = Integer.toString(PCB.JobPC);
      traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
     }
     PCB.JobPC = PCB.JobPC + 1;
     CLOCK = CLOCK + 1;
     IOClock = IOClock + 10;
     PCB.EachJobIO = PCB.EachJobIO + 1;
     PCB.TimeSlice = PCB.TimeSlice + 1;
     PCB.JobClockOut = CLOCK;
     String outputInHex = LOADER.binHex(PCB.R5);    
     finalOutput = outputInHex;
     if(PCB.tobewrittencount==0)
    {
     PCB.OutputLinesBaseAddressPointer = PCB.dataLinesBoundAddressPointer;
     PCB.OutputLinesBoundAddressPointer = PCB.OutputLinesBaseAddressPointer
       + PCB.outputlineslength;
     PCB.tobewrittencount++;
     }
     MEMORY.IO_write_operation1(PCB, PCB.OutputLinesBaseAddressPointer,
       PCB.OutputLinesBoundAddressPointer, PCB.R5);
     count++;
     PCB.OutputLinesBaseAddressPointer= PCB.OutputLinesBaseAddressPointer+1;
     if (PCB.traceSwitch == 1) {
      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList("R5", "NA", tempR, "NA", PCB.R5, "NA"));
      traceFileGeneration(traceFile, PCB);
     }

     PCB.JobStatus = 0;
     SCHEDULER.ScheduleJob();
    }
    /* Contents of R4 will be displayed on screen via system */
    if (R_bit_Instuction2.equals("1") && writebit.equals("1")) {

     if (PCB.TimeSlice > 35) {
      SCHEDULER.TimeSliceCheck();
     }
     String tempR = "";
     if (PCB.traceSwitch == 1) {
      tempR = PCB.R4;
      String PC_temp = Integer.toString(PCB.JobPC);
      traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
     }
     PCB.JobPC = PCB.JobPC + 1;
     CLOCK = CLOCK + 1;
     IOClock = IOClock + 10;
     PCB.EachJobIO = PCB.EachJobIO + 1;
     PCB.TimeSlice = PCB.TimeSlice + 1;
     PCB.JobClockOut = CLOCK;
     String outputInHex = LOADER.binHex(PCB.R4);     
     finalOutput = outputInHex;
     if(PCB.tobewrittencount==0)
     {
     PCB.OutputLinesBaseAddressPointer = PCB.dataLinesBoundAddressPointer;
     PCB.OutputLinesBoundAddressPointer = PCB.OutputLinesBaseAddressPointer
       + PCB.outputlineslength;
     PCB.tobewrittencount++;
     }
     MEMORY.IO_write_operation1(PCB, PCB.OutputLinesBaseAddressPointer,
       PCB.OutputLinesBoundAddressPointer, PCB.R4);
     PCB.OutputLinesBaseAddressPointer=PCB.OutputLinesBaseAddressPointer+1;
     count++;
     if (PCB.traceSwitch == 1) {

      contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
      String EA_String = Integer.toString(EA_decimal);
      traceFile.addAll(Arrays.asList("R4", "NA", tempR, "NA", PCB.R4, "NA"));
      traceFileGeneration(traceFile, PCB);
     }
     PCB.JobStatus = 0;
     SCHEDULER.ScheduleJob();
    }
    /* Halt bit is set which terminates the execution based on R bit */
    if (haltbit.equals("1")) {
    
     if (PCB.TimeSlice > 35) {
      SCHEDULER.TimeSliceCheck();
     }

     if (PCB.traceSwitch == 1) {

      String PC_temp = Integer.toString(PCB.JobPC);
      traceFile.addAll(Arrays.asList(PC_temp, current_instruction, "NA", "NA",
        "NA", "NA", "NA", "NA"));
      traceFileGeneration(traceFile, PCB);
     }
     PCB.JobPC = PCB.JobPC + 1;
     CLOCK = CLOCK + 1;
     PCB.TimeSlice = PCB.TimeSlice + 1;
     PCB.JobClockOut = CLOCK;
     count++;
     SYSTEM.DisplayClock();
    
     PCB.JobStatus = 1;
     SCHEDULER.haltJob();
   

    }

    /*
     * If more than one bit is set, than an exception is thrown in Error handler
     * class
     */
    if (count > 1) {
     new ERROR_HANDLER(108, PCB);
    }

    break;
   case "111":
    /*
     * Opcode 111 specifies the 3rd and 4th instruction based on the bit0 Based
     * on bit0 we decide the instruction type i.e either type 3 or type 4
     */

    current_instruction = MEMORY.memory("READ", PCB.JobPC, tempaccess, PCB);
    instruction_3_array = current_instruction.split("");
    String flag = "true";
    int count1 = 0;
    if (instruction_3_array[0].equals("0")) {

     String tempR = "";
     /* Clear bit is set */
     if (instruction_3_array[5].equals("1")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }

      if (PCB.traceSwitch == 1) {
       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
      }
      flag = "false";
      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;
      PCB.JobPC = PCB.JobPC + 1;
      if (instruction_3_array[4].equals("0")) {
       tempR = PCB.R5;
       PCB.R5 = "000000000000";
       if (PCB.traceSwitch == 1) {
        contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
        String EA_String = Integer.toString(EA_decimal);
        traceFile.addAll(Arrays.asList("R5", "NA", tempR, "NA", PCB.R5, "NA"));
        traceFileGeneration(traceFile, PCB);
       }
      } else if (instruction_3_array[4].equals("1")) {
       tempR = PCB.R4;
       PCB.R4 = "000000000000";
       if (PCB.traceSwitch == 1) {
        contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
        String EA_String = Integer.toString(EA_decimal);
        traceFile.addAll(Arrays.asList("R4", "NA", tempR, "NA", PCB.R4, "NA"));
        traceFileGeneration(traceFile, PCB);
       }
      }

     }

     /* Increment bit is set */
     if (instruction_3_array[6].equals("1")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {
       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;
      PCB.JobPC = PCB.JobPC + 1;
      count1++;
      if (instruction_3_array[4].equals("0")) {
       tempR = PCB.R5;
       // String R5Temp = String.format("%12s", R5).replace(' ', '0');
       PCB.R5 = checkoverflowbit(PCB.R5, "000000000001", "", PCB);
       if (PCB.traceSwitch == 1) {
        contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
        String EA_String = Integer.toString(EA_decimal);
        traceFile.addAll(Arrays.asList("R5", "NA", tempR, "NA", PCB.R5, "NA"));
        traceFileGeneration(traceFile, PCB);
       }

      } else if (instruction_3_array[4].equals("1")) {
       tempR = PCB.R4;
       String R4Temp = String.format("%12s", PCB.R4).replace(' ', '0');
       PCB.R4 = checkoverflowbit(PCB.R4, "000000000001", "", PCB);
       if (PCB.traceSwitch == 1) {

        contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
        String EA_String = Integer.toString(EA_decimal);
        traceFile.addAll(Arrays.asList("R4", "NA", tempR, "NA", PCB.R4, "NA"));
        traceFileGeneration(traceFile, PCB);
       }
      }

     }
     /* Compliment bit is set */
     if (instruction_3_array[7].equals("1")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {

       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;
      PCB.JobPC = PCB.JobPC + 1;
      count1++;
      if (instruction_3_array[4].equals("0")) {
       tempR = PCB.R5;

       // String bin[] = new String[R5.length()];
       String bin[] = PCB.R5.split("");
       for (int j = 0; j < PCB.R5
         .length(); j++)/* loop for obtaining 1's complement */
       {
        if (bin[j].equals("1")) {
         bin[j] = "0";
         sbf.append(bin[j]);
        } else if (bin[j].equals("0")) {
         bin[j] = "1";
         sbf.append(bin[j]);
        }
       }

       PCB.R5 = sbf.toString();
       if (PCB.traceSwitch == 1) {

        contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
        String EA_String = Integer.toString(EA_decimal);
        traceFile.addAll(Arrays.asList("R5", "NA", tempR, "NA", PCB.R5, "NA"));
        traceFileGeneration(traceFile, PCB);
       }
      }

      else if (instruction_3_array[4].equals("1")) {
       tempR = PCB.R4;
       count1++;
       // String bin[] = new String[R4.length()];
       String bin[] = PCB.R4.split("");
       /* loop for obtaining 1's complement */
       for (int j = 0; j < PCB.R4.length(); j++) {
        if (bin[j].equals("1")) {
         bin[j] = "0";
         sbf.append(bin[j]);
        } else if (bin[j].equals("0")) {
         bin[j] = "1";
         sbf.append(bin[j]);
        }
       }
       PCB.R4 = sbf.toString();
       if (PCB.traceSwitch == 1) {
        contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
        String EA_String = Integer.toString(EA_decimal);
        traceFile.addAll(Arrays.asList("R4", "NA", PCB.R4, "NA", tempR, "NA"));
        traceFileGeneration(traceFile, PCB);
       }
      }
     }
     /* Byte Swap bit is set */
     if (instruction_3_array[8].equals("1")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {
       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;
      PCB.JobPC = PCB.JobPC + 1;

      count1++;
      if (instruction_3_array[4].equals("0")) {
       tempR = PCB.R5;
       if (PCB.traceSwitch == 1) {
        contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
        String EA_String = Integer.toString(EA_decimal);

        String byteswapBefore = PCB.R5.substring(0, 5);
        String byteswapAfter = PCB.R5.substring(6, 11);
        PCB.R5 = byteswapAfter + byteswapBefore;
        traceFile.addAll(Arrays.asList("R5", "NA", tempR, "NA", PCB.R5, "NA"));
        traceFileGeneration(traceFile, PCB);
       }
      } else if (instruction_3_array[4].equals("1")) {
       tempR = PCB.R4;
       if (PCB.traceSwitch == 1) {
        contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
        String EA_String = Integer.toString(EA_decimal);
        String byteswapBefore = PCB.R4.substring(0, 5);
        String byteswapAfter = PCB.R4.substring(6, 11);
        PCB.R4 = byteswapAfter + byteswapBefore;
        traceFile.addAll(Arrays.asList("R4", "NA", tempR, "NA", PCB.R4, "NA"));
        traceFileGeneration(traceFile, PCB);
       }
      }
     }
     /*
      * The rotate left bit is set and the number of bits to be rotated depends
      * on shift magnitude Here we check that if the shift magnitude bit is 0
      * ,therefore the bit is rotated to left by 1 bit
      */
     if (instruction_3_array[9].equals("1")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {

       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;
      PCB.JobPC = PCB.JobPC + 1;
      count1++;
      if (instruction_3_array[11].equals("0")) {
       if (instruction_3_array[4].equals("0")) {
        tempR = PCB.R5;
        String rotate_leftBefore = PCB.R5.substring(0, 1);
        String rotate_leftAfter = PCB.R5.substring(1, 12);
        PCB.R5 = rotate_leftAfter + rotate_leftBefore;

        if (PCB.traceSwitch == 1) {

         contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
         String EA_String = Integer.toString(EA_decimal);
         traceFile.addAll(Arrays.asList("R4", "NA", tempR, "NA", PCB.R5, "NA"));
         traceFileGeneration(traceFile, PCB);
        }
       } else if (instruction_3_array[4].equals("1")) {
        tempR = PCB.R4;
        String rotate_leftBefore = PCB.R4.substring(0, 1);
        String rotate_leftAfter = PCB.R4.substring(1, 12);
        PCB.R4 = rotate_leftAfter + rotate_leftBefore;

        if (PCB.traceSwitch == 1) {
         contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
         String EA_String = Integer.toString(EA_decimal);
         traceFile.addAll(Arrays.asList("R4", "NA", tempR, "NA", PCB.R4, "NA"));
         traceFileGeneration(traceFile, PCB);
        }
       }
      }

      /*
       * The rotate left bit is set and the number of bits to be rotated depends
       * on shift magnitude Here we check that if the shift magnitude magnitude
       * bit is 1 ,therefore the bit is rotated to left by 2 bit
       */
      if (instruction_3_array[9].equals("1")) {
       if (PCB.traceSwitch == 1) {

        contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
        String PC_temp = Integer.toString(PCB.JobPC);
        traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
       }

       count1++;
       if (instruction_3_array[11].equals("1")) {
        if (instruction_3_array[4].equals("0")) {
         tempR = PCB.R5;
         String rotateLeftBefore = PCB.R5.substring(0, 2);
         String rotateLeftAfter = PCB.R5.substring(2, 12);
         PCB.R5 = rotateLeftAfter + rotateLeftBefore;

         if (PCB.traceSwitch == 1) {
          contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
          String EA_String = Integer.toString(EA_decimal);
          traceFile
            .addAll(Arrays.asList("R5", "NA", tempR, "NA", PCB.R5, "NA"));
          traceFileGeneration(traceFile, PCB);
         }
        } else if (instruction_3_array[4].equals("1")) {
         tempR = PCB.R4;
         String rotateLeftBefore = PCB.R4.substring(0, 2);
         String rotateLeftAfter = PCB.R4.substring(2, 12);
         PCB.R4 = rotateLeftAfter + rotateLeftBefore;
         if (PCB.traceSwitch == 1) {
          contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
          String EA_String = Integer.toString(EA_decimal);
          traceFile
            .addAll(Arrays.asList("R4", "NA", tempR, "NA", PCB.R4, "NA"));
          traceFileGeneration(traceFile, PCB);
         }
        }
       }
      }
     }
     /*
      * The rotate right bit is set and the number of bits to be rotated depends
      * on shift magnitude Here we check that if the shift magnitude magnitude
      * bit is 1 ,therefore the bit is rotated to right by 1 bit
      */
     if (instruction_3_array[10].equals("1")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {

       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;
      PCB.JobPC = PCB.JobPC + 1;
      count1++;
      if (instruction_3_array[11].equals("0")) {
       if (instruction_3_array[4].equals("0")) {
        tempR = PCB.R5;
        String rotate_right_first = PCB.R5.substring(0, 11);
        String rotate_right_last = PCB.R5.substring(11, 12);
        PCB.R5 = rotate_right_last + rotate_right_first;
        if (PCB.traceSwitch == 1) {
         contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
         String EA_String = Integer.toString(EA_decimal);
         traceFile.addAll(Arrays.asList("R5", "NA", tempR, "NA", PCB.R5, "NA"));
         traceFileGeneration(traceFile, PCB);
        }
       } else if (instruction_3_array[4].equals("1")) {
        tempR = PCB.R4;
        String rotate_right_first = PCB.R4.substring(0, 11);
        String rotate_right_last = PCB.R4.substring(11, 12);
        PCB.R4 = rotate_right_last + rotate_right_first;
        if (PCB.traceSwitch == 1) {
         contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
         String EA_String = Integer.toString(EA_decimal);
         traceFile.addAll(Arrays.asList("R4", "NA", tempR, "NA", PCB.R4, "NA"));
         traceFileGeneration(traceFile, PCB);
        }
       }
      }

      /*
       * The rotate right bit is set and the number of bits to be rotated
       * depends on shift magnitude Here we check that if the shift magnitude
       * magnitude bit is 1 ,therefore the bit is rotated to right by 2 bit
       */
      if (instruction_3_array[10].equals("1")) {
       if (PCB.traceSwitch == 1) {
        contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
        String PC_temp = Integer.toString(PCB.JobPC);
        traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
       }

       count1++;
       if (instruction_3_array[11].equals("1")) {
        if (instruction_3_array[4].equals("0")) {
         tempR = PCB.R5;
         String rotateRightFirst = PCB.R5.substring(0, 10);
         String rotateRightLast = PCB.R5.substring(10, 12);
         PCB.R5 = rotateRightLast + rotateRightFirst;
         if (PCB.traceSwitch == 1) {
          contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
          String EA_String = Integer.toString(EA_decimal);
          traceFile
            .addAll(Arrays.asList("R5", "NA", tempR, "NA", PCB.R5, "NA"));
          traceFileGeneration(traceFile, PCB);
         }
        } else if (instruction_3_array[4].equals("1")) {
         String rotateRightFirst = PCB.R4.substring(0, 10);
         String rotateRightLast = PCB.R4.substring(10, 12);
         PCB.R4 = rotateRightLast + rotateRightFirst;
         if (PCB.traceSwitch == 1) {
          contentsOfEA = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
          String EA_String = Integer.toString(EA_decimal);
          traceFile
            .addAll(Arrays.asList("R4", "NA", tempR, "NA", PCB.R4, "NA"));
          traceFileGeneration(traceFile, PCB);
         }
        }
       }
      }
     }
    }
    /* Checks the wrong combination and throws the error */
    if (count1 > 1 && flag.equals("false")) {
     new ERROR_HANDLER(112, PCB);
    }
    /*
     * The below else condition is used to check if the instruction is type 4
     * with opcode 111
     */
    else if (instruction_3_array[0].equals("1")) {
     current_instruction = MEMORY.memory("READ", PCB.JobPC, tempaccess, PCB);
     instruction_4_array = current_instruction.split("");
     /* The NSK bit is set */
     if (instruction_4_array[5].equals("0")
       && instruction_4_array[6].equals("0")
       && instruction_4_array[7].equals("0")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {
       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction));
      }
      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;
      PCB.JobPC = PCB.JobPC + 1;
      PCB.JobPC = PCB.JobPC + 1;

     }
     /* The GTR bit is set */
     else if (instruction_4_array[5].equals("0")
       && instruction_4_array[6].equals("0")
       && instruction_4_array[7].equals("1")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {
       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction, "NA", "NA",
         "NA", "NA", "NA", "NA"));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;
      PCB.JobPC = PCB.JobPC + 1;
      /* The R5 bit is set */
      if (instruction_4_array[4].equals("0")) {
       // int Rvalue_instrution4 = Integer.parseInt(R5, 2);
       // int R5temp = getDecimalFromBinary(Rvalue_instrution4);
       String[] R5Array = PCB.R5.split("");
       int[] R5intArray = new int[R5Array.length];
       for (int i = 0; i < R5intArray.length; i++) {
        R5intArray[i] = Integer.parseInt(R5Array[i]);
       }
       int Countones = 0;
       if (R5intArray[0] == 0) {
        for (int i = 0; i < R5intArray.length; i++) {
         if (R5intArray[i] == 1) {
          Countones++;
         }
        }
       }
       if (R5intArray[0] == 0 && Countones > 0)
        PCB.JobPC = PCB.JobPC + 1;

      }
      /* The R4 bit is set */
      else if (instruction_4_array[4].equals("1")) {

       // int Rvalue_instrution4 = Integer.parseInt(R4, 2);
       // int R4temp = getDecimalFromBinary(Rvalue_instrution4);
       String[] R4Array = PCB.R4.split("");
       int[] R4intArray = new int[R4Array.length];
       for (int i = 0; i < R4intArray.length; i++) {
        R4intArray[i] = Integer.parseInt(R4Array[i]);
       }
       int Countones = 0;
       if (R4intArray[0] == 0) {
        for (int i = 0; i < R4intArray.length; i++) {
         if (R4intArray[i] == 1) {
          Countones++;
         }
        }
       }
       if (R4intArray[0] == 0 && Countones > 0)
        PCB.JobPC = PCB.JobPC + 1;

      }
     }
     /* The LSS bit is set */
     else if (instruction_4_array[5].equals("0")
       && instruction_4_array[6].equals("1")
       && instruction_4_array[7].equals("0")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {
       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction, "NA", "NA",
         "NA", "NA", "NA", "NA"));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;

      PCB.JobPC = PCB.JobPC + 1;
      /* The R5 bit is set */
      if (instruction_4_array[4].equals("0")) {
       String[] R5Array = PCB.R5.split("");
       int[] R5intArray = new int[R5Array.length];
       for (int i = 0; i < R5intArray.length; i++) {
        R5intArray[i] = Integer.parseInt(R5Array[i]);
       }
       if (R5intArray[0] == 1) {
        PCB.JobPC = PCB.JobPC + 1;

       }
      }
      /* The R4 bit is set */
      else if (instruction_4_array[4].equals("1")) {
       String[] R4Array = PCB.R4.split("");
       int[] R4intArray = new int[R4Array.length];
       for (int i = 0; i < R4intArray.length; i++) {
        R4intArray[i] = Integer.parseInt(R4Array[i]);
       }
       if (R4intArray[0] == 1) {
        PCB.JobPC = PCB.JobPC + 1;

       }

      }
     }
     /* The NEQ bit is set */
     else if (instruction_4_array[5].equals("0")
       && instruction_4_array[6].equals("1")
       && instruction_4_array[7].equals("1")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {
       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction, "NA", "NA",
         "NA", "NA", "NA", "NA"));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;

      PCB.JobPC = PCB.JobPC + 1;
      /* The R5 bit is set */
      if (instruction_4_array[4].equals("0")) {
       String[] R5Array = PCB.R5.split("");
       int[] R5intArray = new int[R5Array.length];
       for (int i = 0; i < R5intArray.length; i++) {
        R5intArray[i] = Integer.parseInt(R5Array[i]);
       }
       int Countones = 0;

       for (int i = 0; i < R5intArray.length; i++) {
        if (R5intArray[i] == 1) {
         Countones++;
        }
       }
       if (R5intArray[0] == 0 && Countones > 0)
        PCB.JobPC = PCB.JobPC + 1;
       if (R5intArray[0] == 1) {
        PCB.JobPC = PCB.JobPC + 1;
       }

      }
      /* The R4 bit is set */
      else if (instruction_4_array[4].equals("1")) {

       String[] R4Array = PCB.R4.split("");
       int[] R4intArray = new int[R4Array.length];
       for (int i = 0; i < R4intArray.length; i++) {
        R4intArray[i] = Integer.parseInt(R4Array[i]);
       }
       int Countones = 0;

       for (int i = 0; i < R4intArray.length; i++) {
        if (R4intArray[i] == 1) {
         Countones++;
        }
       }
       if (R4intArray[0] == 0 && Countones > 0) {
        PCB.JobPC = PCB.JobPC + 1;
       } else if (R4intArray[0] == 1) {
        PCB.JobPC = PCB.JobPC + 1;
       }

      }

     }
     /* The EQL bit is set */
     else if (instruction_4_array[5].equals("1")
       && instruction_4_array[6].equals("0")
       && instruction_4_array[7].equals("0")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {
       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction, "NA", "NA",
         "NA", "NA", "NA", "NA"));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;
      PCB.JobPC = PCB.JobPC + 1;

      /* The R5 bit is set */
      if (instruction_4_array[4].equals("0")) {
       // int Rvalue_instrution4 = Integer.parseInt(R5, 2);
       // int R5temp = getDecimalFromBinary(Rvalue_instrution4);
       String[] R5Array = PCB.R5.split("");
       int[] R5intArray = new int[R5Array.length];
       for (int i = 0; i < R5intArray.length; i++) {
        R5intArray[i] = Integer.parseInt(R5Array[i]);
       }
       int Countones = 0;
       if (R5intArray[0] == 0) {
        for (int i = 0; i < R5intArray.length; i++) {
         if (R5intArray[i] == 1) {
          Countones++;
         }
        }
       }
       if (R5intArray[0] == 0 && Countones == 0)
        PCB.JobPC = PCB.JobPC + 1;

      }
      /* The R4 bit is set */
      else if (instruction_4_array[4].equals("1")) {

       String[] R4Array = PCB.R4.split("");
       int[] R4intArray = new int[R4Array.length];
       for (int i = 0; i < R4intArray.length; i++) {
        R4intArray[i] = Integer.parseInt(R4Array[i]);
       }
       int Countones = 0;
       if (R4intArray[0] == 0) {
        for (int i = 0; i < R4intArray.length; i++) {
         if (R4intArray[i] == 1) {
          Countones++;
         }
        }
       }
       if (R4intArray[0] == 0 && Countones == 0)
        PCB.JobPC = PCB.JobPC + 1;

      }

     }

     /* The GRE bit is set */
     else if (instruction_4_array[5].equals("1")
       && instruction_4_array[6].equals("0")
       && instruction_4_array[7].equals("1")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {
       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction, "NA", "NA",
         "NA", "NA", "NA", "NA"));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;

      PCB.JobPC = PCB.JobPC + 1;
      /* The R5 bit is set */
      if (instruction_4_array[4].equals("0")) {
       String[] R5Array = PCB.R5.split("");
       int[] R5intArray = new int[R5Array.length];
       for (int i = 0; i < R5intArray.length; i++) {
        R5intArray[i] = Integer.parseInt(R5Array[i]);
       }
       if (R5intArray[0] == 0) {
        PCB.JobPC = PCB.JobPC + 1;

       }
      }
      /* The R4 bit is set */
      else if (instruction_4_array[4].equals("1")) {
       String[] R4Array = PCB.R4.split("");
       int[] R4intArray = new int[R4Array.length];
       for (int i = 0; i < R4intArray.length; i++) {
        R4intArray[i] = Integer.parseInt(R4Array[i]);
       }
       if (R4intArray[0] == 0) {
        PCB.JobPC = PCB.JobPC + 1;

       }

      }
     }
     /* The LSE bit is set */
     else if (instruction_4_array[5].equals("1")
       && instruction_4_array[6].equals("1")
       && instruction_4_array[7].equals("0")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }
      if (PCB.traceSwitch == 1) {
       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction, "NA", "NA",
         "NA", "NA", "NA", "NA"));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;

      PCB.JobPC = PCB.JobPC + 1;
      /* The R5 bit is set */
      if (instruction_4_array[4].equals("0")) {
       String[] R5Array = PCB.R5.split("");
       int[] R5intArray = new int[R5Array.length];
       for (int i = 0; i < R5intArray.length; i++) {
        R5intArray[i] = Integer.parseInt(R5Array[i]);
       }

       if (R5intArray[0] == 0) {
        int Countones = 0;

        for (int i = 0; i < R5intArray.length; i++) {
         if (R5intArray[i] == 1) {
          Countones++;
         }
        }
        if (R5intArray[0] == 1) {
         PCB.JobPC = PCB.JobPC + 1;
        } else if (R5intArray[0] == 0 && Countones == 0) {
         PCB.JobPC = PCB.JobPC + 1;
        }
       }
      }
      /* The R4 bit is set */
      else if (instruction_4_array[4].equals("1")) {

       String[] R4Array = PCB.R4.split("");
       int[] R4intArray = new int[R4Array.length];
       for (int i = 0; i < R4intArray.length; i++) {
        R4intArray[i] = Integer.parseInt(R4Array[i]);
       }
       if (R4intArray[0] == 1) {
        PCB.JobPC = PCB.JobPC + 1;

       } else if (R4intArray[0] == 0) {
        int Countones = 0;

        for (int i = 0; i < R4intArray.length; i++) {
         if (R4intArray[i] == 1) {
          Countones++;
         }
        }
        if (R4intArray[0] == 0 && Countones == 0) {
         PCB.JobPC = PCB.JobPC + 1;
        }
       }
      }
     }
     /* The USK bit is set */
     else if (instruction_4_array[5].equals("1")
       && instruction_4_array[6].equals("1")
       && instruction_4_array[7].equals("1")) {
      if (PCB.TimeSlice > 35) {
       SCHEDULER.TimeSliceCheck();
      }

      if (PCB.traceSwitch == 1) {
       contentsOfEABefore = MEMORY.memory("READ", EA_decimal, tempaccess, PCB);
       String PC_temp = Integer.toString(PCB.JobPC);
       traceFile.addAll(Arrays.asList(PC_temp, current_instruction, "NA", "NA",
         "NA", "NA", "NA", "NA"));
      }

      CLOCK = CLOCK + 1;
      PCB.TimeSlice = PCB.TimeSlice + 1;

      PCB.JobPC = PCB.JobPC + 1;
      /* The R5 bit is set */
      if (instruction_4_array[4].equals("0")) {
       PCB.JobPC = PCB.JobPC + 1;

      }
      /* The R4 bit is set */
      else if (instruction_4_array[4].equals("1")) {
       PCB.JobPC = PCB.JobPC + 1;

      }
     }

    }

    break;

   default:
    new ERROR_HANDLER(111, PCB);
    break;
   }
  }
 }

 /*
  * In case if the input instruction is instruction 1 type , than this function
  * is used to calculate Addressing mode The below method is to perform this
  * operation: A=PC(0-11)+INSTR(6-11SE) The
  * parsing_inputmemory_consisting_binaryelements function call is to perform
  * the parsing for storing the each bit of instruction in instuction1 array To
  * perform arithmetic operations, we call a function called two's_complement()
  */
 public String calculateInstruction1Addressingmode(int PC,
   PROCESSMANAGER PCB2) {
  String each_instructionbit_in_array = parsing_inputmemory_consisting_binaryelements(
    PC, PCB2);
  String PC_BinaryValue = Decimal_to_binary(PC);
  String outputOfAddition = checkoverflowbit(PC_BinaryValue,
    each_instructionbit_in_array, "true", PCB2);
  int m3 = Integer.parseInt(outputOfAddition, 2);
  String A = Integer.toString(m3);
  return A;
 }

 /* This function is used to convert the Decimal value to binary */
 public static String Decimal_to_binary(int m) {

  String m1 = Integer.toBinaryString(m);

  String m2 = String.format("%12s", m1).replace(' ', '0');

  return m2;

 }

 /*
  * This function is used to store each instruction in respective instruction
  * array which helps to keep track of various bits
  */
 public String parsing_inputmemory_consisting_binaryelements(int PC,
   PROCESSMANAGER PCB3) {
  String INSTR = ""; /* Instruction which is nothing but ADDR */
  current_instruction = MEMORY.memory("READ", PC - 1, tempaccess, PCB3);
  instruction_1_array = current_instruction.split("");
  String instruction_1_array_temp = Arrays.toString(instruction_1_array);
  instruction_1_array_temp = instruction_1_array_temp.replaceAll(",", "");
  instruction_1_array_temp = instruction_1_array_temp.replaceAll("\\s+", "");
  INSTR = instruction_1_array_temp.substring(7, 13);
  return INSTR;
 }

 /*
  * This method is used for calculating the two's compliment for performing
  * arithmetic operations.
  */
 public String two_complement(String X1) {
  int n = X1.length(), i;
  boolean flag = false;
  char[] a = X1.toCharArray();
  for (i = n - 1; i >= 0; i--) {
   if (a[i] == '1' && flag == false) {
    flag = true;
    continue;
   }
   if (a[i] == '0' && flag == false) {
    continue;
   }
   if (a[i] == '1')
    a[i] = '0';
   else
    a[i] = '1';
  }
  String two_compliment_output = String.valueOf(a);

  return two_compliment_output;

 }

 /* This function is used to perform addition of two's compliment numbers */
 public String additionBinarynumbers(String twocomplimentvalue1,
   String twocomplimentvalue2) {

  int p1 = twocomplimentvalue1.length() - 1;
  int p2 = twocomplimentvalue2.length() - 1;
  StringBuilder buf = new StringBuilder();
  int carry = 0;
  while (p1 >= 0 || p2 >= 0) {
   int sum = carry;
   if (p1 >= 0) {
    sum += twocomplimentvalue1.charAt(p1) - '0';
    p1--;
   }
   if (p2 >= 0) {
    sum += twocomplimentvalue2.charAt(p2) - '0';
    p2--;
   }
   carry = sum >> 1;
   sum = sum & 1;
   buf.append(sum == 0 ? '0' : '1');
  }
  if (carry > 0) {
   buf.append('1');
  }
  buf.reverse();

  return buf.toString();

 }

 /*
  * This function is used to check if two's compliment has generated an overflow
  * bit
  */
 public String checkoverflowbit(String addition_number1,
   String addition_number2, String flag, PROCESSMANAGER PCB) {
  String number1 = "";
  String number2 = "";
  char overflowBit;
  String binaryAdditionoutput = "";

  /*
   * The below loop is for performing addition and checking overflow while
   * adding PC+INSTR
   */
  if (flag.equals("true")) {
   number1 = String.format("%12s", (addition_number1)).replace(' ', '0');
   if (addition_number2.charAt(0) == '1') {
    number2 = String.format("%12s", (addition_number2)).replace(' ', '1');
    if (number2.charAt(0) == '1') {
     binaryAdditionoutput = additionBinarynumbers(number1, number2);
     if (binaryAdditionoutput.length() > 12) {
      binaryAdditionoutput = binaryAdditionoutput.substring(1, 13);
     }

    }
   } else if (addition_number2.charAt(0) == '0') {
    number2 = String.format("%12s", (addition_number2)).replace(' ', '0');
    binaryAdditionoutput = additionBinarynumbers(number1, number2);
    if (binaryAdditionoutput.length() > 12) {
     overflowBit = binaryAdditionoutput.charAt(0);
     if (overflowBit == '1') {
      String[] numberInArray = number2.split("");
      String[] binaryOverflowBitCheck = binaryAdditionoutput.split("");

      if (!(binaryOverflowBitCheck[1].equals(numberInArray[0]))) {

       new ERROR_HANDLER(107, PCB);
      } else if (binaryOverflowBitCheck[1].equals(numberInArray[0])) {
       binaryAdditionoutput = binaryAdditionoutput.substring(1, 13);
      }
     }
    }
   }

  }
  /* The below loop is for performing addition and checking overflow */
  else {

   if ((addition_number1.charAt(0) != addition_number2.charAt(0))) {
    binaryAdditionoutput = additionBinarynumbers(addition_number1,
      addition_number2);
    if (binaryAdditionoutput.length() > 12) {
     binaryAdditionoutput = binaryAdditionoutput.substring(1, 13);
    }
   } else {
    binaryAdditionoutput = additionBinarynumbers(addition_number1,
      addition_number2);
    if (binaryAdditionoutput.length() > 12) {
     overflowBit = binaryAdditionoutput.charAt(0);

     if (overflowBit == '1') {
      String[] numberInArray = addition_number2.split("");
      String[] binaryOverflowBitCheck = binaryAdditionoutput.split("");
      if (!(binaryOverflowBitCheck[1].equals(numberInArray[0]))) {
       new ERROR_HANDLER(107, PCB);
      } else if (binaryOverflowBitCheck[1].equals(numberInArray[0])) {
       binaryAdditionoutput = binaryAdditionoutput.substring(1, 13);
      }
     }
    }
   }

  }
  return binaryAdditionoutput;
 }

 /*
  * This function is used to calculate the effective address based on indexing
  * and Indirection
  */
 public String calculate_effective_address(String Addressmode,
   PROCESSMANAGER PCB1) {
  /*
   * The below methods make use of temp variables which are local to specific
   * case for calculating Effective address
   */
  String EA = "";
  String I = instruction_1_array[0];
  String X = instruction_1_array[5];
  /* Direct Addressing */
  if (I.equals("0") && X.equals("0")) {
   /* EA=A. */

   String EA_temp = Addressmode;
   int EA_int = Integer.parseInt(EA_temp);
   EA = Decimal_to_binary(EA_int);

  }
  /* Indexing */
  else if (I.equals("0") && X.equals("1")) {
   /* EA=A+(R4) */
   String R4temp = String.format("%12s", PCB1.R4).replace(' ', '0');
   int A = Integer.parseInt(Addressmode);
   String A1 = Decimal_to_binary(A);
   EA = checkoverflowbit(A1, R4temp, "", PCB1);

  }
  /* Indirection */
  else if (I.equals("1") && X.equals("0")) {
   /* EA=(A) */
   int A = Integer.parseInt(Addressmode);

   String temp = MEMORY.memory("READ", A, tempaccess, PCB1);
   EA = temp;

  }
  /* Indexing and Indirection */
  else if (I.equals("1") && X.equals("1")) {
   /* EA=(A)+(R4) */
   int A = Integer.parseInt(Addressmode);
   String temp = MEMORY.memory("READ", A, tempaccess, PCB1);
   // String R4temp = String.format("%12s", R4).replace(' ', '0');
   EA = checkoverflowbit(temp, PCB1.R4, "", PCB1);
  }

  return EA;
 }

 /* This is used to convert binary bits to decimal */
 public int getDecimalFromBinary(int binary) {
  int decimal = 0;
  int power = 0;
  while (true) {
   if (binary == 0) {
    break;
   } else {
    int tmp = binary % 10;
    decimal += tmp * Math.pow(2, power);
    binary = binary / 10;
    power++;
   }
  }
  return decimal;
 }

 /* Trace File Generation Function */
 public static void traceFileGeneration(ArrayList<String> traceFileOutput,
   PROCESSMANAGER PCB) {

  BufferedWriter writeTraceFile = null;
  String heading = "";
  String m = "";

  try {
   if (PCB.FileStatus.equals("false")) {
    writeTraceFile = new BufferedWriter(new FileWriter(TRACEFILENAME, false));
    PCB.writeEachPCBTraceFile = writeTraceFile;
   
     heading = "PC" + "\t" + "\t" + "\t" + "\t" + "Instruction" + "\t" + "\t"
       + "\t" + "\t" + "\t" + "R-Register" + "\t" + "\t" + "\t" + "EA" + "\t"
       + "\t" + "\t" + "\t" + "Contents of R & EA Before Execution" + "\t"
       + "\t" + "\t" + "Contents of R & EA After Execution";
  
     PCB.writeEachPCBTraceFile.write(heading);
     PCB.FileStatus = "true";
    
   }
   if (PCB.FileStatus.equals("true")) {
    writeTraceFile = new BufferedWriter(new FileWriter(TRACEFILENAME, true));
    PCB.writeEachPCBTraceFile = writeTraceFile;
    PCB.writeEachPCBTraceFile.newLine();
    m = traceFileOutput.toString();
    String[] n = m.split(",");
    int pc_binary = Integer
      .parseInt(traceFileOutput.get(0).replace(" ", "").replace("]", ""));
    n[0] = Decimal_to_binary(pc_binary);
    traceFileOutput.remove(0);
    traceFileOutput.add(0, n[0]);
    if (!(traceFileOutput.get(3).equals("NA"))) {
     int EA_binary = Integer
       .parseInt(traceFileOutput.get(3).replace(" ", "").replace("]", ""));
     n[3] = Decimal_to_binary(EA_binary);
     traceFileOutput.remove(3);
     traceFileOutput.add(3, n[3]);
    }
    for (int i = 0; i < traceFileOutput.size(); i++) {

     if (traceFileOutput.get(i).contains("0")
       || traceFileOutput.get(i).contains("1")) {
      String traceoutput = traceConversion(
        n[i].replace(" ", "").replace("]", ""));
      traceFileOutput.remove(i);
      traceFileOutput.add(i, traceoutput);
     }

    }

    m = traceFileOutput.toString();
    String formattedTraceString = m.replace("NA", " ").replace("]", "(HEX)")
      .replace("[", "").replace(",", "(HEX)\t\t\t");
    PCB.writeEachPCBTraceFile.write(formattedTraceString);
    PCB.writeEachPCBTraceFile.newLine();
   }
  } catch (IOException e) {
   try {
    PCB.writeEachPCBTraceFile.flush();
    PCB.writeEachPCBTraceFile.close();
   } catch (IOException e1) {
    // TODO Auto-generated catch block
    e1.printStackTrace();
   }

   // new ERROR_HANDLER(102,null);
  } finally {
   if (PCB.writeEachPCBTraceFile != null) {
    try {
     PCB.writeEachPCBTraceFile.flush();
     PCB.writeEachPCBTraceFile.close();

    } catch (IOException e) {
     try {
      PCB.writeEachPCBTraceFile.flush();
      PCB.writeEachPCBTraceFile.close();
     } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
     }

     // new ERROR_HANDLER(102,null);
    }
   }
  }

 }

 public static String traceConversion(String traceelement) {

  int decimal = Integer.parseInt(traceelement, 2);
  String hexString = Integer.toString(decimal, 16);
  hexString = String.format("%03x", Integer.parseInt(hexString, 16));
  return hexString;

 }

}
