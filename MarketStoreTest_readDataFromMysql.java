
/**
 *  このクラスはMarketStoreServiceのDB読み取りでテストのクラスです。
 *  複数行に渡って記述することが可能です。
 *  @version 1.0
 *  @param string 时彬彬
 */
package com.sample.marketstore;

import static org.junit.jupiter.api.Assertions.*;

import org.dbunit.database.IDatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.base.UnitTestBase;
import com.cms.form.marketstore.MarketStoreForm;
import com.cms.form.marketstore.MarketStoreListForm;
import com.cms.service.marketstore.MarketStoreService;

@SuppressWarnings("rawtypes") //无视警告
@RunWith(SpringRunner.class) //用spring提供的环境执行，是属于SpringBootTest  junit的测试
@SpringBootTest  //这样就知道到底是什么对象

public class MarketStoreTest_readDataFromMysql {

	private static IDatabaseConnection conn;
	private final static String path = "src\\test\\data\\practice4\\";
	
	/* テストデータを登録する */
	@BeforeEach
	public  void init() throws Exception {
		conn = UnitTestBase.connect();

		// 実施前にテーブルの既存データをクリアする// 実施前にテーブルの既存データをクリアする
		//调用init方法中有delete，clear可以是双重保险，确保数据清楚，不写也可以
		UnitTestBase.clearData(path, conn);
		// CSVデータを取り込む
		//对数据初期化 path,是参数 conn 数据库链接
		UnitTestBase.initData(path, conn);
	}

	@AfterEach
	public  void closeConnection() throws Exception {
		//删除数据，因为占内存空间，数据库打开大门后，处理清除后，要关门
		UnitTestBase.clearData(path, conn);
		// CSVデータを取り込む
		UnitTestBase.closeConnection(conn);
	}

	//调用control里的service
	@Autowired
	MarketStoreService service;

	/**
	 * ケース：No1
	 * 　　　　異常系
	 * 条件：
	 *     　　販売店名：DBに存在しない
	 *     　　販売店アドレス：DBに存在しない
	 * 予想値：
	 * 　　　　エラー：検索結果はありません。
	 */
	@Test
	public void test_select_01() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setStoreName("回転寿司");
			form.setAddress("三重県");
			service.select(form);

		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果はありません。", message);
		}
	}

	/**
	 * ケース：No2
	 * 　　　　異常系
	 * 条件：
	 *     　　販売店名：DBに存在する
	 *     　　販売店アドレス：DBに存在しない
	 * 予想値：
	 * 　　　　エラー：検索結果はありません。
	 */
	@Test
	public void test_select_02() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setStoreName("大排档");
			form.setAddress("三重県");
			service.select(form);

		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果はありません。", message);
		}
	}
	/**
	 * ケース：No3
	 * 　　　　異常系
	 * 条件：
	 *     　　販売店名：DBに存在しない
	 *     　　販売店アドレス：DBに存在する
	 * 予想値：
	 * 　　　　エラー：検索結果はありません。
	 */
	@Test
	public void test_select_03() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();			
			form.setStoreName("回転寿司");
			form.setAddress("北海道");
			service.select(form);

		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果はありません。", message);
		}
	}
	
	/**
	 * ケース：No4
	 * 　　　　異常系
	 * 条件：
	 *     　　販売店名：DBに存在しない
	 *     　　販売店アドレス：未入力
	 * 予想値：
	 * 　　　　エラー：検索結果はありません。
	 */
	@Test
	public void test_select_04() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setStoreName("回転寿司");
			service.select(form);

		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果はありません。", message);
		}
	}
	
	/**
	 * ケース：No5
	 * 　　　　異常系
	 * 条件：
	 *     　　販売店名：DBに存在しない（文字の一部のみ）
	 *    	　 販売店アドレス：未入力
	 * 予想値：
	 * 　　　　エラー：検索結果はありません。
	 */
	@Test
	public void test_select_05() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setStoreName("花");
			service.select(form);
			

		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果はありません。", message);
		}
	}
	/**
	 * ケース：No6
	 * 　　　　異常系
	 * 条件：
	 *     　　販売店名：未入力
	 *    	　 販売店アドレス：DBに存在しない（文字の一部のみ）
	 * 予想値：
	 * 　　　　エラー：検索結果はありません。
	 */
	@Test
	public void test_select_06() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setAddress("三重");
			service.select(form);
			

		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果はありません。", message);
		}
	}
	/**
	 * ケース：No7
	 * 　　　　正常系
	 * 条件：
	 *     　　販売店名：DBに存在する
	 *     　　販売店アドレス：未入力
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_select_07() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setStoreName("肉夹馍");
			MarketStoreListForm results = service.select(form);

			assertEquals(1, results.getResults().size());
			assertEquals("肉夹馍",results.getResults().get(0).getStoreName());
			assertEquals("埼玉県", results.getResults().get(0).getAddress()); 
			assertEquals("10451000004", results.getResults().get(0).getPhone()); 
			assertEquals("2011-02-14", results.getResults().get(0).getStartDay()); 
			assertEquals("2015-05-01", results.getResults().get(0).getFinishDay()); 
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No8
	 * 　　　　正常系
	 * 条件：
	 *     　  販売店名：未入力
	 *     　　販売店アドレス：DBに存在する（完全一致）
	 *     
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_select_08() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setAddress("埼玉県");
			MarketStoreListForm results = service.select(form);
			assertEquals(1, results.getResults().size());
			assertEquals("肉夹馍",results.getResults().get(0).getStoreName());
			assertEquals("埼玉県", results.getResults().get(0).getAddress()); 
			assertEquals("10451000004", results.getResults().get(0).getPhone()); 
			assertEquals("2011-02-14", results.getResults().get(0).getStartDay()); 
			assertEquals("2015-05-01", results.getResults().get(0).getFinishDay()); 	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No9
	 * 　　　　正常系
	 * 条件：
	 *     　　販売店名：DBに存在する（文字の一部のみ）
	 *     	   販売店アドレス：未入力
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_select_09() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setStoreName("肉");
			MarketStoreListForm results = service.select(form);

			assertEquals(2, results.getResults().size());
			assertEquals("烤肉",results.getResults().get(0).getStoreName());
			assertEquals("肉夹馍",results.getResults().get(1).getStoreName());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No10
	 * 　　　　正常系
	 * 条件：
	 *     　　販売店名：未入力
	 *     　　販売店アドレス：DBに存在する（文字の一部のみ）
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_select_10() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setAddress("埼玉");
			MarketStoreListForm results = service.select(form);
			assertEquals(1, results.getResults().size());
			assertEquals("肉夹馍",results.getResults().get(0).getStoreName());
			assertEquals("埼玉県", results.getResults().get(0).getAddress()); 
			assertEquals("10451000004", results.getResults().get(0).getPhone()); 
			assertEquals("2011-02-14", results.getResults().get(0).getStartDay()); 
			assertEquals("2015-05-01", results.getResults().get(0).getFinishDay()); 				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No11
	 * 　　　　正常系
	 * 条件：
	 *     　　販売店名：DBに存在する（文字の一部のみ）
	 *     　　販売店アドレス：DBに存在する
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_select_11() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setStoreName("馍");
			form.setAddress("埼玉県");
			MarketStoreListForm results = service.select(form);

			assertEquals(1, results.getResults().size());
			assertEquals("肉夹馍",results.getResults().get(0).getStoreName());
			assertEquals("埼玉県", results.getResults().get(0).getAddress()); 
			assertEquals("10451000004", results.getResults().get(0).getPhone()); 
			assertEquals("2011-02-14", results.getResults().get(0).getStartDay()); 
			assertEquals("2015-05-01", results.getResults().get(0).getFinishDay()); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No12
	 * 　　　　正常系
	 * 条件：
	 *     　　販売店名：DBに存在する
	 *     　　販売店アドレス：DBに存在する（文字の一部のみ）
	 * 予想値：
	 * 　　　　エラー：明細データが抽出されること
	 */
	@Test
	public void test_select_12() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setStoreName("肉夹馍");
			form.setAddress("埼");
			MarketStoreListForm results = service.select(form);
			
			assertEquals(1, results.getResults().size());
			assertEquals("肉夹馍",results.getResults().get(0).getStoreName());
			assertEquals("埼玉県", results.getResults().get(0).getAddress()); 
			assertEquals("10451000004", results.getResults().get(0).getPhone()); 
			assertEquals("2011-02-14", results.getResults().get(0).getStartDay()); 
			assertEquals("2015-05-01", results.getResults().get(0).getFinishDay()); 
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ケース：No13
	 * 　　　　正常系
	 * 条件：
	 *     　　販売店名：DBに存在する（完全一致）
	 *     　　販売店アドレス：DBに存在する（完全一致）
	 * 予想値：
	 * 　　　　明細データが抽出されること
	 */
	@Test
	public void test_select_13() {
		try {
			MarketStoreListForm form = new MarketStoreListForm();
			form.setStoreName("肉夹馍");
			form.setAddress("埼玉県");
			MarketStoreListForm results = service.select(form);
			
			assertEquals(1, results.getResults().size());
			assertEquals("肉夹馍",results.getResults().get(0).getStoreName());
			assertEquals("埼玉県", results.getResults().get(0).getAddress()); 
			assertEquals("10451000004", results.getResults().get(0).getPhone()); 
			assertEquals("2011-02-14", results.getResults().get(0).getStartDay()); 
			assertEquals("2015-05-01", results.getResults().get(0).getFinishDay()); 
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
	
//	@Test
//	public void test_select_14() {
//		try {
//			MarketStoreListForm form = new MarketStoreListForm();
//			form.setStoreName("肉夹馍");
//			form.setAddress("埼玉県");
//			MarketStoreListForm results = service.select(form);
//			//按条件查找出1条数据
//
//			//検査結果のサイズ，式样书没写，但是确认件数没问题，再往下测试
//			assertEquals(1, results.getResults().size());
//			assertEquals("肉夹馍",results.getResults().get(0).getStoreName());
//			assertEquals("埼玉県", results.getResults().get(0).getAddress()); 
//			assertEquals("10451000004", results.getResults().get(0).getPhone()); 
//			assertEquals("2011-02-14", results.getResults().get(0).getStartDay()); 
//			assertEquals("2015-05-01", results.getResults().get(0).getFinishDay()); 
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
//	/**
//	 * ケース：No15
//	 * 　　　　境界値
//	 * 条件：
//	 *     　　検索結果：複数件
//	 * 予想値：
//	 * 　　　　明細ヘッダとデータの揃えが設計書の通りに設定されること
//	 * 　　　　明細項目が設計書の通りにDBから抽出されること
//	 * 　　　　日付フォーマットが設計書通りに設定されること
//	 * 　　　　性別の名称が正常に表示されること
//	 */
//	@Test
//	public void test_select_15() {
//		try {
//			MarketStoreListForm form = new MarketStoreListForm();
//			form.setStoreName("肉");
//			
//			MarketStoreListForm results = service.select(form);
//
//			List<MarketStoreBean> expectedValues = new ArrayList<MarketStoreBean>();
//			MarketStoreBean bean1 = new MarketStoreBean();
//			bean1.setStoreId("1");
//			bean1.setStoreName("烤肉");
//			bean1.setAddress("神奈川県");
//			bean1.setPhone("10451000001");
//			bean1.setStartDay("2010-04-01");
//			bean1.setFinishDay("2015-04-08");
//			
//			MarketStoreBean bean2 = new MarketStoreBean();
//			bean2.setStoreId("4");
//			bean2.setStoreName("肉夹馍");
//			bean2.setAddress("埼玉県");
//			bean2.setPhone("10451000004");
//			bean2.setStartDay("2011-02-14");
//			bean2.setFinishDay("2015-05-01");
//			expectedValues.add(bean1);
//			expectedValues.add(bean2);
//			
//			//検査結果のサイズ 
//			//把预测的Bean通过toArray()转成数组，进行对比
//			assertArrayEquals(expectedValues.toArray(), results.getResults().get(0).toArray());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
//	/**
//	 * ケース：No15
//	 * 　　　　境界値
//	 * 条件：
//	 *     　　検索結果：複数件
//	 * 予想値：
//	 * 　　　　明細ヘッダとデータの揃えが設計書の通りに設定されること
//	 * 　　　　明細項目が設計書の通りにDBから抽出されること
//	 * 　　　　日付フォーマットが設計書通りに設定されること
//	 * 　　　　性別の名称が正常に表示されること
//	 */
//	@Test
//	public void test_select_17_18() {
//		try {
//			ReadDataBaseForm1 form = new ReadDataBaseForm1();
//			
//			List<MarketStoreBean> results = service.select(form);
//			//検索結果のIDリスト
//            Object[] ids = results.stream().map(MarketStoreBean::getStoreId).collect(Collectors.toList()).toArray();
//		    
//            //予想値
//            Object[] expectedIds = {"1","2","3","4","5","6"};
//			
//			//検査結果のサイズ
//			assertArrayEquals(ids, expectedIds);
//			
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
	
	/**
	 * ケース：No1
	 * 　　　　正常系
	 * 条件：
	 *     　　新規登録
	 * 予想値：
	 * 　　　　DBのデータを増える
	 */
	@Test
	public void test_insert() {
		//新規データの準備
		MarketStoreForm form=new MarketStoreForm();
		form.setStoreName("和食");
		form.setAddress("銀座");
		form.setPhone("09066666666");
		form.setStartDay("2015-01-01");
		form.setFinishDay("2022-07-01");
		
		try {
			//insert方法をテストする
			service.insert(form);

			//selectを利用して、結果を取り出す
			MarketStoreListForm listForm = new MarketStoreListForm();
			listForm.setStoreName("和食");
			listForm.setAddress("銀座");		
			MarketStoreListForm results = service.select(listForm);
		
			//予想のデータと実際のDBのデータとチェックする
			assertEquals(1, results.getResults().size());
			assertEquals("和食",results.getResults().get(0).getStoreName());
			assertEquals("銀座", results.getResults().get(0).getAddress()); 
			assertEquals("09066666666", results.getResults().get(0).getPhone()); 
			assertEquals("2015-01-01", results.getResults().get(0).getStartDay()); 
			assertEquals("2022-07-01", results.getResults().get(0).getFinishDay()); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * ケース：No1
	 * 　　　　正常系
	 * 条件：
	 *     　　編集画面
	 * 予想値：
	 * 　　　　ID "4" の関連情報は呼び出されます
	 */
	@Test
	public void test_editInit() {
		MarketStoreForm form=new MarketStoreForm();
		form.setStoreId("4");
		MarketStoreForm  results=null;
			try {
				results = service.editInit(form);
				assertEquals("4",results.getStoreId());
				assertEquals("肉夹馍",results.getStoreName());
				assertEquals("埼玉県", results.getAddress()); 
				assertEquals("10451000004", results.getPhone()); 
				assertEquals("2011-02-14", results.getStartDay()); 
				assertEquals("2015-05-01", results.getFinishDay()); 				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}		
	}
	
	/**
	 * ケース：No1
	 * 　　　　正常系
	 * 条件：
	 *     　　データの更新
	 * 予想値：
	 * 　　　　更新したデータと準備のデータは同じです
	 */
	@Test
	public void test_update() {
		//更新データの準備
		MarketStoreForm form=new MarketStoreForm();
		form.setStoreId("6");
		form.setStoreName("パスタ");
		form.setAddress("銀座");
		form.setPhone("09066666666");
		form.setStartDay("2015-01-01");
		form.setFinishDay("2022-07-01");
		try {
			service.update(form);

			//selectを利用して、結果を取り出す
			MarketStoreListForm listForm = new MarketStoreListForm();
			listForm.setStoreName("パスタ");
			listForm.setAddress("銀座");		
			MarketStoreListForm results = service.select(listForm);
		
			//予想のデータと実際のDBのデータとチェックする
			assertEquals(1, results.getResults().size());
			assertEquals("6",results.getResults().get(0).getStoreId());
			assertEquals("パスタ",results.getResults().get(0).getStoreName());
			assertEquals("銀座", results.getResults().get(0).getAddress()); 
			assertEquals("09066666666", results.getResults().get(0).getPhone()); 
			assertEquals("2015-01-01", results.getResults().get(0).getStartDay()); 
			assertEquals("2022-07-01", results.getResults().get(0).getFinishDay()); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * ケース：No1
	 * 　　　　正常系
	 * 条件：
	 *     　　データの削除
	 * 予想値：
	 * 　　　　検索結果はありません
	 */
	@Test
	public void test_delete() {
		
		try {
			service.delete("6");

			//selectを利用して、結果を取り出す
			MarketStoreListForm listForm = new MarketStoreListForm();
			listForm.setStoreName("パスタ");
			listForm.setAddress("銀座");		
			service.select(listForm);
		
		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果はありません。", message);
		}
	}
	
	
	/**
	 * ケース：No1
	 * 　　　　正常系
	 * 条件：
	 *     　　データの全削除
	 * 予想値：
	 * 　　　　検索結果はありません
	 */
	@Test
	public void test_deleteAll() {
		String storeIds = "1,2,3,4,5,6";
	
		try {
			service.deleteAll(storeIds);

			//selectを利用して、結果を取り出す
			MarketStoreListForm listForm = new MarketStoreListForm();
					
			service.select(listForm);
		
		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("検索結果はありません。", message);
		}
	}
	
	/**
	 * ケース：No1
	 * 　　　　正常系
	 * 条件：
	 *     　　参照画面
	 * 予想値：
	 * 　　　　ID "4" の関連情報を参照する
	 */
	@Test
	public void test_readInit() {
		MarketStoreForm form=new MarketStoreForm();
		form.setStoreId("4");
			try {
				MarketStoreForm  results = service.readInit(form);
				assertEquals("4",results.getStoreId());
				assertEquals("肉夹馍",results.getStoreName());
				assertEquals("埼玉県", results.getAddress()); 
				assertEquals("10451000004", results.getPhone()); 
				assertEquals("2011-02-14", results.getStartDay()); 
				assertEquals("2015-05-01", results.getFinishDay()); 				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}		
	}
		
}
	