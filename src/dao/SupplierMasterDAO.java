package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.Supplier;
import bean.SupplierMaster;
import tool.DateUtils;

public class SupplierMasterDAO extends DAO {

	/**中尾
	 * 仕入先マスタテーブルから入力した仕入先コードと一致するレコードを取得するメソッド
	 * @param supplierNo　（仕入先コード）
	 * @return　supplierMasterbean
	 */
	public SupplierMaster searchSupplier(String supplierNo) {
		SupplierMaster supplierMasterbean = new SupplierMaster();
		try {
			Connection con = getConnection();
			PreparedStatement st = con.prepareStatement(
					"SELECT * FROM SUPPLIER_MASTER WHERE SUPPLIER_NO=?");
			st.setString(1, supplierNo);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				supplierMasterbean.setSupplierNo(rs.getString("supplier_no"));
				supplierMasterbean.setSupplierName(rs.getString("supplier_name"));
				supplierMasterbean.setBranchName(rs.getString("branch_name"));
				supplierMasterbean.setZipNo(rs.getString("zip_no"));
				supplierMasterbean.setAddress1(rs.getString("address1"));
				supplierMasterbean.setAddress2(rs.getString("address2"));
				supplierMasterbean.setAddress3(rs.getString("address3"));
				supplierMasterbean.setTel(rs.getString("tel"));
				supplierMasterbean.setFax(rs.getString("fax"));
				supplierMasterbean.setManager(rs.getString("manager"));
				supplierMasterbean.setEtc(rs.getString("etc"));
				supplierMasterbean.setRegistdate(rs.getString("registdate"));
				supplierMasterbean.setRegistuser(rs.getString("registuser"));
			}
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return supplierMasterbean;

	}

	/**中尾
	 * 仕入先マスタテーブルに登録するメソッド
	 * @param screenvalue　（画面で入力した値）
	 * @param request
	 * @return　result
	 */
	public String insertSupplierMaster(Supplier screenvalue, HttpServletRequest request) {
		int result = 0;
		String getSupplierNo = "";
		try {
			Connection con = getConnection();
			HttpSession session = request.getSession();
			//ログイン画面のセッション情報を取得　（ユーザーID）
			String userId = (String) session.getAttribute("Login");

			PreparedStatement sqlset = con.prepareStatement("SELECT LPAD(SUPPLIER_NO.NEXTVAL,6,0)AS NEW_NO FROM DUAL");
			ResultSet getNo = sqlset.executeQuery();
			getNo.next();
			getSupplierNo = getNo.getString("NEW_NO");

			sqlset = con.prepareStatement(
					"INSERT INTO SUPPLIER_MASTER VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			sqlset.setString(1, getSupplierNo);
			sqlset.setString(2, screenvalue.getSupplierName());
			sqlset.setString(3, screenvalue.getBranchName());
			sqlset.setString(4, screenvalue.getZipNo());
			sqlset.setString(5, screenvalue.getAddress1());
			sqlset.setString(6, screenvalue.getAddress2());
			sqlset.setString(7, screenvalue.getAddress3());
			sqlset.setString(8, screenvalue.getTel());
			sqlset.setString(9, screenvalue.getFax());
			sqlset.setString(10, screenvalue.getManager());
			sqlset.setString(11, screenvalue.getEtc());
			sqlset.setString(12, DateUtils.getSystemDateWithSlash());//登録日
			sqlset.setString(13, userId);//登録者

			result = sqlset.executeUpdate();

			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (result == 1) {
			return getSupplierNo;
		}
		return "0";
	}

	/**中尾
	 * 仕入先マスタテーブルを更新するメソッド
	 * @param screenvalue　（画面で入力した値）
	 * @param request
	 * @return　成功：1　失敗：1以外
	 */
	public boolean updateSupplierMaster(Supplier screenvalue, HttpServletRequest request) {
		//booleanの型falseにしておく
		boolean result = false;
		try {

			Connection con = getConnection();
			HttpSession session = request.getSession();
			String userId = (String) session.getAttribute("Login");

			PreparedStatement sqlset = con.prepareStatement(
					"UPDATE SUPPLIER_MASTER SET SUPPLIER_NAME=?,"
							+ "BRANCH_NAME=?,ZIP_NO=?,ADDRESS1=?,ADDRESS2=?,"
							+ "ADDRESS3=?,TEL=?,FAX=?,MANAGER=?,ETC=?,REGISTDATE=?,"
							+ "REGISTUSER=? WHERE SUPPLIER_NO=?");
			sqlset.setString(1, screenvalue.getSupplierName());
			sqlset.setString(2, screenvalue.getBranchName());
			sqlset.setString(3, screenvalue.getZipNo());
			sqlset.setString(4, screenvalue.getAddress1());
			sqlset.setString(5, screenvalue.getAddress2());
			sqlset.setString(6, screenvalue.getAddress3());
			sqlset.setString(7, screenvalue.getFax());
			sqlset.setString(8, screenvalue.getTel());
			sqlset.setString(9, screenvalue.getManager());
			sqlset.setString(10, screenvalue.getEtc());
			sqlset.setString(11, DateUtils.getSystemDateWithSlash());
			sqlset.setString(12, userId);
			sqlset.setString(13, screenvalue.getSupplierNo());

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
	 * 仕入先マスタテーブルを削除するメソッド
	 * @param supplierNo　（仕入先コード）
	 * @return　成功：1　失敗：1以外
	 */
	public boolean deleteSupplierMaster(String supplierNo) {
		//booleanの型falseにしておく
		boolean result = false;
		try {
			Connection con = getConnection();

			PreparedStatement sqlset = con.prepareStatement(
					"DELETE FROM SUPPLIER_MASTER WHERE SUPPLIER_NO=?");
			sqlset.setString(1, supplierNo);

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

}
