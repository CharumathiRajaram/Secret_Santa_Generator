package com.acme.secret.santa;

import com.acme.secret.santa.controller.SecretSantaGeneratorAPIController;
import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import com.acme.secret.santa.service.CSVService;
import com.acme.secret.santa.service.SecretSantaAssignmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class SecretSantaApiServiceApplicationTests {
    @Mock
    private CSVService csvService;
    @Mock
    private SecretSantaAssignmentService secretSantaAssignmentService;
    @InjectMocks
    public SecretSantaGeneratorAPIController secretSantaGeneratorAPIController;

    @Test
    void testParseEmployeeCSV_ValidFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("employees", "employees.csv",
                "text/csv", "Employee_Name,Employee_EmailID\nHamish Murray,hamish.murray@acme.com\n".getBytes());
        when(csvService.parseEmployeeCsv(file)).thenReturn(List.of(new Employee("Hamish Murray", "hamish.murray@acme.com")));
        List<Employee> employees = csvService.parseEmployeeCsv(file);
        assertEquals(1, employees.size());
        assertEquals("Hamish Murray", employees.get(0).employeeName());
    }

    @Test
    void testAssignSecretSantaAPI_ValidCSVFile() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(secretSantaGeneratorAPIController).build();

        MockMultipartFile employeeFile = new MockMultipartFile("employees", "employees.csv",
                "text/csv", "Employee_Name,Employee_EmailID\nHamish Murray,hamish.murray@acme.com\nMatthew King,matthew.king@acme.com\n".getBytes());

        when(csvService.parseEmployeeCsv(any())).thenReturn(
                List.of(new Employee("Hamish Murray", "hamish.murray@acme.com"),
                        new Employee("Matthew King", "matthew.king@acme.com")));

        when(secretSantaAssignmentService.assignSecretSanta(any(), any())).thenReturn(
                List.of(new SecretSantaAssignment("hamish.murray@acme.com", "Hamish Murray", "Matthew King", "matthew.king@acme.com")));

        mockMvc.perform(multipart("/secret_santa/assign").file(employeeFile))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=secret_santa.csv"))
                .andExpect(content().contentType(MediaType.TEXT_PLAIN));
    }

    @Test
    void testAssignSecretSanta_NoSelfAssignment() {
        List<Employee> employees = List.of(
                new Employee("Hamish Murray", "hamish.murray@acme.com"),
                new Employee("Matthew King", "matthew.king@acme.com"));

        List<SecretSantaAssignment> assignments = secretSantaAssignmentService.assignSecretSanta(employees, List.of());

        for (SecretSantaAssignment assignment : assignments) {
            assertNotEquals(assignment.employeeEmail(), assignment.secretChildEmail());
        }
    }

    @Test
    void testAssignSecretSanta_NoPrevAssignment() {
        List<Employee> employees = List.of(
                new Employee("Hamish Murray", "hamish.murray@acme.com"),
                new Employee("Matthew King", "matthew.king@acme.com"));

        when(secretSantaAssignmentService.assignSecretSanta(employees, List.of()))
                .thenReturn(List.of(
                        new SecretSantaAssignment("hamish.murray@acme.com", "Hamish Murray", "Matthew King", "matthew.king@acme.com"),
                        new SecretSantaAssignment("matthew.king@acme.com", "Matthew King", "Hamish Murray", "hamish.murray@acme.com")
                ));

        List<SecretSantaAssignment> assignments = secretSantaAssignmentService.assignSecretSanta(employees, List.of());

        assertEquals(2, assignments.size());
        assertNotEquals(assignments.get(0).employeeEmail(), assignments.get(0).secretChildEmail());
        assertNotEquals(assignments.get(1).employeeEmail(), assignments.get(1).secretChildEmail());
    }
}
