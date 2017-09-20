/*
Description:
The Process Manager is used to define the various properties of the process which can be used by each process in various functions.
These properties defines the Process with PCB as the object 
*/
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class PROCESSMANAGER extends SYSTEM {
   int jobid;
   int JobBoundAddress;
   int JobBaseAddress;
   int dataLinesBaseAddressPointer;
   int dataLinesBoundAddressPointer;
   int JobStatus=2;
   int reminderOfLastQuantum;
   int TimeSlice=0;
   int JobClockOut;
   int JobClockIn;
   int EachJobIO;
   int JobPC;
   int JobLength;
   int DataLinesLength;
   int index;
   int outputindex;
   int outputlineslength;
   int OutputLinesBaseAddressPointer;
   int OutputLinesBoundAddressPointer;
   int Startaddress;
   int traceSwitch;
   String R5="000000000000";
   String R4="000000000000";
   String FlagStatus;
   int timer;
   int tobewrittencount=0;
   int outputlinescount1;
   ArrayList<String> InputLines = new ArrayList<String>(Arrays.asList());
    String FileStatus;
    BufferedWriter writeEachPCBTraceFile;
   int readwritecount=0;
    void CreateTraceFile(String Filename)
    {
    String heading = "PC" + "\t" + "\t" + "\t" + "\t" + "Instruction" + "\t" + "\t"
        + "\t" + "\t" + "\t" + "R-Register" + "\t" + "\t" + "\t" + "EA"
        + "\t" + "\t" + "\t" + "\t" + "Contents of R & EA Before Execution"
        + "\t" + "\t" + "\t" + "Contents of R & EA After Execution";
  try{
    FileWriter fw=new FileWriter(Filename,false);
    fw.write(heading);
    fw.close();
    }
   catch(Exception e)
   {
    
     }
   
    }
        
     
  /*public PROCESSMANAGER(int jobid,int JobBoundAddress,int JobBaseAddress,int dataLinesBaseAddressPointer,int dataLinesBoundAddressPointer,int JobStatus )
  {
     this.jobid = jobid;       
     this.JobBoundAddress = JobBoundAddress;  
     this.JobBaseAddress = JobBaseAddress;  
     this.dataLinesBaseAddressPointer = dataLinesBaseAddressPointer;  
     this.dataLinesBoundAddressPointer = dataLinesBoundAddressPointer; 
     this.JobStatus=JobStatus;
  }*/
  
  
}
