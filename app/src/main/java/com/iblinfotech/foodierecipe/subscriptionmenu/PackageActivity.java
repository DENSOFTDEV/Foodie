package com.iblinfotech.foodierecipe.subscriptionmenu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.iblinfotech.foodierecipe.Activity.LogInActivity;
import com.iblinfotech.foodierecipe.Activity.MainActivity;
import com.iblinfotech.foodierecipe.Activity.ReviewActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.subscriptionmenu.util.IabHelper;
import com.iblinfotech.foodierecipe.subscriptionmenu.util.IabResult;
import com.iblinfotech.foodierecipe.subscriptionmenu.util.Inventory;
import com.iblinfotech.foodierecipe.subscriptionmenu.util.Purchase;
import com.iblinfotech.foodierecipe.utils.GlobalClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackageActivity extends AppCompatActivity {
    public ImageView iv_back;
    private Button remove_ads, premium_recepies, remove_ads_and_add_premium_recepies, restore;
    String payload = "";

    //    static final String SKU_REMOVE_ADS = "android.test.purchased";
    static final String SKU_REMOVE_ADS = "fitnessrezepte.inapp.remove_ads";
    //    static final String SKU_GET_PREMIUM_RECIPE = "android.test.purchased";
    static final String SKU_GET_PREMIUM_RECIPE = "fitnessrezepte.inapp.premium_recipe";
    //    static final String SKU_REMOVE_ADS_GET_PREMIUM_RECIPE = "android.test.purchased";
    static final String SKU_REMOVE_ADS_GET_PREMIUM_RECIPE = "fitnessrezepte.inapp.remove_ads_and_premium_recipe";

    IabHelper mHelper;

    boolean isremove_adds = false;
    boolean is_premium_recipe = false;
    boolean is_remove_adds_and_premium_recipe = false;

    static final int RC_REQUEST1 = 501;
    static final int RC_REQUEST2 = 502;
    static final int RC_REQUEST3 = 503;

    IInAppBillingService mService;

    List<String> arrSKUs = Arrays.asList(SKU_REMOVE_ADS, SKU_GET_PREMIUM_RECIPE, SKU_REMOVE_ADS_GET_PREMIUM_RECIPE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        remove_ads = (Button) findViewById(R.id.remove_ads);
        premium_recepies = (Button) findViewById(R.id.premium_recepies);
        remove_ads_and_add_premium_recepies = (Button) findViewById(R.id.ad_premium_recepies);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        restore = (Button) findViewById(R.id.restore);

        final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo9n4voABuIMfY0G0QiZdpnLlsNnc0Iuz1iVF/x/MLNKqYBDmwSpNYr6KMKa6mApoWLPswWCjdsueaf2VMo16i0IfpmC646S3zmWeFXJ6Z1J+Y3gGKtXiJx7lstXOhDuLkOHddGx94MKhdGdre54t75rn9sml+0mrk7hX0CLIWEPddtAzVVuOf8e5AFuy/WDDLHTbWng6BpV6hN+SEodOAg0gYJ/3W7OjPAh39370Hjrd6hhGx0DP2FX8A7TDsKIVuKp7lNwyOQc+Tt5Bz2XNYie3KfQJQp1+Xr0x2c39AehBCn74tqEtmrUD1UVHrHtzgVY7zwP5DCrh+5Amou0MhQIDAQAB";

        mHelper = new IabHelper(PackageActivity.this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }
                if (mHelper == null) return;
                GlobalClass.printLog("PremiumApp", "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

        ServiceConnection mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PackageActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        remove_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mHelper != null) {
                        mHelper.flagEndAsync();
                        mHelper.launchPurchaseFlow(PackageActivity.this, SKU_REMOVE_ADS, RC_REQUEST1, purchaseFinishedListener, payload);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                                /*AlertDialog.Builder builder = new AlertDialog.Builder(PackageActivity.this);
                builder.setMessage("Remove ads.")
                        .setCancelable(false)
                        .setPositiveButton("purchase", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GlobalClass.setPrefrenceInt(PackageActivity.this, "removeads", 0);
                                Log.e("TAG......", "onClick: " + GlobalClass.getPrefrenceInt(PackageActivity.this, "removeads", 0));

                                Intent i = new Intent(PackageActivity.this, MainActivity.class);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();*/

            }
        });

        premium_recepies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mHelper != null) {
                        mHelper.flagEndAsync();
                        mHelper.launchPurchaseFlow(PackageActivity.this, SKU_GET_PREMIUM_RECIPE, RC_REQUEST2, purchaseFinishedListener, payload);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

               /* AlertDialog.Builder builder = new AlertDialog.Builder(PackageActivity.this);
                builder.setMessage("Add premium Recipe.")
                        .setCancelable(false)
                        .setPositiveButton("purchase", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                GlobalClass.is_purchase = 1;
*//*
                                SharedPreferences sp = getSharedPreferences("FoodieAPP", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("InApp-Purchase", 1);
                                editor.commit();

                                Intent i = new Intent(PackageActivity.this, MainActivity.class);
                                startActivity(i);*//*

                                GlobalClass.setPrefrenceInt(PackageActivity.this, "addpremiumrecipe", 1);

                                Intent i = new Intent(PackageActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();*/
            }
        });

        remove_ads_and_add_premium_recepies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mHelper != null) {
                        mHelper.flagEndAsync();
                        mHelper.launchPurchaseFlow(PackageActivity.this, SKU_REMOVE_ADS_GET_PREMIUM_RECIPE, RC_REQUEST3, purchaseFinishedListener, payload);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

               /* AlertDialog.Builder builder = new AlertDialog.Builder(PackageActivity.this);
                builder.setMessage("Remove ads and Add premium recipe.")
                        .setCancelable(false)
                        .setPositiveButton("purchase", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                GlobalClass.setPrefrenceInt(PackageActivity.this, "removeads", 0);
                                GlobalClass.setPrefrenceInt(PackageActivity.this, "addpremiumrecipe", 1);

                                Intent i = new Intent(PackageActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();*/
            }
        });

        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final IabHelper iabHelper = new IabHelper(PackageActivity.this, base64EncodedPublicKey);
                iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                    @Override

                    public void onIabSetupFinished(IabResult result) {
                        if (result.isSuccess()) {

                            arrSKUs = iabHelper.getPurchasesDetail();//get purchase list
                            Log.e("arrSKUs","------"+arrSKUs);
                            //Now you have the purchased SKU list in an arraylist, you can use SharedPreferences or something else for the further process.
                        }
                    }
                });
//                GlobalClass.showToast(PackageActivity.this, "packageActivity");
//                {
//                    try {
//                        Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
//// Check response
//                        int responseCode = ownedItems.getInt("RESPONSE_CODE");
//                        if (responseCode != 0) {
//                            throw new Exception("Error");
//                        }
//// Get the list of purchased items
//                        ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
//
//                        ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
//
//                        Toast.makeText(PackageActivity.this, "Restore Successfully ", Toast.LENGTH_SHORT).show();
//
//                        int flagI = 0;
//                        for (String purchaseData : purchaseDataList) {
//                            JSONObject o = new JSONObject(purchaseData);
//                            String purchaseToken = o.optString("token", o.optString("purchaseToken"));
//                            // Consume purchaseToken, handling any errors
//
////                        Toast.makeText(PackageActivity.this, "Consumed: " + ownedSkus.get(flagI), Toast.LENGTH_SHORT).show();
//
////                        int response = mService.consumePurchase(3, getPackageName(), purchaseToken);
////
////                        if (response == 0) {
////                            Log.d("Consumed", "Consumed");
////
////                            Toast.makeText(PackageActivity.this, "Consumed: " + ownedSkus.get(flagI), Toast.LENGTH_SHORT).show();
////                        } else {
////                            Log.d("", "No" + response);
//////                                Toast.makeText(PackageActivity.this, ""+purchaseToken, Toast.LENGTH_SHORT).show();
////                            Toast.makeText(PackageActivity.this, "NOT: " + ownedSkus.get(flagI), Toast.LENGTH_SHORT).show();
////                        }
//                            flagI++;
//                        }
//
//                        for (final String singleSKU : arrSKUs) {
////                        String purchaseToken = "inapp:" + getPackageName() + ":"+singleSKU;
////                        try {
////                            Log.d("","Running");
//////                            Toast.makeText(PackageActivity.this, "Running", Toast.LENGTH_SHORT).show();
////                            int response = mService.consumePurchase(3, getPackageName(), "android.test.puchased");
////                            if(response==0)
////                            {
////                                Log.d("Consumed","Consumed");
////
////                                Toast.makeText(PackageActivity.this, "Consumed: "+singleSKU, Toast.LENGTH_SHORT).show();
////                            }else {
////                                Log.d("","No"+response);
////                                Toast.makeText(PackageActivity.this, ""+purchaseToken, Toast.LENGTH_SHORT).show();
////                                Toast.makeText(PackageActivity.this, "NOT: "+singleSKU, Toast.LENGTH_SHORT).show();
////                            }
////                        }catch (RemoteException e)
////                        {
////                            Log.d("Errorr",""+e);
////                            Toast.makeText(PackageActivity.this, "Error", Toast.LENGTH_SHORT).show();
////                        }
//
////                        Log.e("SKUs", "onClick: "+arrSKUs );
////
////                        Log.e("SKUs", "onClick: "+singleSKU );
////
////                        mIabHelper.queryInventoryAsync(true, arrSKUs, mGotInventoryListener);
////
////// Listener that's called when we finish querying the items and subscriptions we own
////                        IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
////                            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
////
////                                Log.e("SKUs", "inonClick: "+arrSKUs );
////
////                                Log.e("SKUs", "inonClick: "+singleSKU );
////                                Log.d("PAY", "Query inventory finished.");
////
////                                // Have we been disposed of in the meantime? If so, quit.
////                                if (mIabHelper == null) return;
////                                Purchase purchase = inventory.getPurchase(singleSKU);
////
////                                Log.e("SKUs", "onQueryInventoryFinished: "+singleSKU );
////                                if (purchase != null) {
////                                    //purchased
////                                    Toast.makeText(PackageActivity.this, "Purchased"+singleSKU, Toast.LENGTH_SHORT).show();
////                                }else {
////                                    Toast.makeText(PackageActivity.this, "NotPurchased"+singleSKU, Toast.LENGTH_SHORT).show();
////
////                                }
////                            }
////                        };
//                        }
////                    Bundle ownedItems = mService.getPurchases(3, "com.ibl.apps.myphotokeyboard", "inapp", null);
////                    Toast.makeText(PackageActivity.this, "ownedItems: " + ownedItems.size(), Toast.LENGTH_SHORT).show();
////                    int response = ownedItems.getInt("RESPONSE_CODE");
////                    Log.e("7887", "onClick: "+response );
////                    if (response == 0) {
////
////                        ArrayList<String> ownedSkus =
////                                ownedItems.getStringArrayList("myphotokeyboard.inapp.removead");
//////                        ArrayList<String> ownedSkus1 =
//////                                ownedItems.getStringArrayList("myphotokeyboard.inapp.texualcolorbg");
//////                        ArrayList<String> ownedSkus2 =
//////                                ownedItems.getStringArrayList("myphotokeyboard.inapp.theamslotes");
//////                        ArrayList<String> ownedSkus3 =
//////                                ownedItems.getStringArrayList("myphotokeyboard.inapp.colors");
//////                        ArrayList<String> ownedSkus4 =
//////                                ownedItems.getStringArrayList("myphotokeyboard.inapp.fonts");
//////                        ArrayList<String> ownedSkus5 =
//////                                ownedItems.getStringArrayList("myphotokeyboard.inapp.sounds");
////                        ArrayList<String> purchaseDataList =
////                                ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
////                        ArrayList<String> signatureList =
////                                ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
////
////                        Log.e("454545454545", "onClick: "+purchaseDataList );
////                        Log.e("454545454545", "onClick: "+signatureList );
////
//////                        Log.e("454545454545", "onClick1: "+ownedSkus1 );
//////                        Log.e("454545454545", "onClick2: "+ownedSkus2);
//////                        Log.e("454545454545", "onClick3: "+ownedSkus3 );
//////                        Log.e("454545454545", "onClick4: "+ownedSkus4 );
//////                        Log.e("454545454545", "onClick5: "+ownedSkus5 );
////                        String continuationToken =
////                                ownedItems.getString("INAPP_CONTINUATION_TOKEN");
////
////                        String allPurchasedProduct = "";
////                        for (int i = 0; i < ownedItems.size(); i++) {
////                            String purchaseData = purchaseDataList.get(i);
////                            String signature = signatureList.get(i);
////                            String sku = ownedSkus.get(i);
//////                            String sku1 = ownedSkus1.get(i);
//////                            String sku2 = ownedSkus2.get(i);
//////                            String sku3 = ownedSkus3.get(i);
//////                            String sku4 = ownedSkus4.get(i);
//////                            String sku5 = ownedSkus5.get(i);
////                            allPurchasedProduct = allPurchasedProduct + sku + ", ";
////                            // do something with this purchase information
////                            // e.g. disple updated list of products owned by user
////                        }
////
////                        GlobalClass.printLog("allPurchasedProduct: ", allPurchasedProduct);
////                        Log.e("12121", "onClick: "+ownedSkus );
////                        Toast.makeText(PackageActivity.this, "PurchasedProduct: " + ownedSkus.size(), Toast.LENGTH_SHORT).show();
////                        Toast.makeText(PackageActivity.this, "allPurchasedProduct: " + ownedSkus, Toast.LENGTH_SHORT).show();
////
////                        // if continuationToken != null, call getPurchases again
////                        // and pass in the token to retrieve more items
////                    }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        });
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            GlobalClass.printLog("premiumActivity", "Query inventory finished.");
            if (mHelper == null) return;
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }
            GlobalClass.printLog("premiumActivity", "Query inventory was successful.");

            Purchase remove_ads = inventory.getPurchase(SKU_REMOVE_ADS);
            isremove_adds = (remove_ads != null && verifyDeveloperPayload(remove_ads));
            GlobalClass.printLog("premiumActivity", "User " + (isremove_adds ? "HAS" : "DOES NOT HAVE") + " Purchase Yearly Subsription.");
            GlobalClass.printLog("getYearPurchase====================", "" + remove_ads);
            if (remove_ads != null && verifyDeveloperPayload(remove_ads)) {
                mHelper.consumeAsync(remove_ads, consumeFinishedListener);
            }

            Purchase get_premium_recipe = inventory.getPurchase(SKU_GET_PREMIUM_RECIPE);
            is_premium_recipe = (get_premium_recipe != null && verifyDeveloperPayload(get_premium_recipe));
            GlobalClass.printLog("premiumActivity", "User " + (is_premium_recipe ? "HAS" : "DOES NOT HAVE") + " Purchase Monthly Subscription.");
            GlobalClass.printLog("getBackgroundPurchase====================", "" + get_premium_recipe);
            if (get_premium_recipe != null && verifyDeveloperPayload(get_premium_recipe)) {
                mHelper.consumeAsync(get_premium_recipe, consumeFinishedListener);
            }

            Purchase remove_ads_and_get_premium_recipe = inventory.getPurchase(SKU_REMOVE_ADS_GET_PREMIUM_RECIPE);
            is_remove_adds_and_premium_recipe = (remove_ads_and_get_premium_recipe != null && verifyDeveloperPayload(remove_ads_and_get_premium_recipe));
            GlobalClass.printLog("premiumRecipe", "User " + (is_remove_adds_and_premium_recipe ? "HAS" : "DOES NOT HAVE") + " Purchase Monthly Subscription.");
            GlobalClass.printLog("getthemePurchase====================", "" + remove_ads_and_get_premium_recipe);
            if (remove_ads_and_get_premium_recipe != null && verifyDeveloperPayload(remove_ads_and_get_premium_recipe)) {
                mHelper.consumeAsync(remove_ads_and_get_premium_recipe, consumeFinishedListener);
            }
            GlobalClass.printLog("premiumRecipe", "Initial inventory query finished; enabling main UI.");
        }
    };

    boolean verifyDeveloperPayload(Purchase purchase) {
        String payload = purchase.getDeveloperPayload();
        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */
        return true;
    }

    IabHelper.OnConsumeFinishedListener consumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            GlobalClass.printLog("premiumActivity", "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                GlobalClass.printLog("premiumActivity", "Consumption successful. Provisioning.");
            } else {
                complain("Error while consuming: " + result);
            }
            //updateUi();
            GlobalClass.printLog("premiumActivity", "End consumption flow.");
        }
    };

    public IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            GlobalClass.printLog("premiumActivity", "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }

            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }
            //updateUserPlan();
            //updateUi();
            if (purchase.getSku().equals("fitnessrezepte.inapp.remove_ads")) {

                GlobalClass.setPrefrenceInt(PackageActivity.this, "removeads", 0);
                Intent i = new Intent(PackageActivity.this, MainActivity.class);
                startActivity(i);
//                SharedPreferences ads = getSharedPreferences("RemoveAds", Activity.MODE_PRIVATE);
//                SharedPreferences.Editor editorAds = ads.edit();
//                editorAds.putInt("removeads", 0);
//                editorAds.commit();

            } else if (purchase.getSku().equals("fitnessrezepte.inapp.premium_recipe")) {

//                SharedPreferences sp = getSharedPreferences("AddPremiumRecipe", Activity.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putInt("addpremiumrecipe", 1);
//                editor.commit();

                GlobalClass.setPrefrenceInt(PackageActivity.this, "addpremiumrecipe", 1);
                Intent i = new Intent(PackageActivity.this, MainActivity.class);
                startActivity(i);

            } else if (purchase.getSku().equals("fitnessrezepte.inapp.remove_ads_and_premium_recipe")) {

//                SharedPreferences ads = getSharedPreferences("RemoveAds", Activity.MODE_PRIVATE);
//                SharedPreferences.Editor editorAds = ads.edit();
//                editorAds.putInt("removeads", 0);
//                editorAds.commit();
//
//                SharedPreferences sp = getSharedPreferences("AddPremiumRecipe", Activity.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putInt("addpremiumrecipe", 1);
//                editor.commit();

                GlobalClass.setPrefrenceInt(PackageActivity.this, "removeads", 0);
                GlobalClass.setPrefrenceInt(PackageActivity.this, "addpremiumrecipe", 1);
                Intent i = new Intent(PackageActivity.this, MainActivity.class);
                startActivity(i);
            }
        }
    };

    void complain(String message) {
        GlobalClass.printLog("premium Activity", "**** TrivialDrive Error: " + message);
        GlobalClass.printLog(" PremiumActivity", "Error: " + message);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        GlobalClass.printLog("PremiumActivity", "onActivityResult" + requestCode + "," + resultCode + "," + data);
//
//        if (requestCode == 1001) {
//
//            GlobalClass.printLog("PremiumActivity", "-----requestCode------10001--" + requestCode);
//            if (resultCode == RESULT_OK) {
//                try {
//                    SharedPreferences sp = getSharedPreferences("FoodieAPP", Activity.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putInt("InApp-Purchase", 1);
//                    editor.commit();
//
//                    Intent i = new Intent(PackageActivity.this, MainActivity.class);
//                    startActivity(i);
//
////                    GlobalClass.ism_purchase = 1;
//                } catch (Exception e) {
//                    GlobalClass.printLog("PremiumActivity", "Failed to parse purchase data.");
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        // Global.showToast(getApplicationContext(), "=====requestCode=====" +requestCode + "\n=====resultCode=====" + resultCode + "\n=====data=====" + data);
//
//        if (mHelper != null && !mHelper.handleActivityResult(requestCode, resultCode, data)) {
//            GlobalClass.printLog("on Activity", "this is on activity result --- with data finish");
//            super.onActivityResult(requestCode, resultCode, data);
//        } else {
//            GlobalClass.printLog("packageActivity", "onActivityResult handled by IABUtil.");
//        }
//    }
}