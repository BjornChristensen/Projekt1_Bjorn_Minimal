import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Salon {
    static Scanner keyboard=new Scanner(System.in);
    String name;
    ArrayList<Appointment> appointments=new ArrayList<Appointment>();

    Salon(String name){
        this.name=name;
    }

    // Display the main menu, and retrieve user input.
    void menu(){
        char input;
        boolean keepGoing=true;
        do {
            System.out.println();
            System.out.println(name+ " - Hoved menu");
            System.out.println("  Tryk 1 for ny aftale");
            System.out.println("  Tryk 2 for vis aftaler");
            System.out.println("  Tryk 3 for slet aftale");
            System.out.println("  Tryk 4 for regnskab");
            System.out.println("  Tryk 0 for afslut");
            System.out.print("Indtast valg: ");
            input=keyboard.next().charAt(0);
            System.out.println();
            switch (input){
                case '1': createAppointment(); break;
                case '2': showAppointments(); break;
                case '3': deleteAppointment(); break;
                case '4': accounting(); break;
                case '0': keepGoing=false; break;
                default:  System.out.println("Ugyldigt indput. Prøv igen");
            }
            if (keepGoing) Main.waitForEnter();
        } while (keepGoing);
        System.out.println("Tak for i dag");
    }

    // Create a new Appointment and add it to appointment list
    void createAppointment(){
        System.out.println(name+" - Ny aftale");
        LocalDate date=inputWorkDate();                             // user inputs a legal work date
        LocalTime time=inputTimeSlot();                             // user inputs time
        LocalDateTime datetime=LocalDateTime.of(date, time);        // combined datetime on selected day
        if (isBusy(datetime)) {
            System.out.println("Tidspunktet er optaget. Prøv igen");
        } else {
            System.out.print("Indtast kunde navn: ");
            String customer=keyboard.next();
            appointments.add(new Appointment(customer, datetime));
            System.out.println("Aftalen er oprettet");
        }
    }

    // Print all appointments on a given day
    void showAppointments(){
        System.out.println(name+" - Vis aftaler ");
        LocalDate date=inputDate();
        ArrayList<Appointment> list=appointmentsOn(date);
        if (list.isEmpty()) System.out.println("Ingen aftaler på valgte dato");
        for (Appointment a: list) System.out.println(a);
    }

    // Delete an appointment input by user
    void deleteAppointment(){
        System.out.println(name+" - Slet aftale ");
        LocalDate date=inputWorkDate();
        LocalTime time=inputTimeSlot();
        LocalDateTime datetime=LocalDateTime.of(date, time);
        Appointment app=getAppointment(datetime);           // lookup appointment from appointments list
        if (app==null){                                     // it wasn´t there
            System.out.println("Beklager. Aftale ikke fundet");
        } else {
            appointments.remove(app);
            System.out.println("Aftalen er slettet");
        }
    }

    // Display accounting info for a given date
    void accounting(){
        System.out.println(name+" - Regnskab");
        LocalDate date=inputWorkDate();
        ArrayList<Appointment> list=appointmentsOn(date);
        if (list.isEmpty()) System.out.println("Ingen aftaler på valgte dato");
        double total=0;
        for (Appointment a: list) {
            System.out.printf("%s %7.2f \n", a ,a.price);
            total=total+a.price;
        }
        System.out.printf("Ialt %7.2f \n", total);
    }

    /**************************************** Bookkeeping of appointment list *****************************************/
    // Find all appointments on a given LocalDate
    ArrayList<Appointment> appointmentsOn(LocalDate date) {
        ArrayList<Appointment> list=new ArrayList<Appointment>();
        for (Appointment a: appointments) {
            if (date.equals(a.datetime.toLocalDate())) {
                list.add(a);
            }
        }
        return list;
    }

    // Find an appointment at a given timeslot dt in appointment list. Return null if no appointment exists
    Appointment getAppointment(LocalDateTime dt) {
        for (Appointment a: appointments) {
            if (a.datetime.equals(dt)) return a;
        }
        return null;
    }

    // Check for an appointment on a given timeslot
    boolean isBusy(LocalDateTime dt) {
        return getAppointment(dt)!=null;
    }

    /******************************************** Date and time methods ***********************************************/
    // Read a LocalDate that is not a weekend from keyboard
    LocalDate inputWorkDate(){
        LocalDate date;
        while (true){
            date=inputDate();
            if (date.getDayOfWeek()==DayOfWeek.SATURDAY || date.getDayOfWeek()==DayOfWeek.SUNDAY) {
                System.out.println(date + " er en weekend");
            } else {
                return date;
            }
        } // while
    }

    // Read a LocalDate from keyboard
    LocalDate inputDate(){
        while (true){
            System.out.print("Indtast dato (åååå-mm-dd): ");
            String input=keyboard.next();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Fejl i dato. Prøv igen");
            }
        }
    }

    // Read a LocalTime in [10:00,17:30] in ½-hour steps from keyboard
    LocalTime inputTimeSlot(){
        LocalTime slot;
        while (true){
            slot=inputTime();
            if (10<=slot.getHour() && slot.getHour()<=17 && (slot.getMinute()==0 || slot.getMinute()==30))
                return slot;
            else
                System.out.println("Kun tider hver ½ timen mellem 10:00 og 17:30");
        }
    }

    // Read a LocalTime from keyboard
    LocalTime inputTime(){
        while (true){
            System.out.print("Indtast tid (tt:mm): ");
            String input=keyboard.next();
            try {
                return LocalTime.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Fejl i tid. Prøv igen");
            }
        }
    }
} // class Salon