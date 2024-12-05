package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.entities.Device;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toPersonDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getDescription(), device.getMHEC(), device.getAddress() , device.getUserEmail());
    }

    public static DeviceDetailsDTO toPersonDetailsDTO(Device device) {
        return new DeviceDetailsDTO(device.getId(), device.getDescription(), device.getAddress(), device.getMHEC(), device.getUserEmail());
    }

    public static DeviceDTO toDeviceDTO(Device device) {
        DeviceDTO dto = new DeviceDTO();
        dto.setId(device.getId());
        dto.setDescription(device.getDescription());
        dto.setAddress(device.getAddress());
        return dto;
    }

    public static DeviceDetailsDTO toDeviceDetailsDTO(Device device) {
        DeviceDetailsDTO detailsDTO = new DeviceDetailsDTO();
        detailsDTO.setId(device.getId());
        detailsDTO.setDescription(device.getDescription());
        detailsDTO.setAddress(device.getAddress());
        detailsDTO.setMhec(device.getMHEC());
        detailsDTO.setUserEmail(device.getUserEmail());
        return detailsDTO;
    }

    public static Device toEntity(DeviceDetailsDTO deviceDetailsDTO) {
        return new Device(deviceDetailsDTO.getDescription(),
                deviceDetailsDTO.getAddress(),
                deviceDetailsDTO.getMhec(),
                deviceDetailsDTO.getUserEmail());
    }
}
