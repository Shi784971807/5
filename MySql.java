package com.sample.practice2;

import static org.junit.jupiter.api.Assertions.*;

import org.dbunit.database.IDatabaseConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.base.UnitTestBase;
import com.cms.service.practice2.RegistCsvService;

@SuppressWarnings("rawtypes")
@RunWith(SpringRunner.class)  
@SpringBootTest
public class ReadcsvServiceTest_registCsvToMySql
{
	private static IDatabaseConnection conn;

	private final static String path = "src\\test\\data\\practice2\\";
	/* テストデータを登録する */
    @BeforeAll
    public static void init() throws Exception {
    	conn = UnitTestBase.connect();
		//CSVデータを取り込む
    	UnitTestBase.clearData(path,conn);
	}
    
    @AfterAll
    public static void closeConnection() throws Exception {
    	UnitTestBase.clearData(path,conn);
		//CSVデータを取り込む
    	UnitTestBase.closeConnection(conn);
	}
	@Autowired
	RegistCsvService service;
   
    /**
     * 正常パターン（複数件が登録される）
     * 
     * ※全レコード抽出
     */
    @Test
    public void test_readDataFromMysql_01_OK() 
    {
    	
		try {
			int registCount = service.registCsvToMySql(path+"unit_case_ok_01.csv", 9);
	    	assertEquals(2, registCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * 異常終了（ファイルが設定されない。）
     * 
     */
    @Test
    public void test_readDataFromMysql_01_NG() {
    	try {
			service.registCsvToMySql("", 7);
		} catch (Exception e) {
	    	assertEquals("ファイルが指定されません。", e.getMessage());
		}
    }
    
    /**
     * 異常終了（空白ファイル）
     * 
     */
    @Test
    public void test_readDataFromMysql_02_NG() {
    	try {
			service.registCsvToMySql(path+"unit_case_ng_02.csv", 7);
		} catch (Exception e) {
			assertEquals("空白ファイルです。", e.getMessage());
		}
    }
    
    /**
     * 異常終了（項目数不一致）
     * 
     */
    @Test
    public void test_readDataFromMysql_03_NG() {
    	try {
			service.registCsvToMySql(path+"unit_case_ng_03.csv", 7);
		} catch (Exception e) {
			assertEquals("ヘッダ項目件数は正しくありません。(正確の項目数：７)", e.getMessage());
		}
    }
}