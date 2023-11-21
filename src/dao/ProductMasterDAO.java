package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.Product;
import bean.ProductMaster;
import bean.Stock;
import tool.DateUtils;

public class ProductMasterDAO extends DAO {

	/**中尾
	 * 品番マスタテーブルから入力した品番と一致するレコードを取得するメソッド
	 * @param productNo　（品番）
	 * @return　productBean
	 */
	public ProductMaster searchProductMaster(String productNo) {
		ProductMaster productBean = new ProductMaster();
		try {
			Connection con = getConnection();
			PreparedStatement sqlset = con.prepareStatement(
					"SELECT * FROM PRODUCT_MASTER WHERE PRODUCT_NO=?");
			sqlset.setString(1, productNo);
			ResultSet getData = sqlset.executeQuery();

			while (getData.next()) {
				productBean.setProductNo(getData.getString("product_no"));
				productBean.setProductName(getData.getString("product_name"));
				productBean.setSupplierNo(getData.getString("supplier_no"));
				productBean.setUnitprice(getData.getDouble("unitprice"));
				productBean.setSellingprice(getData.getDouble("sellingprice"));
				productBean.setLeadtime(Integer.parseInt(getData.getString("leadtime")));
				productBean.setLot(Integer.parseInt(getData.getString("lot")));
				productBean.setLocation(getData.getString("location"));
				productBean.setEtc(getData.getString("etc"));
				productBean.setRegistdate(getData.getString("registdate"));
				productBean.setRegistuser(getData.getString("registuser"));

			}
			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productBean;
	}

	/**中尾
	 * 品番マスタテーブルに登録するメソッド
	 * @param screenvalue　（画面で入力した値）
	 * @param request
	 * @return　result
	 */
	public String insertProductMaster(Product screenvalue, HttpServletRequest request) {
		int result = 0;
		String getProductNo = "";
		try {
			Connection con = getConnection();
			HttpSession session = request.getSession();
			//ログイン画面のセッション情報を取得　（ユーザーID）
			String userId = (String) session.getAttribute("Login");

			PreparedStatement sqlset = con.prepareStatement("SELECT LPAD(PRODUCT_NO.NEXTVAL,10,0)AS NEW_NO FROM DUAL");
			ResultSet getNo = sqlset.executeQuery();
			getNo.next();
			getProductNo = getNo.getString("NEW_NO");

			sqlset = con.prepareStatement(
					"INSERT INTO PRODUCT_MASTER VALUES(?,?,?,?,?,?,?,?,?,?,?)");
			sqlset.setString(1, getProductNo);
			sqlset.setString(2, screenvalue.getProductName());
			sqlset.setString(3, screenvalue.getSupplierNo());
			sqlset.setDouble(4, screenvalue.getUnitprice());
			sqlset.setDouble(5, screenvalue.getSellingprice());
			sqlset.setInt(6, screenvalue.getLeadtime());
			sqlset.setInt(7, screenvalue.getLot());
			sqlset.setString(8, screenvalue.getLocation());
			sqlset.setString(9, screenvalue.getEtc());
			sqlset.setString(10, DateUtils.getSystemDateWithSlash());
			sqlset.setString(11, userId);

			result = sqlset.executeUpdate();

			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (result == 1) {
			return getProductNo;
		}
		return "0";
	}

	/**中尾
	 * 品番マスタテーブルを削除するメソッド
	 * @param productNo　（品番）
	 * @return　成功：1　失敗：1以外
	 */
	public boolean deleteProductMaster(String productNo) {
		//初期値はfalseにしておく
		boolean result = false;
		try {
			Connection con = getConnection();

			PreparedStatement sqlset = con.prepareStatement(
					"DELETE FROM PRODUCT_MASTER WHERE PRODUCT_NO=?");
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

	/**中尾
	 * 品番マスタテーブルを更新するメソッド
	 * @param screenvalue　（画面で入力した値）
	 * @param request
	 * @return　成功：1　失敗：1以外
	 */
	public boolean updateProductMaster(Product screenvalue, HttpServletRequest request) {
		//初期値はfalseにしておく
		boolean result = false;
		try {

			Connection con = getConnection();
			HttpSession session = request.getSession();
			//ログイン画面のセッション情報を取得　（ユーザーID）
			String userId = (String) session.getAttribute("Login");

			PreparedStatement sqlset = con.prepareStatement(
					"UPDATE PRODUCT_MASTER SET PRODUCT_NAME=?,"
							+ "SUPPLIER_NO=?,UNITPRICE=?,SELLINGPRICE=?,LEADTIME=?,"
							+ "LOT=?,LOCATION=?,ETC=?,REGISTDATE=?,REGISTUSER=? WHERE PRODUCT_NO=?");
			sqlset.setString(1, screenvalue.getProductName());
			sqlset.setString(2, screenvalue.getSupplierNo());
			sqlset.setDouble(3, screenvalue.getUnitprice());
			sqlset.setDouble(4, screenvalue.getSellingprice());
			sqlset.setInt(5, screenvalue.getLeadtime());
			sqlset.setInt(6, screenvalue.getLot());
			sqlset.setString(7, screenvalue.getLocation());
			sqlset.setString(8, screenvalue.getEtc());
			sqlset.setString(9, DateUtils.getSystemDateWithSlash());
			sqlset.setString(10, userId);
			sqlset.setString(11, screenvalue.getProductNo());

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

	//木長　CalcMain品番マスタ全てのデータを取得
	public List<Stock> getProductMasterNo(List<Stock> listmaster) {

		try {

			Connection con = getConnection();
			PreparedStatement st = con.prepareStatement(
					"SELECT * FROM PRODUCT_MASTER ORDER BY PRODUCT_NO");

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Stock p = new Stock();
				p.setProductNo(rs.getString("PRODUCT_NO"));
				p.setProductName(rs.getString("PRODUCT_NAME"));
				p.setSupplierNo(rs.getString("SUPPLIER_NO"));
				p.setLeadtime(rs.getInt("LEADTIME"));
				p.setLot(rs.getInt("LOT"));
				p.setRegistdate(rs.getString("REGISTDATE"));
				p.setRegistuser(rs.getString("REGISTUSER"));

				listmaster.add(p);
			}
			st.close();
			con.close();

		} catch (Exception e) {
		}
		return listmaster;
	}

	//木長StockMain 品番から品名を取得する。
	public Stock searchStock(String getmasterkod) {
		Stock stockmasterbean = new Stock();

		try {
			Connection con = getConnection();

			PreparedStatement sqlset = con.prepareStatement(
					"SELECT * FROM PRODUCT_MASTER WHERE PRODUCT_NO=?");

			sqlset.setString(1, getmasterkod);

			ResultSet getData = sqlset.executeQuery();

			while (getData.next()) {
				stockmasterbean.setProductNo(getData.getString("product_no"));
				stockmasterbean.setProductName(getData.getString("product_name"));
				stockmasterbean.setYmd(getData.getString("REGISTDATE"));

			}
			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stockmasterbean;
	}
}
