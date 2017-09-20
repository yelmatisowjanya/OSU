import java.util.ArrayList;
import java.util.Arrays;

/*
Description:
The memory subsystem uses memory(X,Y,Z)as static because loader can directly access it.
The memory function contains the variables like X,Y,Z which are used in memory to read and
write into memory.The memory can be accessed only through these variables. The memory also contain dump
which is called when an error occurs.
*/
public class MEMORY extends SYSTEM {
 public static String[] loaderToMemoryArray = new String[512];
 public static String[] Block1Job = new String[32];
 public static String[] Block2Job = new String[32];
 public static String[] Block3Job = new String[64];
 public static String[] Block4Job = new String[64];
 public static String[] Block5Job = new String[64];
 public static String[] Block6Job = new String[128];
 public static String[] Block7Job = new String[128];
 static ArrayList<Integer> AvailableMemoryCheck = new ArrayList<Integer>(
   Arrays.asList());
 static int i = 0;
 static int block1 = 32, block2 = 32, block3 = 64, block4 = 64, block5 = 64,
   block6 = 128, block7 = 128;
 static int internalfragmentation;
 static int RemainingMemoryInBlock;
 static String MemoryCheckflag = "false";
 static String flag1 = "false", flag2 = "false", flag3 = "false",
   flag4 = "false", flag5 = "false", flag6 = "false", flag7 = "false";
 static ArrayList<Integer> bestfitblocktemp = new ArrayList<Integer>(
   Arrays.asList());

 /*
  * EA variable is used to calculate the Effective Address Variable Z is the
  * memory buffer register or MBR
  */


 public static String memory(String X, int Y, String Z, PROCESSMANAGER PCB) {
  int EA = 0;
  EA = Y;

  if (X == "READ") {
   Z = MemoryRead(PCB, EA, Z);
 
  } else if (X == "WRIT") {
 
   MemoryWrite(PCB, EA, Z);
  }

  else if (X == "DUMP") {

   DumpOutput(PCB);

   /* System.exit(1); */
  } else {
   new ERROR_HANDLER(106, PCB);
  }
  return Z;
 }

 /* This function is used to calculate the best fit */
 public static String MemoryAllocation(int TotalMemoryRequired) {
  if (TotalMemoryRequired > 128) {
   new ERROR_HANDLER(142, null);
  }
  int min = 0;

  String bestfitblock = "";
  int[] BlockArray = new int[7];
  BlockArray[0] = block1;
  BlockArray[1] = block2;
  BlockArray[2] = block3;
  BlockArray[3] = block4;
  BlockArray[4] = block5;
  BlockArray[5] = block6;
  BlockArray[6] = block7;

  if (TotalMemoryRequired <= block1 && flag1.equals("false")) {
   AvailableMemoryCheck.add(BlockArray[0]);
  }
  if (TotalMemoryRequired <= block2 && flag2.equals("false")) {
   AvailableMemoryCheck.add(BlockArray[1]);
  }
  if (TotalMemoryRequired <= block3 && flag3.equals("false")) {
   AvailableMemoryCheck.add(BlockArray[2]);
  }
  if (TotalMemoryRequired <= block4 && flag4.equals("false")) {
   AvailableMemoryCheck.add(BlockArray[3]);
  }
  if (TotalMemoryRequired <= block5 && flag5.equals("false")) {
   AvailableMemoryCheck.add(BlockArray[4]);
  }
  if (TotalMemoryRequired <= block6 && flag6.equals("false")) {
   AvailableMemoryCheck.add(BlockArray[5]);
  }
  if (TotalMemoryRequired <= block7 && flag7.equals("false")) {
   AvailableMemoryCheck.add(BlockArray[6]);
  }
  if (AvailableMemoryCheck.isEmpty()) {
   MemoryCheckflag = "false";
  }
  for (int i1 = 0; i1 < AvailableMemoryCheck.size(); i1++) {
  
   min = AvailableMemoryCheck.get(0);
  
   int number = AvailableMemoryCheck.get(i1);
   if (number < min)
    min = number;

  }
  if (!(AvailableMemoryCheck.isEmpty())) {
   for (int i2 = 0; i2 < 7; i2++) {
    if (BlockArray[i2] == min) {
     AvailableMemoryCheck.clear();
     MemoryCheckflag = "true";
     bestfitblocktemp.add(i2);
     
    }

   }
  }
  int count = 0;
  for (int j = 0; j < bestfitblocktemp.size(); j++) {
   min = bestfitblocktemp.get(0);
   int number = bestfitblocktemp.get(j);
   if (number < min)
    min = number;
   bestfitblock = "block" + (min + 1);
   
   if (count == 0) {
    BlockArray[min] = BlockArray[min] - TotalMemoryRequired;
    count++;
   }
  }

  bestfitblocktemp.clear();
  if (bestfitblock.equals("block1")) {
    internalfragmentation=internalfragmentation+(32-TotalMemoryRequired);
   block1 = BlockArray[0];
   flag1 = "true";
   BaseAddress = 0;
   BoundAddress = BaseAddress + TotalMemoryRequired;
   BoundAddress = BoundAddress - 1;
  } else if (bestfitblock.equals("block2")) {
    internalfragmentation=internalfragmentation+(32-TotalMemoryRequired);
   block2 = BlockArray[1];
   flag2 = "true";
   BaseAddress = 32;
   BoundAddress = BaseAddress + TotalMemoryRequired;
   BoundAddress = BoundAddress - 1;
  } else if (bestfitblock.equals("block3")) {
    internalfragmentation=internalfragmentation+(64-TotalMemoryRequired);
   block3 = BlockArray[2];
   flag3 = "true";
   BaseAddress = 64;
   BoundAddress = BaseAddress + TotalMemoryRequired;
   BoundAddress = BoundAddress - 1;
  } else if (bestfitblock.equals("block4")) {
    internalfragmentation=internalfragmentation+(64-TotalMemoryRequired);
   block4 = BlockArray[3];
   flag4 = "true";
   BaseAddress = 128;
   BoundAddress = BaseAddress + TotalMemoryRequired;
   BoundAddress = BoundAddress - 1;
  } else if (bestfitblock.equals("block5")) {
    internalfragmentation=internalfragmentation+(64-TotalMemoryRequired);
   block5 = BlockArray[4];
   flag5 = "true";
   BaseAddress = 192;
   BoundAddress = BaseAddress + TotalMemoryRequired;
   BoundAddress = BoundAddress - 1;
  } else if (bestfitblock.equals("block6")) {
    internalfragmentation=internalfragmentation+(128-TotalMemoryRequired);
   block6 = BlockArray[5];
   flag6 = "true";
   BaseAddress = 256;
   BoundAddress = BaseAddress + TotalMemoryRequired;
   BoundAddress = BoundAddress - 1;
  } else if (bestfitblock.equals("block7")) {
    internalfragmentation=internalfragmentation+(128-TotalMemoryRequired);
   block7 = BlockArray[6];
   flag7 = "true";
   BaseAddress = 384;
   BoundAddress = BaseAddress + TotalMemoryRequired;
   BoundAddress = BoundAddress - 1;

  }

  return MemoryCheckflag;
 }

 public static void MemoryDeallocation(PROCESSMANAGER PCB) {
  int BlockToBeDealloacted = PCB.JobBaseAddress;
  if (BlockToBeDealloacted == 0) {
   flag1 = "false";
   block1 = 32;
   Arrays.fill(Block1Job, null);
   for (int i = 0; i < 32; i++) {
    loaderToMemoryArray[i] = null;
   }
  } else if (BlockToBeDealloacted == 32) {
   flag2 = "false";
   block2 = 32;
   Arrays.fill(Block2Job, null);
   for (int i = 32; i < 64; i++) {
    loaderToMemoryArray[i] = null;
   }
  } else if (BlockToBeDealloacted == 64) {
   flag3 = "false";
   block3 = 64;
   Arrays.fill(Block3Job, null);
   for (int i = 64; i < 128; i++) {
    loaderToMemoryArray[i] = null;
   }
  } else if (BlockToBeDealloacted == 128) {
   flag4 = "false";
   block4 = 64;
   Arrays.fill(Block4Job, null);
   for (int i = 128; i < 192; i++) {
    loaderToMemoryArray[i] = null;
   }
  } else if (BlockToBeDealloacted == 192) {
   flag5 = "false";
   block5 = 64;
   Arrays.fill(Block5Job, null);
   for (int i = 198; i < 256; i++) {
    loaderToMemoryArray[i] = null;
   }

  } else if (BlockToBeDealloacted == 256) {
   flag6 = "false";
   block6 = 128;
   Arrays.fill(Block6Job, null);
   for (int i = 256; i < 384; i++) {
    loaderToMemoryArray[i] = null;
   }
  } else if (BlockToBeDealloacted == 384) {
   flag7 = "false";
   block7 = 128;
   Arrays.fill(Block7Job, null);
   for (int i = 384; i < 512; i++) {
    loaderToMemoryArray[i] = null;
   }
  }

 }

 public static void check() {
  flag1 = "false";
  flag2 = "false";
  flag3 = "false";
  flag4 = "false";
  flag5 = "false";
  flag6 = "false";
  flag7 = "false";
  block1 = 32;
  block2 = 32;
  block3 = 64;
  block4 = 64;
  block5 = 64;
  block6 = 128;
  block7 = 128;

 }

 public static String MemoryRead(PROCESSMANAGER PCB, int EA, String Z) {

  if (PCB.JobBaseAddress == 0) {
   Z = Block1Job[EA];
  } else if (PCB.JobBaseAddress == 32) {
   Z = Block2Job[EA];
  } else if (PCB.JobBaseAddress == 64) {

   Z = Block3Job[EA];

  } else if (PCB.JobBaseAddress == 128) {
   Z = Block4Job[EA];
  } else if (PCB.JobBaseAddress == 192) {
   Z = Block5Job[EA];
  } else if (PCB.JobBaseAddress == 256) {
   Z = Block6Job[EA];
  } else if (PCB.JobBaseAddress == 384) {
   Z = Block7Job[EA];
  }
  return Z;
 }

 public static void MemoryWrite(PROCESSMANAGER PCB, int EA, String Z) {

  if (PCB.JobBaseAddress == 0) {
   Block1Job[EA] = Z;
  } else if (PCB.JobBaseAddress == 32) {
   Block2Job[EA] = Z;
  } else if (PCB.JobBaseAddress == 64) {

   Block3Job[EA] = Z;

  } else if (PCB.JobBaseAddress == 128) {

   Block4Job[EA] = Z;
  } else if (PCB.JobBaseAddress == 192) {
   Block5Job[EA] = Z;
  } else if (PCB.JobBaseAddress == 256) {
   Block6Job[EA] = Z;
  } else if (PCB.JobBaseAddress == 384) {
   Block7Job[EA] = Z;
  }

 }

 public static void EachJobInBlock(int address) {
  if (address == 0) {
   for (int i = 0, j = 0; i < 32; i++, j++) {
    Block1Job[j] = loaderToMemoryArray[i];
   }
  } else if (address == 32) {
   for (int i = 32, j = 0; i < 64; i++, j++) {
    Block2Job[j] = loaderToMemoryArray[i];
   }
  } else if (address == 64) {
   for (int i = 64, j = 0; i < 128; i++, j++) {
    Block3Job[j] = loaderToMemoryArray[i];
   }
  } else if (address == 128) {
   for (int i = 128, j = 0; i < 192; i++, j++) {
    Block4Job[j] = loaderToMemoryArray[i];
   }
  } else if (address == 192) {
   for (int i = 192, j = 0; i < 256; i++, j++) {
    Block5Job[j] = loaderToMemoryArray[i];
   }
  } else if (address == 256) {
   for (int i = 256, j = 0; i < 384; i++, j++) {
    Block6Job[j] = loaderToMemoryArray[i];
   }
  } else if (address == 384) {
   for (int i = 384, j = 0; i < 512; i++, j++) {
    Block7Job[j] = loaderToMemoryArray[i];
   }
  }
 }

 public static void LoadertoMemory(String X, int Y, String Z) {
  if (X == "WRIT") {
   loaderToMemoryArray[Y] = Z;

  }
 }

 public static void IO_read_operation1(PROCESSMANAGER PCB,
   int dataLinesBaseAddressPointer, int dataLinesBoundAddressPointer) {
  if (PCB.JobBaseAddress == 0) {
   for (int i = dataLinesBaseAddressPointer; i < dataLinesBoundAddressPointer; i++) {
    PCB.InputLines.add(Block1Job[i]);

   }
  } else if (PCB.JobBaseAddress == 32) {
   for (int i = dataLinesBaseAddressPointer; i < dataLinesBoundAddressPointer; i++) {
    PCB.InputLines.add(Block2Job[i]);
   }
  } else if (PCB.JobBaseAddress == 64) {
   for (int i = dataLinesBaseAddressPointer; i < dataLinesBoundAddressPointer; i++) {
    PCB.InputLines.add(Block3Job[i]);
   }
  } else if (PCB.JobBaseAddress == 128) {
   for (int i = dataLinesBaseAddressPointer; i < dataLinesBoundAddressPointer; i++) {
    PCB.InputLines.add(Block4Job[i]);
   }
  } else if (PCB.JobBaseAddress == 192) {
   for (int i = dataLinesBaseAddressPointer; i < dataLinesBoundAddressPointer; i++) {
    PCB.InputLines.add(Block5Job[i]);
   }
  } else if (PCB.JobBaseAddress == 256) {
   for (int i = dataLinesBaseAddressPointer; i < dataLinesBoundAddressPointer; i++) {
    PCB.InputLines.add(Block6Job[i]);
   }
  } else if (PCB.JobBaseAddress == 384) {
   for (int i = dataLinesBaseAddressPointer; i < dataLinesBoundAddressPointer; i++) {
    PCB.InputLines.add(Block7Job[i]);
   }

  }

 }

 public static void IO_write_operation1(PROCESSMANAGER PCB,
   int OutputLinesBaseAddressPointer, int OutputLinesBoundAddressPointer,
   String output) {

  if (LOADER.outputLine < PCB.outputlinescount1) {

  }
  if (PCB.JobBaseAddress == 0) {
   Block1Job[OutputLinesBaseAddressPointer] = output;
   OutputLinesBoundAddressPointer++;

  } else if (PCB.JobBaseAddress == 32) {

   Block2Job[OutputLinesBaseAddressPointer] = output;
   OutputLinesBoundAddressPointer++;

  } else if (PCB.JobBaseAddress == 64) {

   Block3Job[OutputLinesBaseAddressPointer] = output;
   OutputLinesBoundAddressPointer++;

  } else if (PCB.JobBaseAddress == 128) {

   Block4Job[OutputLinesBaseAddressPointer] = output;
   OutputLinesBoundAddressPointer++;

  } else if (PCB.JobBaseAddress == 192) {

   Block5Job[OutputLinesBaseAddressPointer] = output;
   OutputLinesBoundAddressPointer++;

  } else if (PCB.JobBaseAddress == 256) {

   Block6Job[OutputLinesBaseAddressPointer] = output;
   OutputLinesBoundAddressPointer++;

  } else if (PCB.JobBaseAddress == 384) {

   Block7Job[OutputLinesBaseAddressPointer] = output;
   OutputLinesBoundAddressPointer++;

  }
  PCB.outputlinescount1++;

 }

}
