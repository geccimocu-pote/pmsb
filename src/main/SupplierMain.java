package main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Supplier;
import bean.SupplierMaster;
import dao.SupplierMasterDAO;

@WebServlet("/supplier")
public class SupplierMain extends HttpServlet {

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

		SupplierMasterDAO smdao = new SupplierMasterDAO();
		String getBunki = request.getParameter("bunki");
		Supplier screenvalue = getScreenValue(request);
		SupplierMaster suppliervalue = searchSupplier(request);

		//ログインのセッション情報を確認
		HttpSession session = request.getSession();
		boolean login = loginCheck(session);
		if (login == false) {
			request.setAttribute("alertMessage", "ログインができてません。ログインしてください。");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else {

			switch (getBunki) {
			//品番を入力したら起動
			case "searchSupplierNo":
				//入力した仕入先コードとデータベースが一致すれば自動で挿入
				if (suppliervalue.getSupplierNo() != null
						&& suppliervalue.getSupplierNo().equals(request.getParameter("supplierNo"))) {
					smdao.searchSupplier(suppliervalue.getSupplierNo());
					request.setAttribute("SupplierMaster", suppliervalue);
					//仕入先コードがデータベースにあれば登録ボタンにdisabled
					request.setAttribute("flag", 1);
					//画面左上の状態表示用
					request.setAttribute("state", "更新・削除中");

				} else {

					request.setAttribute("alertMessage", "仕入先コードが登録されていません。");
					//入力情報をクリア
					screenvalue = clearvalue(request);
					request.setAttribute("SupplierMaster", screenvalue);
					//仕入先コードがデータベースにあれば更新ボタン、削除ボタンにdisabled
					request.setAttribute("flag", 0);
					//画面左上の状態表示用
					request.setAttribute("state", "新規登録中");
				}
				break;

			//登録ボタン
			case "register":
				//必須項目に空欄が無いかを判断
				boolean error = errorMessage(request);
				if (error == true) {
					//在庫テーブルにインサートできたらシーケンスで登録した仕入先コードを返す、
					//失敗したら”0”を返す
					String result = smdao.insertSupplierMaster(screenvalue, request);
					if (result == "0") {
						request.setAttribute("alertMessage", "登録に失敗しました。");
					} else {
						request.setAttribute("alertMessage", "登録が完了しました。\\n新しい仕入先コードは[" + result + "]です。");
					}
				}
				break;

			//更新ボタン
			case "update":
				//必須項目に空欄が無いかを判断
				error = errorMessage(request);
				if (error == true) {
					boolean update = smdao.updateSupplierMaster(screenvalue, request);
					if (update == true) {
						request.setAttribute("alertMessage", "更新が完了しました。");
					} else {
						request.setAttribute("alertMessage", "更新が失敗しました。");
					}
				}
				break;

			//削除ボタン
			case "delete":
				String supplierNo = request.getParameter("supplierNo");
				boolean resultvalue = smdao.deleteSupplierMaster(supplierNo);
				if (resultvalue == true) {
					request.setAttribute("alertMessage", "削除が完了しました。");
				} else {
					request.setAttribute("alertMessage", "削除が失敗しました。");
				}
				break;

			//クリアボタン
			case "clear":
				screenvalue = null;
				break;
			}

			request.getRequestDispatcher("supplier.jsp").forward(request, response);
		}

	}

	//以下メソッド
	//----------------------------------------------------------------------------

	/**
	 * 画面で入力した値を取得するメソッド
	 * @param request
	 * @return　screenvalue
	 */
	private Supplier getScreenValue(HttpServletRequest request) {
		Supplier screenvalue = new Supplier();
		screenvalue.setSupplierNo(request.getParameter("supplierNo"));
		screenvalue.setSupplierName(request.getParameter("supplierName"));
		screenvalue.setBranchName(request.getParameter("branchName"));
		screenvalue.setZipNo(request.getParameter("zipNo"));
		screenvalue.setAddress1(request.getParameter("address1"));
		screenvalue.setAddress2(request.getParameter("address2"));
		screenvalue.setAddress3(request.getParameter("address3"));
		screenvalue.setTel(request.getParameter("tel"));
		screenvalue.setFax(request.getParameter("fax"));
		screenvalue.setManager(request.getParameter("manager"));
		screenvalue.setEtc(request.getParameter("etc"));
		return screenvalue;
	}

	/**
	 * 画面で入力した仕入先コードで仕入先マスタテーブルからレコードを取得するメソッド
	 * @param request
	 * @return　smvalue
	 */
	public SupplierMaster searchSupplier(HttpServletRequest request) {
		SupplierMasterDAO smdao = new SupplierMasterDAO();
		SupplierMaster smvalue = smdao.searchSupplier(request.getParameter("supplierNo"));
		return smvalue;
	}

	/**
	 * 画面で入力した値に空白が無いかを判断するメソッド
	 * @param request
	 * @return　成功：1　失敗：1以外
	 */
	private boolean errorMessage(HttpServletRequest request) {
		String errorMessage = "";
		if (request.getParameter("supplierName").isEmpty()) {
			errorMessage = errorMessage + "会社名が入力されていません。\\n";
		}
		if (request.getParameter("tel").isEmpty()) {
			errorMessage = errorMessage + "電話番号が入力されていません。";
		}
		//最終判定　errorMessageが空白ならば、trueを戻す。
		if (errorMessage.isEmpty()) {
			return true;
		}
		request.setAttribute("alertMessage", errorMessage);
		return false;
	}

	/**
	 * clearvalueの値をクリアするメソッド
	 * @param request
	 * @return　screenvalue
	 */
	private Supplier clearvalue(HttpServletRequest request) {
		Supplier screenvalue = getScreenValue(request);
		screenvalue.setSupplierNo(null);
		screenvalue.setSupplierName(null);
		screenvalue.setBranchName(null);
		screenvalue.setZipNo(null);
		screenvalue.setAddress1(null);
		screenvalue.setAddress2(null);
		screenvalue.setAddress3(null);
		screenvalue.setTel(null);
		screenvalue.setFax(null);
		screenvalue.setManager(null);
		screenvalue.setEtc(null);
		return screenvalue;
	}

	/**
	 * ログイン画面で取得したセッション情報が存在するか判断するメソッド
	 * @param session　（ユーザーID）
	 * @return　成功：1　失敗：1以外
	 */
	public boolean loginCheck(HttpSession session) {
		String result = (String) session.getAttribute("Login");
		if (result == null) {
			return false;
		} else {
			return true;
		}

	}
}
