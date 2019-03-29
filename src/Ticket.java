

public class Ticket {
    private String ticketNumber;


    public Ticket(long id){
        this.setTicketNumber(id);
    }

    public void setTicketNumber(Long l){
        this.ticketNumber="Park ticket №:"+l;
    }

    public String getTicketNumber(){
        return ticketNumber;
    }

}
