/*
package ro.tuc.ds2020.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ro.tuc.ds2020.Ds2020TestConfig;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.MeasurementDetailsDTO;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.util.List;
import java.util.UUID;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/test-sql/create.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/test-sql/delete.sql")
public class MeasurementServiceIntegrationTests extends Ds2020TestConfig {

    @Autowired
    MeasurementService MeasurementService;

    @Test
    public void testGetCorrect() {
        List<MeasurementDTO> MeasurementDTOList = MeasurementService.findMeasurements();
        assertEquals("Test Insert Person", 1, MeasurementDTOList.size());
    }

    @Test
    public void testInsertCorrectWithGetById() {
        UserDetailsDTO p = new UserDetailsDTO("John", "Somewhere Else street","email@email", false);
        UUID insertedID = userService.insert(p);

        UserDetailsDTO insertedPerson = new UserDetailsDTO(insertedID, p.getName(),p.getPassword(),p.getEmail() ,p.getAdmin());
        UserDetailsDTO fetchedPerson = userService.findUserById(insertedID);

        assertEquals("Test Inserted Person", insertedPerson, fetchedPerson);
    }

    @Test
    public void testInsertCorrectWithGetAll() {
        UserDetailsDTO p = new UserDetailsDTO("John", "Somewhere Else street","email@email", false);
        userService.insert(p);

        List<UserDTO> userDTOList = userService.findUsers();
        assertEquals("Test Inserted Persons", 2, userDTOList.size());
    }
}
*/
