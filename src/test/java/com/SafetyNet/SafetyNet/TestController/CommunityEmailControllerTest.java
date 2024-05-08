package com.SafetyNet.SafetyNet.TestController;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.SafetyNet.SafetyNet.controller.CommunityEmailController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.SafetyNet.service.contracts.IPersonService;

@WebMvcTest(controllers = CommunityEmailController.class)
public class CommunityEmailControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IPersonService iPersonService;

	@Test
	public void getEmailsByCity_returnOk() throws Exception {
		List<String> emails = new ArrayList<>(Arrays.asList("geralt.deriv@kaermorhen.kdw", "ciri@kaermorhen.kdw"));

		when(iPersonService.getEmailsByCity(anyString()))
			.thenReturn(emails);

		mockMvc.perform(get("/communityEmail")
				.param("city", "Kaer Morhen")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0]").value("geralt.deriv@kaermorhen.kdw"))
			.andExpect(jsonPath("$.[1]").value("ciri@kaermorhen.kdw"));
	}

	@Test
	public void getEmailsByCity_returnNotFound() throws Exception {
		when(iPersonService.getEmailsByCity(anyString()))
			.thenReturn(new ArrayList<>());

		mockMvc.perform(get("/communityEmail")
				.param("city", "Mahakam")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
}