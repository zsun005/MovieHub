package edu.uci.ics.zexis1.service.api_gateway.threadpool;

import edu.uci.ics.zexis1.service.api_gateway.logger.ServiceLogger;

public class ClientRequestQueue {
    private ListNode head;
    private ListNode tail;

    public ClientRequestQueue() {
        head = null;
        tail = null;
    }
    public synchronized void enqueue(ClientRequest clientRequest) {
        ListNode temp = new ListNode(clientRequest, null);
        if(tail == null){
            head = temp;
            tail = temp;
        }
        else{
            tail.setNext(temp);
            tail = tail.getNext();
        }
        this.notify();
    }

    public synchronized ClientRequest dequeue() {
//        ServiceLogger.LOGGER.info("dequeue");
        if(head == null){
//            ServiceLogger.LOGGER.info("Dequeue Request return NULL");
            if(isEmpty()){
                try{
                    this.wait();
                } catch (InterruptedException e){}
            }
        }
        ListNode temp = this.head;
        this.head = this.head.getNext();

        if(this.head == null)
            this.tail = null;
        return temp.getClientRequest();
    }

    boolean isEmpty() {
        return (head == null);
    }
}
