package com.xiaomi.mitv.shop.account;

import android.accounts.*;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.xiaomi.mitv.api.miui.net.ExtendedAuthToken;

import java.io.IOException;

public class MiTVAccount {

	public static final String TAG = "AccountUtil";
	
	public static final String XIAOMI_ACCOUNT_TYPE = "com.xiaomi";
	
	public static final int ERROR_CODE_NETWORK_ERROR = 101;
	
	public static final int ERROR_CODE_AUTHENTICATION_ERROR = 105;
	
	public static final int ERROR_CODE_CANCELED = 104;
	
	public static final int ERROR_CODE_INVALID_ACCOUNT = 106;
	
	private Context mContext;
	
	private AccountManager mAccountManager;
	
	public static final String ACCOUNT_ACTION_OPTION_NAME ="client_action";
	
	public static final int ACCOUNT_ACTION_GETAUTHENTICATION = 1;
	
	/**
	 * looper线程中调用
	 * @param context
	 */
	public MiTVAccount(Context context) {
		mContext = context;
		mAccountManager = AccountManager.get(mContext);
	}

	public boolean isValidAccount() {
		boolean valid = false;
		for (AuthenticatorDescription authenticator : mAccountManager
				.getAuthenticatorTypes()) {
			if (authenticator.type.equals(XIAOMI_ACCOUNT_TYPE)) {
				valid = true;
				break;
			}
		}
		return valid;
	}

	public Account getAccount() {
		Account[] account = mAccountManager.getAccountsByType(XIAOMI_ACCOUNT_TYPE);
		if (account.length > 0) {
			return account[0];
		} else {
			Log.i(TAG, "no account");
			return null;
		}
	}

	public String getName() {
		Account account = getAccount();
		if (account != null) {
			return account.name;
		} else {
			Log.i(TAG, "account is null, so can't get name");
			return null;
		}
	}

	public void invalidateServiceToken(String token) {
		mAccountManager.invalidateAuthToken(XIAOMI_ACCOUNT_TYPE, token);
	}

	private String getServiceToken(AccountManager accountManager,String authority, Account account) {
		String serviceToken;
		try {
			Bundle options = new Bundle();
			options.putInt(ACCOUNT_ACTION_OPTION_NAME, ACCOUNT_ACTION_GETAUTHENTICATION);
			AccountManagerFuture<Bundle> future = accountManager.getAuthToken(
			account, authority, options, true, null, null);
			if (future == null) {
				Log.i(TAG, "getPassToken: Null future.");
				return null;
			}
			if (future.getResult() == null) {
				Log.i(TAG, "getPassToken: Null future result.");
				return null;
			}
			serviceToken = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
		} catch (OperationCanceledException e) {
			Log.i(TAG, "getPassToken error: ", e);
			return null;
		} catch (AuthenticatorException e) {
			Log.i(TAG, "getPassToken error:", e);
			return null;
		} catch (IOException e) {
			Log.i(TAG, "getPassToken error: ", e);
			return null;
		} catch (ClassCastException e) {
			Log.i(TAG, "getPassToken error: ", e);
			return null;
		}
		if (serviceToken == null || serviceToken.isEmpty()) {
			Log.i(TAG, "getPassToken: No ext token string.");
			return null;
		}
		return serviceToken;
	}
	
	/**
	 * 获取ServiceToken,此方法不能用于主线程调用
	 * @param authTokenType
	 * @return
	 */
	public ExtendedAuthToken getExtServiceToken(String authTokenType) {
		Log.i(TAG, "getServiceToken called");
		Account account = getAccount();
		if(account != null){
			String tokenStr = getServiceToken(mAccountManager, authTokenType,account);
			return ExtendedAuthToken.parse(tokenStr);	
		}else {
			Log.i(TAG, "no account,return null token");
			return null;
		}
	}
	
	public void login(Activity activity, final LoginCallback callback, final Bundle bundle) {
		Log.i(TAG, "login called ");
		if (!isValidAccount()) {
			Log.i(TAG, "invalid account");
			callback.onFailed(ERROR_CODE_INVALID_ACCOUNT,"Account not support.");
			return;
		}
		if (getAccount() == null) {
			AccountCallback accountCallback = new AccountCallback() {

				@Override
				public void onSuccess(Account account) {
					callback.onSuccess(account);
				}

				@Override
				public void onFailed(int error, String message) {
					callback.onFailed(error, message);
				}
			};
			mAccountManager.addAccount(XIAOMI_ACCOUNT_TYPE, null, null, bundle, activity, accountCallback, null);
		} else {
			callback.onSuccess(getAccount());
		}
	}

	private abstract class AccountCallback implements AccountManagerCallback<Bundle>, LoginCallback {

		@Override
		public void run(AccountManagerFuture<Bundle> future) {
			if (future.isDone()) {
				Bundle result = null;
				try {
					result = future.getResult();
				} catch (OperationCanceledException e) {
					Log.e(TAG, "login failed : user canceled " + e);
					onFailed(ERROR_CODE_CANCELED, e.getMessage());
					return;
				} catch (AuthenticatorException e) {
					Log.e(TAG, "login failed : authenticator exception " + e);
					onFailed(ERROR_CODE_AUTHENTICATION_ERROR, e.getMessage());
					return;
				} catch (IOException e) {
					Log.e(TAG, "login failed : io exception " + e);
					onFailed(ERROR_CODE_NETWORK_ERROR, e.getMessage());
					return;
				}
				if (result == null) {
					Log.e(TAG, "login failed : authentication failed");
					onFailed(ERROR_CODE_AUTHENTICATION_ERROR,"authentication failed");
					return;
				}
				Account account = getAccount();
				if (account == null) {
					Log.e(TAG, "login failed : authentication failed");
					onFailed(ERROR_CODE_AUTHENTICATION_ERROR,"authentication failed");
					return;
				}
				Log.e(TAG, "login success");
				onSuccess(account);
			}else {
				Log.i(TAG, "future.isDone is false");
			}
		}
	}

	public interface LoginCallback {
		void onFailed(int error, String message);

		void onSuccess(Account account);
	}
}
