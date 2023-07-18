package com.sample.practice1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.dbunit.database.IDatabaseConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.base.UnitTestBase;
import com.cms.entity.employee.CmsEmployeeBean;
import com.cms.form.practice1.ReadDataBaseForm;
import com.cms.service.practice1.ReadDataBaseService;

@SuppressWarnings("rawtypes") //无视警告
@RunWith(SpringRunner.class) //用spring提供的环境执行，是属于SpringBootTest  junit的测试
@SpringBootTest  //这样就知道到底是什么对象

public class ReadcsvServiceTest_readDataFromMysql { //和普通的类是一样的

	private static IDatabaseConnection conn;
	//path放的CSV文件
	private final static String path = "src\\test\\data\\practice1\\";
	
	 	
//不一样的地方！！！！！！！！！！！！！！！！！！！！！！！！！
	/* テストデータを登録する */
	@BeforeAll
	public static void init() throws Exception {
		conn = UnitTestBase.connect(); //自己定义的连接数据库的方法

		// 実施前にテーブルの既存データをクリアする
		//调用init方法中有delete，clear可以是双重保险，确保数据清楚，不写也可以
		//UnitTestBase.clearData(path, conn);
		// CSVデータを取り込む
		//对数据初期化 path,是参数 conn 数据库链接
		UnitTestBase.initData(path, conn);
	}

	//不一样的地方！！！！！！！！！！！！！！！！！！！！！！！！！	
	@AfterAll
	public static void closeConnection() throws Exception {
		//删除数据，因为占内存空间，数据库打开大门后，处理清除后，要关门
		UnitTestBase.clearData(path, conn);
		// CSVデータを取り込む
		UnitTestBase.closeConnection(conn);
	}

	
//调用control里的service
	@Autowired
	ReadDataBaseService service;

	/**
	 * ケース：No1
	 * 　　　　異常系
	 * 条件：
	 *     　　社員ID：DBに存在しない
	 *     　　社員名：DBに存在しない
	 * 予想値：
	 * 　　　　エラー：検索結果がありません。
	 */
	@Test //test方法，测试对象
	public void test_readDataFromMysql_01() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeId("111");
			form.setEmployeeName("龍一");
			service.readDataFromMysql(form); //通过service调用实例类
			//异常系，不管结果有没有，一定是报错，所以不需要接收

		} catch (Exception e) {
			String message = e.getMessage();  //后台传过来的信息
			//前面是假象的信息，测试式样书，对比后面传来的信息，如果相同，则ok
			assertEquals("検索結果がありません。", message);  
		}

	}

	/**
	 * ケース：No2
	 * 　　　　異常系
	 * 条件：
	 *     　　社員ID：DBに存在する
	 *     　　社員名：DBに存在しない
	 * 予想値：
	 * 　　　　エラー：検索結果がありません。
	 */
	@Test
	public void test_readDataFromMysql_02() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeId("1");
			form.setEmployeeName("龍一");
			service.readDataFromMysql(form);

		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果がありません。", message);
		}
	}
	/**
	 * ケース：No3
	 * 　　　　異常系
	 * 条件：
	 *     　　社員ID：DBに存在しない
	 *     　　社員名：DBに存在する
	 * 予想値：
	 * 　　　　エラー：検索結果がありません。
	 */
	@Test
	public void test_readDataFromMysql_03() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeId("123321");
			form.setEmployeeName("王二");
			service.readDataFromMysql(form);

		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果がありません。", message);
		}
	}
	
	/**
	 * ケース：No4
	 * 　　　　異常系
	 * 条件：
	 *     　　社員ID：DBに存在しない
	 *     　　社員名：未入力
	 * 予想値：
	 * 　　　　エラー：検索結果がありません。
	 */
	@Test
	public void test_readDataFromMysql_04() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeId("123321");
			service.readDataFromMysql(form);

		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果がありません。", message);
		}
	}
	
	/**
	 * ケース：No5
	 * 　　　　異常系
	 * 条件：
	 *     　　社員ID：未入力
	 *     　　社員名：DBに存在しない（姓のみ）
	 * 予想値：
	 * 　　　　エラー：検索結果がありません。
	 */
	@Test
	public void test_readDataFromMysql_05() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeName("呉");
			service.readDataFromMysql(form);

		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果がありません。", message);
		}
	}
	/**
	 * ケース：No6
	 * 　　　　異常系
	 * 条件：
	 *     　　社員ID：未入力
	 *     　　社員名：DBに存在しない（名のみ）
	 * 予想値：
	 * 　　　　エラー：検索結果がありません。
	 */
	@Test
	public void test_readDataFromMysql_06() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeName("柳");
			service.readDataFromMysql(form);

		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果がありません。", message);
		}
	}
	/**
	 * ケース：No7
	 * 　　　　正常系
	 * 条件：
	 *     　　社員ID：DBに存在する
	 *     　　社員名：未入力
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_readDataFromMysql_07() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeId("1");
			List<CmsEmployeeBean> results = service.readDataFromMysql(form);

			assertEquals(1, results.size());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No8
	 * 　　　　正常系
	 * 条件：
	 *     　　社員ID：未入力
	 *     　　社員名：DBに存在する（完全一致）
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_readDataFromMysql_08() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeName("王六");
			List<CmsEmployeeBean> results = service.readDataFromMysql(form);

			assertEquals(1, results.size());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No9
	 * 　　　　正常系
	 * 条件：
	 *     　　社員ID：未入力
	 *     　　社員名：DBに存在する（姓のみ）
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_readDataFromMysql_09() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeName("王");
			List<CmsEmployeeBean> results = service.readDataFromMysql(form);

			assertEquals(2, results.size());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No10
	 * 　　　　正常系
	 * 条件：
	 *     　　社員ID：未入力
	 *     　　社員名：DBに存在する（名のみ）
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_readDataFromMysql_10() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeName("六");
			List<CmsEmployeeBean> results = service.readDataFromMysql(form);

			assertEquals(1, results.size());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No11
	 * 　　　　正常系
	 * 条件：
	 *     　　社員ID：DBに存在する
	 *     　　社員名：DBに存在する（姓のみ）
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_readDataFromMysql_11() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeId("3");
			form.setEmployeeName("王");
			List<CmsEmployeeBean> results = service.readDataFromMysql(form);

			assertEquals(1, results.size());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No12
	 * 　　　　正常系
	 * 条件：
	 *     　　社員ID：DBに存在する
	 *     　　社員名：DBに存在する（名のみ）
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_readDataFromMysql_12() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeId("4");
			form.setEmployeeName("四");
			List<CmsEmployeeBean> results = service.readDataFromMysql(form);

			assertEquals(1, results.size());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No13
	 * 　　　　正常系
	 * 条件：
	 *     　　社員ID：DBに存在する
	 *     　　社員名：DBに存在する（完全一致）
	 * 予想値：
	 * 　　　　明細データが抽出されること
	 */
	@Test
	public void test_readDataFromMysql_13() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeId("5");
			form.setEmployeeName("柳五");
			List<CmsEmployeeBean> results = service.readDataFromMysql(form);

			assertEquals(1, results.size());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No14
	 * 　　　　境界値
	 * 条件：
	 *     　　検索結果：1件のみ
	 * 予想値：
	 * 　　　　明細ヘッダとデータの揃えが設計書の通りに設定されること
	 * 　　　　明細項目が設計書の通りにDBから抽出されること
	 * 　　　　日付フォーマットが設計書通りに設定されること
	 * 　　　　性別の名称が正常に表示されること
	 */
	@Test
	public void test_readDataFromMysql_14() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeId("5");
			form.setEmployeeName("五");
			List<CmsEmployeeBean> results = service.readDataFromMysql(form);
			//按条件查找出1条数据

			//検査結果のサイズ，式样书没写，但是确认件数没问题，再往下测试
			assertEquals(1, results.size());
			for (CmsEmployeeBean bean : results) {
				//検索明細の比較
				assertEquals("5", bean.getEmployeeId());
				assertEquals("柳五", bean.getName());
				assertEquals("女", bean.getSexForList());
				assertEquals("2005-05-09", bean.getBirthday());
				assertEquals("2014-04-01", bean.getJoiningDate());
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No15
	 * 　　　　境界値
	 * 条件：
	 *     　　検索結果：複数件
	 * 予想値：
	 * 　　　　明細ヘッダとデータの揃えが設計書の通りに設定されること
	 * 　　　　明細項目が設計書の通りにDBから抽出されること
	 * 　　　　日付フォーマットが設計書通りに設定されること
	 * 　　　　性別の名称が正常に表示されること
	 */
	@Test
	public void test_readDataFromMysql_15() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			form.setEmployeeName("柳");
			List<CmsEmployeeBean> results = service.readDataFromMysql(form);

			List<CmsEmployeeBean> expectedValues = new ArrayList<CmsEmployeeBean>();
			CmsEmployeeBean bean1 = new CmsEmployeeBean();
			bean1.setEmployeeId("5");
			bean1.setName("柳五");
			bean1.setSexForList("女");
			bean1.setBirthday("2005-05-09");
			bean1.setJoiningDate("2014-04-01");
			bean1.setJoiningDate("2014-04-01");
			
			CmsEmployeeBean bean2 = new CmsEmployeeBean();
			bean2.setEmployeeId("6");
			bean2.setName("柳六");
			bean2.setSexForList("男");
			bean2.setBirthday("2006-06-07");
			bean2.setJoiningDate("2015-04-01");
			expectedValues.add(bean1);
			expectedValues.add(bean2);
			
			//検査結果のサイズ 
			//把预测的Bean通过toArray()转成数组，进行对比
			assertArrayEquals(expectedValues.toArray(), results.toArray());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No15
	 * 　　　　境界値
	 * 条件：
	 *     　　検索結果：複数件
	 * 予想値：
	 * 　　　　明細ヘッダとデータの揃えが設計書の通りに設定されること
	 * 　　　　明細項目が設計書の通りにDBから抽出されること
	 * 　　　　日付フォーマットが設計書通りに設定されること
	 * 　　　　性別の名称が正常に表示されること
	 */
	@Test
	public void test_readDataFromMysql_17_18() {
		try {
			ReadDataBaseForm form = new ReadDataBaseForm();
			
			List<CmsEmployeeBean> results = service.readDataFromMysql(form);
			//検索結果のIDリスト
            Object[] ids = results.stream().map(CmsEmployeeBean::getEmployeeId).collect(Collectors.toList()).toArray();
		    
            //予想値
            Object[] expectedIds = {"2","1","3","4","5","6"};
			
			//検査結果のサイズ
			assertArrayEquals(ids, expectedIds);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}