package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.DeviceReferenceDTO;
import ro.tuc.ds2020.dtos.DeviceReferenceDetailsDTO;
import ro.tuc.ds2020.entities.DeviceReference;

public class DeviceReferenceBuilder {

    private DeviceReferenceBuilder() {
    }

    public static DeviceReferenceDTO toDeviceReferenceDTO(DeviceReference deviceReference) {
        return new DeviceReferenceDTO(deviceReference.getId(), deviceReference.getDevice_id(), deviceReference.getMhec(), deviceReference.getUser_email());
    }

    public static DeviceReferenceDetailsDTO toDeviceReferenceDetailsDTO(DeviceReference deviceReference) {
        return new DeviceReferenceDetailsDTO(deviceReference.getId(),  deviceReference.getDevice_id(), deviceReference.getUser_email(), deviceReference.getMhec());
    }

    public static DeviceReference toEntity(DeviceReferenceDetailsDTO deviceReferenceDetailsDTO) {
        return new DeviceReference(
                deviceReferenceDetailsDTO.getDevice_id(),
                deviceReferenceDetailsDTO.getUser_email(),
                deviceReferenceDetailsDTO.getMhec()
        );
    }

}

