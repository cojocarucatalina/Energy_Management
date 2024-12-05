package ro.tuc.ds2020.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ro.tuc.ds2020.Ds2020TestConfig;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.util.List;
import java.util.UUID;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/test-sql/create.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/test-sql/delete.sql")
public class DeviceServiceIntegrationTests extends Ds2020TestConfig {

    @Autowired
    DeviceService deviceService;

    @Test
    public void testGetCorrect() {
        List<DeviceDTO> deviceDTOList = deviceService.findDevices();
        assertEquals("Test Insert Person", 1, deviceDTOList.size());
    }

//    @Test
//    public void testInsertCorrectWithGetById() {
//        DeviceDetailsDTO p = new DeviceDetailsDTO("John", "Somewhere Else street", 22);
//        UUID insertedID = deviceService.insert(p);
//
//        DeviceDetailsDTO insertedPerson = new DeviceDetailsDTO(insertedID, p.getDescription(),p.getAddress(), p.getMhec(), p.getUser_id());
//        DeviceDetailsDTO fetchedPerson = deviceService.findDeviceById(insertedID);
//
//        assertEquals("Test Inserted Person", insertedPerson, fetchedPerson);
//    }
//
//    @Test
//    public void testInsertCorrectWithGetAll() {
//        DeviceDetailsDTO p = new DeviceDetailsDTO("John", "Somewhere Else street", 22);
//        deviceService.insert(p);
//
//        List<DeviceDTO> deviceDTOList = deviceService.findDevices();
//        assertEquals("Test Inserted Persons", 2, deviceDTOList.size());
//    }
}
