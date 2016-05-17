package com.andking.chatty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import com.andking.chatty.custom.BaseActivity;
import com.andking.chatty.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * The Class RegisterActivity is the Activity class that shows user registration screen
 * that allows user to register itself on Parse server for this ChatActivity app.
 */
public class RegisterActivity extends BaseActivity
{
	private final int GALLERY_REQUEST = 1;
	private byte[] photo;

	/** The username EditText. */
	private EditText user;

	/** The password EditText. */
	private EditText pwd;
	/** The email EditText. */
	private EditText email;
	private ImageView userPhoto;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		setTouchNClick(R.id.btnReg);

		user = (EditText) findViewById(R.id.user);
		pwd = (EditText) findViewById(R.id.pwd);
		email = (EditText) findViewById(R.id.email);

		userPhoto = (ImageView) findViewById(R.id.imageView2);
	}


	@Override
	public void onClick(View v)
	{
		super.onClick(v);

		String u = user.getText().toString();
		String p = pwd.getText().toString();
		String e = email.getText().toString();
		if (u.length() == 0 || p.length() == 0 || e.length() == 0)
		{
			Utils.showDialog(this, R.string.err_fields_empty);
			return;
		}
		final ProgressDialog dia = ProgressDialog.show(this, null,
				getString(R.string.alert_wait));
		final ParseFile photoFile = new ParseFile("photo.jpg", photo);
		photoFile.saveInBackground();
		final ParseUser pu = new ParseUser();
		pu.setEmail(e);
		pu.setPassword(p);
		pu.setUsername(u);
		pu.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(ParseException e)
			{
				dia.dismiss();
				if (e == null)
				{
					pu.put("userphoto", photoFile);
					pu.saveInBackground();
					UserListActivity.user = pu;
					startActivity(new Intent(RegisterActivity.this, MapActivity.class));
					setResult(RESULT_OK);
					finish();
				}
				else
				{
					Utils.showDialog(
							RegisterActivity.this,
							getString(R.string.err_singup) + " "
									+ e.getMessage());
					e.printStackTrace();
				}
			}
		});

	}

	public void loadPhoto(View view) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = null;

		switch(requestCode) {
			case GALLERY_REQUEST:
				if(resultCode == RESULT_OK){
					Uri selectedImage = data.getData();
					try {
						bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
						photo = getBytesFromBitmap(bitmap);
					} catch (IOException e) {
						e.printStackTrace();
					}
					userPhoto.setImageBitmap(bitmap);
				}
		}
	}


	public byte[] getBytesFromBitmap(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		return stream.toByteArray();
	}
}
