package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Client;
import dao.ClientDAO;

@WebServlet("/clientMain")

public class ClientMain extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("JSPから実行してください。");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		//セッション確認
		HttpSession session = request.getSession();
				boolean login = loginCheck(session);
				if (login == false) {
					request.setAttribute("alertMessage", "ログインができてません。ログインしてください。");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				} else {

		//リクエストパラメータのbunkiの値を取得する。

		String getBunki = request.getParameter("bunki");

		//画面の入力情報を取得してUserビーンに収める。
		Client clientBean = getScreenValue(request);

		//bunkiの値の内容で処理を分岐する。
		switch (getBunki) {

		case "searchId"://idが入力された場合
			Client getClient = searchIdMethod(request);//idで検索するメソッド
			//入力されたIDがデータベーステーブルに未登録の場合
			//エラーメッセージを、登録ありの場合、userBeanに値を渡す。
			if (getClient.getCustomerName() == null) {

				//アラート用のメッセージをリクエスト属性で保存
				request.setAttribute(
						"alertMessage", "入力されたIDは登録されていません。新規登録してください。");



				//隠し要素で品番がデータベースにあれば登録ボタンにdisabled
				request.setAttribute("flag", 0);
				//画面左上の状態表示用
				request.setAttribute("state", "新規登録中");
				request.getRequestDispatcher("client.jsp")
				.forward(request, response);
			} else {
				//隠し要素で品番がデータベースにあれば更新ボタン、削除ボタンにdisabled
				request.setAttribute("flag", 1);
				//画面左上の状態表示用
				request.setAttribute("state", "更新・削除中");
				clientBean = getClient;
			}
			break;

		case "touroku"://登録ボタンがクリックされた場合(登録と更新）
			boolean checkOk = checkValue(request);//入力値チェック用メソッド
			if (checkOk == true) {
				//ここで更新か登録かを分岐する。
				//現在入力中のIDが存在すれば更新メソッド、しなければ新規登録メソッド
				ClientDAO cd = new ClientDAO();//ClientDAOをインスタンス化する。
				Client getClientBean = cd.search(clientBean.getCustomerNo());//IDで検索
				if (getClientBean.getCustomerNo() == null) {//取得結果のidがnull（そのidで登録されていない）
					touroku(request);//登録用メソッドを呼び出す。
					clientBean = null;
				} else {
					//アラート用のメッセージをリクエスト属性で保存
					request.setAttribute(
							"alertMessage", "入力されたIDは既に登録されています。");
				}
				//			{//取得結果のidがnull以外の場合は更新メソッドを呼び出す。
				//					updateUser(request, userBean);
				//				}
				//
			}
			break;

		case "kosin": //更新する場合の処理
			//			  //更新用メソッドを呼び出す。requestと
			//			 //画面情報が格納されているuserBeanを引数として渡す。
			updateClient(request, clientBean);
						 clientBean=null;
			break;

		case "clear"://クリアーボタンがクリックされた場合
//			clientBean = null;
			break;

		case "delete"://削除する場合の処理
			//削除用メソッドを呼び出す。
			delete(request, clientBean);
			clientBean = null;
			break;

		default:
		}
		//画面に表示する情報（ビーン）をリクエスト属性で保存する。
		//名前："user”　値：userBean
		request.setAttribute("client", clientBean);

		//画面を遷移する。
		request.getRequestDispatcher("client.jsp").forward(request, response);
	}

	}

		//登録メソッド
	private void touroku(HttpServletRequest request) {
		// 画面に入力された情報をUserビーンに格納する。
		Client clientBean = new Client();

		clientBean.setCustomerNo(request.getParameter("customerNo"));
		clientBean.setCustomerName(request.getParameter("customerName"));
		clientBean.setBranchName(request.getParameter("branchName"));
		clientBean.setZipNo(request.getParameter("zipNo"));

		clientBean.setAddress1(request.getParameter("address1"));
		clientBean.setAddress2(request.getParameter("address2"));
		clientBean.setAddress3(request.getParameter("address3"));
		clientBean.setTel(request.getParameter("tel"));
		clientBean.setFax(request.getParameter("fax"));

		clientBean.setManager(request.getParameter("manager"));
		clientBean.setDelivaryLeadtime(request.getParameter("delivaryLeadtime"));
		clientBean.setEtc(request.getParameter("etc"));
//		clientBean.setRegistdate(request.getParameter("registdate"));
//		clientBean.setRegistuser(request.getParameter("registuser"));

		//ClientDAOをインスタンス化する。
		ClientDAO cd = new ClientDAO();
		//ClientDAOのtourokuメソッドを呼び(引数にはClientビーンをセット）結果を受け止める。
		int result = cd.touroku(clientBean, request);

		if (result > 0) {
			request.setAttribute("alertMessage", "登録に成功しました。");
			//新番号で検索を行い、データをClientビーンに収める。
			Client getClient = cd.search(String.valueOf(result));
			//JSPの一覧表示がわはList<user>形式でデータを送る必要があるので
			//送る形式を合わす
			List<Client> listClient = new ArrayList<Client>();
			//ud.searchから取得したgetUserをList<User>に追加
			listClient.add(getClient);
			//新iDで取得した情報をリクエスト属性で保存
			request.setAttribute("listclient", listClient);

		} else {
			request.setAttribute("alertMessage", "登録に失敗しました。管理者に問い合わせてください。");
		}

	}


	/**
	 * 更新用メソッド
	 * @param request
	 * @param clientBean
	 */
	private void updateClient(HttpServletRequest request, Client clientBean) {
		//UserDAOをインスタンス化する。
		ClientDAO cd = new ClientDAO();
		//更新用メソッドを呼び出す。　引数は受け取ったUserビーン
		int result = cd.updateData(clientBean);
		//更新成功ならば1、更新失敗なら１以外が戻ってくる。
		//どちらの場合もメッセージをリクエスト属性に保存する。
		if (result != 1) {
			request.setAttribute("alertMessage", "更新に失敗しました。");
		} else {
			request.setAttribute("alertMessage", "更新に成功しました。");
			//更新されたIDを一覧に表示する
			//userBeanに格納されているIDで検索を行う
			Client getCustomerNo = cd.search(clientBean.getCustomerNo());
			//結果をjsp表示用に準備したlistUserに追加する
			List<Client> listClient = new ArrayList<>();
			listClient.add(getCustomerNo);
			//リクエスト属性で保存する
			request.setAttribute("listclient", listClient);
		}
	}

	/**
	 * 削除用メソッド
	 */
	private void delete(HttpServletRequest request, Client clientBean) {
		//UserDAOをインスタンス化する。
		ClientDAO cd = new ClientDAO();
		//更新用メソッドを呼び出す。　引数は受け取ったUserビーン
		int result = cd.delete(clientBean);
		//更新成功ならば1、更新失敗なら１以外が戻ってくる。
		//どちらの場合もメッセージをリクエスト属性に保存する。
		if (result != 1) {
			request.setAttribute("alertMessage", "削除に失敗しました。");
		} else {
			request.setAttribute("alertMessage", "削除に成功しました。");
		}
	}

	/**
	 * リクエストパラメータidの値を取得して、データベーステーブルに
	 * 問い合わせて、Clientビーンで受け取って
	 * Userビーンで戻す。
	 * @param request
	 * @return Client
	 */
	private Client searchIdMethod(HttpServletRequest request) {
		//1.リクエストパラメータuser_idから値を取得する。
		String getCustomerNo = request.getParameter("customerNo");
		//2.ClientDAOをインスタンス化する。
		ClientDAO cd = new ClientDAO();
		//3.serchメソッドを呼び出す。引数はgetCustomer_No,戻り値は
		//Clientビーンなので、Clientビーンで受け取る。
		//この下二個のgeCustomer_NoをgetCustomerNoにするとエラーが出るのでそのまま
		Client getCustomerN = cd.search(getCustomerNo);
		//4.受け取ったUserビーンを戻す。
		return getCustomerN;
	}

	/**
	 * 画面のリクエストパラメーターを取得し、Userビーンに格納するメソッド
	 * @param request
	 * @return User
	 */
	private Client getScreenValue(HttpServletRequest request) {
		// 画面に入力された情報をUserビーンに格納する。
		Client clientBean = new Client();//このuserBeanを戻り値とする。
		//jspの name="userId" の値を取得して、UserBeanのuserIdにセットする。
		clientBean.setCustomerNo(request.getParameter("customerNo"));
		clientBean.setCustomerName(request.getParameter("customerName"));
		clientBean.setBranchName(request.getParameter("branchName"));
		clientBean.setZipNo(request.getParameter("zipNo"));

		clientBean.setAddress1(request.getParameter("address1"));
		clientBean.setAddress2(request.getParameter("address2"));
		clientBean.setAddress3(request.getParameter("address3"));
		clientBean.setTel(request.getParameter("tel"));
		clientBean.setFax(request.getParameter("fax"));

		clientBean.setManager(request.getParameter("manager"));
		clientBean.setDelivaryLeadtime(request.getParameter("delivaryLeadtime"));
		clientBean.setEtc(request.getParameter("etc"));


		//画面情報が格納されたuserBeanを戻す。
		return clientBean;
	}

	/**
	 * 全入力値をチェックするメソッド
	 * 空白及び数字項目が数字なのかをチェックする。
	 * @param request
	 * @return チェックOK: true  ,  NGの場合:false
	 */
	private boolean checkValue(HttpServletRequest request) {
		//エラーメッセージ格納用変数
		String errorMessage = "";

		/*String errorCustomerNo = "0";
		String errorCustomerName = "0";
		String errorBranchName = "0";

		String errorAddress1 = "0";
		String errorAddress2 = "0";
		String errorAddress3 = "0";
		String errorTel = "0";
		String errorFax = "0";

		String errorManager = "0";
		String errorDelivaryLeadtime = "0";
		String errorEtc = "0";
		String errorRegistdate = "0";
		String errorRegistuser = "0";*/

		//IDチェック  リクエストパラメータから取得した値が空白をチェック(.isEmpty)
		// 数字かどうかをチェックするにはメソッド.chars().allMatch(Character::isDigit)を使用


		//名前チェック　リクエストパラメータから取得した値が空白をチェック(.isEmpty)
		if (request.getParameter("customerName").isEmpty()) {
			errorMessage = errorMessage + "会社名が入力されていません。\\n";
		}
		//パスワードチェック　リクエストパラメータから取得した値が空白をチェック(.isEmpty)
		if (request.getParameter("tel").isEmpty()) {
			errorMessage = errorMessage + "電話番号が入力されていません。\\n";
		}
		//分類チェック  リクエストパラメータから取得した値が空白をチェック(.isEmpty)
		if (request.getParameter("delivaryLeadtime").isEmpty()) {
			errorMessage = errorMessage + "輸送リードタイムが入力されていません。\\n";
		}


		//最終判定　errorMessageが空白ならば、trueを戻す。
		if (errorMessage.isEmpty()) {
			return true;
		}
		//エラーの内容をリクエスト属性で保存する。
		request.setAttribute("alertMessage", errorMessage);
		//errorMessageがnullなら、falseを戻す。
		return false;
	}




	//ログインのセッションがあるかチェック
	public boolean loginCheck(HttpSession session) {
		String result = (String) session.getAttribute("Login");
		if (result == null) {
			return false;
		} else {
			return true;
		}

	}

}
