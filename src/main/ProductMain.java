package main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Product;
import bean.ProductMaster;
import bean.SupplierMaster;
import dao.ProductMasterDAO;
import dao.ProductStockDAO;
import dao.SupplierMasterDAO;
import tool.DateUtils;

@WebServlet("/product")
public class ProductMain extends HttpServlet {

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
		ProductMasterDAO pmdao = new ProductMasterDAO();
		ProductStockDAO psdao = new ProductStockDAO();
		String getBunki = request.getParameter("bunki");
		Product screenvalue = getScreenValue(request);
		ProductMaster productvalue = searchProductNo(request);

		//ログインのセッション情報を確認
		HttpSession session = request.getSession();
		boolean login = loginCheck(session);
		if (login == false) {
			request.setAttribute("alertMessage", "ログインができてません。ログインしてください。");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else {

			switch (getBunki) {
			//品番を入力したら起動
			case "searchProductNo":
				//入力した品番とデータベースが一致すれば自動で挿入
				if (productvalue.getProductNo() != null
						&& productvalue.getProductNo().equals(request.getParameter("productNo"))) {
					SupplierMaster supplierBean = smdao.searchSupplier(productvalue.getSupplierNo());
					String supplierName = supplierBean.getSupplierName();
					productvalue.setSupplierName(supplierName);

					request.setAttribute("ProductMaster", productvalue);
					//品番がデータベースにあれば登録ボタンにdisabled
					request.setAttribute("flag", 1);
					//画面左上の状態表示用
					request.setAttribute("state", "更新・削除中");
				} else {
					request.setAttribute("alertMessage", "品番が登録されていません。");
					//入力情報をクリア
					screenvalue = clearvalue(request);
					request.setAttribute("ProductMaster", screenvalue);
					//品番がデータベースにあれば更新ボタン、削除ボタンにdisabled
					request.setAttribute("flag", 0);
					//画面左上の状態表示用
					request.setAttribute("state", "新規登録中");
				}
				break;
			//仕入先コードを入力したら起動
			case "searchSupplierNo":
				SupplierMaster supplierBean = smdao.searchSupplier(request.getParameter("supplierNo"));
				//入力した仕入先コードとデータベースが一致すれば自動で挿入
				if (supplierBean.getSupplierName() == null ||
						supplierBean.getSupplierName().isEmpty()) {
					screenvalue = getScreenValue(request);
					request.setAttribute("alertMessage", "仕入先コードが登録されていません。");
					screenvalue.setSupplierNo(null);
					screenvalue.setSupplierName(null);
					request.setAttribute("ProductMaster", screenvalue);
				} else {

					String supplierName = supplierBean.getSupplierName();
					screenvalue.setSupplierName(supplierName);

					request.setAttribute("ProductMaster", screenvalue);
					//ボタンのdisable起動用
					request.setAttribute("flag", request.getParameter("flag"));
					//画面左上の状態表示用
					request.setAttribute("state", request.getParameter("state"));
				}

				break;

			//登録ボタン
			case "register":
				//必須項目に空欄が無いかを判断
				boolean error = errorMessage(request);
				//品番マスタテーブルにインサートできたらシーケンスで登録した品番を返す、失敗したら”0”を返す
				if (error == true) {
					String result = pmdao.insertProductMaster(screenvalue, request);
					if (result == "0") {
						request.setAttribute("alertMessage", "登録に失敗しました。");
					} else {
						psdao.newProductStockDataMake(DateUtils.getFormatDateYYYYMM(), result);
						request.setAttribute("alertMessage", "登録が完了しました。\\n新しい品番は[" + result + "]です。");
					}
				}
				break;

			//更新ボタン
			case "update":
				//必須項目に空欄が無いかを判断
				error = errorMessage(request);
				if (error == true) {
					boolean update = pmdao.updateProductMaster(screenvalue, request);
					if (update == true) {
						request.setAttribute("alertMessage", "更新が完了しました。");
					} else {
						request.setAttribute("alertMessage", "更新が失敗しました。");
					}
				}
				break;

			//削除ボタン
			case "delete":
				String productNo = request.getParameter("productNo");
				boolean resultvalue = pmdao.deleteProductMaster(productNo);
				String errorMessage = "";
				//品番テーブルの削除
				if (resultvalue == true) {
					errorMessage = errorMessage + "品番マスタテーブルの削除が完了しました。\\n";
					boolean delete = psdao.deletePuroductStock(productNo);
					//在庫テーブルの削除
					if (delete == true) {
						errorMessage = errorMessage + "在庫テーブルの削除が完了しました。";
					} else {
						errorMessage = errorMessage + "在庫テーブルの削除が失敗しました。";
					}
				} else {
					errorMessage = errorMessage + "品番マスタテーブルの削除が失敗しました。";
				}
				request.setAttribute("alertMessage", errorMessage);
				break;

			//クリアボタン
			case "clear":
				screenvalue = null;
				break;
			}
			request.getRequestDispatcher("product.jsp").forward(request, response);
		}
	}

	//以下メソッド
	//----------------------------------------------------------------------------

	/**
	 * 画面で入力した値を取得するメソッド
	 * @param request
	 * @return　screenvalue
	 */
	private Product getScreenValue(HttpServletRequest request) {
		Product screenvalue = new Product();
		screenvalue.setProductNo(request.getParameter("productNo"));
		screenvalue.setProductName(request.getParameter("productName"));
		screenvalue.setSupplierNo(request.getParameter("supplierNo"));
		screenvalue.setSupplierName(request.getParameter("supplierName"));
		if (request.getParameter("unitprice").isEmpty()) {
		} else {
			screenvalue.setUnitprice(Double.parseDouble(request.getParameter("unitprice")));
		}
		if (request.getParameter("sellingprice").isEmpty()) {
		} else {
			screenvalue.setSellingprice(Double.parseDouble(request.getParameter("sellingprice")));
		}
		if (request.getParameter("leadtime").isEmpty()) {
		} else {
			screenvalue.setLeadtime(Integer.parseInt(request.getParameter("leadtime")));
		}
		if (request.getParameter("lot").isEmpty()) {
		} else {
			screenvalue.setLot(Integer.parseInt(request.getParameter("lot")));
		}
		screenvalue.setLocation(request.getParameter("location"));
		screenvalue.setEtc(request.getParameter("etc"));
		return screenvalue;
	}

	/**
	 * 画面で入力した品番で品番マスタテーブルからレコードを取得するメソッド
	 * @param request
	 * @return　pmvalue
	 */
	public ProductMaster searchProductNo(HttpServletRequest request) {
		ProductMasterDAO pmdao = new ProductMasterDAO();
		ProductMaster pmvalue = pmdao.searchProductMaster(request.getParameter("productNo"));
		return pmvalue;
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
		//取得した値が空白かどうかをチェック

		if (request.getParameter("productName").isEmpty()) {
			errorMessage = errorMessage + "品名が入力されていません。\\n";
		}
		if (request.getParameter("supplierNo").isEmpty()) {
			errorMessage = errorMessage + "仕入先コードが入力されていません。\\n";
		}
		if (request.getParameter("unitprice").isEmpty()) {
			errorMessage = errorMessage + "仕入単価が入力されていません。\\n";
		}
		if (request.getParameter("leadtime") == null ||
				request.getParameter("leadtime").isEmpty() ||
				Integer.parseInt(request.getParameter("leadtime")) < 1) {
			errorMessage = errorMessage + "購買リードタイムは1以上の数字を入力してください。\\n";
		}
		if (request.getParameter("lot") == null ||
				request.getParameter("lot").isEmpty() ||
				Integer.parseInt(request.getParameter("lot")) < 1) {
			errorMessage = errorMessage + "購買ロットは1以上の数字を入力してください。\\n";
		}
		//最終判定　errorMessageが空白ならば、trueを戻す。
		if (errorMessage.isEmpty()) {
			return true;
		}
		request.setAttribute("alertMessage", errorMessage);
		//errorMessageが空白以外なら、falseを戻す。
		return false;
	}

	/**
	 * clearvalueの値をクリアするメソッド
	 * @param request
	 * @return　screenvalue
	 */
	private Product clearvalue(HttpServletRequest request) {
		Product screenvalue = getScreenValue(request);
		screenvalue.setProductNo(null);
		screenvalue.setProductName(null);
		screenvalue.setSupplierNo(null);
		screenvalue.setSupplierName(null);
		screenvalue.setUnitprice(0);
		screenvalue.setSellingprice(0);
		screenvalue.setLeadtime(0);
		screenvalue.setLot(0);
		screenvalue.setLocation(null);
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
