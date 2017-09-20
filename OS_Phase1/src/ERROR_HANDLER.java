import java.io.BufferedWriter;
import java.io.FileWriter;

/*
Description:
The error_handler subsystem handles the errors with custom exceptions by throwing
a string message to the Exception class.It handles both warnings and errors.
In case of warnings the program continue's execution and In case of errors it
calls the dump and written  to handle the termination.The static variable message used to
print the Description of error or warning outputfile.txt file;
*/

public class ERROR_HANDLER extends Exception {
  
  public ERROR_HANDLER(int errornumber,PROCESSMANAGER PCB) {
SCHEDULER.blockElements.clear();
SCHEDULER.DataElements.clear();
SCHEDULER.blockElements1.clear();
SCHEDULER.Outputelements.clear();
int pid=0;
    if (errornumber == 101) {
      errorcaseswrite("File not found exception",101);
  
      AvoidErrorJobs();
    }  
 
    if (errornumber == 104) {
      SCHEDULER.ReadyQueue.remove(0);  
      errorcaseswrite("Address out of range",104);
  
      MEMORY.memory("DUMP", 0, "0",PCB);
    }
    if (errornumber == 105) {
     
      errorcaseswrite("Illegal Input",105);
    
      AvoidErrorJobs();
    }
    if (errornumber == 106) {
      errorcaseswrite("Invalid Loader Format Character",106);

      AvoidErrorJobs();
    }
    if (errornumber == 107) {  
      
      SCHEDULER.ReadyQueue.remove(0);      
      errorcaseswrite("Overlow",107);
   
      MEMORY.memory("DUMP", 0, "0",PCB);
     LOADER.readeachJob(); 
   
    }
    if (errornumber == 108) {
      errorcaseswrite("More than one of the three bits is set",108);
    
      MEMORY.memory("DUMP", 0, "0",PCB);
      LOADER.readeachJob(); 
     
    }
    if (errornumber == 109) {
      errorcaseswrite("The input accepts only 3 digit Hex Number",109);
   
      AvoidErrorJobs();
    }
    if (errornumber == 110) {
      errorcaseswrite("The input has to be in Hex format",110);
 
      AvoidErrorJobs();
    }
    if (errornumber == 111) {
      SCHEDULER.ReadyQueue.remove(0);  
      errorcaseswrite("invalid op-code",111);
    
      MEMORY.memory("DUMP", 0, "0",PCB);
        }
    if (errornumber == 112) {
      SCHEDULER.ReadyQueue.remove(0);  
      errorcaseswrite("Wrong combinations of actions",112);
   
      MEMORY.memory("DUMP", 0, "0",PCB);
      LOADER.readeachJob(); 
     
    }
    if (errornumber == 140) 
    {
     
      SCHEDULER.ReadyQueue.remove(0);  
      errorcaseswrite("Suspected Infinite Loop",140);
      
      MEMORY.memory("DUMP", 0, "0",PCB);     
      LOADER.readeachJob(); 
     
    }
if (errornumber == 113) {
  errorcaseswrite("Unrecognizable Character Encountered while Loading",113);
 
      AvoidErrorJobs();
    }
if (errornumber == 114) {
  errorcaseswrite("Bad input file format: Extra characters detected.",114);
   
      AvoidErrorJobs();
    }
if (errornumber == 122) {
  errorcaseswrite("Warning : Bad Trace Flag.",122);
      
    }
if (errornumber == 131) {
  errorcaseswrite("Missing The ** END record",131);
     
       AvoidErrorJobs();
}
if (errornumber == 130) {
  errorcaseswrite("Missing The ** DATA record",130);
    
       AvoidErrorJobs();
}
if (errornumber == 132) {
  errorcaseswrite("Missing The ** JOB record",132);
  
       AvoidErrorJobs();
}
if (errornumber == 133) {
  errorcaseswrite("Missing Data for Job",133);
    
       AvoidErrorJobs();
}
if (errornumber == 134) {
  errorcaseswrite("Missing Job",134);
  
       AvoidErrorJobs();
}
if (errornumber == 135) {
  errorcaseswrite("NULL JOB",135);
    
       AvoidErrorJobs();
}
if (errornumber == 141) {
  errorcaseswrite("Illegal Input",141);
   
       AvoidErrorJobs();
}
if (errornumber == 142) {
  errorcaseswrite("Program too Long",142);
    
       AvoidErrorJobs();
}
if (errornumber == 143) {
  errorcaseswrite("Missing/Unrecognizable Trace Bit",143);

       AvoidErrorJobs();
}
if (errornumber == 150) {
  SCHEDULER.ReadyQueue.remove(0);  
  errorcaseswrite("Insufficient Output Space",150);
 
  MEMORY.memory("DUMP", 0, "0",PCB);
  LOADER.readeachJob(); 
}
  }
  public void errorcaseswrite(String msg,int id)
  {
    try{
    SYSTEM.writeProgressFile= new FileWriter("ProgressFile", true);
    SYSTEM.writeProgressFile.write("Error Message"+msg);
    SYSTEM.writeProgressFile.write(System.lineSeparator());
    SYSTEM.writeProgressFile.write("ErrorID"+id);
    SYSTEM.writeProgressFile.write(System.lineSeparator());
    SYSTEM.writeProgressFile.write("Bad Trace Flag"+msg);
    int count=0;
    
    if(id==104)
    {    
      SYSTEM.writeProgressFile.write("Unrecognizable Character Encountered");
      SYSTEM.writeProgressFile.write(System.lineSeparator());
     
      count++;
    }
    if(id==104 && count==1)
    { 
      SYSTEM.writeProgressFile.write("");
      count++;
    }
    SYSTEM.writeProgressFile.close();
    }
    catch(Exception e)
    {
    }
  }
  public static void AvoidErrorJobs()
  {
    
    LOADER.jobid=LOADER.jobid+1;
      try {
      String sCurrentLine;
      int k=0;
      SYSTEM.br.reset();
      while ((sCurrentLine = SYSTEM.br.readLine())!=null) {
       
        if(sCurrentLine.isEmpty())
        {
          LOADER.PCBCheckFlag="true";
          LOADER.initialline=0;
          LOADER.count=0;
          LOADER.DataLinesCount=0;
          LOADER.LoaderLinesCount=0;
          LOADER.initialline=0;
          LOADER.count=0;
          LOADER.StoreLines.clear();
          LOADER.FilePointer=0;
          LOADER.readeachJob(); 
        }
       k=k+1;
        }
     
      }
      catch(Exception e)
      {
       
      
    }
  }
  
  
  

}
