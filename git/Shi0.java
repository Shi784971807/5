
/**
 *  このクラスはMarketStoreServiceのMockでテストのクラスです。
 *  複数行に渡って記述することが可能です。
 *  @version 1.0
 *  @param string huang
 */
package com.sample.marketstore;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cms.entity.marketstore.MarketStoreBean;
import com.cms.form.marketstore.MarketStoreForm;
import com.cms.form.marketstore.MarketStoreListForm;
import com.cms.mapper.marketstore.MarketStoreMapper;
import com.cms.service.marketstore.MarketStoreServiceImpl;
import com.exception.BusinessException;

//告诉编译器忽略指定类型的警告信息。rawtypes 表示忽略关于未经参数化的原始类型的警告。
@SuppressWarnings("rawtypes")
//指定运行测试的测试运行器。
//SpringRunner.class 是 Spring Boot 提供的测试运行器，用于在测试期间启动 Spring 容器。
@RunWith(SpringRunner.class)  
//指定当前类是一个 Spring Boot 测试类。
//它告诉 Spring Boot 创建一个测试环境，并加载应用程序的上下文。
@SpringBootTest

public class MarketStoreService_Test_Mock {
	
	//不需要数据库
	@InjectMocks  //调用实例化类
	private MarketStoreServiceImpl service;    
    @Mock
    private MarketStoreMapper mapper;
    
    /**
     * 正常終了
     * 検索件数：複数件 検索条件なし
     */
	@Test
	public void test_select_01_OK(){
		
// 1.検索結果Mock。假想取出数据
		
		List<MarketStoreBean> mockResults = new ArrayList<MarketStoreBean>();
		mockResults.add(createBeanS("1"));
		mockResults.add(createBeanS("2"));
		mockResults.add(createBeanS("3"));

		// MarketStoreBean paramBean = new MarketStoreBean(); //引数，mapper方法传递的容器
		//为什么用any()？因为定义的paramBean和实际调用可能有偏差，导致测试代码无法运行
		// mock固定写法，调用this.mapper.select方法，返回假想值
		
		Mockito.when(this.mapper.select(any())).thenReturn(mockResults);

//2.假想画面传进form的值，做测试数据准备,没有参数，所以只新建form,没有设值
		
		MarketStoreListForm form=new  MarketStoreListForm();
		// try 块之前将 beanList 设置为 null 是一种常见的做法，以确保变量得到正确初始化，
    	//无论是否发生异常，在后续都可以使用，因为具有一个有效的值（null）。		
		MarketStoreListForm beanList = null;
		
//3.调用被测试方法，并验证结果
		try {			
			beanList = service.select(form);
			// 验证结果
			assertEquals(3, beanList.getResults().size()); // 验证结果列表长度
			for (int index = 0; index < beanList.getResults().size(); index++) {
				assertEquals(mockResults.get(index).getStoreName(), beanList.getResults().get(index).getStoreName()); // 验证属性值
				assertEquals(mockResults.get(index).getAddress(), beanList.getResults().get(index).getAddress()); 
				assertEquals(mockResults.get(index).getPhone(), beanList.getResults().get(index).getPhone()); 
				assertEquals(mockResults.get(index).getStartDay(), beanList.getResults().get(index).getStartDay()); 
				assertEquals(mockResults.get(index).getFinishDay(), beanList.getResults().get(index).getFinishDay()); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    
    /**
     * 正常終了
     * 検索件数：1件 検索条件　販売店名　+　販売店アドレス
     */
	@Test
	public void test_select_02_OK(){
		
// 1.検索結果Mock。假想取出数据
		
		List<MarketStoreBean> mockResults = new ArrayList<MarketStoreBean>();
		mockResults.add(createBean());
	
		Mockito.when(this.mapper.select(any())).thenReturn(mockResults);

//2.假想画面传进form的值，做测试数据准备
		
		MarketStoreListForm form = new MarketStoreListForm();
		form.setStoreName("Store Name");// 设置参数值
		form.setAddress("Address"); // 设置参数值
	
		MarketStoreListForm beanList = null;
		
//3.调用被测试方法，并验证结果
		try {			
			beanList = service.select(form);
			// 验证结果
			assertEquals(1, beanList.getResults().size()); // 验证结果列表长度
			
			assertEquals("Store Name", beanList.getResults().get(0).getStoreName());
			assertEquals("Address", beanList.getResults().get(0).getAddress());
			assertEquals("Phone", beanList.getResults().get(0).getPhone());
			assertEquals("Start Day", beanList.getResults().get(0).getStartDay());
			assertEquals("Finish Day", beanList.getResults().get(0).getFinishDay());	

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 正常終了 
     * 検索件数：1件(検索結果と条件が一致する) 販売店名だけ
     */
    @Test
    public void test_select_03_OK() {
    	
 //1. 假想mock  
    	
    	//検索結果Mock
    	List<MarketStoreBean> mockResults = new ArrayList<MarketStoreBean>();

    	mockResults.add(createBean());
    	
    	Mockito.when(this.mapper.select(any())).thenReturn(mockResults);
    	
 //2.假想画面form 
    	
    	MarketStoreListForm form = new MarketStoreListForm();
		form.setStoreName("Store Name");
    	
 //3.调方法验证
		
    	MarketStoreListForm beanList = null;
		try {			
			beanList = service.select(form);
			assertEquals("Store Name", beanList.getResults().get(0).getStoreName());
			assertEquals("Address", beanList.getResults().get(0).getAddress());
			assertEquals("Phone", beanList.getResults().get(0).getPhone());
			assertEquals("Start Day", beanList.getResults().get(0).getStartDay());
			assertEquals("Finish Day", beanList.getResults().get(0).getFinishDay());			
		} catch (Exception e) {
			e.printStackTrace();
		}  	
    }
      
    /**
     * 正常終了
     * 検索件数：1件 アドレスだけ
     */
    @Test
    public void test_select_04_OK() {
//1.
    	//検索結果Mock
    	List<MarketStoreBean> mockResults = new ArrayList<MarketStoreBean>();

    	mockResults.add(createBean());
    	   	  	
    	Mockito.when(this.mapper.select(any())).thenReturn(mockResults);   
 //2.   
    	MarketStoreListForm form = new MarketStoreListForm();
		form.setAddress("Address");
 //3.   	
    	MarketStoreListForm beanList = null;
		try {					
			beanList = service.select(form);
			assertEquals("Store Name", beanList.getResults().get(0).getStoreName());
			assertEquals("Address", beanList.getResults().get(0).getAddress());
			assertEquals("Phone", beanList.getResults().get(0).getPhone());
			assertEquals("Start Day", beanList.getResults().get(0).getStartDay());
			assertEquals("Finish Day", beanList.getResults().get(0).getFinishDay());				
		} catch (Exception e) {
			e.printStackTrace();
		}	
    }
    
    /**
     * 異常終了　販売店名DBに存在しません
     * 予想値：検索結果がありません。
     */
    @Test
    public void test_select_01_NG(){
    	//System.out.println("■test_marketStoreServiceImpl__01_NG 開始");
//1.
    	List<MarketStoreBean> mockResults = new ArrayList<MarketStoreBean>();
    	mockResults.add(createBean());
    	//createBean()如果放any(),则会返回数据
        when(this.mapper.select(createBean())).thenReturn(mockResults);
//2.
        MarketStoreListForm form = new MarketStoreListForm();
		form.setStoreName("数据库里有我么？");
		
//3.
		MarketStoreListForm beanList = null;
        try {
          beanList =service.select(form);
		} catch (BusinessException e) {
	    	assertEquals("検索結果はありません。", e.getMessage());
		}catch (Exception e) {
		
			e.printStackTrace();
		}	
    	//System.out.println("■test_marketStoreServiceImpl__01_NG 終了");
    }
    
    /**
     * 異常終了　販売店アドレスはDBに存在しません
     * 予想値：検索結果がありません。
     */
    @Test
    public void test_select_02_NG(){
    	//System.out.println("■test_marketStoreServiceImpl__01_NG 開始");
//1.
    	List<MarketStoreBean> mockResults = new ArrayList<MarketStoreBean>();
    	mockResults.add(createBean());
    	//createBean()如果放any(),则会返回数据
        when(this.mapper.select(createBean())).thenReturn(mockResults);
//2.
        MarketStoreListForm form = new MarketStoreListForm();
		form.setAddress("数据库里有我么？");
		
//3.
		MarketStoreListForm beanList = null;
        try {
          beanList =service.select(form);
		} catch (BusinessException e) {
	    	assertEquals("検索結果はありません。", e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}	
    	//System.out.println("■test_marketStoreServiceImpl__01_NG 終了");
    }
 
    /**
     * 正常終了
     * 検索件数：1件
     */
	@Test
	public void test_Insert() {
		
//1. 设置selectMaxId方法的返回值
		
        Mockito.when(this.mapper.selectMaxId()).thenReturn("100");
        
//2.3. form在方法中创建，直接调方法使用并验证
        
        try {
        	 service.insert(creatForm());
        	     			 
        	/*
			 * Mockito.verify() 是一个用于验证方法调用的 Mockito 框架方法。 
			 * 它用于验证模拟对象（mockobject）的方法是否按预期进行了调用。 
			 * 如果 找到了 匹配的方法调用，Mockito 将通过验证，不会引发异常。 
			 * 如果 找不到 匹配的方法调用，或者方法调用的次数不符合预期， 
			 * Mockito 将引发 VerificationError 异常，表示验证失败。
			 */
        	 
             Mockito.verify(mapper,Mockito.times(1)).insert(any(MarketStoreBean.class));
             ArgumentCaptor<MarketStoreBean> capture=ArgumentCaptor.forClass(MarketStoreBean.class);
            /*
			 * 验证insert方法是否被调用，并传入了正确的参数 
			 * （Argument Matcher） 是Mockito 框架中的参数匹配器
			 * ArgumentMatchers.any(MarketStoreBean.class) 表示该方法接受
			 * 任何类型为 MarketStoreBean的参数，无论具体参数对象是什么。 
			 * 
			 * 如果方法调用的参数匹配了，则验证通过，不会引发异常。
			 * 如果调用的参数不匹配或不符合预期，将引发 VerificationError 异常，表示验证失败。
			 */
             
             Mockito.verify(mapper).insert(capture.capture());
             MarketStoreBean resultBean=capture.getValue();
             
             assertEquals("101", resultBean.getStoreId());
             assertEquals("Updated Store Name", resultBean.getStoreName());
             assertEquals("Updated Address", resultBean.getAddress());
             assertEquals("Updated Phone", resultBean.getPhone());
             assertEquals("Updated Start Day", resultBean.getStartDay());
             assertEquals("Updated Finish Day", resultBean.getFinishDay());
             
		} catch (Exception e) {
			
			//e.printStackTrace();
		}    
    }
	
	 /**
     * 正常終了
     * 検索件数：1件 验证编辑功能
     */
	@Test
	public void test_editInit() {
//1.创建一个模拟的查询结果列表
		
        List<MarketStoreBean> searchResults = new ArrayList<>();
        searchResults.add(createBean());
        
        // 设置mapper的select方法返回模拟的查询结果
        Mockito.when(this.mapper.select(any())).thenReturn(searchResults);
        
 //2.创建一个要传入的MarketStoreForm对象
        
        MarketStoreForm form = new MarketStoreForm();
        form.setStoreId("123");
        
 //3.调用方法验证      
        
        try {
            MarketStoreForm resultForm = service.editInit(form);
           
            // 验证resultForm对象的属性值是否与模拟的查询结果一致
            assertEquals("123", resultForm.getStoreId()); // 验证属性值
            assertEquals("Store Name", resultForm.getStoreName());
            assertEquals("Address", resultForm.getAddress());
            assertEquals("Phone", resultForm.getPhone());
            assertEquals("Start Day", resultForm.getStartDay());
            assertEquals("Finish Day", resultForm.getFinishDay());
		} catch (Exception e) {
			
			//e.printStackTrace();
		}    
    }
    
	 /**
     * 正常終了
     * 検索件数：1件
     */
	@Test
	public void test_update() {
//1. 创建一个模拟的查询结果列表，只包含一个元素
        List<MarketStoreBean> searchResults = new ArrayList<>();
        MarketStoreBean result = new MarketStoreBean();
        result.setStoreId("123");
        searchResults.add(result);

        // 设置mapper的select方法返回模拟的查询结果
        Mockito.when(this.mapper.select(any(MarketStoreBean.class))).thenReturn(searchResults);

//2.3 调用MarketStoreForm对象，测试验证
		
       try {	 
    	   // 调用被测试的update方法
    	   service.update(creatForm());

        // 验证select方法是否被调用
        Mockito.verify(this.mapper).select(any(MarketStoreBean.class));

        // 验证update方法是否被调用
        Mockito.verify(this.mapper).update(any(MarketStoreBean.class));

        // 验证updateBean对象的属性是否已更新为form对象的属性值
        assertEquals("123", result.getStoreId());
        assertEquals("Updated Store Name", result.getStoreName());
        assertEquals("Updated Address", result.getAddress());
        assertEquals("Updated Phone", result.getPhone());
        assertEquals("Updated Start Day", result.getStartDay());
        assertEquals("Updated Finish Day", result.getFinishDay());
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
		}    
    }
	
	 /**
     * 正常終了
     * 検索件数：1件
     * 検索結果はありません。
     */
	
	@Test
	public void test_delete() {
//1. 创建待删除的MarketStoreBean对象
		MarketStoreBean deleteBean = new MarketStoreBean();
		deleteBean.setStoreId("123"); // 设置正确的待删除的storeId
		// 创建一个Mock对象
     //   MarketStoreBean beanMock = mock(MarketStoreBean.class);
//2.3
		try {   	        
	        // 调用被测方法
	        service.delete("123");
	     // 创建ArgumentCaptor对象
	        ArgumentCaptor<MarketStoreBean> captor = ArgumentCaptor.forClass(MarketStoreBean.class);

	        // 捕获delete方法的参数
	        Mockito.verify(mapper).delete(captor.capture());

	        // 获取捕获的参数值
	        MarketStoreBean capturedValue = captor.getValue();

	        // 验证捕获的参数值是否符合预期
	        assertEquals("123", capturedValue.getStoreId());
			
			//Mockito.verify(this.mapper).delete(deleteBean);
			//mockだから、DBからデータを取れない
//			MarketStoreListForm form = new MarketStoreListForm();
//			form.setStoreName("Store Name");
//			form.setAddress("Address");
//			MarketStoreListForm resultForm= service.select(form);
			// 验证delete方法是否被调用，并传入了正确的参数
			
			//assertEquals(0, resultForm.getResults().size()); 
			
		} catch (Exception e) {
			
			// 处理异常情况
			//e.printStackTrace();
			//?????????????????????????????????????????????
			Assert.fail("Exception should not be thrown");
			//assertEquals("検索結果はありません。", e.getMessage());
		}
	}

	 /**
     * 正常終了
     * 検索件数：1件
     */
	@Test
	public void test_deleteAll() {
		
		// 准备测试数据
	    String storeIds = "1,2,3";
	    String[] delIds = storeIds.split(",");

	    try {
	        // 调用被测试的方法
	        service.deleteAll(storeIds);
	     // 创建ArgumentCaptor对象
	        ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);
	     

	        // 捕获delete方法的参数
	        Mockito.verify(mapper).deleteAll(captor.capture());
	        String[] deleteId=captor.getValue();
	        assertArrayEquals(delIds, deleteId);
	        
	    } catch (Exception e) {
	        // 处理异常情况
	        e.printStackTrace();
	        Assert.fail("Exception should not be thrown");
	    }	
	}
	
	 /**
     * 正常終了
     * 検索件数：1件
     */
	@Test
	public void test_readInit() {
//1.		
		List<MarketStoreBean> searchResults = new ArrayList<>();
	    searchResults.add(createBean());
	
	    // 定义Mock行为
	    Mockito.when(mapper.select(Mockito.any())).thenReturn(searchResults);

//2.准备测试数据
	    MarketStoreForm form = new MarketStoreForm();
	    form.setStoreId("123");


//3. 调用被测试的方法
	    try {
	    	MarketStoreForm resultForm = service.readInit(form);
		    // 验证mapper.select方法是否被调用，并传入了正确的参数
		    Mockito.verify(mapper).select(any());

		    // 验证预期结果
		    Assert.assertEquals("123", resultForm.getStoreId());
		    Assert.assertEquals("Store Name", resultForm.getStoreName());
		    Assert.assertEquals("Address", resultForm.getAddress());
		    Assert.assertEquals("Phone", resultForm.getPhone());
		    Assert.assertEquals("Start Day", resultForm.getStartDay());
		    Assert.assertEquals("Finish Day", resultForm.getFinishDay());
		} catch (Exception e) {
			e.printStackTrace();
		}    
	}
	
	/**
	 * 创建 多个bean
	 * @return bean
	 */
	 private MarketStoreBean createBeanS(String key) {
	    	MarketStoreBean mockBeanS = new MarketStoreBean();
	    	mockBeanS.setStoreName("店名"+key);
	    	mockBeanS.setAddress("aaaaaaa"+key);
	    	mockBeanS.setPhone("電話"+key);
	    	mockBeanS.setStartDay("StartDay"+key);
	    	mockBeanS.setFinishDay("FinishDay"+key);
	    	return mockBeanS;
	    }

	/**
	 * 创建 form
	 * @return form
	 */
    private MarketStoreForm creatForm() {
    	MarketStoreForm form = new MarketStoreForm();
    	form.setStoreId("123");
        form.setStoreName("Updated Store Name");
        form.setAddress("Updated Address");
        form.setPhone("Updated Phone");
        form.setStartDay("Updated Start Day");
        form.setFinishDay("Updated Finish Day");
        return form;		
	}
    
    /**
	 * 创建 bean
	 * @return bean
	 */
    private MarketStoreBean createBean() {
    	MarketStoreBean bean = new MarketStoreBean();
    	bean.setStoreId("123");
    	bean.setStoreName("Store Name");
    	bean.setAddress("Address");
    	bean.setPhone("Phone");
    	bean.setStartDay("Start Day");
    	bean.setFinishDay("Finish Day");    	
    	return bean;
    }
    
    
    
    
}