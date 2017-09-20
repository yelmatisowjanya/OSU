
/*
Description:
The loader susbsystem takes the input file which is retrieved from the system
class.It calls the memory  to load the jobs from the file through buffer.The Loader can access the memory only
through buffer only. The loader takes the input from a file called InputFile.The loader responsibilty is to load the file input
and send them to memory through buffer.
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LOADER extends SYSTEM {
 static int m1temp;
 FileReader fr = null;
 static int LoaderLinesCount = 0;
 static int DataLinesCount;
 static int JobLength;
 static int initialline = 0;

 static int DataLine;
 static int count;
 static int LoaderLineCount;
 static int jobid = 1;
 static int outputLine;
 static PROCESSMANAGER PCB;
 static int FilePointer;
 /* The buffer variable is used for block transfer of each 12 bits long. */
 static String DataforJob;
 public static String FILENAME = "";
 static ArrayList<String> LoadDataLines = new ArrayList<String>(
   Arrays.asList());
 static ArrayList<String> buffer = new ArrayList<String>(Arrays.asList());
 static ArrayList<String> LoaderJobCheck = new ArrayList<String>(
   Arrays.asList());
 static ArrayList<String> LoaderModuleCheck = new ArrayList<String>(
   Arrays.asList());
 static String PCBCheckFlag;
 static String FirstLine;
 static int EndCheckFlag;
 
 static ArrayList<String> StoreLines = new ArrayList<String>(Arrays.asList());
 /*
  * The input_file_in_hexformat variable is used to store the hex input from a
  * file
  */

 public LOADER(int X, int Y) {

  /*
   * This Constructor Reads the input file and stores them in a arraylist with
   * name input_file_in_hexformat
   */
  try {

   fr = new FileReader(FILENAME);
   br = new BufferedReader(fr);
   String sCurrentLine;
   String name1[]=SYSTEM.name.split("/");
   name2=name1[4];
   br = new BufferedReader(new FileReader(FILENAME));
   while ((sCurrentLine = br.readLine()) != null) {

    br.mark(100);
    hexBin(sCurrentLine);
   }

  } catch (FileNotFoundException e1) {

  } catch (IOException e) {

  }

 }

 public static void readeachJob() {
 
  try {

   if (PCBCheckFlag == "false") {
    initialline = 0;
    count = 0;
    for (int i = 0; i < 2; i++) {
     hexBin(StoreLines.get(i));

    }
    FilePointer--;
   }
   if (PCBCheckFlag == "true") {
    String sCurrentLine;
    while ((sCurrentLine = br.readLine()) != null) {
     br.mark(100);
     hexBin(sCurrentLine);
    }
    if ((sCurrentLine = br.readLine()) == null) {
   
     SYSTEM.PCBCreation(0, 0, 0, "false", 0, 0, 0, 0, 0);
    }
   }
  } catch (FileNotFoundException e1) {

  } catch (IOException e) {

  }
 }

 /*
  * The below function is to convert from hex to binary and to store the trace
  * switch. wherein binStrBuilder variable is used to append all hex to binary
  * converted elements
  */
 public static String hexBin(String input_file_in_hexformat) {

  StoreLines.add(input_file_in_hexformat);
  FilePointer++;
  StringBuilder binStrBuilder = new StringBuilder();
  String output = "";

  if(name2.equals("tb"))
      {
    terminated=106;

    abnormalterminated=0;
    timer=1300;
      }
  else
  {
    terminated=80;
    abnormalterminated=24;
    timer=10;
  }
  if (input_file_in_hexformat.contains("JOB") && initialline == 0) {
   EndCheckFlag = 1;
   String[] LoaderModule_InitialLine = input_file_in_hexformat.split(" ");
   String DataLine_Hex = LoaderModule_InitialLine[2];
   String OutputLine_Hex = LoaderModule_InitialLine[3];
   int DataLine_Binary = Integer.parseInt(DataLine_Hex, 16);
   int outputLine_Binary = Integer.parseInt(OutputLine_Hex, 16);
   String DataLine_Binarytemp = Integer.toBinaryString(DataLine_Binary);
   String outputLine_Binarytemp = Integer.toBinaryString(outputLine_Binary);
   int DataLine_Binarytemp1 = Integer.parseInt(DataLine_Binarytemp);
   int outputLine_Binarytemp1 = Integer.parseInt(outputLine_Binarytemp);
   int DataLineDecimal = getDecimalFromBinary(DataLine_Binarytemp1);
   int OutputlineDecimal = getDecimalFromBinary(outputLine_Binarytemp1);
   DataLine = DataLineDecimal;
   outputLine = OutputlineDecimal;
   JobLength = DataLine + outputLine;
   LoaderModuleCheck.add(DataLine_Hex);
   initialline++;

  } else if (!(input_file_in_hexformat.contains("JOB")) && initialline == 0) {
   new ERROR_HANDLER(132, null);
  }
  /* This loop is executed when the loader module has Loader format and Data */
  else if (initialline == 1) {
   count++;
   int c = 1;
   int m = 0;
   String Each_hexinputelements = input_file_in_hexformat.toString();
   if (Each_hexinputelements.contains("DATA") && DataLine != 0) {
    try {
     DataforJob = br.readLine();
     br.mark(100);
     if (DataforJob.contains("END"))
      new ERROR_HANDLER(134, null);
    } catch (Exception e) {

    }
   }
   if (Each_hexinputelements.contains("END") && EndCheckFlag == 1) {
    try {

     new ERROR_HANDLER(135, null);
    } catch (Exception e) {

    }
   }
   String Hex_input_elements_temp2 = Each_hexinputelements.replaceAll(",", "");
   Hex_input_elements_temp2 = Hex_input_elements_temp2.replaceAll("\\s+", "");
   String[] hex_each_elements = Hex_input_elements_temp2.split("");
   int j = (Hex_input_elements_temp2.length()) - 3;
   for (int i = 1; i <= (hex_each_elements.length); i += 3) { /*
                                                               * to read 3 hex
                                                               * bits
                                                               */
    if (hex_each_elements.length <= 3 && count == 1) {
     EndCheckFlag = 2;
     output = Hex_input_elements_temp2;
     length = Integer.parseInt(output, 16);
     count++;
     JobLength = JobLength + length;
     PCBCheckFlag = MEMORY.MemoryAllocation(JobLength);
     if ((PCBCheckFlag.equals("false"))) {
      SYSTEM.PCBCreation(0, 0, 0, PCBCheckFlag, 0, 0, 0, 0, 0);
     }
    }

    else if (!input_file_in_hexformat.contains("DATA")
      && !input_file_in_hexformat.contains("END")
      && !(input_file_in_hexformat.isEmpty())) {
     output = Hex_input_elements_temp2.substring(i - 1, (i + 2));
     output = output.replace("[", "").replaceAll("]", "");

    }

    if ((LoaderLinesCount) == length) {
     if (!(input_file_in_hexformat.contains("DATA"))) {
      new ERROR_HANDLER(130, null);
     }
     if (input_file_in_hexformat.contains("DATA")) {

      try {
       for (int k1 = 0; k1 < DataLine; k1++) {
        if (DataLinesCount < DataLine) {
         if (DataLinesCount != 0)
          DataforJob = br.readLine();
         br.mark(100);
         if (DataforJob.length() > 3) {
          new ERROR_HANDLER(141, null);
         }
         try {
          int inputcheck = Integer.parseInt(DataforJob, 16);
         } catch (Exception e) {
          new ERROR_HANDLER(141, null);
         }

         if ((DataforJob.contains("END") && DataLine == DataLinesCount)) {
          new ERROR_HANDLER(133, null);
         }
         DataLinesCount++;
         int DataforJob_Binary = Integer.parseInt(DataforJob, 16);
         String DataforJob_Binarytemp = Integer
           .toBinaryString(DataforJob_Binary);

         String DataJob_Binary = DataforJob_Binarytemp.replace("[", "")
           .replace("]", "");
         String Data = String.format("%12s", DataJob_Binary).replace(' ', '0');
         LoadDataLines.add(Data);

         LoaderModuleCheck.add(DataJob_Binary);
        }
       }
       if (DataLinesCount == DataLine) {
        String exitcheck = br.readLine();
        br.mark(100);
        if (!(exitcheck.contains("END"))) {
         new ERROR_HANDLER(131, null);
        }
        if ((exitcheck.contains("END"))) {
         DataLinesCount = 0;
         LoaderLinesCount = 0;
         initialline = 0;
         count = 0;
         StoreLines.clear();
         FilePointer = 0;
         if (PCBCheckFlag.equals("true")) {
          for (int m4 = 0, m1 = BaseAddress; m4 < buffer.size(); m1++, m4++) {
           m1temp++;
           // MEMORY.memory("WRIT", m1,buffer.get(m1),PCB);
           MEMORY.LoadertoMemory("WRIT", m1, buffer.get(m4));
          }

          for (int m3 = 0, m2 = m1temp + BaseAddress; m3 < LoadDataLines
            .size(); m2++, m3++) {
           // MEMORY.memory("WRIT", m2,LoadDataLines.get(m2),PCB);
           MEMORY.LoadertoMemory("WRIT", m2, LoadDataLines.get(m3));
          }
          MEMORY.EachJobInBlock(BaseAddress);
          int dataLinesBaseAddress = 0;
          int dataLinesBoundAddress = 0;
          m1temp = 0;
          buffer.clear();
          LoadDataLines.clear();
          SYSTEM.PCBCreation(jobid, dataLinesBaseAddress, dataLinesBoundAddress,
            PCBCheckFlag, length, DataLine, outputLine, start_address,
            traceSwitch);
          jobid++;
         }

         input_file_in_hexformat = br.readLine();
         if (input_file_in_hexformat == null) {
          SYSTEM.exitflag = "true";
          
        

          SYSTEM.PCBCreation(0, 0, 0, "false", 0, 0, 0, 0, 0);

         }
         br.mark(100);
        }

       }
      } catch (Exception e) {

      }
     }

    }

    if (!input_file_in_hexformat.contains("DATA")
      && !input_file_in_hexformat.contains("END")
      && !(input_file_in_hexformat.isEmpty())) {

     int decimal = 0;
     try {
      decimal = Integer.parseInt(output, 16);
     } catch (Exception e) {
      new ERROR_HANDLER(113, null);
     }
     String binStr = Integer.toBinaryString(decimal);
     int len = binStr.length();
     StringBuilder sbf = new StringBuilder();
     if (len < 12) {
      for (int k = 0; k < (12 - len); k++) {
       sbf.append("0");
      }
      sbf.append(binStr);

     } else {
      sbf.append(binStr);
     }

     c++;

     if (count > 2) {
      LoaderLinesCount++;
      buffer.add(sbf.toString());
     }
     m++;

     binStrBuilder.append(sbf.toString());
    }

    if ((LoaderLinesCount) == length) {
     try {
      String startaddressLine = br.readLine();
      br.mark(100);
      String[] startaddress = startaddressLine.split(" ");
      start_address = Integer.parseInt(startaddress[0], 16);
      // start_address=getDecimalFromBinary(startaddress_binary);
      String traceSwitchtemp = startaddress[1];
      traceSwitch = Integer.parseInt(traceSwitchtemp);
      if (startaddress[1] == null) {
       new ERROR_HANDLER(143, null);
      }
      if (!(traceSwitch == 0 || traceSwitch == 1)) {
       new ERROR_HANDLER(122, null);

      }
     } catch (Exception e) {

     }
    }
   }
  }

  return binStrBuilder.toString();

 }

 public static int getDecimalFromBinary(int binary) {
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

 public static String binHex(String dumpelement) {

  try {
   int decimal = Integer.parseInt(dumpelement, 2);
   String dumpOutputinHex = Integer.toString(decimal, 16);
   dumpOutputinHex = String.format("%3s", (dumpOutputinHex)).replace(' ', '0');
   return dumpOutputinHex;
  } catch (NumberFormatException e) {
   return "000";
  }

 }

}
