import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
class House {
    private String id, owner, location;
    private double price;
    private int bedrooms;
    public House(String id, String location, double price, int bedrooms, String owner) {
        this.id = id;
        this.location = location;
        this.price = price;
        this.bedrooms = bedrooms;
        this.owner = owner;
    }
    public String getId() {
        return id;
    }
    public String getLocation() {
        return location;
    }
    public double getPrice() {
        return price;
    }
    public int getBedrooms() {
        return bedrooms;
    }
    public String getOwner() {
        return owner;
    }
    @Override
    public String toString() {
        return "[" + id + ", " + location + ", " + price + ", " + bedrooms + ", " + owner + "]";
    }
}

class Tenant {
    private String id;
    private String name;
    private String contact;
    private String preferredLocation;

    public Tenant(String id, String name, String contact, String preferredLocation) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.preferredLocation = preferredLocation;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }
}

class RentalAgreement {
    private Tenant tenant;
    private House house;
    private double deposit;
    private LocalDate startDate;
    private LocalDate endDate;

    public RentalAgreement(Tenant tenant, House house, double deposit, LocalDate startDate, LocalDate endDate) {
        this.tenant = tenant;
        this.house = house;
        this.deposit = deposit;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    @Override
    public String toString() {
        return "RentalAgreement [tenant=" + tenant.getName() + ", house=" + house.getId() + ", deposit=" + deposit + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }
}

public class HouseRentalSystem {
    private List<House> houseList = new ArrayList<>();
    private List<Tenant> tenantList = new ArrayList<>();
    private List<RentalAgreement> rentalAgreements = new ArrayList<>();
    private String housesFilePath = "houses.txt";
    private String tenantsFilePath = "tenants.txt";

    public HouseRentalSystem() {
        loadHouses();
        loadTenants();
    }

    public void addHouse(House house) throws IOException {
        houseList.add(house);
        saveHouses();
    }

    public List<House> searchHouses(String location, double maxPrice) {
        return houseList.stream()
                .filter(h -> h.getLocation().equals(location) && h.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }



    public void registerTenant(Tenant tenant) throws IOException {
        tenantList.add(tenant);
        saveTenants();
    }

    private void saveHouses() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(housesFilePath, false))) { 
            for (House house : houseList) {
                writer.write(house.getId() + "," + house.getLocation() + "," + house.getPrice() + "," + house.getBedrooms() + "," + house.getOwner());
                writer.newLine();
            }
        }
    }

    private void loadHouses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(housesFilePath))) {
            String line;
            houseList.clear(); 
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    houseList.add(new House(parts[0], parts[1], Double.parseDouble(parts[2]), Integer.parseInt(parts[3]), parts[4]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading houses: " + e.getMessage());
        }
    }

    private void saveTenants() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tenantsFilePath))) {
            for (Tenant tenant : tenantList) {
                writer.write(tenant.getId() + "," + tenant.getName() + "," + tenant.getContact() + "," + tenant.getPreferredLocation());
                writer.newLine();
            }
        }
    }

    private void loadTenants() {
        try (BufferedReader reader = new BufferedReader(new FileReader(tenantsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    tenantList.add(new Tenant(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tenants: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        HouseRentalSystem rentalSystem = new HouseRentalSystem();

        House house = new House("H3", "Chennai", 40000, 2, "KUMUTHA");
        House house1 = new House("H4", "Chennai", 30000, 2, "KUMAR");
        rentalSystem.addHouse(house);
        rentalSystem.addHouse(house1);


        Tenant tenant1 = new Tenant("T1", "REENA", "123-456-7890", "Chennai");
        Tenant tenant2 = new Tenant("T2", "DIYA", "987-654-3210", "Mumbai");
        rentalSystem.registerTenant(tenant1);
        rentalSystem.registerTenant(tenant2);

        String searchLocation = "Chennai";
        double maxPrice = 50000;

        List<House> foundHouses = rentalSystem.searchHouses(searchLocation, maxPrice);
        System.out.println("Found " + foundHouses.size() + " house(s):");
        for (House foundHouse : foundHouses) {
            System.out.println(foundHouse);
        }

    }
}
