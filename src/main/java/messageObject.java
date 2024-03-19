import java.io.Serializable;
import java.util.ArrayList;

public class messageObject implements Serializable {
    int sender = -1;
    String message;
    ArrayList<Integer> clients;
}
