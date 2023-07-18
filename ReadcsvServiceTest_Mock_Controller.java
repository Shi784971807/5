package com.sample.practice1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cms.controller.practice1.ReadDataBaseController;
import com.cms.form.practice1.ReadDataBaseForm;

@SuppressWarnings("rawtypes")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReadcsvServiceTest_Mock_Controller {

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {

		// Spring MVCのモックを設定する
		this.mockMvc = MockMvcBuilders.standaloneSetup(new ReadDataBaseController()).build();
	}

	/**
	 * GETテスト
	 */
	@Test
	public void test_request_get() {


		try {
//			ResultActions ret1 = mockMvc.perform(MockMvcRequestBuilders.get("/practice1/readdatabase"))
//					// レスポンスのステータスコードが200であることを検証する
//					.andExpect(status().isOk())
//					// レスポンスボディが「Hello World」であることを検証する
//					.andExpect(content().string("Hello World"));

			ResultActions ret2 = mockMvc
					.perform(MockMvcRequestBuilders.post("/practice1/readdatabase").param("name", "penguin")
							.param("birthday", "1998-01-01"))
					.andExpect(MockMvcResultMatchers.status().isFound())
					.andExpect(MockMvcResultMatchers.view().name("redirect:/profile/list"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * GETテスト
	 */
	@Test
	public void test_request_post() {
		ReadDataBaseForm form = new ReadDataBaseForm();
		form.setEmployeeId("1");
		form.setEmployeeName("test");

		try {

//			MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/practice1/readdatabase")
//					.content(form)
//					.accept("application/json;charset=UTF-8")
//					.contentType("MediaType.APPLICATION_JSON");
//			MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andExcept("")
//					.andReturn();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}