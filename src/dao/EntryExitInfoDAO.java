package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.EntryExitInfo;
import bean.InOut;
import tool.DateUtils;

public class EntryExitInfoDAO extends DAO{

	public EntryExitInfo searchEntryExitInfo(String enExId) {
		EntryExitInfo bean=new EntryExitInfo();

		try {

		Connection con=getConnection();

		PreparedStatement sqlset=con.prepareStatement("SELECT * FROM ENTRY_EXIT_INFO WHERE EN_EX_ID=?");
		sqlset.setString(1,enExId);
		ResultSet getData=sqlset.executeQuery();

		while(getData.next()) {
			bean.setEnExId(getData.getString("EN_EX_ID"));
			bean.setEnExDate(getData.getString("EN_EX_DATE"));
			bean.setProductNo(getData.getString("PRODUCT_NO"));
			bean.setNyukoQty(getData.getInt("NYUKO_QTY"));
			bean.setSyukoQty(getData.getInt("SYUKO_QTY"));
			bean.setReason(getData.getString("REASON"));
			bean.setRegistdate(getData.getString("REGISTDATE"));
			bean.setRegistuser(getData.getString("REGISTUSER"));
		}
		sqlset.close();
		con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	//m
	public int insertEntryExitInfo(HttpServletRequest request,InOut inoutbean,EntryExitInfo eeibean) {

		int getResult =0;

		try {
			Connection con = getConnection();
			HttpSession session = request.getSession();
			String um = (String) session.getAttribute("Login");

			PreparedStatement sqlset = con.prepareStatement("INSERT INTO ENTRY_EXIT_INFO VALUES(EN_EX_ID.NEXTVAL,?,?,?,?,?,?,?)");

			sqlset.setString(1,DateUtils.getSystemDateWithSlash());//日付
			sqlset.setString(2,inoutbean.getProductNo());//品番
			sqlset.setInt(3,eeibean.getNyukoQty());//入庫数
			sqlset.setInt(4,eeibean.getSyukoQty());//出庫数
			sqlset.setString(5,inoutbean.getReason());//理由
			sqlset.setString(6,DateUtils.getSystemDateWithSlash());//登録日
			sqlset.setString(7,um);//登録者

			getResult = sqlset.executeUpdate();
	        sqlset.close();
	        con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getResult;
	}
}
