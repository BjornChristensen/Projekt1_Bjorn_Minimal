import java.time.LocalDateTime;

public class Appointment {
    String customer;
    LocalDateTime datetime;
    double price;

    Appointment(String cust, LocalDateTime dt){     // Constructor
        customer =cust;
        datetime=dt;
        price=130;
    }

    public String toString() {
        return datetime.toLocalDate()+" "+datetime.toLocalTime()+" "+customer;
    }
}