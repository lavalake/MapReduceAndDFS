package utility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.PriorityQueue;



/**
 * <code>RecordWriter</code> writes the output &lt;key, value&gt; pairs 
 * to an output file.
 
 * <p><code>RecordWriter</code> implementations write the job outputs to the
 * {@link FileSystem}.
 * 
 * @see OutputFormat
 */
public  class ReducerRecordWriter extends RecordWriter {
  /** 
   * Writes a key/value pair.
   *
   * @param key the key to write.
   * @param value the value to write.
   * @throws IOException
   */    
  PriorityQueue pairQ;
  public ReducerRecordWriter(){
      pairQ = new PriorityQueue();
  }

  
  @Override
  public void write(Object key, Object value, int taskId) throws IOException{
      
      String strTaskID = Long.toString(taskId);
      
      File fileToWrite = new File("Output/Intermedia/" + strTaskID +".output");
      try {
          if (fileToWrite.exists() == false) {

              fileToWrite.createNewFile();

          }
          KeyValue pair = new KeyValue();
          pair.setKey(key);
          pair.setValue(value);
          /*FileOutputStream fileStream = new FileOutputStream(fileToWrite, true);
          ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
          outputStream.writeObject(pair);
          
          //close the writer
          outputStream.close();*/
          pairQ.add(pair);
          
      } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
  }

  /** 
   * Close this <code>RecordWriter</code> to future operations.
   * 
   * @param reporter facility to report progress.
   * @throws IOException
   */ 
  public void close() throws IOException{
      
  }

  public PriorityQueue getPairQ(){
      return pairQ;
  }


}