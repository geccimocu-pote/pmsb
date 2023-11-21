package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.Delivery;
import bean.InOut;
import bean.Issue;
import bean.ProductStock;
import bean.Stock;
import tool.DateUtils;

public class ProductStockDAO extends DAO {
	public ProductStock searchPuroductStock(String stockInfoDate, String productNo) {
		ProductStock ProductStockbean = new ProductStock();
		try {

			Connection con = getConnection();
			PreparedStatement st = con.prepareStatement(
					"SELECT * FROM PRODUCT_STOCK WHERE STOCK_INFO_DATE=? AND PRODUCT_NO=?");

			st.setString(1, stockInfoDate);
			st.setString(2, productNo);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				ProductStockbean.setStockInfoDate(rs.getString("stock_info_date"));
				ProductStockbean.setProductNo(rs.getString("product_no"));
				ProductStockbean.setStockQty(rs.getInt("stock_qty"));
				ProductStockbean.settNyuko(rs.getInt("t_nyuko"));
				ProductStockbean.settSyuko(rs.getInt("t_syuko"));
				ProductStockbean.settSyuka(rs.getInt("t_syuka"));
				ProductStockbean.setUpDate(rs.getString("up_date"));

			}
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductStockbean;

	}

	public boolean deletePuroductStock(String productNo) {
		boolean result = false;
		try {

			Connection con = getConnection();
			PreparedStatement sqlset = con.prepareStatement(
					"DELETE PRODUCT_STOCK WHERE PRODUCT_NO=?");

			sqlset.setString(1, productNo);

			int getData = sqlset.executeUpdate();

			if (getData == 1) {
				result = true;
			}
			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//木長　StockMain、CalcMain該当する品番の在庫状況を取得
	public Stock searchproductStock(String getmasterkod) {
		Stock stockproductbean = new Stock();

		try {
			Connection con = getConnection();

			PreparedStatement sqlset = con.prepareStatement(
					"SELECT * FROM PRODUCT_STOCK WHERE PRODUCT_NO=? and STOCK_INFO_DATE=TO_CHAR(SYSDATE,'YYYYMM')");

			sqlset.setString(1, getmasterkod);

			ResultSet getData = sqlset.executeQuery();

			while (getData.next()) {
				stockproductbean.setProductNo(getData.getString("product_no"));
				stockproductbean.setStockQty(getData.getString("stock_qty"));
				stockproductbean.setYmd("0000/00/00");

			}
			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stockproductbean;
	}

	//takagi help method

	/**
		 * 在庫テーブルに品番毎のデータを作成するメソッド
		 * @param yyyymm　（対象月）
		 * @param product_no　（品番）
		 * @return　成功：1　失敗：1以外
		 */
	public int newProductStockDataMake(String yyyymm, String result) {
		int getResult = 0;
		try {
			Connection con = getConnection();

			PreparedStatement st = con
					.prepareStatement("INSERT INTO PRODUCT_STOCK VALUES(?,?,0,0,0,0,TO_CHAR(SYSDATE,'YYYY/MM/DD'))");

			st.setString(1, yyyymm);
			st.setString(2, result);

			getResult = st.executeUpdate();

			st.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getResult;

	}

	//
	//		public List<Stock> getStockNoQty(List<Stock> list) {
	//			Stock stockproductbean = new Stock();
	//
	//			try {
	//				Connection con = getConnection();
	//
	//				PreparedStatement sqlset = con.prepareStatement(
	//						"SELECT * FROM PRODUCT_STOCK WHERE PRODUCT_NO=? and STOCK_INFO_DATE=TO_CHAR(SYSDATE,'YYYYMM')");
	//
	//				sqlset.setString(1, getmasterkod);
	//
	//				ResultSet getData = sqlset.executeQuery();
	//
	//				while (getData.next()) {
	//					stockproductbean.setProductNo(getData.getString("product_no"));
	//					stockproductbean.setStockQty(getData.getString("stock_qty"));
	//					stockproductbean.setYmd(getData.getString("STOCK_INFO_DATE"));
	//
	//				}
	//				sqlset.close();
	//				con.close();
	//			} catch (Exception e) {
	//				e.printStackTrace();
	//			}
	//			return stockproductbean;
	//		}	// TODO 自動生成されたメソッド・スタブ
	//			return null;
	//		}

	//matsumoto 入出庫処理
	public int updateProductStockInOut(InOut iobean, ProductStock psbean) {
		int getResult = 0;
		try {
			Connection con = getConnection();
			String nyukoSyuko = iobean.getNyukoSyuko();
			int qty = Integer.parseInt(iobean.getNyukoQty_or_syukoQty());

			if (nyukoSyuko.equals("nyuko")) {
				PreparedStatement sqlset = con.prepareStatement(
						"UPDATE PRODUCT_STOCK SET STOCK_QTY=STOCK_QTY+?, T_NYUKO=T_NYUKO+?, UP_DATE=? WHERE STOCK_INFO_DATE=? AND PRODUCT_NO=?");
				sqlset.setInt(1, qty);//現在在庫数
				sqlset.setInt(2, qty);//当月入庫数
				sqlset.setString(3, DateUtils.getSystemDateWithSlash());//更新日
				sqlset.setString(4, psbean.getStockInfoDate());//年月
				sqlset.setString(5, psbean.getProductNo());//品番
				getResult = sqlset.executeUpdate();
				sqlset.close();
			}
			if (nyukoSyuko.equals("syuko")) {
				PreparedStatement sqlset = con.prepareStatement(
						"UPDATE PRODUCT_STOCK SET STOCK_QTY=STOCK_QTY-?, T_SYUKO=T_SYUKO+?, UP_DATE=? WHERE STOCK_INFO_DATE=? AND PRODUCT_NO=?");
				sqlset.setInt(1, qty);//現在在庫数
				sqlset.setInt(2, qty);//当月出庫数
				sqlset.setString(3, DateUtils.getSystemDateWithSlash());//更新日
				sqlset.setString(4, psbean.getStockInfoDate());//年月
				sqlset.setString(5, psbean.getProductNo());//品番
				getResult = sqlset.executeUpdate();
				sqlset.close();
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getResult;
	}

	//matsumoto 納入処理
	public int updateProductStockDelivery(Delivery delibean) {
		int getResult = 0;
		try {
			Connection con = getConnection();
			PreparedStatement sqlset = con.prepareStatement(
					"UPDATE PRODUCT_STOCK SET STOCK_QTY=STOCK_QTY+?, T_NYUKO=T_NYUKO+?, UP_DATE=? WHERE STOCK_INFO_DATE=? AND PRODUCT_NO=?");
			sqlset.setInt(1, Integer.parseInt(delibean.getDueQty()));//現在在庫数
			sqlset.setInt(2, Integer.parseInt(delibean.getDueQty()));//当月入庫数
			sqlset.setString(3, DateUtils.getSystemDateWithSlash());//更新日
			sqlset.setString(4, DateUtils.getSystemDateWithSlash().replace("/", "").substring(0, 6));//年月
			sqlset.setString(5, delibean.getProductNo());//品番

			getResult = sqlset.executeUpdate();
			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getResult;
	}

	//matsumoto 出荷処理
	public int updateProductStockIssue(Issue issuebean) {
		int getResult = 0;
		try {
			Connection con = getConnection();
			PreparedStatement sqlset = con.prepareStatement(
					"UPDATE PRODUCT_STOCK SET STOCK_QTY=STOCK_QTY-?, T_SYUKA=T_SYUKA+?, UP_DATE=? WHERE STOCK_INFO_DATE=? AND PRODUCT_NO=?");
			sqlset.setInt(1, Integer.parseInt(issuebean.getPoQty()));//現在庫数
			sqlset.setInt(2, Integer.parseInt(issuebean.getPoQty()));//当月出荷数
			sqlset.setString(3, DateUtils.getSystemDateWithSlash());//更新日
			sqlset.setString(4, DateUtils.getSystemDateWithSlash().replace("/", "").substring(0, 6));//年月
			sqlset.setString(5, issuebean.getProductNo());//品番

			getResult = sqlset.executeUpdate();
			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getResult;

	}

}