package com.gigaweb.mytrucks;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    private static DownloadManager downloadManager;
    private static ProgressDialog progressDialog;

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9002;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;
    public static final String ROOT_URL = "http://seabornetrade.net/trucks/android/";
    public static final String URL_GENERATE_REPORT = ROOT_URL+"report.php";
    public static final String URL_GENERATE_TRANSACTION = ROOT_URL+"statement.php";
    public static final String URL_GET = ROOT_URL+"get_truck_details.php";
    public static final String URL_GET_LOGIN = ROOT_URL+"login.php";
    public static final String URL_GET_RECENT = ROOT_URL+"get_recently_added.php";
    public static final String URL_ADD_EXPENSE = ROOT_URL+"add_expense.php";
    public static final String URL_ADD_INCOME = ROOT_URL+"add_income.php";
    public static final String URL_GET_EXPENSE_CATEGORIES = ROOT_URL+"get_expense_categories.php";
    public static final String URL_GET_INCOME_CATEGORIES = ROOT_URL+"get_income_categories.php";
    public static final String URL_GET_MONTHLY_ALL = ROOT_URL+"get_monthly_all.php";
    public static final String URL_GET_CATEGORY_EXPENSE = ROOT_URL+"get_category_expense.php";
    public static final String URL_GET_CATEGORY_INCOME = ROOT_URL+"get_category_income.php";
    public static final String URL_GET_TRIPS = ROOT_URL+"get_trips.php";
    private static final String URL_GENERATE_MONTH_REPORT = ROOT_URL+"get_month_report.php";
    public static final String API_KEY = "AIzaSyCjO3VEuuWvmeBB8hsmFAm5FFkEfK6bhvE";


    public static void generateMonthReport(Context context, String year, String monthName, String monthNumber){
        String url = Constants.ROOT_URL+"/reports/"+monthName+"_report.pdf";
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Generating Report...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GENERATE_MONTH_REPORT, response -> {
            progressDialog.dismiss();
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            downloadManager.enqueue(request);
        },

                error -> {
                    progressDialog.hide();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("month_name", monthName);
                params.put("month_no", monthNumber);
                params.put("year", year);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

}
