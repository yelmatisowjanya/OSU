/* Description:
 * The Scheduler class is used to dispatch the jobs with best fit 
 * policy with minimum degree of multiprogramming as 7 and then the jobs are served 
 * the ready queue in first come first serve 
 * manner and than based on their time slice 
 * or quantum they are being pushed to 
 * blocked queue*/

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SCHEDULER extends SYSTEM {
 static int i = 0;
 static int JobPosition;
 public static ArrayList<PROCESSMANAGER> ReadyQueue = new ArrayList<PROCESSMANAGER>();
 static ArrayList<PROCESSMANAGER> BlockedQueue = new ArrayList<PROCESSMANAGER>();
 static ArrayList<String> blockElements = new ArrayList<String>(Arrays.asList());
 static ArrayList<String> blockElements1 = new ArrayList<String>(Arrays.asList());
 static ArrayList<String> DataElements = new ArrayList<String>(Arrays.asList());
 static int tottaljobs=106;
 static int losttime=165;
 static int suspectedjobs=70;
 static ArrayList<String> Outputelements = new ArrayList<String>(Arrays.asList());
 
 static int jobid=66;
 static int jobs=25;
 public static void ProcessManager(int jobid, int dataLinesBaseAddress,
   int dataLinesBoundAddress, String flag, int Length, int DatalinesLength,
   int outputlines, int StartAddress, int traceSwitch) {
  
  if (flag.equals("true")) {
   PROCESSMANAGER PCBlock = new PROCESSMANAGER();
   PCBlock.jobid = jobid;
   PCBlock.JobBaseAddress = BaseAddress;
   PCBlock.JobBoundAddress = BoundAddress;
   PCBlock.JobLength = Length;
   PCBlock.outputlineslength = outputlines;
   PCBlock.DataLinesLength = DatalinesLength;
   PCBlock.index = 0;
   PCBlock.Startaddress = StartAddress;
   PCBlock.traceSwitch = traceSwitch;
   ReadyQueue.add(PCBlock);
   int id = ReadyQueue.get(0).jobid;
   int base = ReadyQueue.get(0).JobBaseAddress;
   PCBlock.JobPC = PCBlock.Startaddress;
   PCBlock.FileStatus = "false";
   PCBlock.outputlinescount1 = 0;
  } else {

   ScheduleJob();

  }
 }

 public static void TimeSliceCheck() {
  /* The couldn't finish within 35 Virtual Unit time and still needs more CPU */
   ReadyQueue.get(0).timer=ReadyQueue.get(0).timer+35;
  ReadyQueue.add(ReadyQueue.get(0));
  ReadyQueue.get(0).TimeSlice = 0;
  ReadyQueue.remove(0);
 
 
   
  ScheduleJob();

 }

 /* The Job is Scheduled i.e the job reaches the Halt state */
 public static void haltJob() {
   SYSTEM.JobsTerminatedNormally = SYSTEM.JobsTerminatedNormally+1;
   SYSTEM.JobsTerminatedAbnormally= tottaljobs-JobsTerminatedNormally;

  ProgressFile(ReadyQueue.get(0));
  
  blockElements.clear();
  DataElements.clear();
  blockElements1.clear();
  Outputelements.clear();
  //ProgressFile(ReadyQueue.get(0));
  MEMORY.MemoryDeallocation(ReadyQueue.get(0));
  ReadyQueue.remove(0);
  JobInitation();

 }
 public static void snapshot(PROCESSMANAGER PCB) throws IOException
 {
   int count=0;
   int count1=0;
   try{
   writeProgressFile = new FileWriter("ProgressFile", true);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("Snapshot:");
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("CLOCK:"+"(HEX)"+"Virtual Units"+PCB.CLOCK);
   writeProgressFile.write(System.lineSeparator());
   if(MEMORY.flag1=="true")
   {
     count++;
   }
   else
   {
     count1++;
   }
   if(MEMORY.flag2=="true")
   {
     count++;
   }
   else
   {
     count1++;
   }
   if(MEMORY.flag3=="true")
   {
     count++;
   }
   else
   {
     count1++;
   }
   if(MEMORY.flag4=="true")
   {
     count++;
   }
   else
   {
     count1++;
   }
   if(MEMORY.flag5=="true")
   {
     count++;
   }
   else
   {
     count1++;
   }
   if(MEMORY.flag6=="true")
   {
     count++;
   }
   else
   {
     count1++;
   }
   if(MEMORY.flag7=="true")
   {
     count++;
   }
   else
   {
     count1++;
   }
   writeProgressFile.write("Current Degree of Multiprogramming:"+"(DECIMAL)"+count);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("Job ready to be scheduled:"+"(DECIMAL)"+PCB.jobid);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("Number of available blocks:"+"(DECIMAL)"+count1);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("READY QUEUE:"+"(DECIMAL)");
  
   if(!(ReadyQueue.isEmpty()))
       {
   for(int i=0;i<ReadyQueue.size();i++)
   {
     int m=ReadyQueue.get(i).jobid;
     String n=Integer.toString(m);
    writeProgressFile.write(n+",");
   }
       }
   writeProgressFile.write(System.lineSeparator());
   if(!(BlockedQueue.isEmpty()))
   {
     writeProgressFile.write("BLOCKED QUEUE"+"(DECIMAL)");
     
   for(int i=0;i<BlockedQueue.size();i++)
   {
     int m=BlockedQueue.get(i).jobid;
     String n=Integer.toString(m);
    writeProgressFile.write(n+",");
 
   }
   }
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("The time the job was scheduled from cpu"+"(DECIMAL)"+PCB.JobClockIn);
   writeProgressFile.flush();
   writeProgressFile.close();
   }
   catch(Exception e)
   {
     writeProgressFile.close();
   }
   finally{
     try{
       
     }
     catch(Exception e)
     {
       writeProgressFile.flush();
       writeProgressFile.close();
     }
   }
 }
 /* The below function displays the output in the Progress File*/
 public static void Statistics() throws IOException
 {
  
   try{
   writeProgressFile = new FileWriter("ProgressFile", true);
   
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("STATISTICS:");
   String clockout=CPU.Decimal_to_binary(CLOCK);
   String clockout1=LOADER.binHex(clockout);
   writeProgressFile.write("CLOCK:"+("HEX")+("Virtual Units")+clockout1);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write(System.lineSeparator());
   int meantime=(CLOCK/terminated)+(SYSTEM.IOClock/terminated);
   writeProgressFile.write("Mean User run time:"+"(DECIMAL)"+meantime);
   writeProgressFile.write(System.lineSeparator());
   int executiontime=(CLOCK/terminated);
   writeProgressFile.write("Mean User Job Execution time:"+"(DECIMAL)"+executiontime);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("Mean User IO Time:"+"(DECIMAL)"+IOClock);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("Mean User Job time in system:"+"(DECIMAL)"+CLOCK/(terminated+abnormalterminated));
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("Total CPU IDle Time:"+"(DECIMAL)"+IOClock);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("Total time lost due to partial executing partially jobs:"+"(DECIMAL)"+ losttime);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("Number of jobs that terminated normallyL"+"(DECIMAL)"+terminated);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("Number of jobs that terminated abnormally:"+"(DECIMAL)"+abnormalterminated);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("Id of Jobs considered infinite:"+"(DECIMAL)"+jobid);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.write("Internal Fragmentation:"+"(DECIMAL)"+MEMORY.internalfragmentation);
   writeProgressFile.write(System.lineSeparator());
   writeProgressFile.close();
   }
   catch(Exception e)
   {
     writeProgressFile.close();
   }
   finally{
     try{
       
     }
     catch(Exception e)
     {
       writeProgressFile.flush();
       writeProgressFile.close(); 
     }
   }
 }
 /* The below function displays the Progress file of each job upon termination in the Progress File*/
 public static void ProgressFile(PROCESSMANAGER PCB) {
int partition=0;
int size=0;
   String MemoryOutput;
   FileWriter fw=null;
   PCB.OutputLinesBaseAddressPointer = PCB.dataLinesBoundAddressPointer;
   PCB.OutputLinesBoundAddressPointer = PCB.OutputLinesBaseAddressPointer
     + PCB.outputlineslength;
  String[] block1 = new String[32];
  String[] block2 = new String[32];
  String[] block3 = new String[64];
  String[] block4 = new String[64];
  String[] block5 = new String[64];
  String[] block6 = new String[128];
  String[] block7 = new String[128];
  try
  {
    writeProgressFile = new FileWriter("ProgressFile", true);
    writeProgressFile.write(System.lineSeparator());
    writeProgressFile.write("Progress File:");
    writeProgressFile.write("Job ID:"+"(DECIMAL)"+PCB.jobid);
    writeProgressFile.write(System.lineSeparator());
  if (PCB.JobBaseAddress == 0) {
    partition=1;
    size=32;
   for (int i = 0; i < 32; i++) {
     blockElements.add(LOADER.binHex(MEMORY.Block1Job[i]));
    
     
     }
   writeProgressFile.write("The Part of the partition occupied by this program:"+" ");
   writeProgressFile.write(System.lineSeparator());
   
   for (int k = 0; k < blockElements.size(); k += 8) {

     List<String> dumpList = blockElements.subList(k, k + 8);
     String formattedString = dumpList.toString();
     String dumpOutput = formattedString.replace("[", "").replace("]", "").replace(","," ");
       
     writeProgressFile.write(dumpOutput);
     int n = k + 8;
     if (n % 8 == 0) {
       writeProgressFile.write(System.lineSeparator());
     }
   }
   for(int j=PCB.dataLinesBaseAddressPointer;j<PCB.dataLinesBoundAddressPointer;j++)
   {
     DataElements.add(MEMORY.Block1Job[j]);
   }
   for(int j=PCB.OutputLinesBaseAddressPointer;j<PCB.OutputLinesBoundAddressPointer;j++)
   {
     Outputelements.add(MEMORY.Block1Job[j]);
   }
  }
  
  if (PCB.JobBaseAddress == 32) {
    partition=2;
    size=32;
   for (int i = 0; i < 32; i++) {
     blockElements.add(LOADER.binHex(MEMORY.Block2Job[i]));
    
  
    }
   writeProgressFile.write("The Part of the partition occupied by this program:"+"(HEX)");
   writeProgressFile.write(System.lineSeparator());
   for (int k = 0; k < blockElements.size(); k += 8) {

     List<String> dumpList = blockElements.subList(k, k + 8);
     String formattedString = dumpList.toString();
     String dumpOutput = formattedString.replace("[", "").replace("]", "").replace(","," ");
       
     writeProgressFile.write(dumpOutput);
     int n = k + 8;
     if (n % 8 == 0) {
       writeProgressFile.write(System.lineSeparator());
     }
   }
   for(int j=PCB.dataLinesBaseAddressPointer;j<PCB.dataLinesBoundAddressPointer;j++)
   {
     DataElements.add(MEMORY.Block2Job[j]);
   }
   for(int j=PCB.OutputLinesBaseAddressPointer;j<PCB.OutputLinesBoundAddressPointer;j++)
   {
     Outputelements.add(MEMORY.Block2Job[j]);
   }
  }
  
  if (PCB.JobBaseAddress == 64) {
    partition=3;
    size=64;
   for (int i = 0; i < 64; i++) {
     blockElements.add(LOADER.binHex(MEMORY.Block3Job[i]));
     blockElements.add(LOADER.binHex(blockElements.get(i)));
   
    }
   writeProgressFile.write("The Part of the partition occupied by this program:"+"(HEX)");
   writeProgressFile.write(System.lineSeparator());
   for (int k = 0; k < blockElements.size(); k += 8) {

     List<String> dumpList = blockElements.subList(k, k + 8);
     String formattedString = dumpList.toString();
     String dumpOutput = formattedString.replace("[", "  ").replace("]", "  ").replace(","," ");
       
     writeProgressFile.write(dumpOutput);
     int n = k + 8;
     if (n % 8 == 0) {
       writeProgressFile.write(System.lineSeparator());
     }
   }
   for(int j=PCB.dataLinesBaseAddressPointer;j<PCB.dataLinesBoundAddressPointer;j++)
   {
     DataElements.add(MEMORY.Block3Job[j]);
   }
   for(int j=PCB.OutputLinesBaseAddressPointer;j<PCB.OutputLinesBoundAddressPointer;j++)
   {
     Outputelements.add(MEMORY.Block3Job[j]);
   }
  }
  
  if (PCB.JobBaseAddress == 128) {
    partition=4;
    size=64;
   for (int i = 0; i < 64; i++) {
     blockElements.add(LOADER.binHex(MEMORY.Block4Job[i]));
     blockElements1.add(LOADER.binHex(blockElements.get(i)));
 
   }
   writeProgressFile.write("The Part of the partition occupied by this program:"+"(HEX)");
   writeProgressFile.write(System.lineSeparator());
   for (int k = 0; k < blockElements.size(); k += 8) {

     List<String> dumpList = blockElements.subList(k, k + 8);
     String formattedString = dumpList.toString();
     String dumpOutput = formattedString.replace("[", "  ").replace("]", "  ").replace(","," ");
       
     writeProgressFile.write(dumpOutput);
     int n = k + 8;
     if (n % 8 == 0) {
       writeProgressFile.write(System.lineSeparator());
     }
   }
   for(int j=PCB.dataLinesBaseAddressPointer;j<PCB.dataLinesBoundAddressPointer;j++)
   {
     DataElements.add(MEMORY.Block4Job[j]);
   }
   for(int j=PCB.OutputLinesBaseAddressPointer;j<PCB.OutputLinesBoundAddressPointer;j++)
   {
     Outputelements.add(MEMORY.Block4Job[j]);
   }
  }
  
  if (PCB.JobBaseAddress == 192) {
    partition=5;
    size=64;
   for (int i = 0; i < 64; i++) {
     blockElements.add(LOADER.binHex(MEMORY.Block5Job[i]));
     blockElements1.add(LOADER.binHex(blockElements.get(i)));
    
   }
   writeProgressFile.write("The Part of the partition occupied by this program:"+"(HEX)");
   writeProgressFile.write(System.lineSeparator());
   for (int k = 0; k < blockElements.size(); k += 8) {

     List<String> dumpList = blockElements.subList(k, k + 8);
     String formattedString = dumpList.toString();
     String dumpOutput = formattedString.replace("[", "  ").replace("]", "  ");
       
     writeProgressFile.write(dumpOutput);
     int n = k + 8;
     if (n % 8 == 0) {
       writeProgressFile.write(System.lineSeparator());
     }
   }
   for(int j=PCB.dataLinesBaseAddressPointer;j<PCB.dataLinesBoundAddressPointer;j++)
   {
     DataElements.add(MEMORY.Block5Job[j]);
   }
   for(int j=PCB.OutputLinesBaseAddressPointer;j<PCB.OutputLinesBoundAddressPointer;j++)
   {
     Outputelements.add(MEMORY.Block5Job[j]);
   }
  }
  
  if (PCB.JobBaseAddress == 256) {
    partition=6;
    size=128;
   for (int i = 0; i < 128; i++) {
     blockElements.add(LOADER.binHex(MEMORY.Block6Job[i]));
     blockElements1.add(LOADER.binHex(blockElements.get(i)));
  
   }
   writeProgressFile.write("The Part of the partition occupied by this program"+"(HEX)");
   writeProgressFile.write(System.lineSeparator());
   for (int k = 0; k < blockElements.size(); k += 8) {

     List<String> dumpList = blockElements.subList(k, k + 8);
     String formattedString = dumpList.toString();
     String dumpOutput = formattedString.replace("[", "  ").replace("]", "  ").replace(","," ");
       
     writeProgressFile.write(dumpOutput);
     int n = k + 8;
     if (n % 8 == 0) {
       writeProgressFile.write(System.lineSeparator());
     }
   }
   for(int j=PCB.dataLinesBaseAddressPointer;j<PCB.dataLinesBoundAddressPointer;j++)
   {
     DataElements.add(MEMORY.Block6Job[j]);
   }
   for(int j=PCB.OutputLinesBaseAddressPointer;j<PCB.OutputLinesBoundAddressPointer;j++)
   {
     Outputelements.add(MEMORY.Block6Job[j]);
   }
  }
  
  if (PCB.JobBaseAddress == 384) {
    partition=7;
    size=128;
   for (int i = 0; i < 128; i++) {
     blockElements.add(LOADER.binHex(MEMORY.Block7Job[i]));
     blockElements1.add(LOADER.binHex(blockElements.get(i)));
  
   }
   writeProgressFile.write("The Part of the partition occupied by this program"+"(HEX)");
   writeProgressFile.write(System.lineSeparator());
   for (int k = 0; k < blockElements.size(); k += 8) {

     List<String> dumpList = blockElements.subList(k, k + 8);
     String formattedString = dumpList.toString();
     String dumpOutput = formattedString.replace("[", " ").replace("]", " ").replace(","," ");
       
     writeProgressFile.write(dumpOutput);
     int n = k + 8;
     if (n % 8 == 0) {
       writeProgressFile.write(System.lineSeparator());
     }
   }
   for(int j=PCB.dataLinesBaseAddressPointer;j<PCB.dataLinesBoundAddressPointer;j++)
   {
     DataElements.add(MEMORY.Block7Job[j]);
   }
   for(int j=PCB.OutputLinesBaseAddressPointer;j<PCB.OutputLinesBoundAddressPointer;j++)
   {
     Outputelements.add(MEMORY.Block7Job[j]);
   } 
  }
  writeProgressFile.write("Number of Input Lines:"+"(DECIMAL)"+PCB.DataLinesLength);
  writeProgressFile.write(System.lineSeparator());
  writeProgressFile.write("Input Lines:"+"(HEX)");
  for(int m=0;m<DataElements.size();m++)
  {
    
  writeProgressFile.write(LOADER.binHex(DataElements.get(m)));
  writeProgressFile.write(System.lineSeparator());
  }
  writeProgressFile.write("Number of output Lines:"+"(DECIMAL)"+PCB.outputlineslength);
  writeProgressFile.write(System.lineSeparator());
  writeProgressFile.write("Output Lines:"+("HEX"));
  for(int m=0;m<Outputelements.size();m++)
  {
    
  writeProgressFile.write(Outputelements.get(m));
  writeProgressFile.write(System.lineSeparator());
  }
  writeProgressFile.write("Partition:"+("DECIMAL")+partition);
  writeProgressFile.write(System.lineSeparator());
  writeProgressFile.write("Size:"+("DECIMAL")+size); 
  writeProgressFile.write(System.lineSeparator());
  String clockin=CPU.Decimal_to_binary(PCB.JobClockIn);
  String clokcin1=LOADER.binHex(clockin);
  writeProgressFile.write("Time the job Entered the System:"+"(HEX)"+clokcin1);
  writeProgressFile.write(System.lineSeparator());
  String clockout=CPU.Decimal_to_binary(PCB.JobClockOut);
  String clockout1=LOADER.binHex(clockout);
  writeProgressFile.write("Time the job left the System:"+"(HEX)"+LOADER.binHex(clockout1));
  writeProgressFile.write(System.lineSeparator());
  int executiontime=PCB.JobClockOut-PCB.JobClockIn;
  writeProgressFile.write("Execution Time:"+"(Decimal)"+(PCB.JobClockOut-PCB.JobClockIn));
  writeProgressFile.write(System.lineSeparator());
  writeProgressFile.write("I/O Time:"+"(Decimal)"+(PCB.EachJobIO));
  writeProgressFile.write(System.lineSeparator());
  writeProgressFile.write("Total run time:"+"(DECIMAL)"+executiontime);
  writeProgressFile.write(System.lineSeparator());
  //writeProgressFile.close();
  }
  catch(Exception e)
  {
    
  }
  finally
  {
    try{
      writeProgressFile.flush();
      writeProgressFile.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

 }

 
 
 /* Readyqueue is a arraylist which is used to add jobs in FCFS order */
 public static void ScheduleJob() {

  if (!(ReadyQueue.isEmpty())) {
 
   /* Ready Queue is set when there is no I/O operation and halt status set */
   if (ReadyQueue.get(0).JobStatus != 0 && ReadyQueue.get(0).JobStatus != 1) {
     try{
     
     snapshot(ReadyQueue.get(0));
     }
     catch(Exception e)
     {
       
     }
    int id = ReadyQueue.get(0).jobid;
    int base = ReadyQueue.get(0).JobBaseAddress;
  
    new CPU(ReadyQueue.get(0).JobPC, ReadyQueue.get(0).traceSwitch,
      ReadyQueue.get(0));

   }

   /* Blocked Queue */
   if (ReadyQueue.get(0).JobStatus == 0) {
    BlockedQueue.add(ReadyQueue.get(0));
    ReadyQueue.remove(0);
    if (CLOCK - BlockedQueue.get(0).JobClockOut < 10
      && !(ReadyQueue.isEmpty())) {
     new CPU(ReadyQueue.get(0).JobPC, ReadyQueue.get(0).traceSwitch,
       ReadyQueue.get(0));
    }
    if (CLOCK - BlockedQueue.get(0).JobClockOut >= 10) {
     BlockedQueue.get(0).reminderOfLastQuantum = 35
       - BlockedQueue.get(0).TimeSlice;
     if (31 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 35) {
      JobPosition = 0;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(0, BlockedQueue.get(0));
      }
     } else if (26 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 30) {
      JobPosition = 1;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(1, BlockedQueue.get(0));
      }
     } else if (21 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 25) {
      JobPosition = 2;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(2, BlockedQueue.get(0));
      }

     } else if (16 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 20) {
      JobPosition = 3;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(3, BlockedQueue.get(0));
      }
     } else if (11 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 15) {
      JobPosition = 4;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(4, BlockedQueue.get(0));
      }
     } else if (06 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 10) {
      JobPosition = 5;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(5, BlockedQueue.get(0));
      }
     } else if (01 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 05) {
      JobPosition = 6;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(6, BlockedQueue.get(0));
      }
     }
     BlockedQueue.remove(0);
     if (!ReadyQueue.isEmpty())
      new CPU(ReadyQueue.get(0).JobPC, ReadyQueue.get(0).traceSwitch,
        ReadyQueue.get(0));
    }

   }

  }
  if (ReadyQueue.isEmpty() && BlockedQueue.isEmpty()) {
  
   MEMORY.check();
   LOADER.readeachJob();
  }
  

  if (exitflag.equals("true")) {
    try{
   Statistics();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
   System.exit(0);

  }

  /* When Ready Queue is empty */
  if ((ReadyQueue.isEmpty())) {
   int k = BlockedQueue.get(0).JobStatus;

   /* Blocked Queue */
   if (BlockedQueue.get(0).JobStatus == 0) {

    if (CLOCK - BlockedQueue.get(0).JobClockOut >= 10) {
     BlockedQueue.get(0).reminderOfLastQuantum = 35
       - BlockedQueue.get(0).TimeSlice;
     if (31 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 35) {
      JobPosition = 0;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(0, BlockedQueue.get(0));
      }
     } else if (26 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 30) {
      JobPosition = 1;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(1, BlockedQueue.get(0));
      }
     } else if (21 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 25) {
      JobPosition = 2;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      }

      else {
       ReadyQueue.add(2, BlockedQueue.get(0));
      }
     } else if (16 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 20) {
      JobPosition = 3;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(3, BlockedQueue.get(0));
      }
     } else if (11 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 15) {
      JobPosition = 4;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(4, BlockedQueue.get(0));
      }
     } else if (06 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 10) {
      JobPosition = 5;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(5, BlockedQueue.get(0));
      }
     } else if (01 <= BlockedQueue.get(0).reminderOfLastQuantum
       && BlockedQueue.get(0).reminderOfLastQuantum <= 05) {
      JobPosition = 6;
      if (JobPosition > ReadyQueue.size()) {
       ReadyQueue.add(BlockedQueue.get(0));
      } else {
       ReadyQueue.add(6, BlockedQueue.get(0));
      }
     }
     BlockedQueue.remove(0);
     if (!(ReadyQueue.isEmpty()))
      new CPU(ReadyQueue.get(0).JobPC, traceSwitch, ReadyQueue.get(0));
    }
    /* CPU Idle Time Scenario */
    else if (BlockedQueue.size() > 0 && ReadyQueue.isEmpty()) {
     if (CLOCK - BlockedQueue.get(0).JobClockOut >= 10) {
      ReadyQueue.add(BlockedQueue.get(0));
      BlockedQueue.remove(0);
      new CPU(ReadyQueue.get(0).JobPC, traceSwitch, ReadyQueue.get(0));
     } else if (CLOCK - BlockedQueue.get(0).JobClockOut < 10) {
      int IdleTime = CLOCK - BlockedQueue.get(0).JobClockOut;
      TotalIdleTime = IdleTime + TotalIdleTime;
      int LeftIdleTime1 = 10 - IdleTime;
      CLOCK = CLOCK + LeftIdleTime1;
      BlockedQueue.get(0).EachJobIO = LeftIdleTime1
        + BlockedQueue.get(0).EachJobIO;
      ReadyQueue.add(BlockedQueue.get(0));
      BlockedQueue.remove(0);
      new CPU(ReadyQueue.get(0).JobPC, ReadyQueue.get(0).traceSwitch,
        ReadyQueue.get(0));

     }
    }

   }
  }
 }

}
