package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.Delivery;
import bean.ProductOrder;
import bean.Stock;
import tool.DateUtils;

public class ProductOrderDAO extends DAO {

	public ProductOrder searchProductOrder(String orderNo) {
		ProductOrder bean = new ProductOrder();

		try {

			Connection con = getConnection();

			PreparedStatement sqlset = con.prepareStatement("SELECT * FROM PRODUCT_ORDER WHERE ORDER_NO=?");
			sqlset.setString(1, orderNo);
			ResultSet getData = sqlset.executeQuery();

			while (getData.next()) {
				bean.setOrderNo(getData.getString("ORDER_NO"));
				bean.setSupplierNo(getData.getString("SUPPLIER_NO"));
				bean.setProductNo(getData.getString("PRODUCT_NO"));
				bean.setOrderQty(getData.getInt("ORDER_QTY"));
				bean.setDeliveryDate(getData.getString("DELIVERY_DATE"));
				bean.setEtc(getData.getString("ETC"));
				bean.setDueDate(getData.getString("DUE_DATE"));
				bean.setDueQty(getData.getInt("DUE_QTY"));
				bean.setFinFlg(getData.getInt("FIN_FLG"));
				bean.setRegistuser(getData.getString("REGISTUSER"));
				bean.setRegistdate(getData.getString("REGISTDATE"));
				bean.setOrderuser(getData.getString("ORDERUSER"));
			}
			sqlset.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}



	//木長　StockMain、CalcMain該当した品番の発注情報を取得
	public List<Stock> searchOrder(String getmasterkod, List<Stock> order) {

		try {
			Connection con = getConnection();

			PreparedStatement sqlset = con.prepareStatement(
					"SELECT * FROM PRODUCT_ORDER WHERE PRODUCT_NO=?");

			sqlset.setString(1, getmasterkod);

			ResultSet getData = sqlset.executeQuery();

			while (getData.next()) {
				Stock stockorderbean = new Stock();
				stockorderbean.setOrderNo(getData.getString("order_no"));
				stockorderbean.setSupplierNo(getData.getString("supplier_no"));
				stockorderbean.setProductNo(getData.getString("product_no"));
				stockorderbean.setOrderQty(getData.getString("order_qty"));
				stockorderbean.setDeliveryDate(getData.getString("delivery_date"));
				stockorderbean.setDueDate(getData.getString("due_date"));
				stockorderbean.setDueQty(getData.getInt("due_qty"));
				stockorderbean.setFinFlg(getData.getInt("fin_flg"));
				stockorderbean.setRegistuser(getData.getString("registuser"));
				stockorderbean.setYmd(getData.getString("delivery_date"));
				stockorderbean.setOrderuser(getData.getString("orderuser"));
				order.add(stockorderbean);

			}
			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}


//matsumoto
	public int updateProductOrder(Delivery delibean,HttpServletRequest request) {
		int getResult =0;
		try {
			Connection con = getConnection();
			HttpSession session = request.getSession();
			String um = (String) session.getAttribute("Login");

			PreparedStatement sqlset = con.prepareStatement
				("UPDATE PRODUCT_ORDER SET DUE_DATE=?, DUE_QTY=?, FIN_FLG=1, REGISTUSER=? WHERE ORDER_NO=?");
			sqlset.setString(1,DateUtils.getSystemDateWithSlash());//納入日
			sqlset.setString(2,delibean.getDueQty());//納入数量
			sqlset.setString(3,um);//登録者
			sqlset.setString(4,delibean.getOrderNo());//注文番号

			getResult = sqlset.executeUpdate();
			sqlset.close();
			con.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return getResult;
	}


//木長　CalcMain、新規発注分を発注テーブルにinsertする。
	public int InsertOrder(HttpServletRequest request, List<Stock> output) {
		int getResult =0;
		for (int j = 0; j < output.size(); j++) {
		try {
			Connection con = getConnection();
			HttpSession session = request.getSession();
			String um = (String) session.getAttribute("Login");

			PreparedStatement sqlset = con.prepareStatement(
					"INSERT INTO PRODUCT_ORDER VALUES"
					+ "('OD-' || TO_CHAR(SYSDATE,'YYMM') || '-' || ORDER_NO.NEXTVAL,?,?,?,?,'0','0',0,0,?,?,?)");

			sqlset.setString(1,output.get(j).getSupplierNo());
			sqlset.setString(2,output.get(j).getProductNo());
			sqlset.setString(3,output.get(j).getOrderQty());
			sqlset.setString(4,output.get(j).getYmd());
		    sqlset.setString(5,um);
			Calendar today = Calendar.getInstance();
			String setDay = today.get(Calendar.YEAR) + "/"
					+ (today.get(Calendar.MONTH) + 1) + "/"
					+ today.get(Calendar.DATE);
			sqlset.setString(6,setDay);
			sqlset.setString(7,um);

			getResult = sqlset.executeUpdate();



		}catch (Exception e) {
			e.printStackTrace();
		}
		}
		return getResult;
	}


	}
