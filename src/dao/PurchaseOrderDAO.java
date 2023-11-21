package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.Issue;
import bean.Order;
import bean.PurchaseOrder;
import bean.Stock;
import tool.DateUtils;



//takada
public class PurchaseOrderDAO extends DAO{
	public List<PurchaseOrder> searchPurchseOrder(String productNo) {
		List<PurchaseOrder> PurchaseOrderList = new ArrayList<>();
		PurchaseOrder PurchaseOrderbean = null;

		try {
			Connection con = getConnection();
			PreparedStatement st = con.prepareStatement(
					"SELECT * FROM PURCHASE_ORDER WHERE PRODUCT_NO=?");

			st.setString(1, productNo);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				PurchaseOrderbean = new PurchaseOrder();
				PurchaseOrderbean.setPoNo(rs.getString("po_no"));
				PurchaseOrderbean.setCustomerNo(rs.getString("customer_no"));
				PurchaseOrderbean.setProductNo(rs.getString("product_no"));
				PurchaseOrderbean.setPoQty(rs.getInt("po_qty"));
				PurchaseOrderbean.setDeliveryDate(rs.getString("delivery_date"));
				PurchaseOrderbean.setShipDate(rs.getString("ship_date"));
				PurchaseOrderbean.setFinFlg(rs.getInt("fin_flg"));
				PurchaseOrderbean.setOrderDate(rs.getString("order_date"));
				PurchaseOrderbean.setRegistuser(rs.getString("registuser"));

				PurchaseOrderList.add(PurchaseOrderbean);
			}
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PurchaseOrderList;

	}



	//matsumoto
	public int insertPurchaseOrder(Order orderbean,String shipdate,HttpServletRequest request) {
		int getResult =0;
		try {
			Connection con = getConnection();
			HttpSession session = request.getSession();
			String um = (String) session.getAttribute("Login");


			PreparedStatement sqlset = con.prepareStatement("INSERT INTO PURCHASE_ORDER VALUES('PO_' || PO_NO.NEXTVAL,?,?,?,?,?,0,?,?)");
			sqlset.setString(1,orderbean.getCustomerNo());//顧客先コード
			sqlset.setString(2,orderbean.getProductNo());//品番
			sqlset.setInt(3,Integer.parseInt(orderbean.getPoQty()));//注文個数
			sqlset.setString(4,orderbean.getDeliveryDate().replace('-','/'));//納期
			sqlset.setString(5,shipdate);//出荷日
			sqlset.setString(6,DateUtils.getSystemDateWithSlash());//注文日
			sqlset.setString(7,um);//登録者

			getResult = sqlset.executeUpdate();
	        sqlset.close();
	        con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getResult;
	}

//木長　StockMain、CalcMain該当する品番の受注状況を取得する。
	public List<Stock> searchPurchase(String getmasterkod,List<Stock> purchase) {


		try {

			Connection con = getConnection();

			PreparedStatement sqlset = con.prepareStatement(
					"SELECT * FROM PURCHASE_ORDER WHERE PRODUCT_NO=?");

			sqlset.setString(1, getmasterkod);

			ResultSet getData = sqlset.executeQuery();

			while (getData.next()) {
				Stock stockpurchasekbean = new Stock();
				stockpurchasekbean.setProductNo(getData.getString("product_no"));
				stockpurchasekbean.setCustomerNo(getData.getString("customer_no"));
				stockpurchasekbean.setPoNo(getData.getString("po_no"));
				stockpurchasekbean.setPoQty(getData.getString("po_qty"));
				stockpurchasekbean.setYmd(getData.getString("ship_date"));
purchase.add(stockpurchasekbean);
			}
			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchase;
	}


//matsumoto
		public PurchaseOrder searchPurchaseOrderSingleData(String poNo) {
			PurchaseOrder puobean = new PurchaseOrder();

			try {
				Connection con = getConnection();

				PreparedStatement st = con.prepareStatement(
						"SELECT * FROM PURCHASE_ORDER WHERE PO_NO=?");

				st.setString(1, poNo);
				ResultSet rs = st.executeQuery();

				while (rs.next()) {
					puobean.setPoNo(rs.getString("PO_NO"));
					puobean.setCustomerNo(rs.getString("CUSTOMER_NO"));
					puobean.setProductNo(rs.getString("PRODUCT_NO"));
					puobean.setPoQty(rs.getInt("PO_QTY"));
					puobean.setDeliveryDate(rs.getString("DELIVERY_DATE"));
					puobean.setShipDate(rs.getString("SHIP_DATE"));
					puobean.setFinFlg(rs.getInt("FIN_FLG"));
					puobean.setOrderDate(rs.getString("ORDER_DATE"));
					puobean.setRegistuser(rs.getString("REGISTUSER"));
				}
				st.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return puobean;
		}


//matsumoto
		public int updatePurchaseOrder(Issue issuebean,HttpServletRequest request) {
			int getResult =0;
			try {
				Connection con = getConnection();

				PreparedStatement sqlset = con.prepareStatement
					("UPDATE PURCHASE_ORDER SET FIN_FLG=1 WHERE PO_NO=?");
				sqlset.setString(1,issuebean.getPoNo());//注文番号

				getResult = sqlset.executeUpdate();
				sqlset.close();
				con.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return getResult;
		}

}
