
/*Name: Ankita Kota
* Course Number:
* Assisgnment title: Phase 1
* Date: 03/21/2017
*  Description:
The system module will take the command line arguments for input file and it calls the
appropriate subsystems.It also handles the  I /o operation and termination of the
program.The below variables are declared to be global.Because they are used by other
subsystems such as execution_time needed for cpu to increment the value, Start_address
and trace_flag are to be set by the loader subsystem.The major static variables are used
to calculate system average performances and for holding times of system for later use like clock time
start address and length of the file keeps track of loader inout format throught out the program.
The system takes care of all the I/O operations.
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SYSTEM {
 static int traceSwitch;
 public static int length;
 static int start_address;
 static String exitflag = "false";
 static int CLOCK = 0;
 static int IOClock;
 static int TotalIdleTime;
 static BufferedReader br;
 static int Timer;
 static FileWriter writeProgressFile;
 static ArrayList<String> dumpOutputInHex = new ArrayList<String>(
   Arrays.asList());
 static ArrayList<String> outputFile = new ArrayList<String>(Arrays.asList());
 public static final String Outputfiledata = "output_file";
 static int m1 = 0;
 static int BoundAddress;
 static int BaseAddress;
 public static int JobsTerminatedNormally;
 public static int JobsTerminatedAbnormally;
 static String name2;
 static PROCESSMANAGER dumptemp;
 static int terminated;
 static int abnormalterminated;
 String[] b=new String[20];
static String name3;
static int timer;
static String name;
 public static void main(String[] args) {
   name=args[0];
  if (args.length < 1) {
   new ERROR_HANDLER(101, null);
  }
  LOADER.FILENAME = args[0];
  new LOADER(0, 0);


 }

 public static String IO_read_operation() {
 
  Scanner keyboard = new Scanner(System.in);
  String input = keyboard.next();
  keyboard.close();
  return input;
 }

 public static void DisplayClock() {
  String ClockBinary = CPU.Decimal_to_binary(CLOCK);
  String ClockInHex = LOADER.binHex(ClockBinary);

 }

 public static void PCBCreation(int jobid, int dataLinesBaseAddress,
   int dataLinesBoundAddress, String flag, int Length, int DatalinesLength,
   int outputlines, int startAddress, int TraceSwitch) {
  SCHEDULER.ProcessManager(jobid, dataLinesBaseAddress, dataLinesBoundAddress,
    flag, Length, DatalinesLength, outputlines, startAddress, TraceSwitch);
 }

 public static void JobInitation() {
  LOADER.readeachJob();
 }

 public static void DumpOutput(PROCESSMANAGER PCB) {
  int range = 0;
  if (PCB.JobBaseAddress == 0) {
   range = 32;
  } else if (PCB.JobBaseAddress == 32) {
   range = 32;
  } else if (PCB.JobBaseAddress == 64) {

   range = 64;

  } else if (PCB.JobBaseAddress == 128) {
   range = 64;
  } else if (PCB.JobBaseAddress == 192) {
   range = 64;
  } else if (PCB.JobBaseAddress == 256) {
   range = 128;
  } else if (PCB.JobBaseAddress == 384) {
   range = 128;
  }

  for (int i = 0; i < range; i++) {
   String dump = MEMORY.memory("READ", i, "", PCB);
   if (!(dump == null)) {

    dumpOutputInHex.add(LOADER.binHex(dump) + " ");
   } else if (dump == null) {

    dumpOutputInHex.add("000");

   }
  }
 
  displayOutput(dumpOutputInHex);
 }

 public static void displayOutput(ArrayList<String> dump) {
   try{
   writeProgressFile = new FileWriter("ProgressFile", true);
  for (int k = 0; k < dump.size(); k += 8) {

   List<String> dumpList = dump.subList(k, k + 8);
   String formattedString = dumpList.toString();
   String dumpOutput = formattedString.replace("[", "").replace("]", "")
     .replace(",", "(HEX)");
   writeProgressFile.write(dumpOutput);
   int n = k + 8;
   if (n % 8 == 0) {
     writeProgressFile.write("\n");

   }
   LOADER.PCBCheckFlag = "true";
   LOADER.initialline = 0;
   LOADER.count = 0;
   LOADER.DataLinesCount = 0;
   LOADER.LoaderLinesCount = 0;
   LOADER.initialline = 0;
   LOADER.count = 0;
   LOADER.StoreLines.clear();
   LOADER.FilePointer = 0;
   LOADER.readeachJob();
   writeProgressFile.flush();
   writeProgressFile.close();
  }
   }
  catch(Exception e)
  {
    
  }

 }

 public static void displayOutputInFile(String errormsg) {
  int Runtime = CLOCK + IOClock;
  String Runtimefinal = Integer.toString(Runtime);
  String Jobid = "1";
  BufferedWriter writeTraceFile = null;

  try {
   writeTraceFile = new BufferedWriter(new FileWriter(Outputfiledata, true));
   if (m1 == 0) {
    writeTraceFile = new BufferedWriter(new FileWriter(Outputfiledata, false));
    m1++;
   }
   outputFile.addAll((Arrays.asList("Cummalative job Identification number:\t\t"
     + Jobid + "\n" + "Execution Type\t\t:" + errormsg + "\n" + "Output\t\t:"
     + CPU.finalOutput + "\n" + "Runtime:\t\t" + Runtimefinal
     + "(DECIMAL)Virtual Time Unit\t\t" + "\n" + "CLOCK Value at termination:"
     + CLOCK + "(HEX)Virtual Time Unit" + "\n" + "IOClock" + IOClock
     + "(HEX)Virtual Time Unit")));
   String outputFileString = outputFile.toString();
   writeTraceFile.write(outputFileString);
  } catch (IOException e) {
   new ERROR_HANDLER(102, null);
  } finally {
   if (writeTraceFile != null) {
    try {
     writeTraceFile.flush();
     writeTraceFile.close();

    } catch (IOException e) {
     new ERROR_HANDLER(102, null);
    }
   }
  }
 }

}
