package NIC.WebGWT.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import NIC.WebGWT.client.GreetingService;
import NIC.WebGWT.shared.Driver;

/**
 * Реализация удалённого сервиса. Хранит данные о пилотах по командам.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
    private Map<String, List<Driver>> db = null;

    public List<String> getTeamList() {
        initDB();
        return new ArrayList<String>(db.keySet());
    }

    public List<Driver> getDriverList(String team) throws IllegalArgumentException {
        initDB();
        // Пустое значение или null: все пилоты
        if (team == null || team.isEmpty()) {
            List<Driver> all = new ArrayList<Driver>();
            for (List<Driver> list : db.values()) {
                all.addAll(list);
            }
            return all;
        }
        List<Driver> result = db.get(team);
        if (result == null) {
            throw new IllegalArgumentException("Нет команды " + team);
        }
        return result;
    }

    /** Заполнение «базы данных» при первом обращении. */
    private synchronized void initDB() {
        if (db != null) {
            return;
        }
        db = new LinkedHashMap<String, List<Driver>>();

        List<Driver> redBull = new ArrayList<Driver>();
        redBull.add(new Driver("Макс Ферстаппен", "Red Bull Racing", 3, 4));
        redBull.add(new Driver("Исак Хаджар", "Red Bull Racing", 6, 0));
        db.put("Red Bull Racing", redBull);

        List<Driver> ferrari = new ArrayList<Driver>();
        ferrari.add(new Driver("Льюис Хэмилтон", "Ferrari", 44, 7));
        ferrari.add(new Driver("Шарль Леклер", "Ferrari", 16, 0));
        db.put("Ferrari", ferrari);

        List<Driver> mclaren = new ArrayList<Driver>();
        mclaren.add(new Driver("Ландо Норрис", "McLaren", 1, 1));
        mclaren.add(new Driver("Оскар Пиастри", "McLaren", 81, 0));
        db.put("McLaren", mclaren);
    }
}