package mapreduce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utility.CommandType;
import utility.IndicationType;
import utility.Message;
import utility.Message.msgType;

/*
 * This is the specific manager server for each worker. This server will communicate with the worker and handle all
 * the response and state update messages from the worker.
 * @Author: Yifan Li
 * @Author: Jian Wang
 * 
 * @Date: 11/9/2014
 * @Version:0.00,developing version
 */
public class WorkerManagerServer implements Runnable{
    private Master master;
    private int workerId;
    private Socket socket;
    private volatile boolean running;

    private ObjectInputStream objInput;
    private ObjectOutputStream objOutput;

    public WorkerManagerServer(Master master, int id, Socket s) throws IOException {

        this.master = master;
        workerId = id;
        running = true;

        System.out.println("adding a new manager server for worker "+id);
        socket = master.workerSocMap.get(id);
        master.workerStatusMap.put(id,1);//set the worker status to be alive
        objInput = new ObjectInputStream(socket.getInputStream());
        objOutput = new ObjectOutputStream(socket.getOutputStream());
       
    }
    
    //This method will handle the heartbeat information from the worker to update the status of all the tasks and workers.
    private void handleHeartbeat(){
    	//update the worker status
    	//update the tasks status
    }
    
    //This method will handle the task complete infomation. If a MapTask completed, the method will check if all the related
    //tasks are completed or not. If a ReduceTask completed, the method will chekc if all the related tasks are completed or
    //not.
    private void handleTaskcomplete(){
    	
    }
    
    public void run(){
        try{
        	// assign ID to worker
        	Message assignIDmsg = new Message(msgType.COMMAND);
        	assignIDmsg.setCommandId(CommandType.ASSIGNID);
        	assignIDmsg.setWorkerID(workerId);
        	sendToWorker(assignIDmsg);
        	
            Message workerMessage;
            System.out.println("managerServer for worker "+workerId+"running");
            
            //start to listen to the response from the worker
            while(running){
                
            	//receive the msg
                try{
                    workerMessage = (Message) objInput.readObject();
                }catch(ClassNotFoundException e){
                    continue;
                }   
                
                //process the msg
             
                if(workerMessage.getMessageType() == msgType.RESPONSE){
                	switch(workerMessage.getResponseId()){
                    	default:
                    		System.out.println("unrecagnized message");
                	}
                }else if(workerMessage.getMessageType() == msgType.INDICATION){
                	switch(workerMessage.getIndicationId()){
                		case HEARTBEAT:
                			handleHeartbeat();
                			break;
                		case TASKCOMPLETE:
                			handleTaskcomplete();
                			break;
                		default:
                			System.out.println("unrecagnized message");
                	}
                }
            }
            objInput.close();
            objOutput.close();
        }catch(IOException e){
            
        }
    }
    // the send msg method
    public int sendToWorker(Message cmd) throws IOException{
        objOutput.writeObject(cmd);
        objOutput.flush();
        return 0;
}

    
    public void stop(){
        running = false;
    }
}
