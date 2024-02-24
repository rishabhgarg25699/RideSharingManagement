package DataModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RideSharingDTO {
    private List<CustomerDTO> customerDTOList;
    private List<VehicleDTO> vehicleDTOList;
    private List<OfferRide> offerRideList;
    private List<RideData> rideDataList;

    public List<CustomerDTO> getCustomerDTOList() {
        return customerDTOList;
    }

    public void setCustomerDTOList(List<CustomerDTO> customerDTOList) {
        this.customerDTOList = customerDTOList;
    }

    public List<VehicleDTO> getVehicleDTOList() {
        return vehicleDTOList;
    }

    public void setVehicleDTOList(List<VehicleDTO> vehicleDTOList) {
        this.vehicleDTOList = vehicleDTOList;
    }

    public List<OfferRide> getOfferRideList() {
        return offerRideList;
    }

    public void setOfferRideList(List<OfferRide> offerRideList) {
        this.offerRideList = offerRideList;
    }

    public List<RideData> getRideDataList() {
        return rideDataList;
    }

    public void setRideDataList(List<RideData> rideDataList) {
        this.rideDataList = rideDataList;
    }
}
