package RideService;

import DataModel.CustomerDTO;
import DataModel.Gender;
import DataModel.OfferRide;
import DataModel.RideData;
import DataModel.RideSharingDTO;
import DataModel.VehicleDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static java.util.Objects.nonNull;

public class RideSharingService {

  RideSharingDTO rideSharingDTO = new RideSharingDTO();
  Map<String, Integer> offeredRides = new HashMap<>();
  Map<String, Integer> findRides = new HashMap<>();

  public void startGame() {
    System.out.println("------------------------------------ ");
    System.out.println("WELCOME TO OUR RIDE SHARING PLATFORM ");
    System.out.println("------------------------------------ ");
    System.out.println("");
    System.out.println("------PLEASE CHOOSE ONE OPTION------");
    System.out.println("");
    System.out.println("1. ENTER PERSON NAME ");
    System.out.println("2. ADD A VEHICLE");
    System.out.println("3. OFFER A RIDE");
    System.out.println("4. FIND RIDE");
    System.out.println("5. Print All Stats");
    System.out.println("6. Print ALL CUSTOMERS");
    System.out.println("7. PRINT ALL VEHICLES");
    System.out.println("8. Exit From the Program");
    System.out.println("");

    while (true) {
      System.out.println("------------------------------------ ");
      System.out.println("PLEASE CHECK THE MENU AND ENTER YOUR OPTION (1 to 6)");
      int s = 0;
      Scanner inputScanner = new Scanner(System.in);
      try {
        s = Integer.parseInt(inputScanner.nextLine());
      } catch (Exception e) {
        System.out.println("");
      }
      switch (s) {
        case 1:
          {
            addCustomerInList(inputScanner);
            break;
          }
        case 2:
          {
            addNewVehicleInList(inputScanner);
            break;
          }
        case 3:
          {
            addOfferRides(inputScanner);
            break;
          }
        case 4:
          {
            findRides(inputScanner);
            break;
          }
        case 5:
          {
            printStats();
            break;
          }
        case 6:
          {
            printCustomersData();
            break;
          }
        case 7:
          {
            printVehicleData();
            break;
          }
        case 8:
          {
            System.exit(0);
            break;
          }
        default:
          {
            System.out.println("WRONG OPTION SELECTED. PLEASE SELECT FROM 1 TO 6 !!!");
            break;
          }
      }
    }
  }

  private void printVehicleData() {
    rideSharingDTO
        .getVehicleDTOList()
        .forEach(
            vehicleDTO -> {
              System.out.println(
                  "Vehicle Name : "
                      + vehicleDTO.getVehicleName()
                      + " And Number : "
                      + vehicleDTO.getVehicleNumber());
            });
  }

  private void printCustomersData() {
    rideSharingDTO
        .getCustomerDTOList()
        .forEach(
            customerDTO -> {
              System.out.println(
                  "Name : " + customerDTO.getName() + " And Age : " + customerDTO.getAge());
            });
  }

  private void printStats() {
    List<CustomerDTO> customerDTOList = rideSharingDTO.getCustomerDTOList();

    customerDTOList.forEach(
        customerDTO -> {
          int takeRide =
              findRides.get(customerDTO.getName()) == null
                  ? 0
                  : findRides.get(customerDTO.getName());
          int offerRide =
              offeredRides.get(customerDTO.getName()) == null
                  ? 0
                  : offeredRides.get(customerDTO.getName());
          System.out.println(
              customerDTO.getName() + ": " + takeRide + " Taken, " + offerRide + " Offered");
        });
  }

  private void findRides(Scanner inputScanner) {
    System.out.println("ENTER RIDER's NAME");
    String riderName = inputScanner.nextLine();
    System.out.println("ENTER SOURCE AND DESTINATION");
    String source = inputScanner.nextLine();
    String destination = inputScanner.nextLine();
    System.out.println("ENTER NO OF SEATS NEEDED");
    int seats = Integer.parseInt(inputScanner.nextLine());
    System.out.println("ENTER CRITERIA(1. MOST VACANT / 2. PREFERRED VEHICLE)");
    int criteria = Integer.parseInt(inputScanner.nextLine());
    String preferredVehicleName = null;
    if (criteria == 2) {
      System.out.println("ENTER Vehicle Name");
      preferredVehicleName = inputScanner.nextLine();
    }
    boolean success = false;
    // LOGIC FOR FINDING THE RIDES FOR 2 CRITERIA'S
    List<OfferRide> availableRides = new ArrayList<>();
    OfferRide ride = new OfferRide();
    rideSharingDTO
        .getOfferRideList()
        .forEach(
            offerRide -> {
              if (Objects.equals(offerRide.getSource(), source)
                  && Objects.equals(offerRide.getDestination(), destination)) {
                availableRides.add(offerRide);
              }
            });
    int max = -1;
    if (criteria == 1) {
      success = maxVacantSeatsCriteria(seats, success, availableRides, ride, max);
    } else {
      success = specialVehicleDemandCriteria(seats, preferredVehicleName, success, availableRides);
    }
    if (!success) {
      System.out.println("SORRY! BUT WE ARE NOT ABLE TO FIND RIDE NOW.");
    }
    //
    RideData rideData =
        RideData.builder()
            .riderName(riderName)
            .source(source)
            .destination(destination)
            .vacantSeats(seats)
            .success(success)
            .build();
    if (success) {
      int count = findRides.getOrDefault(riderName, 0);
      findRides.put(riderName, count + 1);
    }
    List<RideData> rideDataList = new ArrayList<>();
    rideDataList.add(rideData);
    if (nonNull(rideSharingDTO.getRideDataList())) {
      rideSharingDTO.getRideDataList().addAll(rideDataList);
    } else {
      rideSharingDTO.setRideDataList(rideDataList);
    }
  }

  private boolean specialVehicleDemandCriteria(
      int seats, String preferredVehicleName, boolean success, List<OfferRide> availableRides) {
    for (OfferRide availableRide : availableRides) {
      if (Objects.equals(availableRide.getVehicleName(), preferredVehicleName)
          && availableRide.getVacantSeats() >= seats
          && !availableRide.isSelected()) {
        System.out.println(
            "RIDER IS AVAILABLE. RIDER'S NAME : "
                + availableRide.getRiderName()
                + " VEHICLE NAME AND NUMBER IS : "
                + availableRide.getVehicleName()
                + " "
                + availableRide.getVehicleNumber()
                + " and vacant seats are : "
                + availableRide.getVacantSeats());
        availableRide.setSelected(true);
        success = true;
      }
    }
    if (!success && availableRides.size() > 0) {
      System.out.println(
          "RIDES ARE AVAILABLE BUT NOT WITH YOUR PREFERRED VEHICLE. PLEASE SELECT MOST VACANT OPTION IF U WANT TO SELECT A RIDE");
    }
    return success;
  }

  private boolean maxVacantSeatsCriteria(
      int seats, boolean success, List<OfferRide> availableRides, OfferRide ride, int max) {
    for (OfferRide availableRide : availableRides) {
      if (availableRide.getVacantSeats() > max
          && availableRide.getVacantSeats() >= seats
          && !availableRide.isSelected()) {
        max = availableRide.getVacantSeats();
        ride = availableRide;
        success = true;
      }
    }
    System.out.println(
        "RIDER IS AVAILABLE. RIDER'S NAME "
            + ride.getRiderName()
            + " VEHICLE NAME AND NUMBER IS "
            + ride.getVehicleName()
            + " "
            + ride.getVehicleNumber()
            + " and vacant seats are "
            + ride.getVacantSeats());
    ride.setSelected(true);
    return success;
  }

  private void addOfferRides(Scanner inputScanner) {
    String driveName, source, destination, vehicleName, vehicleNumber;
    try {
      System.out.println("ENTER DRIVER's NAME");
      driveName = inputScanner.nextLine();
      System.out.println("ENTER SOURCE AND DESTINATION");
      source = inputScanner.nextLine();
      destination = inputScanner.nextLine();
      System.out.println("ENTER VEHICLE NAME AND NUMBER");
      vehicleName = inputScanner.nextLine();
      vehicleNumber = inputScanner.nextLine();
      System.out.println("ENTER VACANT SEAT");
    } catch (Exception e) {
      System.out.println("Input value is not correct. Please enter again.");
      return;
    }
    int vacantSeats = Integer.parseInt(inputScanner.nextLine());
    OfferRide offerRide =
        OfferRide.builder()
            .riderName(driveName)
            .vehicleName(vehicleName)
            .vehicleNumber(vehicleNumber)
            .source(source)
            .destination(destination)
            .vacantSeats(vacantSeats)
            .build();
    int count = offeredRides.getOrDefault(driveName, 0);
    offeredRides.put(driveName, count + 1);
    List<OfferRide> offerRides = new ArrayList<>();
    offerRides.add(offerRide);
    if (nonNull(rideSharingDTO.getOfferRideList())) {
      rideSharingDTO.getOfferRideList().addAll(offerRides);
    } else {
      rideSharingDTO.setOfferRideList(offerRides);
    }
  }

  private void addNewVehicleInList(Scanner inputScanner) {
    System.out.println("1. ENTER VEHICLE OWNER NAME, VEHICLE NAME, VEHICLE NUMBER");
    String vehicleOwnerName = inputScanner.nextLine();
    String vehicleName = inputScanner.nextLine();
    String vehicleNumber = inputScanner.nextLine();
    VehicleDTO vehicleDTO = new VehicleDTO();
    vehicleDTO.setOwnerName(vehicleOwnerName);
    vehicleDTO.setVehicleName(vehicleName);
    vehicleDTO.setVehicleNumber(vehicleNumber);
    List<VehicleDTO> vehicleDTOList = new ArrayList<>();
    vehicleDTOList.add(vehicleDTO);
    if (nonNull(rideSharingDTO.getVehicleDTOList())) {
      rideSharingDTO.getVehicleDTOList().addAll(vehicleDTOList);
    } else {
      rideSharingDTO.setVehicleDTOList(vehicleDTOList);
    }
    System.out.println(
        rideSharingDTO.getVehicleDTOList().size() + " VEHICLE ADDED SUCCESSFULLY !!");
  }

  private void addCustomerInList(Scanner inputScanner) {
    System.out.println(
        "1. ENTER PERSON FULL NAME, GENDER(MALE/FEMALE/OTHER) AND AGE BY CLICKING ENTER AT EACH VALUE ");
    String name = inputScanner.nextLine();
    Gender gender = Gender.valueOf(inputScanner.nextLine());
    int age = Integer.parseInt(inputScanner.nextLine());
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setName(name);
    customerDTO.setGender(gender);
    customerDTO.setAge(age);
    List<CustomerDTO> customerDTOList = new ArrayList<>();
    customerDTOList.add(customerDTO);
    if (nonNull(rideSharingDTO.getCustomerDTOList())) {
      rideSharingDTO.getCustomerDTOList().addAll(customerDTOList);
    } else {
      rideSharingDTO.setCustomerDTOList(customerDTOList);
    }
    System.out.println(
        rideSharingDTO.getCustomerDTOList().size() + " CUSTOMER ADDED SUCCESSFULLY !!");
  }
}
