import java.util.ArrayList;
import java.util.Collections;

public class Car {
    private static long id = 0L;
    private long idCar;
    private static ArrayList<String> idList =new ArrayList<>();
    private static int count;

    public Car() {
        id++;
        while (count>0){
            id++;
            count = Collections.frequency(idList, Long.toString(id));
        }
        idCar=id;
        idList.add(Long.toString(idCar));
    }

    public long getCarId(){
        return idCar;
    }
}
