import java.io.*;
import java.util.*;

class Toy {
    private int id;
    private String name;
    private int quantity;
    private double weight;

    public Toy(int id, String name, int quantity, double weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

class PrizeToyList {
    private ArrayList<Toy> prizeToys;
    private File prizeListFile;

    public PrizeToyList(String fileName) {
        prizeToys = new ArrayList<>();
        prizeListFile = new File(fileName);
        loadPrizeToysFromFile();
    }

    public void addPrizeToy(Toy toy) {
        prizeToys.add(toy);
    }

    public Toy getPrizeToy() {
        if (prizeToys.isEmpty()) {
            return null;
        }

        Toy winningToy = prizeToys.remove(0);

        try (FileWriter fileWriter = new FileWriter(prizeListFile, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Выиграна игрушка с ID " + winningToy.getId() + ": " + winningToy.getName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return winningToy;
    }

    public void savePrizeToysToFile() {
        try (FileWriter fileWriter = new FileWriter(prizeListFile, false);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (Toy toy : prizeToys) {
                bufferedWriter.write("ID: " + toy.getId() + ", Название: " + toy.getName() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPrizeToysFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(prizeListFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ToyStore {
    private List<Toy> toys;
    private Random random;

    public ToyStore() {
        toys = new ArrayList<>();
        random = new Random();
    }

    public void addToy(int id, String name, int quantity, double weight) {
        Toy toy = new Toy(id, name, quantity, weight);
        toys.add(toy);
    }

    public void updateToyWeight(int id, double newWeight) {
        for (Toy toy : toys) {
            if (toy.getId() == id) {
                toy.setWeight(newWeight);
            }
        }
    }

    public Toy drawToy() {
        List<Toy> weightedToys = new ArrayList<>();
        for (Toy toy : toys) {
            for (int i = 0; i < toy.getWeight(); i++) {
                weightedToys.add(toy);
            }
        }

        if (weightedToys.isEmpty()) {
            return null;
        }

        int randomIndex = random.nextInt(weightedToys.size());
        Toy winningToy = weightedToys.get(randomIndex);

        winningToy.setQuantity(winningToy.getQuantity() - 1);
        if (winningToy.getQuantity() == 0) {
            toys.remove(winningToy);
        } else {
            winningToy.setWeight((double) winningToy.getQuantity() / calculateTotalQuantity());
        }

        return winningToy;
    }

    private int calculateTotalQuantity() {
        int totalQuantity = 0;
        for (Toy toy : toys) {
            totalQuantity += toy.getQuantity();
        }
        return totalQuantity;
    }
}

public class ToyStoreRaffle {
    public static void main(String[] args) {
        ToyStore toyStore = new ToyStore();
        PrizeToyList prizeToyList = new PrizeToyList("prize_list.txt");

        toyStore.addToy(1, "Кукла", 5, 20);
        toyStore.addToy(2, "Мяч", 10, 30);
        toyStore.addToy(3, "Пазл", 8, 10);
        toyStore.addToy(4, "Машинка", 6, 15);

        toyStore.addToy(5, "Конструктор", 7, 10);
        toyStore.updateToyWeight(5, 5);

        Toy winningToy = toyStore.drawToy();

        if (winningToy != null) {
            System.out.println("Поздравляем! Ребенок выиграл игрушку с ID " + winningToy.getId() + ": " + winningToy.getName());
            prizeToyList.addPrizeToy(winningToy);
        } else {
            System.out.println("Все игрушки уже разыграны.");
        }

        prizeToyList.savePrizeToysToFile();
    }
}
