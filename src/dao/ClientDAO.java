package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.Client;

public class ClientDAO extends DAO {

public int updateData(Client clientBean) {
	int getResult =0;//結果返却用変数（整数）
	try {
		//1.接続　 接続にはスーパークラスのgetConnection()メソッドを使用する。
		Connection con = getConnection();
		//2.SQLをセット(ビーンの情報をUserビーンを参照して？にセットする
		PreparedStatement sqlset = con.prepareStatement(
"UPDATE CUSTOMER_MASTER set customer_Name =?,branch_Name=?,zip_No=?,address1=?,address2=?,"
									 + "address3=?,tel=?,fax=?,manager=?,"
                                     + "delivary_leadtime=?,etc=?"
                                     + " WHERE customer_no= ?");

		sqlset.setString(1,clientBean.getCustomerName());
		sqlset.setString(2,clientBean.getBranchName());
		sqlset.setString(3,clientBean.getZipNo());

		sqlset.setString(4,clientBean.getAddress1());
		sqlset.setString(5,clientBean.getAddress2());
		sqlset.setString(6,clientBean.getAddress3());
		sqlset.setString(7,clientBean.getTel());
		sqlset.setString(8,clientBean.getFax());

		sqlset.setString(9,clientBean.getManager());
		sqlset.setString(10,clientBean.getDelivaryLeadtime());
		sqlset.setString(11,clientBean.getEtc());
//		sqlset.setString(12,clientBean.getRegistdate());
//		sqlset.setString(13,clientBean.getRegistuser());

		sqlset.setString(12,clientBean.getCustomerNo());


		//3.SQLを実行
		getResult = sqlset.executeUpdate();
        sqlset.close();
        con.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return getResult;
}
	/**
	 * 受け取ったIDで検索する。
	 * @param recieveID
	 * @return　Client
	 */
	public Client search(String recieveID) {
		//1.戻り値(Client）用の変数を宣言する。
		Client client = new Client();
		try {
			//2.接続　 接続にはスーパークラスのgetConnection()メソッドを使用する。
			Connection con = getConnection();
			//3.検索用SQL
			PreparedStatement sqlset = con.prepareStatement(
					"SELECT * FROM CUSTOMER_MASTER WHERE CUSTOMER_NO=?");
			//4.？に検索キーワードをセットする。　文字列型なので、整数に変換。
			sqlset.setString(1,recieveID);
			//4.SQLを実行した結果を　getDataに格納する。
			ResultSet getData = sqlset.executeQuery();
			//5.取得したレコードをClientビーンに格納する。
			while (getData.next()) {
				//getRecordにテーブルから取得したレコード
				//データを列名＆型を指定してビーンに格納する。
				client.setCustomerNo(getData.getString("customer_No"));
				client.setCustomerName(getData.getString("customer_Name"));
				client.setBranchName(getData.getString("branch_Name"));
				client.setZipNo(getData.getString("zip_No"));

				client.setAddress1(getData.getString("address1"));
				client.setAddress2(getData.getString("address2"));
				client.setAddress3(getData.getString("address3"));
				client.setTel(getData.getString("tel"));
				client.setFax(getData.getString("fax"));

				client.setManager(getData.getString("manager"));
				client.setDelivaryLeadtime(getData.getString("delivary_Leadtime"));
				client.setEtc(getData.getString("etc"));
//				client.setRegistdate(getData.getString("registdate"));
//				client.setRegistuser(getData.getString("registuser"));

			}
			sqlset.close();//SQLクローズ処理
			con.close();//データベース切断処理
		}catch (Exception e ) {
			e.printStackTrace();
		}
			return client;
	}


	//customer_masterテーブルの全レコードを取得して、UserビーンのListで値を戻すメソッド
	/**
	 * customer_masterテーブルの全レコードを取得
	 * @param 無
	 * @return List<Client>
	 */
	public List<Client> allGetUserTableData() {
		//1.戻り値(List<User>）用の変数を宣言する。
		List<Client> clientList = new ArrayList<Client>();
		try {
			//2.接続　 接続にはスーパークラスのgetConnection()メソッドを使用する。
			Connection con = getConnection();
			//3.SQLをセット(このメソッドはuser_infoテーブルのすべてを取得する為に、
			// SQLはWHERE句で条件指定無で、IDでソートする。）
			PreparedStatement sqlset = con.prepareStatement(
					"SELECT * FROM CUSTOMER_MASTER ORDER BY CUSTOMER_NO");
			//4.SQLを実行した結果を　getDataに格納する。
			ResultSet getData = sqlset.executeQuery();
			//5.取得したレコードを一行ずつUserビーンに格納して、
			//戻り値用のuserListに追加していく。
			while (getData.next()) {
				//userListに追加するためのUserビーンをインスタン化する。
				Client getRecord = new Client();
				//getRecordにテーブルから取得したレコード１行分の
				//データを列名＆型を指定してビーンに格納する。
				getRecord.setCustomerNo(getData.getString("customerNo"));
				getRecord.setCustomerName(getData.getString("customerName"));
				getRecord.setBranchName(getData.getString("branchName"));
				getRecord.setZipNo(getData.getString("ZipNo"));

				getRecord.setAddress1(getData.getString("address1"));
				getRecord.setAddress2(getData.getString("address2"));
				getRecord.setAddress3(getData.getString("address3"));
				getRecord.setTel(getData.getString("tel"));
				getRecord.setFax(getData.getString("fax"));

				getRecord.setManager(getData.getString("manager"));
				getRecord.setDelivaryLeadtime(getData.getString("delivaryLeadtime"));
				getRecord.setEtc(getData.getString("etc"));
//				getRecord.setRegistdate(getData.getString("registdate"));
//				getRecord.setRegistuser(getData.getString("registuser"));


				//ビーンをuserListに追加する。
				clientList.add(getRecord);
			}
			sqlset.close();
			con.close();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return clientList;
	}

	/**
	 * CUSTOMER_MASTERテーブルに新しいレコードを追加するメソッド
	 * @param clientBean
	 * @return 成功：新ID番号  失敗:0
	 */

	//削除用メソッド
		public int delete(Client clientBean) {

				try {
					//1.接続　 接続にはスーパークラスのgetConnection()メソッドを使用する。
					Connection con = getConnection();
					//改造①シーケンスの番号を事前に取得しておく
					//2.SQLをセット(ビーンの情報をUserビーンを参照して？にセットする
					PreparedStatement sqlset = con.prepareStatement(

							"DELETE FROM CUSTOMER_MASTER WHERE CUSTOMER_NO=?");
					sqlset.setString(1, clientBean.getCustomerNo());

					//4.SQLを実行
					int getResult = sqlset.executeUpdate();
		            sqlset.close();
		            con.close();
					if (getResult == 1) {
						return getResult;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		public int touroku(Client clientBean, HttpServletRequest request) {
			try {
				//1.接続　 接続にはスーパークラスのgetConnection()メソッドを使用する。
				Connection con = getConnection();

				HttpSession session = request.getSession();
				String um = (String) session.getAttribute("Login");

				//改造①シーケンスの番号を事前に取得しておく
				//2.SQLをセット(ビーンの情報をUserビーンを参照して？にセットする
				PreparedStatement sqlset = con.prepareStatement(
						"SELECT LPAD (CUSTOMER_NO.NEXTVAL,5,'0') AS NEW_NO FROM DUAL");
		//SQLを実行、結果をgetNoで取得
				ResultSet getNo=sqlset.executeQuery();
				getNo.next();//値を取得するためにこの命令を一回実行する。
				String getUserNo=getNo.getString("NEW_NO");

				sqlset = con.prepareStatement(
						"INSERT INTO CUSTOMER_MASTER VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

				sqlset.setString(1, getUserNo);
				sqlset.setString(2, clientBean.getCustomerName());
				sqlset.setString(3, clientBean.getBranchName());
				sqlset.setString(4, clientBean.getZipNo());

				sqlset.setString(5, clientBean.getAddress1());
				sqlset.setString(6, clientBean.getAddress2());
				sqlset.setString(7, clientBean.getAddress3());
				sqlset.setString(8, clientBean.getTel());
				sqlset.setString(9, clientBean.getFax());

				sqlset.setString(10, clientBean.getManager());
				sqlset.setString(11, clientBean.getDelivaryLeadtime());
				sqlset.setString(12, clientBean.getEtc());

//				今日の日付を取得する。
				Calendar today = Calendar.getInstance();
				String setDay = today.get(Calendar.YEAR) + "/"
						+ (today.get(Calendar.MONTH) + 1) + "/"
						+ today.get(Calendar.DATE);
				sqlset.setString(13, setDay);
				//registuserをumを入れる
				sqlset.setString(14, um);

				//4.SQLを実行
				int getResult = sqlset.executeUpdate();
	            sqlset.close();
	            con.close();
				if (getResult == 1) {
					return getResult;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
	}
		}

