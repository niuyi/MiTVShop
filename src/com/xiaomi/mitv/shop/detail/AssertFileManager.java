package com.xiaomi.mitv.shop.detail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.xiaomi.mitv.shop.detail.utils.Utils;
import com.xiaomi.mitv.shop.detail.widget.PostersControlWidget;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.AssetManager;
import android.util.Log;


public class AssertFileManager {
	public static List<PostersControlWidget.IssueAdapterData> getTypeAdapterData(AssetManager assetManager, String key) {
		List<PostersControlWidget.IssueAdapterData> result = new ArrayList<PostersControlWidget.IssueAdapterData>();
		String indexFileName = Constants.sScreenFragment + Constants.SEPERATE + "poster.index";
		String content = null;
		try {
			content = Utils.inputStream2String(assetManager.open(indexFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONArray root = null;
		try {
			root = new JSONArray(content.toString());
			for (int i = 0; i < root.length(); i++) {
				JSONObject typeObject = (JSONObject) root.get(i);
//				Log.e("AssertFileManager: ", typeObject.toString());
				String typeName = typeObject.getString("key");
//				Log.e("typeObject: ", typeObject.toString());
				JSONArray typeArray = null;
				if (typeName == null || !typeName.equals(key)) {
					continue;
				}
				typeArray = typeObject.getJSONArray("value");
//				Log.e("type array: ", typeArray.toString());
				for (int j = 0; j < typeArray.length(); j++) {
					JSONObject issueObject = (JSONObject) typeArray.get(j);
//					Log.e("issueObject: ", issueObject.toString());
					PostersControlWidget.IssueAdapterData issueAdapterData = new PostersControlWidget.IssueAdapterData();
//					issueAdapterData.mBitmap = null;
					issueAdapterData.mName = issueObject.getString("key");
					issueAdapterData.mParentFolderName = typeName;
					JSONArray childrenJSONArray = issueObject.getJSONArray("value");
					String[] posterFullPaths = new String[childrenJSONArray.length()];
					String path = Constants.sScreenFragment + Constants.SEPERATE + typeName + 
							Constants.SEPERATE + issueAdapterData.mName;
//					Log.e("childrenJSONArray: ", childrenJSONArray.toString());
					for (int k = 0; k < childrenJSONArray.length(); k++) {
						String posterName = childrenJSONArray.getString(k);
						posterFullPaths[k] = path + Constants.SEPERATE + posterName;
						Log.i("posterFullPaths : ", posterFullPaths[k]);
					}
					issueAdapterData.mPosterChilren = posterFullPaths;
					issueAdapterData.mChildCount = posterFullPaths.length;
					result.add(issueAdapterData);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}
