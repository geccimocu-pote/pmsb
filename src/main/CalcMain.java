package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Stock;
import dao.ProductMasterDAO;
import dao.ProductOrderDAO;
import dao.ProductStockDAO;
import dao.PurchaseOrderDAO;

@WebServlet("/calc")
public class CalcMain extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		//Mainjavaから実行したときのエラー画面
		PrintWriter out = response.getWriter();
		out.println("JSPから実行してください。");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		//セッション確認
		HttpSession session = request.getSession();
		boolean login = loginCheck(session);
		if (login == false) {
			request.setAttribute("alertMessage", "ログインができてません。ログインしてください。");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else {
			//onClick属性使用
			String getAct = request.getParameter("bunki");

			switch (getAct) {
			case "calc":
				//Stockビーンをリスト化
				List<Stock> listmaster = new ArrayList<Stock>();//品番マスタのリスト
				List<Stock> list = new ArrayList<Stock>();//品番ごとのリスト

				listmaster = getProduct(listmaster);//品番マスタより全品番取得

				for (Stock no : listmaster) {//品番からひとつずつ在庫状況を取得
					String getmasterkod = no.getProductNo();
					String supplierNo = no.getSupplierNo();
					list = getOrder(getmasterkod, list);//発注テーブル
					list = getStock(getmasterkod, list, request);//在庫テーブル
					list = getPurchase(getmasterkod, list);//受注テーブル

					List<Stock> sortBean = new ArrayList<>();//日付順にソート
					sortBean = list.stream().sorted(Comparator.comparing(Stock::getYmd))
							.collect(Collectors.toList());

					list = sortBean;
					if (list.get(0).getStockQty() == null || list.get(0).getStockQty().isEmpty()) {
						request.setAttribute("alertMessage", "テーブルにエラーを発見しました。");
						request.getRequestDispatcher("calc.jsp")
								.forward(request, response);
					}
					int zaiko = Integer.parseInt(list.get(0).getStockQty());
					int lot = no.getLot();
					//在庫・受発注状況から新規に必要な発注数の計算
					for (int i = 1; i < list.size(); i++) {//品番ごとに日付順で計算
						List<Stock> sort = new ArrayList<>();
						//品番順にソートし、品番ごとに日付計算できるようにする。
						sort = list.stream().sorted(Comparator.comparing(Stock::getProductNo))
								.collect(Collectors.toList());
						list = sort;

						if (list.get(i).getStockQty() == null) {//ひとつ前の在庫をint化し取得
							zaiko = Integer.parseInt(list.get(i - 1).getStockQty());

							if (list.get(i).getPoQty() == null) {//受注があるか確認
								//受注がないとき
								if (list.get(i).getOrderQty() == null) {//発注があるか確認

								} else {//在庫足す発注数の計算
									int ordernumber = 0;
									ordernumber = Integer.parseInt(list.get(i).getOrderQty());
									list.get(i).setStockQty(String.valueOf(zaiko + ordernumber));
								}
							} else {//受注があるとき
								int ponumber = 0;
								ponumber = Integer.parseInt(list.get(i).getPoQty());
								list.get(i).setStockQty(String.valueOf(zaiko - ponumber));
								if (zaiko - ponumber < 0) {//在庫数より受注数が多いとき
									int n;

									n = (ponumber - zaiko) / lot + 1;//必要なロット数の計算

									list.get(i).setOrderQty(String.valueOf(n * lot));//新たに発注が必要な数を計算。

									list.get(i).setSupplierNo(supplierNo);//発注先のコードを取得

								}

								if (list.get(i).getOrderQty() == null) {

								} else {//新規に発注したときに在庫数と発注数を足して受注数を引いて受発注後の在庫数を計算
									int ordernumber = 0;
									ordernumber = Integer.parseInt(list.get(i).getOrderQty());
									list.get(i).setStockQty(String.valueOf(zaiko + ordernumber - ponumber));

								}
							}
						}

					}

				}
				//在庫・受発注テーブルから新規に発注する分を取り出す。
				List<Stock> output = new ArrayList<>();
				for (int j = 1; j < list.size(); j++) {//発注以外を取り除く
					if (list.get(j).getSupplierNo() == null || list.get(j).getSupplierNo().isEmpty()) {
						list.set(j, null);
					} else {

						if (list.get(j).getOrderNo() != null) {//発注済みを取り除く
							list.set(j, null);

						} else

							output.add(list.get(j));//新規発注分のリストを作成
					}

				}
				if (output == null || output.isEmpty()) {//新規発注がないとき
					request.setAttribute("alertMessage", "発注が必要な商品はありません。");
					request.getRequestDispatcher("calc.jsp")
							.forward(request, response);
				}

				session = request.getSession();//新規発注するリストを画面に表示
				session.setAttribute("listorder", output);
				request.getRequestDispatcher("calc.jsp")
						.forward(request, response);

				break;

			case "cancel"://画面に表示したリストをリセットして消す。
				output = null;
				session = request.getSession();
				session.setAttribute("listorder", output);

				request.setAttribute("alertMessage", "所要量計算をキャンセルしました。");
				request.getRequestDispatcher("calc.jsp")
						.forward(request, response);
				break;

			case "insert"://画面に表示されているリストを発注テーブルにinsertする。
				session = request.getSession();
				output = (List<Stock>) session.getAttribute("listorder");

				if (output == null) {
					request.setAttribute("alertMessage", "新規発注がありません。所要量計算を行ってください。");
					request.getRequestDispatcher("calc.jsp")
							.forward(request, response);
				}

				Insert(request, output);

				output = null;//insert後画面をリセット
				session = request.getSession();
				session.setAttribute("listorder", output);

				request.getRequestDispatcher("calc.jsp")
						.forward(request, response);
				break;
			}
			request.getRequestDispatcher("calc.jsp")
					.forward(request, response);
		}

	}

	//新規発注分を発注テーブルにinsertして結果を取得
	private void Insert(HttpServletRequest request, List<Stock> output) {

		ProductOrderDAO pod = new ProductOrderDAO();

		int result = pod.InsertOrder(request, output);

		if (result != 1) {
			request.setAttribute("error", "更新に失敗しました。");

		} else {

			request.setAttribute("alertMessage", "発注しました。");

		}
	}

	//受注テーブルから受注状況の取得
	private List<Stock> getPurchase(String getmasterkod, List<Stock> list) {
		PurchaseOrderDAO pcod = new PurchaseOrderDAO();
		list = pcod.searchPurchase(getmasterkod, list);

		return list;
	}

	//在庫テーブルから在庫状況の取得
	private List<Stock> getStock(String getmasterkod, List<Stock> list, HttpServletRequest request) {

		ProductStockDAO psd = new ProductStockDAO();
		Stock stockproductbean = psd.searchproductStock(getmasterkod);
		list.add(stockproductbean);

		if (list.size() == 0) {
			request.setAttribute("alertMessage", "テーブルにエラーを発見しました。");
		}

		return list;

	}

	//発注テーブルから発注状況の取得
	private List<Stock> getOrder(String getmasterkod, List<Stock> list) {

		ProductOrderDAO pod = new ProductOrderDAO();
		list = pod.searchOrder(getmasterkod, list);
		return list;

	}

	//品番マスタから製品情報の取得
	private List<Stock> getProduct(List<Stock> listmaster) {
		ProductMasterDAO pmd = new ProductMasterDAO();
		listmaster = pmd.getProductMasterNo(listmaster);
		return listmaster;

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